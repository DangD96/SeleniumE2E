package org.djd;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class ProductTest extends BaseTest{
    LoginPage loginPage;
    CatalogPage catalogPage;
    ShoppingCart cart;
    @Test(description = "Add correct product to shopping cart")
    public void addProductTest() throws InterruptedException {
        loginPage = new LoginPage(driver);
        catalogPage = loginPage.logIn("rahulshettyacademy", "learning", "", "Teacher", true);
        catalogPage.waitForElementsToBeVisible(catalogPage.productList);
        Assert.assertEquals(catalogPage.getNumberOfProducts(),4);

        // Add to cart
        WebElement e = catalogPage.addProductToCart("Nokia Edge");
        Assert.assertEquals(catalogPage.getProductPrice(e).toString(), "24.99");

        Assert.assertTrue(catalogPage.checkoutBtn.getText().contains("Checkout ( 1 )"));

        // Verify in cart
        cart = catalogPage.goToShoppingCart();
        cart.waitForElementToBeVisible(cart.shoppingCart);
        Assert.assertEquals(cart.getNumberOfProductsInCart(), 1);
        Assert.assertEquals(cart.getProductName(1), "Nokia Edge");
    }
}
