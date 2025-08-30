package pages;

import framework.Is;
import framework.SparkDriver;
import org.openqa.selenium.By;

import static framework.Utilities.appendToXpath;

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
        return appendToXpath(product(name), String.format("//a[text()='%s']", name));
    }

    public static By productName(int n) {return By.xpath(String.format("//app-card[%d]//a", n));}

    public static By productPrice(String name) {return appendToXpath(product(name), "//h5");}

    public static By productAddBtn(String name) {return appendToXpath(product(name), "//button");}
    // endregion


    // region Performers
    public static void goToShoppingCart() {
        SparkDriver.click(checkoutBtn());
        SparkDriver.repeatUntil(checkoutBtn(), SparkDriver.Click, product(""), Is.Displayed);
    }

    public static void clickOnProduct(String name) {
        SparkDriver.click(product(name));
    }

    public static void clickOnProduct(int n) {
        SparkDriver.click(productName(n));
    }

    public static void addProductToCart(String name) {
        SparkDriver.click(productAddBtn(name));
    }
    // endregion


    // region Getters
    public static String getProductName(int n) {
        return SparkDriver.getText(productName(n));
    }

    public static int getNumberOfProducts() {
        return SparkDriver.getCount(product());
    }

    public static String getProductPrice(String name) {
        return SparkDriver.getText(productPrice(name));
    }
    // endregion
}
