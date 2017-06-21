package com.cielo.spider;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.pipeline.JsonFilePageModelPipeline;

/**
 * Created by 63289 on 2017/6/18.
 */
@HelpUrl("http://www.yoka.com/fashion/*")
@TargetUrl("http://www.yoka.com/fashion/\\w+/\\d+/\\d+/\\d+.shtml")
public class YOKASpider {
    @ExtractBy("//h1[@class='infoTitle']/text()")
    private String title;
    @ExtractBy("//div[@class='double_quotes']/allText()")
    private String intro;
    @ExtractBy("//div[@class='textCon']/allText()")
    private String article;
    @ExtractBy("//div[@class='time']/allText()/regex('\\d+-\\d+-\\d+')")
    private String date;

    public static void main(String[] args){
        OOSpider.create(Site.me().setSleepTime(1000),
                new JsonFilePageModelPipeline("data"),YOKASpider.class)
                .addUrl("http://www.yoka.com/fashion/").thread(4).run();
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
