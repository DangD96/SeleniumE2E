package page_objects;

import framework.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ShoppingCartPage extends BasePage {
    public ShoppingCartPage(WebDriver driver) {
        super(driver);
    }

    // region Locators
    // Using "." in XPath matches the entire visible text of the element, including text in child nodes
    public By CheckoutBtn() {return By.xpath("//button[contains(., 'Checkout')]");}

    public By ContinueShoppingBtn() {return By.xpath("//button[contains(., 'Continue Shopping')]");}

    public By Product() {return By.xpath("//tr[td[contains(@class, 'col-sm')]]");}

    public By Product(String name) {return By.xpath(String.format("//tr[td[contains(@class, 'col-sm')] and contains(., '%s')]", name));}

    public By Product(int n) {return By.xpath((String.format("//tr[td[contains(@class, 'col-sm')]][%d]", n)));}

    public By ProductName(int n) {return AppendToXpath(Product(n), "//h4//a");}

    public By RemoveProductBtn(String name) {return AppendToXpath(Product(name), "//button");}
    // endregion


    // region Performers
    public void RemoveProduct(String name) {
        Click(RemoveProductBtn(name));
    }
    // endregion


    // region Getters
    public int GetNumberOfProductsInCart() {
        return GetCount(Product());
    }

    public String GetProductName(int row) {
        return GetText(ProductName(row));
    }
    // endregion
}
