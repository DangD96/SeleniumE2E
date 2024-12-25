import core.Assertion;
import core.BaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;

public class LoginTest extends BaseTest {

    @Test(description = "Unsuccessful Login", dataProvider = "loginInvalidDataProvider")
    /* Method below runs n times, where n is the number of objects in the array returned by dataProvider in superclass.
    The Object returned by dataProvider during each iteration gets passed as argument to the input parameter I defined.
    The dataProvider lives in the super class, so all subclasses can access it */
    protected void loginInvalid(HashMap<String, String> input) {
        LoginPage loginPage = new LoginPage(getDriver()); // Test METHODS run on parallel threads so each method needs its own page object
        loginPage.logIn(input.get("username"), input.get("password"), "", "Teacher", true);
        Assertion.elementIsVisible(loginPage.getLoginErrorMsg());
    }

    @Test(description = "fail", enabled = false)
    protected void intentionalFail() throws InterruptedException {
        Thread.sleep(2000);
        Assertion.fail();
    }

    @Test(description = "Successful Login")
    protected void loginValid() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.clearUsername();
        loginPage.clearPassword();
        CatalogPage catalogPage = loginPage.logIn("rahulshettyacademy", "learning", "", "", false);
        Assertion.elementIsVisible(catalogPage.getProductList());
        Assertion.assertNotNull(catalogPage.getAllProducts());
        Assertion.assertEquals(catalogPage.getURL(), "https://rahulshettyacademy.com/angularpractice/shop");
        Assertion.elementIsInvisible(getDriver(), loginPage.LOGIN_BTN);
    }

    @DataProvider(parallel = true)
    public Object[] loginInvalidDataProvider() throws IOException {return getTestData("LoginTestLoginInvalidData.json");}
}
