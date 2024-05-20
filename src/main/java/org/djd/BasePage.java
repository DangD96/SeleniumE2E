package org.djd;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

abstract class BasePage {
    protected final WebDriver driver;
    protected final JavascriptExecutor js;
    private final Duration TIMEOUT = Duration.ofSeconds(15);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    public boolean waitForElementToBeVisible(WebElement element) {
        return new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.visibilityOf(element)) != null;
    }

    public boolean waitForElementToBeVisible(By locator) {
        return new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(locator)) != null;
    }

    public boolean waitForElementsToBeVisible(List<WebElement> elements) {
        return new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.visibilityOfAllElements(elements)) != null;
    }

    public boolean waitForElementToBeInvisible(WebElement element) {
        return new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.invisibilityOf(element));
    }

    public boolean waitForElementToBeInvisible(By locator) {
        return new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public String getURL() {
        return driver.getCurrentUrl();
    }
}
