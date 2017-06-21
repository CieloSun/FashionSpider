package com.cielo.spider;

import com.cielo.pipeline.MyFilePipeline;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by 63289 on 2017/6/21.
 */

public class VogueBrandSpider implements PageProcessor {
    public static final String URL = "http://brand\\.vogue\\.com\\.cn";
    private Site site = Site.me().setSleepTime(1000).setDomain("http://www.vogue.com.cn");
    @Override
    public void process(Page page) {
        if (page.getUrl().regex(URL).match()) {
            page.putField("title", page.getHtml().xpath("//div[@class='cnt']").toString());
        }
    }
    @Override
    public Site getSite() {
        return site;
    }
    public static void main(String[] args) {
        Spider.create(new VogueBrandSpider()).addUrl("http://brand.vogue.com.cn")
                .addPipeline(new MyFilePipeline("target/brand")).run();
    }
}

