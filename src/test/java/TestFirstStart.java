import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TestFirstStart {
    private WebDriver chromeDriver;

    private WebDriverWait webDriverWait;

    @Before
    public void setUp(){
        chromeDriver = new ChromeDriver();
        webDriverWait = new WebDriverWait(chromeDriver, Duration.ofSeconds(15));
    }

    @Test
    public void openBrowser(){
        chromeDriver.get("https://github.com/");
    }

    @Test
    public void logIn(){
        chromeDriver.get("http://localhost/litecart/admin/");
        chromeDriver.findElement(By.name("username")).sendKeys("admin");
        chromeDriver.findElement(By.name("password")).sendKeys("admin");
        chromeDriver.findElement(By.name("login")).click();
        Assert.assertTrue(chromeDriver.findElement(By.id("sidebar")).isDisplayed());
    }

    @After
    public void closeAndShutDown(){
        chromeDriver.quit();
        chromeDriver = null;
    }
}
