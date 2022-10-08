package SetUp;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SetUp {

    protected WebDriver driver;
    protected WebDriverWait webDriverWait;

    @Before
    public void setUpDriver(){
        driver = new ChromeDriver();
//        driver = new EdgeDriver();
//        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1));
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @After
    public void closeAndShutDown(){
        driver.quit();
        driver = null;
    }
}
