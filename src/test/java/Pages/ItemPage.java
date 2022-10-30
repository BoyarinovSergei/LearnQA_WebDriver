package Pages;

import SetUp.SetUp;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ItemPage extends SetUp {

    @FindBy(xpath = "//select[@name='options[Size]']")
    private WebElement numberOfItemsField;

    @FindBy (css = "table button[name='add_cart_product']")
    private WebElement addProductButton;

    public ItemPage() {
        PageFactory.initElements(driver, this);
    }

    public boolean isNumberOfItemsFieldsPresented(){
        try{
            numberOfItemsField.getText();
            return true;
        }catch (NoSuchElementException e){
            return false;
        }
    }

    public ItemPage sendKeysInNumberOfItems(Keys key){
        numberOfItemsField.sendKeys(key);
        return this;
    }

    public ItemPage clickOnNumberOfItems(){
        numberOfItemsField.click();
        return this;
    }

    public ItemPage clickOnAddProductButton(){
        addProductButton.click();
        return this;
    }
}
