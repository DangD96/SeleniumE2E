import framework.BaseTest;
import org.testng.annotations.Test;
import page_objects.CatalogPage;
import page_objects.LoginPage;
import page_objects.ShoppingCartPage;

public class ProductTest extends BaseTest {

    @Test(description = "Add correct product to shopping cart")
    protected void AddProductTest() {
        LoginPage loginPage = new LoginPage(getDriver());
        CatalogPage catalogPage = loginPage.Login("rahulshettyacademy", "learning", "", "Teacher", true);
        catalogPage.AssertElementExists(catalogPage.CheckoutBtn());
        catalogPage.AssertEquals(catalogPage.GetNumberOfProducts(),4);

        // Add to cart
        catalogPage.AddProductToCart("Nokia Edge");
        catalogPage.AssertEquals(catalogPage.GetProductPrice("Nokia Edge").toString(), "24.99");
        catalogPage.AssertIsTrue(catalogPage.GetText(catalogPage.CheckoutBtn()).contains("Checkout ( 1 )"));

        // Verify in cart
        ShoppingCartPage cart = catalogPage.GoToShoppingCart();
        cart.AssertElementIsVisible(cart.CheckoutBtn());
        cart.AssertEquals(cart.GetNumberOfProductsInCart(), 1);
        cart.AssertEquals(cart.GetProductName(1), "Nokia Edge");

        // Remove product from cart
        cart.RemoveProduct("Nokia Edge");
        cart.AssertEquals(cart.GetNumberOfProductsInCart(), 0);
    }

    @Test(description = "fail", enabled = false)
    protected void IntentionalFail() throws InterruptedException {
        LoginPage loginPage = new LoginPage(getDriver());
        CatalogPage catalogPage = loginPage.Login("rahulshettyacademy", "learning", "", "Teacher", true);
        Thread.sleep(2000);
        catalogPage.Fail();
    }
}
