import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;

public class CatalogPage extends BasePage {
    final By listOfProductsBy = By.cssSelector("app-card-list app-card");
    final By checkoutBtnBy = By.partialLinkText("Checkout");

    public CatalogPage(WebDriver driver) {
        super(driver);
    }

    public int getNumberOfProducts() {
        return waitForElementsToBeVisible(listOfProductsBy).size();
    }

    public WebElement addProductToCart(String productName) {
        WebElement product = waitForElementsToBeVisible(listOfProductsBy).stream()
                .filter(p -> p.getText().contains(productName))
                .toList().get(0);
        System.out.println("PRODUCT: " + product);
        product.findElement(By.cssSelector(".card-footer button")).click();
        return product;
    }

    public BigDecimal getProductPrice(WebElement product) {
        String rawPriceStr = product.findElement(By.cssSelector(".card-body h5")).getText();
        String price = rawPriceStr.replace("$","");
        return new BigDecimal(price); // return a BigDecimal due to floating point imprecision
    }

    public BigDecimal getProductPrice(String productName) {
        String price = waitForElementsToBeVisible(listOfProductsBy).stream()
                .filter(p -> p.getText().contains(productName))
                .toList().get(0).findElement(By.cssSelector(".card-body h5"))
                .getText().replace("$","");
        return new BigDecimal(price);
    }

    public ShoppingCart goToShoppingCart() {
        waitForElementToBeVisible(checkoutBtnBy).click();
        ShoppingCart cart = new ShoppingCart(driver);
        waitForElementToBeVisible(cart.shoppingCartBy);
        return cart;
    }

    public String getCheckoutBtnContents() {
        return waitForElementToBeVisible(checkoutBtnBy).getText();
    }
}
