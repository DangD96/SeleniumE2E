import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

abstract class BasePage {
    protected final WebDriver driver;
    protected final JavascriptExecutor js;
    private final WebDriverWait WAIT;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.WAIT = new WebDriverWait(driver, Duration.ofSeconds(10));
        waitForAjax();
    }

    public WebElement waitForElementToBeVisible(By locator) {
        return WAIT.until(ExpectedConditions.visibilityOfElementLocated(locator));
        // Could also use ExpectedConditions.elementToBeClickable it has visibilityOfElementLocated built into it :)
    }

    public List<WebElement> waitForElementsToBeVisible(By locator) {
        return WAIT.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public boolean waitForElementToBeInvisible(By locator) {
        return WAIT.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    @SuppressWarnings("Convert2Lambda")
    public void waitForAjax() {
        // wait for jQuery to load
        // anonymous inner class
        ExpectedCondition<Boolean> isJQueryLoaded = new ExpectedCondition<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((int) js.executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
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
                return js.executeScript("return document.readyState").toString().equals("complete");
            }
        };

        WAIT.until(isJQueryLoaded);
        WAIT.until(isJsLoaded);
    }

    public String getURL() {
        return driver.getCurrentUrl();
    }
}
