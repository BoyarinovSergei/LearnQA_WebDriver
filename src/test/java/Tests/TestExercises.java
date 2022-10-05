package Tests;

import SetUp.SetUp;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TestExercises extends SetUp {

    @Test
    public void clickOnAllSections(){
        driver.get("http://localhost/litecart/admin/");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();

        List<WebElement> elementList = driver
                .findElements(By.xpath("//div[@id='box-apps-menu-wrapper']//li[@id='app-']"));

        for (int i = 1; i <= elementList.size(); i++){
            driver.findElement(By.xpath(
                    "//div[@id='box-apps-menu-wrapper']//li[@id='app-']["+ i +"]")).click();
            Assert.assertTrue(isElementPresent(By.xpath("//h1")));

            if(isElementPresent(By.xpath("//div[@id='box-apps-menu-wrapper']//li[@id='app-']//li"))){
                List<WebElement> elementList2 = driver
                        .findElements(By.xpath("//div[@id='box-apps-menu-wrapper']//li[@id='app-']//li"));

                for (int t = 1; t <= elementList2.size(); t++){
                    driver.findElement(By.xpath(
                            "//div[@id='box-apps-menu-wrapper']//li[@id='app-']//li["+ t +"]")).click();
                    Assert.assertTrue(isElementPresent(By.xpath("//h1")));
                }
            }
        }
    }
}
