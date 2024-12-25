import core.Assert;
import core.BaseTest;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class ProductTest extends BaseTest {

    @Test(description = "Add correct product to shopping cart")
    protected void addProductTest() {
        LoginPage loginPage = new LoginPage(getDriver());
        CatalogPage catalogPage = loginPage.logIn("rahulshettyacademy", "learning", "", "Teacher", true);
        Assert.assertNotNull(catalogPage.getAllProducts());
        Assert.assertEquals(catalogPage.getNumberOfProducts(),4);

        // Add to cart
        WebElement e = catalogPage.addProductToCart("Nokia Edge");
        System.out.println("E is: " + e);
        Assert.assertEquals(catalogPage.getProductPrice(e).toString(), "24.99");
        Assert.assertEquals(catalogPage.getProductPrice("Nokia Edge").toString(), "24.99"); // Test out overloaded version
        Assert.assertTrue(catalogPage.getCheckoutBtnContents().contains("Checkout ( 1 )"));

        // Verify in cart
        ShoppingCart cart = catalogPage.goToShoppingCart();
        Assert.assertNotNull(cart.getShoppingCart());
        Assert.assertEquals(cart.getNumberOfProductsInCart(), 1);
        Assert.assertEquals(cart.getProductName(1), "Nokia Edge");

        // Remove product from cart
        cart.removeProduct(1);
        Assert.assertEquals(cart.getNumberOfProductsInCart(), 0);
    }
}
