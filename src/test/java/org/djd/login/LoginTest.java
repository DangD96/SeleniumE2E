package org.djd.login;

import org.djd.BaseTest;
import org.djd.CatalogPage;
import org.djd.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;

public class LoginTest extends BaseTest {
    private LoginPage loginPage; // Make this a class variable since it's used multiple times in tests

    @Test(description = "Unsuccessful Login", dataProvider = "getTestData")
    /* Method below runs n times, where n is the number of objects in the array returned by dataProvider in superclass.
    The Object returned by dataProvider during each iteration gets passed as argument to the input parameter I defined.
    The dataProvider lives in the super class, so all subclasses can access it */
    public void loginInvalid(HashMap<String, String> input) {
        loginPage = new LoginPage(driver);
        loginPage.logIn(input.get("username"), input.get("password"), "", "Teacher", true);
        Assert.assertTrue(loginPage.waitForElementToBeVisible(loginPage.loginErrorMessage)); // Make sure you get error
    }

    @Test(description = "Successful Login", groups = {"DJDGroup"})
    public void loginValid() {
        loginPage.clearUsername();
        loginPage.clearPassword();
        CatalogPage catalogPage = loginPage.logIn("rahulshettyacademy", "learning", "", "", false);
        Assert.assertTrue(catalogPage.waitForElementsToBeVisible(catalogPage.productList));
        Assert.assertTrue(catalogPage.waitForElementToBeVisible(catalogPage.checkoutBtnBy)); // Try out the overloaded version because why not
        Assert.assertTrue(loginPage.waitForElementToBeInvisible(loginPage.loginButton));
        Assert.assertTrue(loginPage.waitForElementToBeInvisible(loginPage.loginButtonBy)); // Try out the overloaded version because why not
        Assert.assertEquals(catalogPage.getURL(), "https://rahulshettyacademy.com/angularpractice/shop");
    }
}
