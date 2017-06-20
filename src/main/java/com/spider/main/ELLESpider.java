package com.spider.main;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.pipeline.JsonFilePageModelPipeline;

/**
 * Created by 63289 on 2017/6/18.
 */
@HelpUrl("http://www.ellechina.com/fashion/*")
@TargetUrl("http://www.ellechina.com/fashion/\\w+(/\\w+)?/[\\d-]+([\\w-]+)?.shtml")
public class ELLESpider {
    @ExtractBy("//h1[@class='c-article_title']/allText()")
    private String title;
    @ExtractBy("//div[@class='article-intro']/p[@class='lead']/allText()")
    private String intro;
    @ExtractBy("//div[@class='c-article-content']/allText()")
    private String article;
    @ExtractBy("//div[@class='c-article-source']/allText()/regex('\\d+-\\d+-\\d+')")
    private String date;

    public static void main(String[] args) {
        OOSpider.create(Site.me().setSleepTime(1000),
                new JsonFilePageModelPipeline("data"), ELLESpider.class)
                .addUrl("http://www.ellechina.com/fashion/look/20170616-265235.shtml").thread(4).run();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }
}