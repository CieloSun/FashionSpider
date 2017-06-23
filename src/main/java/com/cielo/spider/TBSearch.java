package com.cielo.spider;

import com.alibaba.fastjson.JSON;
import com.cielo.pipeline.MyTBJsonPipeline;
import com.cielo.utils.JSONUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

/**
 * Created by 63289 on 2017/6/23.
 */
public class TBSearch implements PageProcessor {
    private static final String urlSearch = "https://s\\.taobao\\.com/search.*";
    private static final String urlComment = "https://rate\\.tmall\\.com/list_detail_rate\\.htm.*";
    private Site site = Site.me().setSleepTime(1000);

    public String convert(String utfString) {
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = utfString.indexOf("\\u", pos)) != -1) {
            sb.append(utfString.substring(pos, i));
            if (i + 5 < utfString.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(utfString.substring(i + 2, i + 6), 16));
            }
        }

        return sb.toString();
    }

    @Override
    public void process(Page page) {
        //如果是评论页面
        if (page.getUrl().regex(urlComment).match()) {
//            System.out.println("comment");
            String text = page.getRawText().replace("\"rateDetail\":","");
            Matcher itemIdMatcher = Pattern.compile("itemId=\\d+").matcher(page.getRequest().getUrl());
            String itemIdString = null;
            if (itemIdMatcher.find()) itemIdString = itemIdMatcher.group().replace("itemId=", "");
//            System.out.println("itemId:" + itemIdString);
            Matcher shopIdMatcher = Pattern.compile("sellerId=\\d+").matcher(page.getRequest().getUrl());
            String shopIdString = null;
            if (shopIdMatcher.find()) shopIdString = shopIdMatcher.group().replace("sellerId=", "");
//            System.out.println("shopId:" + shopIdString);
            //记录信息
            try {
                Map map=JSONUtils.parseMap(text);
                map.put("itemId", itemIdString);
                map.put("sellerId",shopIdString);
                map.put("url", page.getRequest().getUrl());
                page.putField(itemIdString,map);
            }catch (IOException e){
                System.out.println("Parse error in comment.");
                System.out.println(text);
            }
        }
        //如果是列表页面
        else if (page.getUrl().regex(urlSearch).match()) {
            //获取页面script并转码为中文
            String origin = convert(page.getHtml().xpath("//head/script[7]").toString());
            //从script中获取json
            Matcher jsonMatcher = Pattern.compile("\\{.*\\}").matcher(origin);
            //如果成功获取json数据
            if (jsonMatcher.find()) {
                //清理乱码
                String jsonString = jsonMatcher.group().replaceAll("\"navEntries\".*?,", "");
                //选择auctions列表
                List<String> auctions = new JsonPathSelector("mods.itemlist.data.auctions[*]").selectList(jsonString);
                //对于每一项商品
                for (String auction : auctions) {
                    //获取评论数
                    String commentCount = new JsonPathSelector("comment_count").select(auction);
//                    System.out.println(commentCount);
                    Integer commentCountValue = 0;
                    //如果该项存在且非空
                    if (!(commentCount.isEmpty() || commentCount == null))
                        commentCountValue = Integer.parseInt(commentCount);
                    //获取评论url
                    String commentUrl = new JsonPathSelector("comment_url").select(auction);
//                    System.out.println(commentUrl);
                    //获取商品id
                    Matcher itemIdMatcher = Pattern.compile("id=\\d+").matcher(commentUrl);
                    String itemIdString = null;
                    if (itemIdMatcher.find()) itemIdString = itemIdMatcher.group().replace("id=", "");
                    //获取商店ip
                    String shopLink = new JsonPathSelector("shopLink").select(auction);
                    Matcher shopIdMatcher = Pattern.compile("user_number_id=\\d+").matcher(shopLink);
                    String shopIdString = null;
                    if (shopIdMatcher.find()) shopIdString = shopIdMatcher.group().replace("user_number_id=", "");
                    //记录信息
                    try {
                        Map map= JSONUtils.parseMap(auction);
                        map.put("itemId",itemIdString);
                        map.put("sellerId",shopIdString);
                        page.putField(itemIdString,map);
                        //如果有评论，则抓取评论页面
                        if (commentCountValue > 0) {
                            page.addTargetRequest("https://rate.tmall.com/list_detail_rate.htm?itemId=" + itemIdString + "&sellerId=" + shopIdString);
                        }
                    }catch (IOException e){
                        System.out.println("Parse error");
                    }
                }
            } else {
                System.out.println("Cannot find json in the script.");
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void tidy(){

    }

    public static void main(String[] args) throws Exception {
        Spider.create(new TBSearch()).addUrl("https://s.taobao.com/search?q=" + args[0])
                .addPipeline(new MyTBJsonPipeline("data")).run();
    }
}
