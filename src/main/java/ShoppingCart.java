import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class ShoppingCart extends BasePage {
    @FindBy(tagName = "table") private WebElement shoppingCart;
    @FindBy(css = "tr:has(td[class*='col-sm'])") private List<WebElement> productsInCart;

    public ShoppingCart(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public int getNumberOfProductsInCart() {
        return productsInCart.size();
    }

    public String getProductName(int row) {
        return productsInCart.get(row-1).findElement(By.cssSelector("td .media-body h4.media-heading")).getText();
    }

    public void removeProduct(int row) {
        productsInCart.get(row-1).findElement(By.cssSelector("td button")).click();
    }

    public boolean isShoppingCartVisible() {
        return waitForElementToBeVisible(shoppingCart);
    }
}
