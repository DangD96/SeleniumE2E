import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ShoppingCart extends BasePage {
    public ShoppingCart(WebDriver driver) {super(driver);}

    // region Locators
    final By SHOPPING_CART = By.tagName("table");
    final By PRODUCTS_IN_CART = By.cssSelector("tr:has(td[class*='col-sm'])");
    public WebElement getShoppingCart() {return waitForElementToBeVisible(SHOPPING_CART);}
    public List<WebElement> getAllProducts() {return waitForElementsToBeVisible(PRODUCTS_IN_CART);}

    // endregion

    // region Performers
    public void removeProduct(int row) {getAllProducts().get(row-1).findElement(By.cssSelector("td button")).click();}
    // endregion


    // region Getters
    public int getNumberOfProductsInCart() {
        try {return getAllProducts().size();}
        catch (Exception e) {return 0;}
    }

    public String getProductName(int row) {
        return getAllProducts().get(row-1).findElement(By.cssSelector("td .media-body h4.media-heading")).getText();
    }
    // endregion
}
