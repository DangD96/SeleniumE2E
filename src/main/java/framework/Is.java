package framework;

import org.openqa.selenium.By;

public class Is {
    public static Boolean displayed(By locator) {
        try {
            return BaseDriver.getElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean enabled(By locator) {
        try {
            return BaseDriver.getElement(locator).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean exists(By locator) {
        try {
            BaseDriver.getElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
