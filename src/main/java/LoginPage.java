import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

public class LoginPage extends BasePage {
    final By usernameFieldBy = By.id("username");
    final By passwordFieldBy = By.id("password");
    final By adminRadioBtnBy = By.cssSelector("input[value='admin']");
    final By userRadioBtnBy = By.cssSelector("input[value='user']");
    final By roleDropdownBy = By.cssSelector("select.form-control");
    final By termsAndServicesCheckboxBy = By.id("terms");
    final By loginButtonBy = By.id("signInBtn");
    final By loginErrorMessageBy = By.cssSelector(".alert.alert-danger");

    public LoginPage(WebDriver driver) {
        super(driver); // Pass the driver to the parent class so the methods in there work
    }

    public CatalogPage logIn(String username, String password, String userType, String userRole, Boolean toggleTermsBox) {
        // Just using JS to fill them out because why not
        //WebElement usernameField = ;
        js.executeScript("arguments[0].value = arguments[1]", waitForElementToBeVisible(usernameFieldBy), username);
        //;
        js.executeScript("arguments[0].value = arguments[1]", waitForElementToBeVisible(passwordFieldBy), password);

        if (!userType.isEmpty()) selectUserType(userType);
        if (!userRole.isEmpty()) selectDropDownOption(userRole);
        if (toggleTermsBox) toggleTermsAndServices();

        waitForElementToBeVisible(loginButtonBy).click();
        return new CatalogPage(driver);
    }

    public void selectDropDownOption(String option) {
        Select roles = new Select(waitForElementToBeVisible(roleDropdownBy));
        roles.selectByVisibleText(option);
    }

    public void selectUserType(String userType) {
        WebElement userRadioBtn = waitForElementToBeVisible(userRadioBtnBy);
        if (userType.equalsIgnoreCase("Admin")) {
            waitForElementToBeVisible(adminRadioBtnBy).click();
        }
        else {
            userRadioBtn.click();
        }
    }

    public void clearUsername() {
        js.executeScript("arguments[0].value = ''", waitForElementToBeVisible(usernameFieldBy));
    }

    public void clearPassword() {
        js.executeScript("arguments[0].value = ''", waitForElementsToBeVisible(passwordFieldBy));
    }

    public void toggleTermsAndServices() {
        waitForElementToBeVisible(termsAndServicesCheckboxBy).click();
    }
}
