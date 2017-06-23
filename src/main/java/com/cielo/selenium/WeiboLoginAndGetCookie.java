package com.cielo.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * Created by 63289 on 2017/6/21.
 */
public class WeiboLoginAndGetCookie {
    public static void main(String[] args) throws Exception{
        //配置ChromeDiver
        System.getProperties().setProperty("webdriver.chrome.driver", "chromedriver.exe");
        //开启新WebDriver进程
        WebDriver webDriver = new ChromeDriver();
        //全局隐式等待
        webDriver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
        //设定网址
        webDriver.get("https://passport.weibo.cn/signin/login?entry=mweibo&res=wel&wm=3349&r=http%3A%2F%2Fm.weibo.cn%2F");
        //显示等待控制对象
        WebDriverWait webDriverWait=new WebDriverWait(webDriver,10);
        //等待输入框可用后输入账号密码
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("loginName"))).sendKeys(args[0]);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("loginPassword"))).sendKeys(args[1]);
        //点击登录
        webDriver.findElement(By.id("loginAction")).click();
        //等待2秒用于页面加载，保证Cookie响应全部获取。
        sleep(2000);
        //获取Cookie并打印
        Set<Cookie> cookies=webDriver.manage().getCookies();
        Iterator iterator=cookies.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next().toString());
        }
        //关闭WebDriver,否则并不自动关闭
        webDriver.close();
    }
}
