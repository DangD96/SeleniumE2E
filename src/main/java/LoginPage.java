import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

public class LoginPage extends BasePage {
    // Pass the driver to the parent class so the methods in there work
    public LoginPage(WebDriver driver) {super(driver);}

    // region Locators
    final By USERNAME_FIELD = By.id("username");
    final By PASSWORD_FIELD = By.id("password");
    final By ADMIN_RADIO_BTN = By.cssSelector("input[value='admin']");
    final By USER_RADIO_BTN = By.cssSelector("input[value='user']");
    final By ROLE_DROPDOWN = By.cssSelector("select.form-control");
    final By TERMS_AND_SERVICES_CHECKBOX = By.id("terms");
    final By LOGIN_BTN = By.id("signInBtn");
    final By LOGIN_ERROR_MESSAGE = By.cssSelector(".alert.alert-danger");
    public WebElement getUsernameField() {return waitForElementToBeClickable(USERNAME_FIELD);}
    public WebElement getPasswordField() {return waitForElementToBeClickable(PASSWORD_FIELD);}
    public WebElement getLoginBtn() {return waitForElementToBeClickable(LOGIN_BTN);}
    public WebElement getRoleDropdown() {return waitForElementToBeClickable(ROLE_DROPDOWN);}
    public WebElement getAdminRadioBtn() {return waitForElementToBeClickable(ADMIN_RADIO_BTN);}
    public WebElement getUserRadioBtn() {return waitForElementToBeClickable(USER_RADIO_BTN);}
    public WebElement getTOSCheckbox() {return waitForElementToBeClickable(TERMS_AND_SERVICES_CHECKBOX);}
    public WebElement getLoginErrorMsg() {return waitForElementToBeVisible(LOGIN_ERROR_MESSAGE);}
    // endregion


    // region Performers
    public CatalogPage logIn(String username, String password, String userType, String userRole, Boolean toggleTermsBox) {
        // Just using JS to fill them out because why not
        js.executeScript("arguments[0].value = arguments[1]", getUsernameField(), username);
        js.executeScript("arguments[0].value = arguments[1]", getPasswordField(), password);

        if (!userType.isEmpty()) selectUserType(userType);
        if (!userRole.isEmpty()) selectDropDownOption(userRole);
        if (toggleTermsBox) toggleTermsAndServices();

        getLoginBtn().click();
        return new CatalogPage(driver);
    }

    public void selectDropDownOption(String option) {
        Select roles = new Select(getRoleDropdown());
        roles.selectByVisibleText(option);
    }

    public void selectUserType(String userType) {
        if (userType.equalsIgnoreCase("Admin")) {getAdminRadioBtn().click();}
        else {getUserRadioBtn().click();}
    }

    public void clearUsername() {js.executeScript("arguments[0].value = ''", getUsernameField());}

    public void clearPassword() {js.executeScript("arguments[0].value = ''", getPasswordField());}

    public void toggleTermsAndServices() {getTOSCheckbox().click();}
    // endregion
}
