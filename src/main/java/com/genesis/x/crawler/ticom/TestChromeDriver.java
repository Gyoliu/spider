package com.genesis.x.crawler.ticom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * @Author liuxing
 * @Date 2021/6/17 16:10
 * @Version 1.0
 * @Description:
 */
public class TestChromeDriver {
    public static void main(String[] args) throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        System.getProperties().setProperty("webdriver.chrome.driver", "D:\\works\\genesisx\\spider\\src\\main\\resources\\chromedriver.exe");
        DesiredCapabilities sCaps = new DesiredCapabilities();
        sCaps.setJavascriptEnabled(true);
        sCaps.setCapability("takesScreenshot", false);
        ArrayList<String> cliArgsCap = new ArrayList();
        cliArgsCap.add("--web-security=false");
        cliArgsCap.add("--ssl-protocol=any");
        cliArgsCap.add("--ignore-ssl-errors=true");
        sCaps.setCapability("phantomjs.cli.args", cliArgsCap);
        sCaps.setCapability("phantomjs.ghostdriver.cli.args", new String[]{"--logLevel=DEBUG"});
        ChromeDriver chromeDriver = new ChromeDriver(sCaps);
        chromeDriver.get("https://ti.com");
        chromeDriver.findElementByClassName("mod-login").click();
        Thread.sleep(1000);
        chromeDriver.findElementByXPath("//*[@id=\"username\"]").sendKeys("Gyoliu@163.com");
        chromeDriver.findElementByXPath("//*[@id=\"nextbutton\"]").click();
        Thread.sleep(1000);
        chromeDriver.findElementByXPath("//*[@id=\"password\"]").sendKeys("Gyo163ti.com");
        chromeDriver.findElementByXPath("//*[@id=\"loginbutton\"]").click();

        chromeDriver.get("https://www.ti.com/product/TMUX7213");
        Thread.sleep(1000);
        WebElement elementByXPath = chromeDriver.findElementByXPath("/html/body/main/div[1]/ti-order-now-table");
        WebElement shadowRoot = (WebElement)chromeDriver.executeScript("return arguments[0].shadowRoot.children[3]", elementByXPath);
        WebElement buy_column_1 = shadowRoot.findElement(By.cssSelector("#buy_column_1 ti-add-to-cart"));
        chromeDriver.executeScript("return arguments[0].shadowRoot.children[0].getElementsByTagName('ti-input')", buy_column_1);
        chromeDriver.executeScript("return arguments[0].shadowRoot.children[0].shadowRoot.children[0]", buy_column_1);
        System.out.println();
        chromeDriver.findElementByXPath("//*[@id=\"buy_column_1\"]/ti-add-to-cart//ti-form-element/ti-input").sendKeys("1");
        chromeDriver.findElementByXPath("//*[@id=\"buy_column_1\"]/ti-add-to-cart//ti-button").click();

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
