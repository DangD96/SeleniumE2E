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
    protected WebDriver driver;
    protected JavascriptExecutor js;
    private final Duration TIMEOUT = Duration.ofSeconds(10);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        waitForJSandJQueryToLoad();
    }

    public WebElement waitForElementToBeVisible(By locator) {
        return new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public List<WebElement> waitForElementsToBeVisible(By locator) {
        return new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public boolean waitForElementToBeInvisible(By locator) {
        return new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForJSandJQueryToLoad() {

        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        // wait for jQuery to load
        ExpectedCondition<Boolean> isJQueryLoaded = driver -> {
            try {
                return ((int)js.executeScript("return jQuery.active") == 0);
            }
            catch (Exception e) {
                // no jQuery present
                return true;
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> isJsLoaded = driver -> js.executeScript("return document.readyState")
                .toString().equals("complete");

        wait.until(isJQueryLoaded);
        wait.until(isJsLoaded);
    }

    public String getURL() {
        return driver.getCurrentUrl();
    }
}
