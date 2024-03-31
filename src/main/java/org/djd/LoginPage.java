package org.djd;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends BasePage {
    @FindBy(id = "user-name")
    WebElement usernameField;
    @FindBy(id = "password")
    WebElement passwordField;
    @FindBy(id = "login-button")
    WebElement loginButton;
    @FindBy(className = "error-message-container error")
    WebElement loginErrorMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public CatalogPage logIn(String username, String password) {
        super.waitForElementToBeVisible(usernameField);
        usernameField.sendKeys(username);
        super.waitForElementToBeVisible(passwordField);
        passwordField.sendKeys(password);
        super.waitForElementToBeVisible(loginButton);
        loginButton.click();
        return new CatalogPage(driver);
    }
}
