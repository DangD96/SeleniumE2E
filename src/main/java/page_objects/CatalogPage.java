package page_objects;

import framework.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;

public class CatalogPage extends BasePage {
    public CatalogPage(WebDriver driver) {
        super(driver);
    }

    // region Locators
    public By CheckoutBtn() {return By.xpath("//a[contains(text(), 'Checkout')]");}

    public By EditButtonForName(String name) {
        return By.xpath(String.format("//div[@class='row' and .//span[text()='%s']]//button[contains(@class,'edit-btn')]", name));
    }

    public By Product() {return By.xpath("//app-card");}

    // The "[.//" part is for searching for a descendant
    public By Product(String name) {
        return By.xpath(String.format("//app-card[.//*[@class='card-title']//a[text()='%s']]", name));
    }

    public By Product(int n) {return By.xpath(String.format("//app-card[%d]", n));}

    public By ProductName(String name) {
        return AppendToXpath(Product(name), String.format("//a[text()='%s']", name));
    }

    public By ProductName(int n) {return By.xpath(String.format("//app-card[%d]//a", n));}

    public By ProductPrice(String name) {return AppendToXpath(Product(name), "//h5");}

    public By AddProductBtn(String name) {return AppendToXpath(Product(name), "//button");}
    // endregion


    // region Performers
    public ShoppingCartPage Checkout() {
        Click(CheckoutBtn());
        return new ShoppingCartPage(driver);
    }

    public void ClickOnProduct(String name) {
        Click(Product(name));
    }

    public void ClickOnProduct(int n) {
        Click(ProductName(n));
    }

    public void AddProductToCart(String name) {
        Click(AddProductBtn(name));
    }
    // endregion


    // region Getters
    public String GetProductName(int n) {
        return GetText(ProductName(n));
    }

    public int GetNumberOfProducts() {
        return GetCount(Product());
    }

    public BigDecimal GetProductPrice(String name) {
        return new BigDecimal(GetText(ProductPrice(name)));
    }
    // endregion
}
