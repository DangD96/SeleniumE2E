package page_objects;

import framework.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

public class LoginPage extends BasePage {
    // Pass the driver to the parent class so the methods in there work
    public LoginPage(WebDriver driver) {super(driver);}

    // region Locators
    public final By USERNAME_FIELD = By.id("username");
    public final By PASSWORD_FIELD = By.id("password");
    public final By ADMIN_RADIO_BTN = By.cssSelector("input[value='admin']");
    public final By USER_RADIO_BTN = By.cssSelector("input[value='user']");
    public final By ROLE_DROPDOWN = By.cssSelector("select.form-control");
    public final By TERMS_AND_SERVICES_CHECKBOX = By.id("terms");
    public final By LOGIN_BTN = By.id("signInBtn");
    public final By LOGIN_ERROR_MESSAGE = By.cssSelector(".alert.alert-danger");
    // endregion


    // region Performers
    public CatalogPage logIn(String username, String password, String userType, String userRole, Boolean toggleTermsBox) {
        typeText(waitForElementToBeVisible(USERNAME_FIELD), username);
        typeText(waitForElementToBeVisible(PASSWORD_FIELD), password);
        if (!userType.isEmpty()) selectUserType(userType);
        if (!userRole.isEmpty()) selectDropDownOption(userRole);
        if (toggleTermsBox) toggleTermsAndServices();
        waitForElementToBeClickable(LOGIN_BTN).click();
        return new CatalogPage(driver);
    }

    public void selectDropDownOption(String option) {
        Select roles = new Select(waitForElementToBeVisible(ROLE_DROPDOWN));
        roles.selectByVisibleText(option);
    }

    public void selectUserType(String userType) {
        if (userType.equalsIgnoreCase("Admin")) {
            waitForElementToBeClickable(ADMIN_RADIO_BTN).click();}
        else {
            waitForElementToBeClickable(USER_RADIO_BTN).click();}
    }

    public void clearUsername() {js.executeScript("arguments[0].value = ''", waitForElementToBeVisible(USERNAME_FIELD));}

    public void clearPassword() {js.executeScript("arguments[0].value = ''", waitForElementToBeVisible(PASSWORD_FIELD));}

    public void toggleTermsAndServices() {
        waitForElementToBeClickable(TERMS_AND_SERVICES_CHECKBOX).click();
    }
    // endregion
}
