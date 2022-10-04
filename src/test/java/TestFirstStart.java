import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void openBrowser(){
        driver.get("https://github.com/");
    }

    @After
    public void closeAndShutDown(){
        driver.quit();
        driver = null;
    }
}
