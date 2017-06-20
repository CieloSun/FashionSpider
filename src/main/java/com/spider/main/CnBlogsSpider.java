package com.spider.main;

import org.apache.http.HttpHost;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * Created by 63289 on 2017/5/9.
 */
@HelpUrl("http://www.cnblogs.com/cielosun/default.html\\?page=\\d+")
@TargetUrl("http://www.cnblogs.com/cielosun/p/\\d+.html")
public class CnBlogsSpider {
    @ExtractBy("//a[@id='cb_post_title_url']/text()")
    private String title;
    @ExtractBy("//div[@id='cnblogs_post_body']/tidyText()")
    private String post;
    @ExtractBy("//div[@class='postDesc']//span[@id='post-date']/text()")
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public static void main(String[] args){
        OOSpider.create(Site.me().setSleepTime(1000),
                new ConsolePageModelPipeline(),CnBlogsSpider.class)
                .addUrl("http://www.cnblogs.com/cielosun").thread(4).run();
    }
}
