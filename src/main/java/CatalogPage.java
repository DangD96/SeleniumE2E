import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.List;

public class CatalogPage extends BasePage {
    public CatalogPage(WebDriver driver) {super(driver);}

    // region Locators
    final By PRODUCT_CARD = By.cssSelector("app-card-list app-card");
    final By CHECKOUT_BTN = By.partialLinkText("Checkout");
    public List<WebElement> getAllProducts() {return waitForElementsToBeVisible(PRODUCT_CARD);}
    public WebElement getCheckoutBtn() {return waitForElementToBeClickable(CHECKOUT_BTN);}
    // endregion


    // region Performers
    public ShoppingCart goToShoppingCart() {
        getCheckoutBtn().click();
        return new ShoppingCart(driver);
    }

    public WebElement addProductToCart(String productName) {
        WebElement product = getAllProducts().stream()
                .filter(p -> p.getText().contains(productName))
                .toList().get(0);
        product.findElement(By.cssSelector(".card-footer button")).click();
        return product;
    }
    // endregion


    // region Getters
    public String getCheckoutBtnContents() {
        return getCheckoutBtn().getText();
    }

    public int getNumberOfProducts() {return getAllProducts().size();}

    public BigDecimal getProductPrice(WebElement product) {
        String rawPriceStr = product.findElement(By.cssSelector(".card-body h5")).getText();
        String price = rawPriceStr.replace("$","");
        return new BigDecimal(price); // return a BigDecimal due to floating point imprecision
    }

    public BigDecimal getProductPrice(String productName) {
        String price = getAllProducts().stream()
                .filter(p -> p.getText().contains(productName))
                .toList().get(0)
                .findElement(By.cssSelector(".card-body h5")).getText().replace("$","");
        return new BigDecimal(price);
    }
    // endregion
}
