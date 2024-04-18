package org.djd;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest{
    private LoginPage loginPage; // Make this a class variable since it's used multiple times in tests

    @Test(description = "Unsuccessful Login")
    public void loginInvalid() throws InterruptedException {
        loginPage = new LoginPage(driver);
        loginPage.logIn("wrong_username", "learning", "", "Teacher", true);
        Assert.assertTrue(loginPage.waitForElementToBeVisible(loginPage.loginErrorMessage));
    }
    @Test(description = "Successful Login")
    public void loginValid() throws InterruptedException {
        loginPage = new LoginPage(driver);
        CatalogPage catalogPage = loginPage.logIn("rahulshettyacademy", "learning", "", "", false);
        Assert.assertTrue(catalogPage.waitForElementsToBeVisible(catalogPage.productList));
        Assert.assertEquals(catalogPage.getURL(), "https://rahulshettyacademy.com/angularpractice/shop");
    }

}
