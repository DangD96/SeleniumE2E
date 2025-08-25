package framework;

import org.openqa.selenium.By;

public class Assert extends org.testng.Assert {
    public static void elementIsVisible(By locator) {
        SparkDriver.pauseForEffect();
        assertTrue(Is.displayed(locator));
    }

    public static void elementIsHidden(By locator) {
        SparkDriver.pauseForEffect();
        assertFalse(Is.displayed(locator));
    }

    public static void elementExists(By locator) {
        SparkDriver.pauseForEffect();
        assertTrue(Is.exists(locator));
    }

    public static void elementDoesNotExist(By locator) {
        SparkDriver.pauseForEffect();
        assertFalse(Is.exists(locator));
    }

    public static void elementIsEnabled(By locator) {
        SparkDriver.pauseForEffect();
        assertTrue(Is.enabled(locator));
    }

    public static void elementIsDisabled(By locator) {
        SparkDriver.pauseForEffect();
        assertFalse(Is.enabled(locator));
    }

    public static void isTrue(boolean condition) {
        SparkDriver.pauseForEffect();
        assertTrue(condition);
    }

    public static void isTrue(boolean condition, String msg) {
        SparkDriver.pauseForEffect();
        assertTrue(condition, msg);
    }

    public static void isFalse(boolean condition) {
        SparkDriver.pauseForEffect();
        assertFalse(condition);
    }

    public static void isFalse(boolean condition, String msg) {
        SparkDriver.pauseForEffect();
        assertFalse(condition, msg);
    }

    public static <T> void equals(T actual, T expected) {
        SparkDriver.pauseForEffect();
        assertEquals(actual, expected);
    }

    public static <T> void equals(T actual, T expected, String msg) {
        SparkDriver.pauseForEffect();
        assertEquals(actual, expected, msg);
    }

    public static void intentionalFail() {
        SparkDriver.pauseForEffect();
        fail();
    }
}
