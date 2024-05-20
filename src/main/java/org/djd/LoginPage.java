package org.djd;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class LoginPage extends BasePage {
    /* With PageFactory, even if the element doesn't actually exist, selenium won't
    throw an error until you try to interact with it */
    @FindBy(id = "username") private WebElement usernameField;
    @FindBy(id = "password") private WebElement passwordField;
    @FindBy(css = "input[value='admin']") private WebElement adminRadioBtn;
    @FindBy(css = "input[value='user']") private WebElement userRadioBtn;
    @FindBy(css = "select.form-control") private WebElement roleDropdown;
    @FindBy(id = "terms") private WebElement termsAndServicesCheckbox;
    @FindBy(id = "signInBtn") private WebElement loginButton;
    @FindBy(css = ".alert.alert-danger") private WebElement loginErrorMessage;

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
        if (userType.equalsIgnoreCase("Admin")) {adminRadioBtn.click();}
        else {userRadioBtn.click();}
    }

    public void clearUsername() {
        js.executeScript("arguments[0].value = ''", usernameField);
    }

    public void clearPassword() {
        js.executeScript("arguments[0].value = ''", passwordField);
    }

    public void toggleTermsAndServices() {
        waitForElementToBeVisible(termsAndServicesCheckbox);
        termsAndServicesCheckbox.click();
    }

    public boolean isLoginErrorPresent() {
        return waitForElementToBeVisible(loginErrorMessage);
    }
}
