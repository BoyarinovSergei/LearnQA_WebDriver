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
    private WebDriver driver;
    private WebDriverWait webDriverWait;

    @Before
    public void setUp(){
        driver = new ChromeDriver();
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    public void openBrowser(){
        driver.get("https://github.com/");
    }

    @Test
    public void logIn(){
        driver.get("http://localhost/litecart/admin/");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
        Assert.assertTrue(driver.findElement(By.id("sidebar")).isDisplayed());
    }

    @After
    public void closeAndShutDown(){
        driver.quit();
        driver = null;
    }
}
