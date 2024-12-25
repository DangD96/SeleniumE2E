package framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PageObjectTemplate extends BasePage {
    /** Call superclass constructor to get access to JavascriptExecutor, Wait methods, and other convenience methods.
     *  By extending BasePage, all page objects will automatically wait for AJAX upon instantiation. */
    public PageObjectTemplate(WebDriver driver) {
        super(driver);
    }

    // region Locators
    public final By MY_BTN = By.id("button");
    // endregion


    // region Getters
    public WebElement getMyBtn() {return getElement(MY_BTN);}
    // endregion


    // region Performers
    public void clickMyBtn() {getMyBtn().click();}
    // endregion
}
