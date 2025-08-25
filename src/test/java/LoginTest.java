import framework.Assert;
import framework.BaseDriver;
import framework.BaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import page_objects.CatalogPage;
import page_objects.LoginPage;

import java.io.IOException;
import java.util.HashMap;

public class LoginTest extends BaseTest {
    @Test(description = "Unsuccessful Login", dataProvider = "loginInvalidDataProvider")
    /* Method below runs n times, where n is the number of objects in the array returned by dataProvider in superclass.
    The Object returned by dataProvider during each iteration gets passed as argument to the input parameter I defined.
    The dataProvider lives in the super class, so all subclasses can access it */
    protected void loginInvalid(HashMap<String, String> input) {
        LoginPage.login(input.get("username"), input.get("password"), "", "Teacher", true);
        Assert.elementIsVisible(LoginPage.loginErrorMsg());
    }

    @Test(description = "Successful Login")
    protected void loginValid() {
        LoginPage.clearUsername();
        LoginPage.clearPassword();
        LoginPage.login("rahulshettyacademy", "learning", "", "", false);
        Assert.elementIsVisible(CatalogPage.checkoutBtn());
        Assert.equals(BaseDriver.getURL(), "https://rahulshettyacademy.com/angularpractice/shop");
        Assert.elementIsHidden(LoginPage.loginBtn());
    }

    @Test(description = "fail", enabled = false)
    protected void intentionalFail() {
        BaseDriver.sleep(3000);
        Assert.intentionalFail();
    }

    @DataProvider(parallel = true)
    public Object[] loginInvalidDataProvider() throws IOException {return getTestData("LoginTestLoginInvalidData.json");}
}
