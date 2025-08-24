package framework;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;

public abstract class BasePage {
    protected WebDriver driver;
    protected JavascriptExecutor js;
    final WebDriverWait WAIT;
    final int MAX_RETRIES = 5;
    final long WAIT_MS = 500;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.WAIT = new WebDriverWait(driver, Duration.ofSeconds(5));
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

    public int GetSize(By locator) {
        return ActionWithRetry(locator, loc -> driver.findElements(loc).size(), false);
    }

    public Boolean IsDisplayed(By locator) {
        return ActionWithRetry(locator, loc -> driver.findElement(loc).isDisplayed(), false);
    }

    public Boolean IsHidden(By locator) {
        return !ActionWithRetry(locator, loc -> driver.findElement(loc).isDisplayed(), false);
    }

    public Boolean IsEnabled(By locator) {
        return ActionWithRetry(locator, loc -> driver.findElement(loc).isEnabled(), false);
    }

    public Boolean IsDisabled(By locator) {
        return !ActionWithRetry(locator, loc -> driver.findElement(loc).isEnabled(), false);
    }

    public Boolean IsExists(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public Boolean IsNotExist(By locator) {
        try {
            driver.findElement(locator);
            return false;
        } catch (NoSuchElementException pass) {
            return true;
        }
    }

    public void Sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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

    public String GetURL() {
        return driver.getCurrentUrl();
    }

    // to help with building locators via composition (parent-child elements in repeatable UI)
    public By AppendToXpath(By baseLocator, String relative) {
        if (!baseLocator.toString().startsWith("By.xpath: ")) {
            throw new IllegalArgumentException("This method only supports XPath By objects");
        }
        String xpathAsString = baseLocator.toString().replace("By.xpath: ", "");
        return By.xpath(xpathAsString + relative);
    }
}
