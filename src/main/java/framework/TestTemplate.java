package framework;

import org.testng.annotations.Test;

public class TestTemplate extends BaseTest {
    @Test(description = "my test", enabled = false)
    protected void myTestMethod() {
        PageObjectTemplate examplePage = new PageObjectTemplate(getDriver());
        examplePage.ClickMyBtn();
        Assertion.ElementIsVisible(getDriver(), examplePage.MyBtn());
    }
}
