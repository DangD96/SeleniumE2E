package framework.templates;

import framework.Is;
import framework.SparkDriver;
import org.openqa.selenium.By;

import static framework.Helpers.extendXPath;

public class TemplatePage {
    // region Locators
    public static By myBtn() {return By.id("btn123");}

    public static By myProduct(String name) {return By.xpath(String.format("//app-card[.//*[@class='card-title']//a[text()='%s']]", name));}

    public static By myProductPrice(String name) {return extendXPath(myProduct(name), "//h5");}
    // endregion


    // region Performers
    public static void clickMyBtn() {
        SparkDriver.click(myBtn());
    }
    // endregion


    // region Getters
    public static String getMyProductPrice(String name) {
        return SparkDriver.getText(myProductPrice(name));
    }
    // endregion


    // region Booleans
    public static Boolean isMyBtnEnabled() {
        return Is.enabled(myBtn());
    }
    // endregion
}
