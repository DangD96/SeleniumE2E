package org.djd;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class LoginPage extends BasePage {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    @FindBy(id = "username")
    WebElement usernameField;
    @FindBy(id = "password")
    WebElement passwordField;
    @FindBy(css = "input[value='admin']")
    WebElement adminRadioBtn;
    @FindBy(css = "input[value='user']")
    WebElement userRadioBtn;
    @FindBy(css = "select.form-control")
    WebElement roleDropdown;
    @FindBy(id = "terms")
    WebElement termsAndServicesCheckbox;
    @FindBy(id = "signInBtn")
    WebElement loginButton;
    @FindBy(css = ".alert.alert-danger")
    WebElement loginErrorMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public CatalogPage logIn(String username, String password, String userType, String userRole, Boolean toggleTermsBox) throws InterruptedException {
        // Just using JS to fill them out because why not
        super.waitForElementToBeVisible(usernameField);
        js.executeScript("arguments[0].value = arguments[1]", usernameField, username);

        super.waitForElementToBeVisible(passwordField);
        js.executeScript("arguments[0].value = arguments[1]", passwordField, password);

        // if (!userType.isEmpty()) // do something
        if (!userRole.isEmpty()) selectDropDownOption(userRole);
        if (toggleTermsBox) toggleTermsAndServices();

        super.waitForElementToBeVisible(loginButton);
        loginButton.click();

        return new CatalogPage(driver);
    }

    public void selectDropDownOption(String option) {
        waitForElementToBeVisible(roleDropdown);
        Select roles = new Select(roleDropdown);
        roles.selectByVisibleText(option);
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
