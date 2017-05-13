package com.spider.main;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by 63289 on 2017/5/9.
 */
@HelpUrl("http://blog.sina.com.cn/s/articlelist_1487828712_0_\\d+.html")
@TargetUrl("http://blog.sina.com.cn/s/blog_\\w+.html")
public class SinaBlogSpider {
    @ExtractBy("//div[@class='articalTitle']/h2")
    private String title;
    @ExtractBy("//div[@id='articlebody']//div[@class='articalContent']")
    private String content;
    @ExtractBy("//div[@id='articlebody']//span[@class='time SG_txtc']")
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static void main(String[] args){
        OOSpider.create(Site.me().setSleepTime(1000)
                , new ConsolePageModelPipeline(), SinaBlogSpider.class)
                .addUrl("http://blog.sina.com.cn/s/articlelist_1487828712_0_1.html").thread(5).run();
    }
}
