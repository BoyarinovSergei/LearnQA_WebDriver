package Tests;

import SetUp.SetUp;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TestExercises extends SetUp {

    public void authMethod(String address) {
        driver.get(address);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
    }

    @Test
    public void clickOnAllSections() {
        authMethod("http://localhost/litecart/admin/");

        List<WebElement> elementList = driver
                .findElements(By.xpath("//div[@id='box-apps-menu-wrapper']//li[@id='app-']"));

        for (int i = 1; i <= elementList.size(); i++) {
            driver.findElement(By.xpath(
                    "//div[@id='box-apps-menu-wrapper']//li[@id='app-'][" + i + "]")).click();
            Assert.assertTrue(isElementPresent(By.xpath("//h1")));

            if (isElementPresent(By.xpath("//div[@id='box-apps-menu-wrapper']//li[@id='app-']//li"))) {
                List<WebElement> elementList2 = driver
                        .findElements(By.xpath("//div[@id='box-apps-menu-wrapper']//li[@id='app-']//li"));

                for (int t = 1; t <= elementList2.size(); t++) {
                    driver.findElement(By.xpath(
                            "//div[@id='box-apps-menu-wrapper']//li[@id='app-']//li[" + t + "]")).click();
                    Assert.assertTrue(isElementPresent(By.xpath("//h1")));
                }
            }
        }
    }

    @Test
    public void checkStickers() {
        driver.get("http://localhost/litecart/en/");

        Assert.assertTrue(stickerChecker("box-most-popular"));
        Assert.assertTrue(stickerChecker("box-campaigns"));
        Assert.assertTrue(stickerChecker("box-latest-products"));
    }

    public Boolean stickerChecker(String name){
        List<WebElement> listOfLatestProducts = driver.findElements(
                By.xpath("//div[@id='"+ name +"']//ul[@class='listing-wrapper products']//li"));

        for (int i = 1; i <= listOfLatestProducts.size(); i++) {
            Assert.assertEquals(1, driver.findElements(By.xpath("//div[@id='"+ name +"']//" +
                    "ul[@class='listing-wrapper products']//li[" + i + "]//div[contains(@class, 'sticker')]")).size());
        }
        return true;
    }
}