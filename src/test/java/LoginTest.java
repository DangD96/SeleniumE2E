import framework.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import page_objects.CatalogPage;
import page_objects.LoginPage;

import java.io.IOException;
import java.util.HashMap;

public class LoginTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(LoginTest.class);

    @Test(description = "Unsuccessful Login", dataProvider = "LoginInvalidDataProvider")
    /* Method below runs n times, where n is the number of objects in the array returned by dataProvider in superclass.
    The Object returned by dataProvider during each iteration gets passed as argument to the input parameter I defined.
    The dataProvider lives in the super class, so all subclasses can access it */
    protected void LoginInvalid(HashMap<String, String> input) {
        LoginPage loginPage = new LoginPage(getDriver()); // Test METHODS run on parallel threads so each method needs its own page object
        loginPage.Login(input.get("username"), input.get("password"), "", "Teacher", true);
        loginPage.AssertElementIsVisible(loginPage.LoginErrorMsg());
    }

    @Test(description = "fail", enabled = false)
    protected void IntentionalFail() throws InterruptedException {
        Thread.sleep(2000);
        LoginPage page = new LoginPage(getDriver());
        page.Fail();
    }

    @Test(description = "Successful Login")
    protected void LoginValid() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.ClearUsername();
        loginPage.ClearPassword();
        CatalogPage catalogPage = loginPage.Login("rahulshettyacademy", "learning", "", "", false);
        catalogPage.AssertElementIsVisible(catalogPage.CheckoutBtn());
        catalogPage.AssertEquals(catalogPage.GetURL(), "https://rahulshettyacademy.com/angularpractice/shop");
        catalogPage.AssertElementIsHidden(loginPage.LoginBtn());
    }

    @DataProvider(parallel = true)
    public Object[] LoginInvalidDataProvider() throws IOException {return getTestData("LoginTestLoginInvalidData.json");}
}
