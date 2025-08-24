package framework;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;

import static org.testng.Assert.*;

public abstract class BasePage {
    protected WebDriver driver;
    protected JavascriptExecutor js;
    static WebDriverWait WAIT;
    static final int MAX_RETRIES = 5;
    static final long WAIT_MS = 500;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        js = (JavascriptExecutor) driver;
        WAIT = new WebDriverWait(driver, Duration.ofSeconds(5));
        WaitForAJAX();
    }

    // Function = takes something, returns something
    public <R> R ActionWithRetry(By locator, Function<By, R> function, boolean isInteractiveAction) {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                if (isInteractiveAction) {
                    WaitForElementToBeInteractable(locator); // for actions that need element to be interactable
                } else {
                    WaitForElementToBeVisible(locator); // for actions like that just need element to be visible
                }
                return function.apply(locator);
            } catch (StaleElementReferenceException | ElementClickInterceptedException retry) {
                retries++;
                Sleep(WAIT_MS);
            }
        }
        throw new RuntimeException(String.format("Action failed after %d retries for locator: %s", MAX_RETRIES, locator));
    }

    public void Click(By locator) {
        // passing locator into the lambda function as an argument
        ActionWithRetry(locator, loc -> {driver.findElement(loc).click(); return null;}, true);
    }

    public void TypeText(By locator, String text) {
        ActionWithRetry(locator, loc -> {driver.findElement(loc).sendKeys(text); return null;}, true);
    }

    public void Clear(By locator) {
        ActionWithRetry(locator, loc -> {driver.findElement(loc).clear(); return null;}, true);
    }

    public WebElement GetElement(By locator) {
        return ActionWithRetry(locator, loc -> driver.findElement(loc), false);
    }

    public String GetText(By locator) {
        return ActionWithRetry(locator, loc -> driver.findElement(loc).getText(), false);
    }

    public int GetCount(By locator) {
        return ActionWithRetry(locator, loc -> driver.findElements(loc).size(), false);
    }

    public static void Sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String GetURL() {
        return driver.getCurrentUrl();
    }

    public void WaitForElementToBeInteractable(By locator) {
        WAIT.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void WaitForElementToBeVisible(By locator) {
        WAIT.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void WaitForElementsToBeVisible(By locator) {
        WAIT.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public void WaitForElementToBeInvisible(By locator) {
        WAIT.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void WaitForAJAX() {
        WAIT.until(IsJQueryLoaded());
        WAIT.until(IsJSLoaded());
    }

    public ExpectedCondition<Boolean> IsJQueryLoaded() {
        return driver -> {
            try {
                // will resolve to something if jQuery is active. Otherwise a ReferenceError
                js.executeScript("jQuery.active");
                return false;
            } catch (Exception loaded) {
                return true; // no jQuery present (jQuery.active == 0)
            }
        };
    }

    public ExpectedCondition<Boolean> IsJSLoaded() {
        return driver -> Objects.requireNonNull(js.executeScript("return document.readyState"))
                .toString().equals("complete");
    }

    public Boolean IsDisplayed(By locator) {
        return ActionWithRetry(locator, loc -> driver.findElement(loc).isDisplayed(), false);
    }

    public Boolean IsEnabled(By locator) {
        return ActionWithRetry(locator, loc -> driver.findElement(loc).isEnabled(), false);
    }

    public Boolean IsExists(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // to help with building locators via composition (parent-child elements in repeatable UI)
    public By AppendToXpath(By baseLocator, String relative) {
        if (!baseLocator.toString().startsWith("By.xpath: ")) {
            throw new IllegalArgumentException("This method only supports XPath By objects");
        }
        String xpathAsString = baseLocator.toString().replace("By.xpath: ", "");
        return By.xpath(xpathAsString + relative);
    }

    public void AssertElementIsVisible(By locator) {
        if (!IsDisplayed(locator)) Fail();
    }

    public void AssertElementIsHidden(By locator) {
        if(IsDisplayed(locator)) Fail();
    }

    public void AssertElementExists(By locator) {
        if(!IsExists(locator)) Fail();
    }

    public void AssertElementDoesNotExist(By locator) {
        if(IsExists(locator)) Fail();
    }

    public void AssertElementIsEnabled(By locator) {
        if(!IsEnabled(locator)) Fail();
    }

    public void AssertElementIsDisabled(By locator) {
        if(IsEnabled(locator)) Fail();
    }

    public void AssertIsTrue(boolean condition) {
        assertTrue(condition);
    }

    public void AssertIsTrue(boolean condition, String msg) {
        assertTrue(condition, msg);
    }

    public void AssertIsFalse(boolean condition) {
        assertFalse(condition);
    }

    public void AssertIsFalse(boolean condition, String msg) {
        assertFalse(condition, msg);
    }

    public <T> void AssertEquals(T actual, T expected) {
        assertEquals(actual, expected);
    }

    public <T> void AssertEquals(T actual, T expected, String msg) {
        assertEquals(actual, expected, msg);
    }

    public void Fail() {
        fail();
    }

    public void Fail(String msg) {
        fail(msg);
    }
}
