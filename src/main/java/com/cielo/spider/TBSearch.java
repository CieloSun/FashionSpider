package com.cielo.spider;

import com.cielo.pipeline.MyTBJsonPipeline;
import com.cielo.utils.JSONUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 63289 on 2017/6/23.
 */
public class TBSearch implements PageProcessor {
    private static final String urlSearch = "https://s\\.taobao\\.com/search.*";
    private static final String tmallComment = "https://rate\\.tmall\\.com/list_detail_rate\\.htm.*";
    private static final String tbComment = "https://rate\\.taobao\\.com/feedRateList\\.htm.*";
    private static final String sec = "https://sec\\.taobao\\.com/.*";
    private Site site = Site.me().setSleepTime(3000).setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");

    public String convert(String utfString) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = utfString.indexOf("\\u", pos)) != -1) {
            stringBuilder.append(utfString.substring(pos, i));
            if (i + 5 < utfString.length()) {
                pos = i + 6;
                stringBuilder.append((char) Integer.parseInt(utfString.substring(i + 2, i + 6), 16));
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public void process(Page page) {
        if(page.getUrl().regex(sec).match()){
            System.out.println("Meet the spider-anti-program");
            return;
        }
        //如果是评论页面
        if (page.getUrl().regex(tmallComment).match()) {
            String text = page.getRawText().replace("\"rateDetail\":", "");
            //记录信息
            Map map = JSONUtils.json2Map(text);
            if (map.get("rateList") == null) return;
            Matcher itemIdMatcher = Pattern.compile("itemId=\\d+").matcher(page.getRequest().getUrl());
            String itemIdString = null;
            if (itemIdMatcher.find()) itemIdString = itemIdMatcher.group().replace("itemId=", "");
            Matcher shopIdMatcher = Pattern.compile("sellerId=\\d+").matcher(page.getRequest().getUrl());
            String shopIdString = null;
            if (shopIdMatcher.find()) shopIdString = shopIdMatcher.group().replace("sellerId=", "");
            map.put("itemId", itemIdString);
            map.put("sellerId", shopIdString);
            map.put("url", page.getRequest().getUrl());
            page.putField(itemIdString, map);
        } else if (page.getUrl().regex(tbComment).match()) {
            Matcher jsonMatcher = Pattern.compile("\\{.*\\}").matcher(page.getRawText());
            if (jsonMatcher.find()) {
                Map map = JSONUtils.json2Map(jsonMatcher.group());
                if(map.get("url")!=null&&map.get("url").toString().matches(sec)){
                    System.out.println("Meet the anti-Spider!");
                    return;
                }
                if (map.get("comments") == null) return;
                Matcher itemIdMatcher = Pattern.compile("auctionNumId=\\d+").matcher(page.getRequest().getUrl());
                String itemIdString = null;
                if (itemIdMatcher.find()) itemIdString = itemIdMatcher.group().replace("auctionNumId=", "");
                Matcher shopIdMatcher = Pattern.compile("userNumId=\\d+").matcher(page.getRequest().getUrl());
                String shopIdString = null;
                if (shopIdMatcher.find()) shopIdString = shopIdMatcher.group().replace("userNumId=", "");
                map.put("itemId", itemIdString);
                map.put("sellerId", shopIdString);
                map.put("url", page.getRequest().getUrl());
                page.putField(itemIdString, map);
            }
        }
        //如果是列表页面
        else if (page.getUrl().regex(urlSearch).match()) {
//            System.out.println("match list.");
            //获取页面script并转码为中文
            String origin = convert(page.getHtml().xpath("//head/script[7]").toString());
            //从script中获取json
            Matcher jsonMatcher = Pattern.compile("\\{.*\\}").matcher(origin);
            //如果成功获取json数据
            if (jsonMatcher.find()) {
                //清理乱码
                String jsonString = jsonMatcher.group().replaceAll("\"navEntries\".*?,", "")
                        .replaceAll(",\"p4pdata\".*?\\\"\\}\"", "").replaceAll("\"spuList\".*?,", "");
//                System.out.println(jsonString);
                //选择auctions列表
                List<String> auctions = new JsonPathSelector("mods.itemlist.data.auctions[*]").selectList(jsonString);
                //对于每一项商品
                for (String auction : auctions) {
//                    System.out.println(auction);
                    Map map = JSONUtils.json2Map(auction);
                    //获取评论url
                    String commentUrl = (String) map.get("comment_url");
//                    System.out.println(commentUrl);
                    if (commentUrl == null) continue;
                    //获取商品id
                    Matcher itemIdMatcher = Pattern.compile("id=\\d+").matcher(commentUrl);
                    String itemIdString = null;
                    if (itemIdMatcher.find()) itemIdString = itemIdMatcher.group().replace("id=", "");
                    else continue;
//                    System.out.println(itemIdString);
                    //获取商店ip
                    String shopLink = new JsonPathSelector("shopLink").select(auction);
                    Matcher shopIdMatcher = Pattern.compile("user_number_id=\\d+").matcher(shopLink);
                    String shopIdString = null;
                    if (shopIdMatcher.find()) shopIdString = shopIdMatcher.group().replace("user_number_id=", "");
                    else continue;
//                    System.out.println(shopIdString);
                    //记录信息
                    map.put("itemId", itemIdString);
                    map.put("sellerId", shopIdString);
                    page.putField(itemIdString, map);
                    //如果有评论，则抓取评论页面
                    if (map.get("comment_count") != null && !map.get("comment_count").toString().isEmpty()) {
                        for (int i = 1; i <= 5; ++i) {
                            String taoBaoUrl = "https://rate.taobao.com/feedRateList.htm?auctionNumId=" + itemIdString + "&userNumId=" + shopIdString + "&currentPageNum=" + i;
                            page.addTargetRequest(taoBaoUrl);
                        }
                        page.addTargetRequest("https://rate.tmall.com/list_detail_rate.htm?itemId=" + itemIdString + "&sellerId=" + shopIdString);
                    }
                }
            } else {
                System.out.println("Cannot find json in the script.");
            }
        } else {
            System.out.println(convert(page.getHtml().xpath("//head/script[7]").toString()));
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws Exception {
        Spider.create(new TBSearch()).addUrl("https://s.taobao.com/search?q=" + args[0])
                .addPipeline(new MyTBJsonPipeline("data\\" + args[0])).run();
    }
}
