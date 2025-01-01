package framework;

import org.testng.annotations.Test;

public class TestTemplate extends BaseTest {
    @Test(description = "my test", enabled = false)
    protected void myTestMethod() {
        PageObjectTemplate examplePage = new PageObjectTemplate(getDriver());
        examplePage.clickMyBtn();
        Assertion.elementIsVisible(getDriver(), examplePage.MY_BTN);
    }
}
