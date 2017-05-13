package com.spider.main;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;

/**
 * Created by 63289 on 2017/5/10.
 */
@HelpUrl("https://www.zhihu.com/explore")
@TargetUrl("https://www.zhihu.com/question/\\d+*")
public class ZhiHuSpider {
    @ExtractBy("//h1[@class='QuestionHeader-title']/text()")
    private String questionTitle;
    @ExtractBy("//div[@class='QuestionRichText QuestionRichText--expandable QuestionRichText--collapsed']" +
            "/div/span[@class='RichText']/text()")
    private String question;
    @ExtractBy("//div[@class='AuthorInfo-head']/allText()")
    private String author;
    @ExtractBy("//div[@class='RichContent-inner']/tidyText()")
    private String answer;
    @ExtractBy("//div[@class='ContentItem-time']/allText()/regex('\\d+-\\d+-\\d+')")
    private String date;
    @ExtractBy("//span[@class='Voters']/allText()/regex('\\d+')")
    private Integer agree;

    public static void main(String[] args){
        OOSpider.create(Site.me().setSleepTime(1000),
                new ConsolePageModelPipeline(),ZhiHuSpider.class)
                .addUrl("https://www.zhihu.com/question/27850529").thread(4).run();
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAgree() {
        return agree;
    }

    public void setAgree(Integer agree) {
        this.agree = agree;
    }
}
