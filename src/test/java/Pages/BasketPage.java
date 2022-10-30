package Pages;

import SetUp.SetUp;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class BasketPage extends SetUp {

    @FindBy (xpath = "//ul[@class='shortcuts']/li[1]")
    private WebElement firstItem;

    @FindBys( {
            @FindBy(css = "#order_confirmation-wrapper tr td[class='item']")} )
    private List<WebElement> orderSummary;

    @FindBy (xpath = "//button[@name='remove_cart_item']")
    private WebElement removeItemButton;

    public BasketPage(){
        PageFactory.initElements(driver, this);
    }

    public BasketPage clickOnFirstItem(){
        firstItem.click();
        return this;
    }

    public int getNumberOfItemsInBasket(){
        return orderSummary.size();
    }

    public BasketPage clickOnRemoveItemButton(){
        removeItemButton.click();
        return this;
    }

    public BasketPage expectCertainNumberOfItems(int finalCounter){
        webDriverWait.until(webDriver -> orderSummary.size() == (finalCounter -1));
        return this;
    }
}
