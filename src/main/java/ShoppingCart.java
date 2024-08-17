import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ShoppingCart extends BasePage {
    final By SHOPPING_CART = By.tagName("table");
    final By PRODUCTS_IN_CART = By.cssSelector("tr:has(td[class*='col-sm'])");

    public ShoppingCart(WebDriver driver) {
        super(driver);
    }

    public int getNumberOfProductsInCart() {
        try {
            return waitForElementsToBeVisible(PRODUCTS_IN_CART).size();
        }
        catch (Exception e) {
            return 0;
        }
    }

    public String getProductName(int row) {
        return waitForElementsToBeVisible(PRODUCTS_IN_CART).get(row-1)
                .findElement(By.cssSelector("td .media-body h4.media-heading"))
                .getText();
    }

    public void removeProduct(int row) {
        waitForElementsToBeVisible(PRODUCTS_IN_CART).get(row-1)
                .findElement(By.cssSelector("td button")).click();
    }
}
