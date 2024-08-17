import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;

public class CatalogPage extends BasePage {
    final By LIST_OF_PRODUCTS = By.cssSelector("app-card-list app-card");
    final By CHECKOUT_BTN = By.partialLinkText("Checkout");

    public CatalogPage(WebDriver driver) {
        super(driver);
    }

    public int getNumberOfProducts() {
        return waitForElementsToBeVisible(LIST_OF_PRODUCTS).size();
    }

    public WebElement addProductToCart(String productName) {
        WebElement product = waitForElementsToBeVisible(LIST_OF_PRODUCTS).stream()
                .filter(p -> p.getText().contains(productName))
                .toList().get(0);
        product.findElement(By.cssSelector(".card-footer button")).click();
        return product;
    }

    public BigDecimal getProductPrice(WebElement product) {
        String rawPriceStr = product.findElement(By.cssSelector(".card-body h5")).getText();
        String price = rawPriceStr.replace("$","");
        return new BigDecimal(price); // return a BigDecimal due to floating point imprecision
    }

    public BigDecimal getProductPrice(String productName) {
        String price = waitForElementsToBeVisible(LIST_OF_PRODUCTS).stream()
                .filter(p -> p.getText().contains(productName))
                .toList().get(0).findElement(By.cssSelector(".card-body h5"))
                .getText().replace("$","");
        return new BigDecimal(price);
    }

    public ShoppingCart goToShoppingCart() {
        waitForElementToBeVisible(CHECKOUT_BTN).click();
        ShoppingCart cart = new ShoppingCart(driver);
        waitForElementToBeVisible(cart.SHOPPING_CART);
        return cart;
    }

    public String getCheckoutBtnContents() {
        return waitForElementToBeVisible(CHECKOUT_BTN).getText();
    }
}
