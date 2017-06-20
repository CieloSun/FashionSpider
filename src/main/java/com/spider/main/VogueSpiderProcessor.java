package com.spider.main;
import com.spider.pipeline.MyPipeline;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

/**
 * Created by 63289 on 2017/6/18.
 */
public class VogueSpiderProcessor implements PageProcessor {
    public static final String URL_LIST = "http://www\\.vogue\\.com\\.cn/fashion.*";
    public static final String URL_POST = "http://www\\.vogue\\.com\\.cn/fashion/[\\w-]+/news_\\w+\\.html";
    public static final String URL_SHARE = "http://application\\.self\\.com\\.cn/share/front/total\\?url=http://www\\.vogue\\.com\\.cn/fashion/[\\w-]+/news_\\w+\\.html";
    private Site site = Site.me().setSleepTime(1000).setDomain("http://www.vogue.com.cn");
    @Override
    public void process(Page page) {
        // 如果是请求分享数页面
        if(page.getUrl().regex(URL_SHARE).match()){
            page.putField("url",page.getUrl().regex(URL_POST).toString());
            page.putField("share",new JsonPathSelector("total").select(page.getRawText()).toString());
        }else {
            // 非特殊页面，抓取所有文章项
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
            //如果是文章页面
            if (page.getUrl().regex(URL_POST).match()) {
                page.putField("url",page.getUrl().toString());
                page.putField("title", page.getHtml().xpath("//h1[@class='artitle']/allText()").toString());
                page.putField("article", page.getHtml().xpath("//div[@class='artile-bodycont']/allText()").toString());
                page.putField("date",
                        page.getHtml().xpath("//div[@class='art-author clearfix']/allText()").regex("\\d+年\\d+月\\d+日")
                                .toString()
                                .replace('年','-').replace('月','-').replace('日','\0'));
                // 抓取对应分享数页面
                page.addTargetRequest("http://application.self.com.cn/share/front/total?url="+page.getUrl());
            }
        }
    }
    @Override
    public Site getSite() {
        return site;
    }
    public static void main(String[] args) {
        Spider.create(new VogueSpiderProcessor()).addUrl("http://www.vogue.com.cn/fashion/")
                .addPipeline(new MyPipeline("data")).thread(4).run();
    }
}
