package org.djd;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductTest extends BaseTest {
    LoginPage loginPage;
    CatalogPage catalogPage;
    ShoppingCart cart;
    @Test(description = "Add correct product to shopping cart", groups = {"DJDGroup"})
    protected void addProductTest() {
        loginPage = new LoginPage(driver);
        catalogPage = loginPage.logIn("rahulshettyacademy", "learning", "", "Teacher", true);
        Assert.assertTrue(catalogPage.isProductListVisible());
        Assert.assertEquals(catalogPage.getNumberOfProducts(),4);

        // Add to cart
        WebElement e = catalogPage.addProductToCart("Nokia Edge");
        Assert.assertEquals(catalogPage.getProductPrice(e).toString(), "24.99");
        Assert.assertEquals(catalogPage.getProductPrice("Nokia Edge").toString(), "24.99"); // Test out overloaded version
        Assert.assertTrue(catalogPage.getCheckoutBtnContents().contains("Checkout ( 1 )"));

        // Verify in cart
        cart = catalogPage.goToShoppingCart();
        Assert.assertTrue(cart.isShoppingCartVisible());
        Assert.assertEquals(cart.getNumberOfProductsInCart(), 1);
        Assert.assertEquals(cart.getProductName(1), "Nokia Edge");

        // Remove product from cart
        cart.removeProduct(1);
        Assert.assertEquals(cart.getNumberOfProductsInCart(), 0);
    }
}
