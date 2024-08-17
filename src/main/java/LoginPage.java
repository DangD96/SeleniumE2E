import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

public class LoginPage extends BasePage {
    final By USERNAME_FIELD = By.id("username");
    final By PASSWORD_FIELD = By.id("password");
    final By ADMIN_RADIO_BTN = By.cssSelector("input[value='admin']");
    final By USER_RADIO_BTN = By.cssSelector("input[value='user']");
    final By ROLE_DROPDOWN = By.cssSelector("select.form-control");
    final By TERMS_AND_SERVICES_CHECKBOX = By.id("terms");
    final By LOGIN_BTN = By.id("signInBtn");
    final By LOGIN_ERROR_MESSAGE = By.cssSelector(".alert.alert-danger");

    public LoginPage(WebDriver driver) {
        super(driver); // Pass the driver to the parent class so the methods in there work
    }

    public CatalogPage logIn(String username, String password, String userType, String userRole, Boolean toggleTermsBox) {
        // Just using JS to fill them out because why not
        js.executeScript("arguments[0].value = arguments[1]", waitForElementToBeVisible(USERNAME_FIELD), username);
        js.executeScript("arguments[0].value = arguments[1]", waitForElementToBeVisible(PASSWORD_FIELD), password);

        if (!userType.isEmpty()) selectUserType(userType);
        if (!userRole.isEmpty()) selectDropDownOption(userRole);
        if (toggleTermsBox) toggleTermsAndServices();

        waitForElementToBeVisible(LOGIN_BTN).click();
        return new CatalogPage(driver);
    }

    public void selectDropDownOption(String option) {
        Select roles = new Select(waitForElementToBeVisible(ROLE_DROPDOWN));
        roles.selectByVisibleText(option);
    }

    public void selectUserType(String userType) {
        WebElement userRadioBtn = waitForElementToBeVisible(USER_RADIO_BTN);
        if (userType.equalsIgnoreCase("Admin")) {
            waitForElementToBeVisible(ADMIN_RADIO_BTN).click();
        }
        else {
            userRadioBtn.click();
        }
    }

    public void clearUsername() {
        js.executeScript("arguments[0].value = ''", waitForElementToBeVisible(USERNAME_FIELD));
    }

    public void clearPassword() {
        js.executeScript("arguments[0].value = ''", waitForElementsToBeVisible(PASSWORD_FIELD));
    }

    public void toggleTermsAndServices() {
        waitForElementToBeVisible(TERMS_AND_SERVICES_CHECKBOX).click();
    }
}
