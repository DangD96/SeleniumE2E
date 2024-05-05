package org.djd;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class LoginPage extends BasePage {
    JavascriptExecutor js = (JavascriptExecutor) driver;

    /*
    With PageFactory, even if the element doesn't actually exist, selenium won't
    throw an error until you try to interact with it
     */
    @FindBy(id = "username") WebElement usernameField;
    @FindBy(id = "password") WebElement passwordField;
    @FindBy(css = "input[value='admin']") WebElement adminRadioBtn;
    @FindBy(css = "input[value='user']") WebElement userRadioBtn;
    @FindBy(css = "select.form-control") WebElement roleDropdown;
    @FindBy(id = "terms") WebElement termsAndServicesCheckbox;
    @FindBy(id = "signInBtn") public WebElement loginButton;
    @FindBy(css = ".alert.alert-danger") public WebElement loginErrorMessage;
    @FindBy(id = "DJD") public WebElement fakeElement;
    public By loginButtonBy = By.id("signInBtn"); // Map using By to showcase other location methods

    public LoginPage(WebDriver driver) {
        super(driver); // Pass the driver to the parent class so the methods in there work
        PageFactory.initElements(driver, this);
    }

    public CatalogPage logIn(String username, String password, String userType, String userRole, Boolean toggleTermsBox) {
        // Just using JS to fill them out because why not
        waitForElementToBeVisible(usernameField);
        js.executeScript("arguments[0].value = arguments[1]", usernameField, username);

        waitForElementToBeVisible(passwordField);
        js.executeScript("arguments[0].value = arguments[1]", passwordField, password);

        if (!userType.isEmpty()) selectUserType(userType);
        if (!userRole.isEmpty()) selectDropDownOption(userRole);
        if (toggleTermsBox) toggleTermsAndServices();

        waitForElementToBeVisible(loginButton);
        waitForElementToBeVisible(loginButtonBy); // Test out the overloaded version because why not
        loginButton.click();

        return new CatalogPage(driver);
    }

    public void selectDropDownOption(String option) {
        waitForElementToBeVisible(roleDropdown);
        Select roles = new Select(roleDropdown);
        roles.selectByVisibleText(option);
    }
    public void selectUserType(String userType) {
        waitForElementToBeVisible(userRadioBtn);
        if (userType.equalsIgnoreCase("Admin")) {
            adminRadioBtn.click();
            return;
        }
        userRadioBtn.click();
    }
    public void clearUsername() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = ''", usernameField);
    }
    public void clearPassword() {
        // the element.clear() method doesn't work on the password field for some reason so using JS
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = ''", passwordField);
    }
    public void toggleTermsAndServices() {
        waitForElementToBeVisible(termsAndServicesCheckbox);
        termsAndServicesCheckbox.click();
    }
}
