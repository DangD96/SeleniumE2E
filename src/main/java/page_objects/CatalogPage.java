package page_objects;

import framework.BaseDriver;
import org.openqa.selenium.By;

public class CatalogPage {
    // region Locators
    public static By checkoutBtn() {return By.xpath("//a[contains(text(), 'Checkout')]");}

    public static By editButtonForName(String name) {
        return By.xpath(String.format("//div[@class='row' and .//span[text()='%s']]//button[contains(@class,'edit-btn')]", name));
    }

    public static By product() {return By.xpath("//app-card");}

    public static By product(String name) {
        // The "[.//" part is for searching for a descendant
        return By.xpath(String.format("//app-card[.//*[@class='card-title']//a[text()='%s']]", name));
    }

    public static By product(int n) {return By.xpath(String.format("//app-card[%d]", n));}

    public static By productName(String name) {
        return BaseDriver.appendToXpath(product(name), String.format("//a[text()='%s']", name));
    }

    public static By productName(int n) {return By.xpath(String.format("//app-card[%d]//a", n));}

    public static By productPrice(String name) {return BaseDriver.appendToXpath(product(name), "//h5");}

    public static By productAddBtn(String name) {return BaseDriver.appendToXpath(product(name), "//button");}
    // endregion


    // region Performers
    public static void goToShoppingCart() {
        BaseDriver.click(checkoutBtn());
    }

    public static void clickOnProduct(String name) {
        BaseDriver.click(product(name));
    }

    public static void clickOnProduct(int n) {
        BaseDriver.click(productName(n));
    }

    public static void addProductToCart(String name) {
        BaseDriver.click(productAddBtn(name));
    }
    // endregion


    // region Getters
    public static String getProductName(int n) {
        return BaseDriver.getText(productName(n));
    }

    public static int getNumberOfProducts() {
        return BaseDriver.getCount(product());
    }

    public static String getProductPrice(String name) {
        return BaseDriver.getText(productPrice(name));
    }
    // endregion
}
