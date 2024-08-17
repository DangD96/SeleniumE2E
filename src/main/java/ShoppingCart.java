import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ShoppingCart extends BasePage {
    final By shoppingCartBy = By.tagName("table");
    final By productsInCartBy = By.cssSelector("tr:has(td[class*='col-sm'])");

    public ShoppingCart(WebDriver driver) {
        super(driver);
    }

    public int getNumberOfProductsInCart() {
        return waitForElementsToBeVisible(productsInCartBy).size();
    }

    public String getProductName(int row) {
        return waitForElementsToBeVisible(productsInCartBy).get(row-1)
                .findElement(By.cssSelector("td .media-body h4.media-heading"))
                .getText();
    }

    public void removeProduct(int row) {
        waitForElementsToBeVisible(productsInCartBy).get(row-1)
                .findElement(By.cssSelector("td button")).click();
    }
}
