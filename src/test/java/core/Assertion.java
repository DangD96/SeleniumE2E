package core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;


@SuppressWarnings("unused")
public class Assertion extends Assert {
    public static void elementIsVisible(WebDriver driver, By locator) {
        try {if (!driver.findElement(locator).isDisplayed()) fail();}
        catch (Exception e) {fail(String.valueOf(e));}
    }

    public static void elementIsVisible(WebElement element) {
        try {if (!element.isDisplayed()) fail();}
        catch (Exception e) {fail(String.valueOf(e));}
    }

    public static void elementIsInvisible(WebDriver driver, By locator) {
        try {if (driver.findElement(locator).isDisplayed()) fail();}
        catch (Exception ignored) {}
    }

    public static void elementIsInvisible(WebElement element) {
        try {if (element.isDisplayed()) fail();}
        catch (Exception ignored) {}
    }
}
