import framework.Assert;
import framework.SparkDriver;
import framework.SparkTest;
import org.testng.annotations.Test;
import pages.CatalogPage;
import pages.LoginPage;
import pages.ShoppingCartPage;

public class ProductTest extends SparkTest {
    @Test(description = "Add correct product to shopping cart")
    protected void addProductTest() {
        LoginPage.login("rahulshettyacademy", "learning", "", "Teacher", true);
        Assert.elementIsVisible(CatalogPage.checkoutBtn());
        Assert.equals(CatalogPage.getNumberOfProducts(), 4);

        // Add to cart
        CatalogPage.addProductToCart("Nokia Edge");
        Assert.equals(CatalogPage.getProductPrice("Nokia Edge"), "$24.99");
        Assert.isTrue(SparkDriver.getText(CatalogPage.checkoutBtn()).contains("Checkout ( 1 )"));

        // Verify in cart
        CatalogPage.goToShoppingCart();
        Assert.elementIsVisible(ShoppingCartPage.checkoutBtn());
        Assert.equals(ShoppingCartPage.getNumberOfProductsInCart(), 1);
        Assert.equals(ShoppingCartPage.getProductName(1), "Nokia Edge");

        // Remove product from cart
        ShoppingCartPage.removeProduct("Nokia Edge");
        Assert.equals(ShoppingCartPage.getNumberOfProductsInCart(), 0);
    }

    @Test(description = "fail", enabled = false)
    protected void intentionalFail() throws InterruptedException {
        LoginPage.login("rahulshettyacademy", "learning", "", "Teacher", true);
        Thread.sleep(2000);
        Assert.intentionalFail();
    }
}
