package com.cielo.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by 63289 on 2017/6/21.
 */
public class Test {
    public static void testSelenium() throws Exception {
        System.getProperties().setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("http://huaban.com/");
        Thread.sleep(5000);
        WebElement webElement = webDriver.findElement(By.xpath("/html"));
        System.out.println(webElement.getAttribute("outerHTML"));
        webDriver.close();
    }
    public static void main(String[] args) throws Exception{
        testSelenium();
    }
}
