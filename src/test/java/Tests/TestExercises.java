package Tests;

import SetUp.SetUp;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.assertj.core.api.SoftAssertions;

import java.io.File;
import java.util.*;

import static Help.Helper.*;

public class TestExercises extends SetUp {

    private SoftAssertions softAssertions = new SoftAssertions();

    public void authMethod(String address) {
        driver.get(address);
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
    }

    @Test
    public void testCheckLog(){
        authMethod("http://localhost/litecart/admin/?app=catalog&doc=catalog&category_id=1");

        List<WebElement> listOfElements = driver.findElements(By.xpath(
                "//table[@class='dataTable']//tr/td[3]/a[contains(@href, 'edit_product')]"));

        for(int i = 0; listOfElements.size() > i; i++){
            listOfElements.get(i).click();

            softAssertions.assertThat(driver.manage().logs().get("browser").getAll().size()).as(
                    "В логах 'browser' появилась запись").isEqualTo(0);

            softAssertions.assertThat(driver.manage().logs().get("driver").getAll().size()).as(
                    "В логах 'driver' появилась запись").isEqualTo(0);

            softAssertions.assertThat(driver.manage().logs().get("client").getAll().size()).as(
                    "В логах 'client' появилась запись").isEqualTo(0);

            driver.navigate().back();
            listOfElements.clear();
            listOfElements = driver.findElements(By.xpath(
                    "//table[@class='dataTable']//tr/td[3]/a[contains(@href, 'edit_product')]"));

            softAssertions.assertAll();
        }
    }

    @Test
    public void testCheckLinksInNewTab(){
        String newWindow;

        authMethod("http://localhost/litecart/admin/");
        driver.findElement(By.xpath("//span[contains(text(),'Countries')]")).click();

        String firstHandle = driver.getWindowHandle();

        Set<String> setOfOldHandle  = driver.getWindowHandles();

        driver.findElement(By.xpath("(//td[@id='content'] //td/a/i)[1]")).click();

        List<WebElement> list = driver.findElements(
                By.cssSelector("#content a[target='_blank'] i[class='fa fa-external-link']"));

        for(int i = 0; i < list.size(); i++){
            list.get(i).click();
            newWindow = webDriverWait.until(anyWindowOtherThan(setOfOldHandle));
            driver.switchTo().window(newWindow);
            driver.close();
            driver.switchTo().window(firstHandle);
        }
    }

    @Test
    public void testWorkWithBasket(){
        driver.get("http://localhost/litecart/en/");
        addProductAndCheckBasketCounter("1");
        addProductAndCheckBasketCounter("2");
        addProductAndCheckBasketCounter("3");

        driver.findElement(By.cssSelector("#cart-wrapper a[class='link']")).click();
        driver.findElement(By.xpath("//ul[@class='shortcuts']/li[1]")).click();

        int counter = driver.findElements(By.cssSelector("#order_confirmation-wrapper tr td[class='item']")).size();

        while(counter != 0){
            driver.findElement(By.xpath("//button[@name='remove_cart_item']")).click();

            int finalCounter = counter;
            webDriverWait.until(webDriver ->
                    driver.findElements(
                            By.cssSelector("#order_confirmation-wrapper tr td[class='item']"))
                            .size() == (finalCounter -1));

            counter = driver.findElements(By.cssSelector("#order_confirmation-wrapper tr td[class='item']")).size();
        }
    }

    public void addProductAndCheckBasketCounter(String counterShouldBe){
        driver.findElement(By.xpath("//div[@id='box-most-popular']//li[1]")).click();

        if(isElementPresentBy(By.xpath("//select[@name='options[Size]']"))){
            driver.findElement(By.xpath("//select[@name='options[Size]']")).click();
            driver.findElement(By.xpath("//select[@name='options[Size]']")).sendKeys(Keys.DOWN);
            driver.findElement(By.xpath("//select[@name='options[Size]']")).sendKeys(Keys.ENTER);
            driver.findElement(By.cssSelector("table button[name='add_cart_product']")).click();
        }else {
            driver.findElement(By.cssSelector("table button[name='add_cart_product']")).click();
        }

        webDriverWait.until(webDriver -> driver.findElement(
                By.cssSelector("#cart-wrapper .quantity")).getText().equals(counterShouldBe));
        driver.findElement(By.cssSelector("#logotype-wrapper")).click();
    }

    @Test
    public void addNewProduct(){
        var nameOfProduct = "Product " + generateString("123456", 3);
        var path = new File("src/files/2862.jpg").getAbsolutePath();

        authMethod("http://localhost/litecart/admin/?app=catalog&doc=catalog");

        driver.findElement(By.xpath("//a[contains(text(),' Add New Product')]")).click();

        driver.findElement(By.xpath("//input[@name='status' and @value='1']")).click();
        driver.findElement(By.xpath("//input[@name='name[en]']"))
                .sendKeys(nameOfProduct);
        driver.findElement(By.xpath("//input[@name='code']"))
                .sendKeys("Product " + generateString("1234567890", 5));
        driver.findElement(By.xpath("//td[contains(text(),'Unisex')]")).click();
        driver.findElement(By.xpath("//input[@name='quantity']"))
                .sendKeys("3");

        driver.findElement(By.xpath("//input[@name='new_images[]']")).sendKeys(path);

        driver.findElement(By.cssSelector("#content a[href='#tab-information']")).click();// преход на вкладку 'Information'
        driver.findElement(By.cssSelector("input[name='keywords']")).sendKeys("Duck 40 000");
        driver.findElement(By.cssSelector("input[name='short_description[en]']")).sendKeys("New Duck");
        driver.findElement(By.cssSelector("div.trumbowyg-editor")).sendKeys("New Duck");

        driver.findElement(By.cssSelector("#content a[href='#tab-prices']")).click(); // преход на вкладку 'Prices'

        driver.findElement(By.cssSelector("input[name='purchase_price']")).sendKeys("1");
        driver.findElement(By.cssSelector("select[name='purchase_price_currency_code']")).click();
        driver.findElement(By.cssSelector("select[name='purchase_price_currency_code']")).sendKeys(Keys.DOWN);
        driver.findElement(By.cssSelector("select[name='purchase_price_currency_code']")).sendKeys(Keys.ENTER);


        driver.findElement(By.cssSelector("input[name='gross_prices[USD]']")).sendKeys("3");
        driver.findElement(By.cssSelector("input[name='gross_prices[EUR]']")).sendKeys("3");
        driver.findElement(By.cssSelector("button[name='save']")).click();

        var actualName =
                driver.findElement(By.xpath("(//form[@name='catalog_form']//td//a)[last()-1]")).getText();

        Assertions.assertThat(nameOfProduct).as(
                "Продукт не был создан").isEqualTo(actualName);
    }

    @Test
    public void createNewCustomer(){
        var email = generateString("qwertyuioasdfghjklzxcvbnm", 6) + "@yandex.ru";
        var password = generateString("qwertyuioasdfghjklzxcvbnm", 15);

        driver.get("http://localhost/litecart/en/");
        driver.findElement(By.cssSelector("#box-account-login table a ")).click();

        driver.findElement(By.cssSelector("#create-account input[name=tax_id]")).sendKeys("12345");
        driver.findElement(By.cssSelector("#create-account input[name=company]")).sendKeys("Name");
        driver.findElement(By.cssSelector("#create-account input[name=firstname]")).sendKeys("First name");
        driver.findElement(By.cssSelector("#create-account input[name=lastname]")).sendKeys("Last name");
        driver.findElement(By.cssSelector("#create-account input[name=address1]")).sendKeys("address1");
        driver.findElement(By.cssSelector("#create-account input[name=address2]")).sendKeys("address2");
        driver.findElement(By.cssSelector("#create-account input[name=postcode]")).sendKeys("12345");
        driver.findElement(By.cssSelector("#create-account input[name=city]")).sendKeys("City");
        driver.findElement(By.cssSelector("#create-account span.selection span span")).click();
        driver.findElement(By.cssSelector("input.select2-search__field")).sendKeys("United States");
        driver.findElement(By.cssSelector("input.select2-search__field")).sendKeys(Keys.ENTER);
        driver.findElement(By.cssSelector("#create-account input[name=email]"))
                .sendKeys(email);
        driver.findElement(By.cssSelector("#create-account input[name=phone]")).sendKeys("+1234567890");
        driver.findElement(By.cssSelector("#create-account input[name=password]")).sendKeys(password);
        driver.findElement(By.cssSelector("#create-account input[name=confirmed_password]")).sendKeys(password);
        driver.findElement(By.cssSelector("#create-account button[name=create_account]")).click();
        driver.findElement(By.cssSelector("#box-account a[href='http://localhost/litecart/en/logout']")).click();
        driver.findElement(By.cssSelector("input[name='email']")).sendKeys(email);
        driver.findElement(By.cssSelector("input[name='password']")).sendKeys(password);
        driver.findElement(By.cssSelector("button[name='login']")).click();

        Assertions.assertThat(isElementPresentBy(
                By.cssSelector("#box-account a[href='http://localhost/litecart/en/logout']")))
                .as("Автоирзоваться не получилось").isTrue();

        driver.findElement(By.cssSelector("#box-account a[href='http://localhost/litecart/en/logout']")).click();
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

        Assertions.assertThat(priceColorOnMainPage[1]).as("Цвет основной цены на главной странице не является красным").isEqualTo("0");
        Assertions.assertThat(priceColorOnMainPage[2]).as("Цвет основной цены на главной странице не является красным").isEqualTo("0");

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

        Assertions.assertThat(priceColorOnProductPage[1]).as("Цвет не является красным").isEqualTo("0");
        Assertions.assertThat(priceColorOnProductPage[2]).as("Цвет не является красным").isEqualTo("0");

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
            Assert.assertTrue(isElementPresentBy(By.xpath("//h1")));

            if (isElementPresentBy(By.xpath("//div[@id='box-apps-menu-wrapper']//li[@id='app-']//li"))) {
                List<WebElement> elementList2 = driver
                        .findElements(By.xpath("//div[@id='box-apps-menu-wrapper']//li[@id='app-']//li"));

                for (int t = 1; t <= elementList2.size(); t++) {
                    driver.findElement(By.xpath(
                            "//div[@id='box-apps-menu-wrapper']//li[@id='app-']//li[" + t + "]")).click();
                    Assert.assertTrue(isElementPresentBy(By.xpath("//h1")));
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