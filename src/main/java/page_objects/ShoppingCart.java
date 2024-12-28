package page_objects;

import framework.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ShoppingCart extends BasePage {
    public ShoppingCart(WebDriver driver) {super(driver);}

    // region Locators
    public final By SHOPPING_CART = By.tagName("table");
    public final By PRODUCTS_IN_CART = By.cssSelector("tr:has(td[class*='col-sm'])");
    public final By PRODUCT_NAME = By.cssSelector("td .media-body h4.media-heading");
    public final By REMOVE_PRODUCDT = By.cssSelector("td button");
    // endregion


    // region Getters
    public int getNumberOfProductsInCart() {
        try {return waitForElementsToBeVisible(PRODUCTS_IN_CART).size();}
        catch (Exception e) {return 0;}
    }

    public String getProductName(int row) {
        return waitForElementsToBeVisible(PRODUCTS_IN_CART).get(row-1).findElement(PRODUCT_NAME).getText();
    }
    // endregion


    // region Performers
    public void removeProduct(int row) {waitForElementsToBeVisible(PRODUCTS_IN_CART).get(row-1).findElement(REMOVE_PRODUCDT).click();}
    // endregion
}
