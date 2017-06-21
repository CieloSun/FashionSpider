package com.cielo.spider;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by 63289 on 2017/6/21.
 */
public class WeiboSpider {

    public static void main(String[] args) throws Exception{
        System.getProperties().setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://passport.weibo.cn/signin/login?entry=mweibo&res=wel&wm=3349&r=http%3A%2F%2Fm.weibo.cn%2F");
        Thread.sleep(2000);
        webDriver.findElement(By.id("loginName")).clear();
        webDriver.findElement(By.id("loginName")).sendKeys("13846266583");
        webDriver.findElement(By.id("loginPassword")).clear();
        webDriver.findElement(By.id("loginPassword")).sendKeys("Linshimima");
        webDriver.findElement(By.id("loginAction")).click();
        Thread.sleep(10000);
        webDriver.findElement(By.cssSelector("#boxId_1498051064779_1 > div.topBarWrap > div.home-sub-nav.layout-box > a.item.box-col.isActive"));

        webDriver.close();
    }
}
