import framework.Assertion;
import framework.BaseTest;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import page_objects.CatalogPage;
import page_objects.LoginPage;
import page_objects.ShoppingCart;

public class ProductTest extends BaseTest {

    @Test(description = "Add correct product to shopping cart")
    protected void addProductTest() {
        LoginPage loginPage = new LoginPage(getDriver());
        CatalogPage catalogPage = loginPage.logIn("rahulshettyacademy", "learning", "", "Teacher", true);
        Assertion.assertNotNull(catalogPage.PRODUCT_LIST);
        Assertion.assertEquals(catalogPage.getNumberOfProducts(),4);

        // Add to cart
        WebElement e = catalogPage.addProductToCart("Nokia Edge");
        Assertion.assertEquals(catalogPage.getProductPrice(e).toString(), "24.99");
        Assertion.assertEquals(catalogPage.getProductPrice("Nokia Edge").toString(), "24.99"); // Test out overloaded version
        Assertion.assertTrue(catalogPage.getCheckoutBtnContents().contains("Checkout ( 1 )"));

        // Verify in cart
        ShoppingCart cart = catalogPage.goToShoppingCart();
        Assertion.elementIsVisible(getDriver(), cart.SHOPPING_CART);
        Assertion.assertEquals(cart.getNumberOfProductsInCart(), 1);
        Assertion.assertEquals(cart.getProductName(1), "Nokia Edge");

        // Remove product from cart
        cart.removeProduct(1);
        Assertion.assertEquals(cart.getNumberOfProductsInCart(), 0);
    }

    @Test(description = "fail", enabled = false)
    protected void intentionalFail() throws InterruptedException {
        LoginPage loginPage = new LoginPage(getDriver());
        CatalogPage catalogPage = loginPage.logIn("rahulshettyacademy", "learning", "", "Teacher", true);
        Thread.sleep(2000);
        Assertion.fail();
    }
}
