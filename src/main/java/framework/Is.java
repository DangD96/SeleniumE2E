package framework;

import org.openqa.selenium.By;

import java.util.function.Predicate;

public class Is {
    // lambda expression members. Format is Class::staticMethod
    public static Predicate<By> Displayed = Is::displayed; // AKA (loc) -> displayed(loc)
    public static Predicate<By> Enabled = Is::enabled;
    public static Predicate<By> Exists = Is::exists;

    public static Boolean displayed(By locator) {
        try {
            return SparkDriver.getElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean enabled(By locator) {
        try {
            return SparkDriver.getElement(locator).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean exists(By locator) {
        try {
            SparkDriver.getElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
