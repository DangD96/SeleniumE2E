package framework;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public abstract class BasePage {
    protected final WebDriver driver;
    protected final JavascriptExecutor js;
    private final WebDriverWait WAIT;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.WAIT = new WebDriverWait(driver, Duration.ofSeconds(10));
        waitForAjax();
    }

    public WebElement waitForElementToBeClickable(By locator) {return WAIT.until(ExpectedConditions.elementToBeClickable(locator));}

    public WebElement waitForElementToBeClickable(WebElement element) {return WAIT.until(ExpectedConditions.elementToBeClickable(element));}

    public WebElement waitForElementToBeVisible(By locator) {return WAIT.until(ExpectedConditions.visibilityOfElementLocated(locator));}

    public WebElement waitForElementToBeVisible(WebElement element) {return WAIT.until(ExpectedConditions.visibilityOf(element));}

    public List<WebElement> waitForElementsToBeVisible(By locator) {return WAIT.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));}

    public List<WebElement> waitForElementsToBeVisible(WebElement element) {return WAIT.until(ExpectedConditions.visibilityOfAllElements(element));}

    public boolean waitForElementToBeInvisible(By locator) {return WAIT.until(ExpectedConditions.invisibilityOfElementLocated(locator));}

    public boolean waitForElementToBeInvisible(WebElement element) {return WAIT.until(ExpectedConditions.invisibilityOf(element));}

    public void typeText(WebElement element, String text) {element.sendKeys(text);}

    public void typeText(By locator, String text) {driver.findElement(locator).sendKeys(text);}

    @SuppressWarnings("Convert2Lambda")
    public void waitForAjax() {
        // wait for jQuery to load
        // anonymous inner class
        ExpectedCondition<Boolean> isJQueryLoaded = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((int) js.executeScript("return jQuery.active") == 0);
                } catch (Exception ignored) {
                    // no jQuery present
                    return true;
                }
            }
        };

        // wait for Javascript to load
        // anonymous inner class
        ExpectedCondition<Boolean> isJsLoaded = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return Objects.requireNonNull(js.executeScript("return document.readyState")).toString().equals("complete");
            }
        };

        WAIT.until(isJQueryLoaded);
        WAIT.until(isJsLoaded);
    }

    public String getURL() {return driver.getCurrentUrl();}
}
