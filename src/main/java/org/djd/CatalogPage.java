package org.djd;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CatalogPage extends BasePage{
    @FindBy(css = "app-card-list app-card")
    List<WebElement> productList;
    @FindBy(partialLinkText = "Checkout")
    WebElement checkoutBtn;

    public CatalogPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public CheckoutPage goToCheckout() {
        waitForElementToBeVisible(checkoutBtn);
        checkoutBtn.click();
        return new CheckoutPage(driver);
    }

    public int getNumberOfProducts() {
        return productList.size();
    }

    public WebElement addProductToCart(String productName) {
        WebElement product = productList.stream().filter(p -> p.getText().contains(productName)).toList().get(0);
        product.findElement(By.cssSelector(".card-footer button")).click();
        return product;
    }

    public BigDecimal getProductPrice(WebElement product) {
        String rawPriceStr = product.findElement(By.cssSelector(".card-body h5")).getText();
        String[] splitPriceStr = rawPriceStr.split("\\$");
        String price = Arrays.asList(splitPriceStr).get(1);
        return new BigDecimal(price);
    }

}
