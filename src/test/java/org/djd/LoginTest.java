package org.djd;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest{
    private LoginPage loginPage; // Make this a class variable since it's used multiple times in tests

    @Test(description = "Unsuccessful Login")
    public void loginInvalid() {
        loginPage = new LoginPage(driver);
        // Make sure you get error
        loginPage.logIn("wrong_username", "learning", "", "Teacher", true);
        Assert.assertTrue(loginPage.waitForElementToBeVisible(loginPage.loginErrorMessage));

        // Confirm you can log in normally after invalid log in
        loginPage.clearUsername();
        loginPage.clearPassword();
        loginValid();
    }
    @Test(description = "Successful Login", groups = {"DJDGroup"})
    public void loginValid() {
        loginPage = new LoginPage(driver);
        CatalogPage catalogPage = loginPage.logIn("rahulshettyacademy", "learning", "", "", false);
        Assert.assertTrue(catalogPage.waitForElementsToBeVisible(catalogPage.productList));
        Assert.assertEquals(catalogPage.getURL(), "https://rahulshettyacademy.com/angularpractice/shop");
    }
}
