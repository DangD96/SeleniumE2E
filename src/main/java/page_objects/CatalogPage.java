package page_objects;

import framework.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;

public class CatalogPage extends BasePage {
    public CatalogPage(WebDriver driver) {super(driver);}

    // region Locators
    public final By PRODUCT_LIST = By.cssSelector("app-card-list");
    public final By PRODUCT_CARD = By.cssSelector("app-card");
    public final By PRODUCT_PRICE = By.cssSelector(".card-body h5");
    public final By ADD_PRODUCT_BTN = By.cssSelector(".card-footer button");
    public final By CHECKOUT_BTN = By.partialLinkText("Checkout");
    public final By ANNOYING_BLINKING_TEXT = By.className("blinkingText");
    // endregion


    // region Getters
    public String getCheckoutBtnContents() {return waitForElementToBeVisible(CHECKOUT_BTN).getText();}

    public int getNumberOfProducts() {return waitForElementsToBeVisible(PRODUCT_CARD).size();}

    public BigDecimal getProductPrice(WebElement product) {
        String price = product.findElement(PRODUCT_PRICE).getText().replace("$","");
        return new BigDecimal(price); // return a BigDecimal due to floating point imprecision
    }

    public BigDecimal getProductPrice(String productName) {
        String price = waitForElementsToBeVisible(PRODUCT_CARD).stream().filter(p -> p.getText().contains(productName))
                .toList().get(0)
                .findElement(PRODUCT_PRICE).getText().replace("$","");
        return new BigDecimal(price);
    }
    // endregion


    // region Performers
    public ShoppingCart goToShoppingCart() {
        js.executeScript("arguments[0].remove()", waitForElementToBeVisible(ANNOYING_BLINKING_TEXT));
        waitForElementToBeClickable(CHECKOUT_BTN).click();
        return new ShoppingCart(driver);
    }

    public WebElement addProductToCart(String productName) {
        WebElement product = waitForElementsToBeVisible(PRODUCT_CARD).stream()
                .filter(p -> p.getText().contains(productName))
                .toList().get(0);
        product.findElement(ADD_PRODUCT_BTN).click();
        return product;
    }
    // endregion
}
