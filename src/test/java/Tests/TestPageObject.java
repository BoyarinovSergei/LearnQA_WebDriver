package Tests;

import Pages.BasketPage;
import Pages.ItemPage;
import Pages.MainPage;
import SetUp.SetUp;
import org.junit.Test;
import org.openqa.selenium.Keys;

public class TestPageObject extends SetUp {
    private MainPage mainPage = new MainPage();
    private ItemPage itemPage = new ItemPage();
    private BasketPage basketPage = new BasketPage();

    @Test
    public void testBasketChecker(){
        open("http://localhost/litecart/en/");

        addProductAndCheckBasketCounter("1");
        addProductAndCheckBasketCounter("2");
        addProductAndCheckBasketCounter("3");

        mainPage.clickOnCheckOutButton()
                .clickOnFirstItem();

        int numberOfItemsInBasket = basketPage.getNumberOfItemsInBasket();

        while(numberOfItemsInBasket != 0){
            basketPage.clickOnRemoveItemButton();

            int finalCounter = numberOfItemsInBasket;
            basketPage.expectCertainNumberOfItems(finalCounter);

            numberOfItemsInBasket = basketPage.getNumberOfItemsInBasket();
        }
    }

    public void addProductAndCheckBasketCounter(String counterShouldBe){
        mainPage.clickOnFirstMostPopularItem();

        if(itemPage.isNumberOfItemsFieldsPresented()){
            itemPage.clickOnNumberOfItems()
                    .sendKeysInNumberOfItems(Keys.DOWN)
                    .sendKeysInNumberOfItems(Keys.ENTER)
                    .clickOnAddProductButton();
        }else {
            itemPage.clickOnAddProductButton();
        }

        mainPage.waitUntilCounterChanged(counterShouldBe)
                .clickOnLogoButton();
    }
}
