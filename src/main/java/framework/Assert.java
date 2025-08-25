package framework;

import org.openqa.selenium.By;

public class Assert extends org.testng.Assert {
    public static void elementIsVisible(By locator) {
        BaseDriver.pauseForEffect();
        assertTrue(Is.displayed(locator));
    }

    public static void elementIsHidden(By locator) {
        BaseDriver.pauseForEffect();
        assertFalse(Is.displayed(locator));
    }

    public static void elementExists(By locator) {
        BaseDriver.pauseForEffect();
        assertTrue(Is.exists(locator));
    }

    public static void elementDoesNotExist(By locator) {
        BaseDriver.pauseForEffect();
        assertFalse(Is.exists(locator));
    }

    public static void elementIsEnabled(By locator) {
        BaseDriver.pauseForEffect();
        assertTrue(Is.enabled(locator));
    }

    public static void elementIsDisabled(By locator) {
        BaseDriver.pauseForEffect();
        assertFalse(Is.enabled(locator));
    }

    public static void isTrue(boolean condition) {
        BaseDriver.pauseForEffect();
        assertTrue(condition);
    }

    public static void isTrue(boolean condition, String msg) {
        BaseDriver.pauseForEffect();
        assertTrue(condition, msg);
    }

    public static void isFalse(boolean condition) {
        BaseDriver.pauseForEffect();
        assertFalse(condition);
    }

    public static void isFalse(boolean condition, String msg) {
        BaseDriver.pauseForEffect();
        assertFalse(condition, msg);
    }

    public static <T> void equals(T actual, T expected) {
        BaseDriver.pauseForEffect();
        assertEquals(actual, expected);
    }

    public static <T> void equals(T actual, T expected, String msg) {
        BaseDriver.pauseForEffect();
        assertEquals(actual, expected, msg);
    }

    public static void intentionalFail() {
        BaseDriver.pauseForEffect();
        fail();
    }
}
