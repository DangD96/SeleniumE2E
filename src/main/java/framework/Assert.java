package framework;

import org.openqa.selenium.By;

public class Assert extends org.testng.Assert {
    public static void elementIsVisible(By locator) {
        assertTrue(Is.displayed(locator));
    }

    public static void elementIsHidden(By locator) {
        assertFalse(Is.displayed(locator));
    }

    public static void elementExists(By locator) {
        assertTrue(Is.exists(locator));
    }

    public static void elementDoesNotExist(By locator) {
        assertFalse(Is.exists(locator));
    }

    public static void elementIsEnabled(By locator) {
        assertTrue(Is.enabled(locator));
    }

    public static void elementIsDisabled(By locator) {
        assertFalse(Is.enabled(locator));
    }

    public static void isTrue(boolean condition) {
        assertTrue(condition);
    }

    public static void isTrue(boolean condition, String msg) {
        assertTrue(condition, msg);
    }

    public static void isFalse(boolean condition) {
        assertFalse(condition);
    }

    public static void isFalse(boolean condition, String msg) {
        assertFalse(condition, msg);
    }

    public static <T> void equals(T actual, T expected) {
        assertEquals(actual, expected);
    }

    public static <T> void equals(T actual, T expected, String msg) {
        assertEquals(actual, expected, msg);
    }

    public static void intentionalFail() {
        fail();
    }
}
