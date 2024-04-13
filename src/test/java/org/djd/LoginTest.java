package org.djd;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest extends BaseTest{
    @Test(description = "Unsuccessful Login")
    public void loginInvalid() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.logIn("wrong_username", "learning", "", "Teacher", true);
        Assert.assertTrue(loginPage.waitForElementToBeVisible(loginPage.loginErrorMessage));
    }
    @Test(description = "Successful Login")
    public void loginValid() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        CatalogPage catalogPage = loginPage.logIn("rahulshettyacademy", "learning", "", "", false);
        Assert.assertTrue(catalogPage.waitForElementToBeVisible((WebElement) catalogPage.productList));
        Assert.assertEquals(catalogPage.getURL(), "https://rahulshettyacademy.com/angularpractice/shop");
    }

}
