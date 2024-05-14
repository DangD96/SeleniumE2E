package org.djd;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

abstract class BasePage {
    protected final WebDriver driver;
    private final Duration TIMEOUT = Duration.ofSeconds(15);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public Boolean waitForElementToBeVisible(WebElement element) {
        return new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.visibilityOf(element)) != null;
    }

    public Boolean waitForElementToBeVisible(By locator) {
        return new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(locator)) != null;
    }

    public Boolean waitForElementsToBeVisible(List<WebElement> elements) {
        return new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.visibilityOfAllElements(elements)) != null;
    }

    public Boolean waitForElementToBeInvisible(WebElement element) {
        return new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.invisibilityOf(element));
    }

    public Boolean waitForElementToBeInvisible(By locator) {
        return new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public String getURL() {
        return driver.getCurrentUrl();
    }
}
