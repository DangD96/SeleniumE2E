import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.List;

public class CatalogPage extends BasePage {
    public CatalogPage(WebDriver driver) {super(driver);}

    // region Locators and Wrappers
    public final By PRODUCT_LIST = By.cssSelector("app-card-list");
    public final By PRODUCT_CARD = By.cssSelector("app-card");
    public final By PRODUCT_PRICE = By.cssSelector(".card-body h5");
    public final By ADD_PRODUCT_BTN = By.cssSelector(".card-footer button");
    public final By CHECKOUT_BTN = By.partialLinkText("Checkout");
    // endregion


    // region Getters
    public WebElement getProductList() {return getElement(PRODUCT_LIST);}
    public List<WebElement> getAllProducts() {return getElements(PRODUCT_CARD);}
    public WebElement getCheckoutBtn() {return getElement(CHECKOUT_BTN);}

    public String getCheckoutBtnContents() {return getCheckoutBtn().getText();}

    public int getNumberOfProducts() {return getAllProducts().size();}

    public BigDecimal getProductPrice(WebElement product) {
        String price = product.findElement(PRODUCT_PRICE).getText().replace("$","");
        return new BigDecimal(price); // return a BigDecimal due to floating point imprecision
    }

    public BigDecimal getProductPrice(String productName) {
        String price = getAllProducts().stream().filter(p -> p.getText().contains(productName))
                .toList().get(0)
                .findElement(PRODUCT_PRICE).getText().replace("$","");
        return new BigDecimal(price);
    }
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
        product.findElement(ADD_PRODUCT_BTN).click();
        return product;
    }
    // endregion
}
