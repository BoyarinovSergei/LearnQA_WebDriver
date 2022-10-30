package SetUp;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SetUp {

    public static WebDriver driver;
    public static WebDriverWait webDriverWait;

    @BeforeClass
    public static void setUpDriver(){
        driver = new ChromeDriver();
//        driver = new EdgeDriver();
//        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1));
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @After
    public void closeAndShutDown(){
        driver.quit();
        driver = null;
    }

    public static void open(String url){
        driver.get(url);
    }
}
