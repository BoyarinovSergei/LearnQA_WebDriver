package Pages;

import SetUp.SetUp;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MainPage  extends SetUp {

    @FindBy (xpath = "//div[@id='box-most-popular']//li[1]")
    private WebElement firstMostPopularItem;

    @FindBy (css = "#cart-wrapper .quantity")
    private WebElement numberOfItemsInBasket;

    @FindBy (css = "#logotype-wrapper")
    private WebElement logoButton;

    @FindBy (css = "#cart-wrapper a[class='link']")
    private WebElement checkOutButton;

    public MainPage() {
        PageFactory.initElements(driver, this);
    }

    public BasketPage clickOnCheckOutButton(){
        checkOutButton.click();
        return new BasketPage();
    }

    public void clickOnFirstMostPopularItem(){
        firstMostPopularItem.click();
    }

    public MainPage waitUntilCounterChanged(String counterShouldBe){
        webDriverWait.until(webDriver -> numberOfItemsInBasket.getText().equals(counterShouldBe));
        return this;
    }

    public MainPage clickOnLogoButton(){
        logoButton.click();
        return this;
    }
}
