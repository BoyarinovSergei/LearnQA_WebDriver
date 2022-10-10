package Tests;

import SetUp.SetUp;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.assertj.core.api.SoftAssertions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestExercises extends SetUp {

    private SoftAssertions softAssertions = new SoftAssertions();

    public void authMethod(String address) {
        driver.get(address);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
    }

    @Test
    public void openPageCorrectly(){
        driver.get("http://localhost/litecart/en/");

        String nameOnMainPage = driver.findElement(By.cssSelector("#box-campaigns div div.name")).getText();
        double oldPriceOnMainPage = Double.parseDouble(driver.findElement(
                By.cssSelector("#box-campaigns div div.price-wrapper s")).getText().replaceAll("\\$",""));
        double priceOnMainPage = Double.parseDouble(driver.findElement(
                By.cssSelector("#box-campaigns div div.price-wrapper strong")).getText().replaceAll("\\$",""));

        String[] oldPriceColorOnMainPage = driver.findElement(
                By.cssSelector("#box-campaigns div div.price-wrapper s.regular-price"))
                .getCssValue("color").replaceAll("[^,0-9]", "").split(",");

        Assertions.assertThat(oldPriceColorOnMainPage[0]).as(
                "Цвет зачеркнутой цены на главной странице не является серым").isEqualTo(oldPriceColorOnMainPage[1]);
        Assertions.assertThat(oldPriceColorOnMainPage[1]).as(
                "Цвет зачеркнутой цены на главной странице не является серым").isEqualTo(oldPriceColorOnMainPage[2]);

        String decorationLine1 =  driver.findElement(
                By.cssSelector("#box-campaigns div div.price-wrapper s.regular-price")).getCssValue("text-decoration-line");

        Assertions.assertThat(decorationLine1).as("Старая цена не перечеркнута").isEqualTo("line-through");

        String[] priceColorOnMainPage =
                driver.findElement(By.cssSelector("#box-campaigns div div.price-wrapper strong.campaign-price"))
                        .getCssValue("color").replaceAll("[^,0-9]", "").split(",");

        Assertions.assertThat(priceColorOnMainPage[1]).as("Цвет основной цены на главной странице не является красным")
                .isEqualTo(priceColorOnMainPage[2]);

        int fontWeightOfPriceOnMainPage = Integer.parseInt(driver.findElement(
                By.cssSelector("#box-campaigns div div.price-wrapper strong.campaign-price"))
                        .getCssValue("font-weight"));

        Assertions.assertThat(fontWeightOfPriceOnMainPage >= 700).as(
                "Текст актуальной цены на странице товара не является жирным").isTrue();

        double fontSizeOfOldPriceOnMainPage = Double.parseDouble(driver.findElement(
                By.cssSelector("#box-campaigns div div.price-wrapper s"))
                .getCssValue("font-size").replaceAll("px",""));

        double fontSizeOfPriceOnMainPage = Double.parseDouble(driver.findElement(
                By.cssSelector("#box-campaigns div div.price-wrapper strong"))
                .getCssValue("font-size").replaceAll("px",""));

        Assertions.assertThat(fontSizeOfOldPriceOnMainPage < fontSizeOfPriceOnMainPage)
                .as("На главной странице, зачеркнутая цена оказалась размером больше, чем обычная").isTrue();

        //переход в карточку товара
        driver.findElement(By.cssSelector("#box-campaigns a.link")).click();

        String decorationLine2 =  driver.findElement(
                By.cssSelector("#box-product div.price-wrapper s.regular-price")).getCssValue("text-decoration-line");

        Assertions.assertThat(decorationLine2).as("Старая цена не перечеркнута").isEqualTo("line-through");

        String nameOnProductPage = driver.findElement(By.cssSelector("h1")).getText();

        double oldPriceOnProductPage = Double.parseDouble(driver.findElement(
                By.cssSelector("div.price-wrapper s")).getText().replaceAll("\\$",""));
        double priceOnProductPage = Double.parseDouble(driver.findElement(
                By.cssSelector("div.price-wrapper strong")).getText().replaceAll("\\$",""));

        String[] oldPriceColorOnProductPage = driver.findElement(
                By.cssSelector("div.price-wrapper s")).getCssValue("color")
                .replaceAll("[^,0-9]", "").split(",");

        Assertions.assertThat(oldPriceColorOnProductPage[0]).as(
                "Цвет не является серым на странице продукта").isEqualTo(oldPriceColorOnProductPage[1]);
        Assertions.assertThat(oldPriceColorOnProductPage[1]).as(
                "Цвет не является серым на странице продукта").isEqualTo(oldPriceColorOnProductPage[2]);

        String[] priceColorOnProductPage =
                driver.findElement(By.cssSelector("div.price-wrapper strong"))
                        .getCssValue("color").replaceAll("[^,0-9]", "").split(",");

        Assertions.assertThat(priceColorOnProductPage[1]).as("Цвет не является красным").isEqualTo(priceColorOnProductPage[2]);

        int fontWeightOfPriceOnProductPage =
                Integer.parseInt(driver.findElement(By.cssSelector("div.price-wrapper strong.campaign-price"))
                        .getCssValue("font-weight"));

        Assertions.assertThat(fontWeightOfPriceOnProductPage >= 700).as(
                "Текст актуальной цены на странице товара не является жирным").isTrue();

        Assertions.assertThat(oldPriceOnMainPage).as(
                "Зачеркнутая цена на главной странице отличается от цены на странице товара")
                .isEqualTo(oldPriceOnProductPage);

        Assertions.assertThat(priceOnMainPage).as(
                "Основная цена на главной странице отличается от цены на странице товара")
                .isEqualTo(priceOnProductPage);

        Assertions.assertThat(nameOnMainPage).as(
                "Название товара на главной странице и в карточке товара отличаются").isEqualTo(nameOnProductPage);
    }

    @Test
    public void zoneSequence(){
        var unsortedListOfZones = new ArrayList<String>();
        var alphabeticallySortedListOfZones = new ArrayList<String>();

        authMethod("http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones");
        List<WebElement> listOfCountries = driver.findElements(By.xpath(
                "//table[@class='dataTable']//td[3]/a"));

        for(int i = 0; i < listOfCountries.size(); i++){
            WebElement element = listOfCountries.get(i);
            element.click();

            List<WebElement> elementList = driver.findElements(
                    By.xpath("//table[@id='table-zones']//td[3]/select/option[@selected='selected']"));

            for(int f = 0; f < elementList.size(); f++){
                WebElement element1 = elementList.get(f);
                unsortedListOfZones.add(element1.getText());
            }

            alphabeticallySortedListOfZones = (ArrayList<String>) unsortedListOfZones.clone();
            Collections.sort(alphabeticallySortedListOfZones);

            softAssertions.assertThat(alphabeticallySortedListOfZones)
                    .as("Элементы расположены не в алфавитном порядке на странице внутри страны")
                    .isEqualTo(unsortedListOfZones);

            driver.navigate().back();
            alphabeticallySortedListOfZones.clear();
            unsortedListOfZones.clear();
            listOfCountries = driver.findElements(By.xpath(
                    "//table[@class='dataTable']//td[3]/a"));
        }
        softAssertions.assertAll();
    }

    @Test
    public void alphabeticalSequence(){
        var unsortedListOfCountries = new ArrayList<String>();
        var alphabeticallySortedListOfCountries = new ArrayList<String>();

        authMethod("http://localhost/litecart/admin/?app=countries&doc=countries");

        List<WebElement> listOfCountries = driver.findElements(By.xpath("//table//tr/td[5]"));

        for(int i = 0; i < listOfCountries.size(); i++){
            WebElement element = listOfCountries.get(i);
            unsortedListOfCountries.add(element.getText());
        }

        alphabeticallySortedListOfCountries = (ArrayList<String>) unsortedListOfCountries.clone();
        Collections.sort(alphabeticallySortedListOfCountries);

        Assert.assertTrue(alphabeticallySortedListOfCountries.equals(unsortedListOfCountries));

        List<WebElement> listOfZonesMainPage = driver.findElements(By.xpath("//table//tr/td[6]"));
        List<WebElement> listOfZones;
        var unsortedListOfTimezones = new ArrayList<String>();
        var alphabeticallySortedListOfTimezones = new ArrayList<String>();

        for(int i = 0; i < listOfZonesMainPage.size(); i++){
            WebElement element = listOfZonesMainPage.get(i);
            if(!element.getText().equals("0")){
                driver.findElement(By.xpath("(//table//tr/td[5])["+ (i + 1) +"]/a")).click();
                listOfZones = driver.findElements(By.xpath(
                        "(//table[@id='table-zones']//tr/td[3])[not(position()=last())]"));

                for(int b = 0; b < listOfZones.size(); b++){
                    WebElement element2 = listOfZones.get(b);
                    unsortedListOfTimezones.add(element2.getText());
                }

                alphabeticallySortedListOfTimezones = (ArrayList<String>) unsortedListOfTimezones.clone();
                Collections.sort(alphabeticallySortedListOfTimezones);

                softAssertions.assertThat(alphabeticallySortedListOfTimezones)
                        .as("Элементы расположены не в алфавитном порядке на странице страны")
                        .isEqualTo(unsortedListOfTimezones);

                driver.navigate().back();
                alphabeticallySortedListOfTimezones.clear();
                unsortedListOfTimezones.clear();
                listOfZonesMainPage = driver.findElements(By.xpath("//table//tr/td[6]"));
            }
        }
        softAssertions.assertAll();
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