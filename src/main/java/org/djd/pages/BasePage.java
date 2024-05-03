package org.djd.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

abstract class BasePage {
    protected WebDriver driver;
    private final Duration TIMEOUT = Duration.ofSeconds(15);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean waitForElementToBeVisible(WebElement element) {
        new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.visibilityOf(element));
        return true; // If we get down here then the above line didn't fail
    }

    public boolean waitForElementToBeVisible(By locator) {
        new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(locator));
        return true;
    }

    public boolean waitForElementToBeInvisible(WebElement element) {
        new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.invisibilityOf(element));
        return true;
    }

    public Boolean waitForElementToBeInvisible(By locator) {
        return new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public String getURL() {
        return driver.getCurrentUrl();
    }

    public boolean waitForElementsToBeVisible(List<WebElement> elements) {
        new WebDriverWait(driver, TIMEOUT).until(ExpectedConditions.visibilityOfAllElements(elements));
        return true;
    }
}
