package page_objects;

import framework.BaseDriver;
import org.openqa.selenium.By;

public class ShoppingCartPage {
    // region Locators
    // Using "." in XPath matches the entire visible text of the element, including text in child nodes
    public static By checkoutBtn() {return By.xpath("//button[contains(., 'Checkout')]");}

    public static By continueShoppingBtn() {return By.xpath("//button[contains(., 'Continue Shopping')]");}

    public static By product() {return By.xpath("//tr[td[contains(@class, 'col-sm')]]");}

    public static By product(String name) {return By.xpath(String.format("//tr[td[contains(@class, 'col-sm')] and contains(., '%s')]", name));}

    public static By product(int n) {return By.xpath((String.format("//tr[td[contains(@class, 'col-sm')]][%d]", n)));}

    public static By productName(int n) {return BaseDriver.appendToXpath(product(n), "//h4//a");}

    public static By productRemoveBtn(String name) {return BaseDriver.appendToXpath(product(name), "//button");}
    // endregion


    // region Performers
    public static void removeProduct(String name) {
        BaseDriver.click(productRemoveBtn(name));
    }
    // endregion


    // region Getters
    public static int getNumberOfProductsInCart() {
        return BaseDriver.getCount(product());
    }

    public static String getProductName(int row) {
        return BaseDriver.getText(productName(row));
    }
    // endregion
}
