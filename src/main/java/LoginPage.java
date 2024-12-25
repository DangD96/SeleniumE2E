import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

public class LoginPage extends BasePage {
    // Pass the driver to the parent class so the methods in there work
    public LoginPage(WebDriver driver) {super(driver);}

    // region Locators and Wrappers
    public final By USERNAME_FIELD = By.id("username");
    public final By PASSWORD_FIELD = By.id("password");
    public final By ADMIN_RADIO_BTN = By.cssSelector("input[value='admin']");
    public final By USER_RADIO_BTN = By.cssSelector("input[value='user']");
    public final By ROLE_DROPDOWN = By.cssSelector("select.form-control");
    public final By TERMS_AND_SERVICES_CHECKBOX = By.id("terms");
    public final By LOGIN_BTN = By.id("signInBtn");
    public final By LOGIN_ERROR_MESSAGE = By.cssSelector(".alert.alert-danger");
    // endregion


    // region Getters
    public WebElement getUsernameField() {return getElement(USERNAME_FIELD);}
    public WebElement getPasswordField() {return getElement(PASSWORD_FIELD);}
    public WebElement getLoginBtn() {return getElement(LOGIN_BTN);}
    public WebElement getRoleDropdown() {return getElement(ROLE_DROPDOWN);}
    public WebElement getAdminRadioBtn() {return getElement(ADMIN_RADIO_BTN);}
    public WebElement getUserRadioBtn() {return getElement(USER_RADIO_BTN);}
    public WebElement getTOSCheckbox() {return getElement(TERMS_AND_SERVICES_CHECKBOX);}
    public WebElement getLoginErrorMsg() {return getElement(LOGIN_ERROR_MESSAGE);}
    // endregion


    // region Performers
    public CatalogPage logIn(String username, String password, String userType, String userRole, Boolean toggleTermsBox) {
        typeText(getUsernameField(), username);
        typeText(getPasswordField(), password);

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
        if (userType.equalsIgnoreCase("Admin")) {
            getAdminRadioBtn().click();}
        else {
            getUserRadioBtn().click();}
    }

    public void clearUsername() {js.executeScript("arguments[0].value = ''", getUsernameField());}

    public void clearPassword() {js.executeScript("arguments[0].value = ''", getPasswordField());}

    public void toggleTermsAndServices() {
        getTOSCheckbox().click();}
    // endregion
}
