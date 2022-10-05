import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TestFirstStartFireFox {
    private WebDriver firefoxDriver;
    private WebDriverWait webDriverWait;

    @Before
    public void setUp(){
        firefoxDriver = new FirefoxDriver();
        webDriverWait = new WebDriverWait(firefoxDriver, Duration.ofSeconds(15));
    }

    @Test
    public void logInFireFox(){
        firefoxDriver.get("http://localhost/litecart/admin/");
        firefoxDriver.findElement(By.name("username")).sendKeys("admin");
        firefoxDriver.findElement(By.name("password")).sendKeys("admin");
        firefoxDriver.findElement(By.name("login")).click();
        Assert.assertTrue(firefoxDriver.findElement(By.id("sidebar")).isDisplayed());
    }

    @After
    public void closeAndShutDown(){
        firefoxDriver.quit();
        firefoxDriver = null;
    }
}
