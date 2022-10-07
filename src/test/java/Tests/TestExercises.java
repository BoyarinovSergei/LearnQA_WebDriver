package Tests;

import SetUp.SetUp;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.assertj.core.api.SoftAssertions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestExercises extends SetUp {

    public void authMethod(String address) {
        driver.get(address);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
    }

    @Test
    public void alphabeticalSequence(){
        var unsortedListOfCountries = new ArrayList<String>();
        var alphabeticallySortedListOfCountries = new ArrayList<String>();
        SoftAssertions softAssertions = new SoftAssertions();

        authMethod("http://localhost/litecart/admin/?app=countries&doc=countries");

        List<WebElement> listOfCountries = driver.findElements(By.xpath("//table//tr/td[5]"));

        for(int i = 0; i < listOfCountries.size(); i++){
            WebElement element = listOfCountries.get(i);
            unsortedListOfCountries.add(element.getText());
        }

        alphabeticallySortedListOfCountries = (ArrayList<String>) unsortedListOfCountries.clone();
        Collections.sort(alphabeticallySortedListOfCountries);

        Assert.assertThat(alphabeticallySortedListOfCountries,
                IsIterableContainingInOrder.contains(unsortedListOfCountries.toArray()));


        List<WebElement> listOfZonesMainPage = driver.findElements(By.xpath("//table//tr/td[6]"));
        List<WebElement> listOfZones;
        var unsortedListOfTimezones = new ArrayList<String>();
        var alphabeticallySortedListOfTimezones = new ArrayList<String>();

        for(int i = 0; i < listOfZonesMainPage.size(); i++){
            WebElement element = listOfZonesMainPage.get(i);
            if(!element.getText().equals("0")){
                driver.findElement(By.xpath("(//table//tr/td[5])["+ (i + 1) +"]/a")).click();
                listOfZones = driver.findElements(By.xpath("//table[@id='table-zones']//tr/td[3]"));

                alphabeticallySortedListOfTimezones = (ArrayList<String>) unsortedListOfTimezones.clone();
                Collections.sort(alphabeticallySortedListOfTimezones);

                softAssertions.assertThat(alphabeticallySortedListOfTimezones)
                        .as("Список геозон расположен не в алфавитом порядке")
                        .isEqualTo(IsIterableContainingInOrder.contains(unsortedListOfTimezones.toArray()));
                driver.navigate().back();
                listOfZonesMainPage = driver.findElements(By.xpath("//table//tr/td[6]"));
            }
            softAssertions.assertAll();
        }
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