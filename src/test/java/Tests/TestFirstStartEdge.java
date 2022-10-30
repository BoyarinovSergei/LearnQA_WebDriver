package Tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TestFirstStartEdge {

    private WebDriver edgeDriver;
    private WebDriverWait webDriverWait;

    @Before
    public void setUp(){
        edgeDriver = new EdgeDriver();
        webDriverWait = new WebDriverWait(edgeDriver, Duration.ofSeconds(15));
    }

    @Test
    public void logInEdge(){
        edgeDriver.get("http://localhost/litecart/admin/");
        edgeDriver.findElement(By.name("username")).sendKeys("admin");
        edgeDriver.findElement(By.name("password")).sendKeys("admin");
        edgeDriver.findElement(By.name("login")).click();
        Assert.assertTrue(edgeDriver.findElement(By.id("sidebar")).isDisplayed());
    }

    @After
    public void closeAndShutDown(){
        edgeDriver.quit();
        edgeDriver = null;
    }
}
