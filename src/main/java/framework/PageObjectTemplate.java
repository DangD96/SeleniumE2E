package framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PageObjectTemplate extends BasePage {
    /** Call superclass constructor to get access to Wait methods and other convenience methods.
     *  By extending BasePage, all page objects will automatically wait for AJAX upon instantiation. */
    public PageObjectTemplate(WebDriver driver) {
        super(driver);
    }

    // region Locators
    public By MyBtn() {return By.id("button");}
    // endregion


    // region Performers
    public void ClickMyBtn() {
        Click(MyBtn());
    }
    // endregion


    // region Getters
    public String GetMyBtnText() {
        return GetText(MyBtn());
    }
    // endregion
}
