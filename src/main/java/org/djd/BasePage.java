package org.djd;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

abstract class BasePage {
    protected WebDriver driver;

    @FindBy(css = "a.shopping_cart_link")
    WebElement shoppingCartLink;

    @FindBy(id = "react-burger-menu-btn")
    WebElement burgerMenuOpen;
    @FindBy(css = "nav.bm-item-list")
    WebElement burgerMenuContents;
    @FindBy(linkText = "Logout")
    WebElement burgerMenuLogout;
    @FindBy(linkText = "Reset App State")
    WebElement burgerMenuResetAppState;
    @FindBy(id = "react-burger-cross-btn")
    WebElement burgerMenuClose;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void waitForElementToBeVisible(WebElement element) {
        new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitForElementToBeVisible(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitForElementToBeInvisible(WebElement element) {
        new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions.invisibilityOf(element));
    }

    public Boolean waitForElementToBeInvisible(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void goToCheckoutPage() {

    }

    public ShoppingCart goToShoppingCart() {
        waitForElementToBeVisible(shoppingCartLink);
        shoppingCartLink.click();
        return new ShoppingCart(driver);
    }

    public String getURL() {
        return driver.getCurrentUrl();
    }

    public void openBurgerMenu() {
        waitForElementToBeVisible(burgerMenuOpen);
        burgerMenuOpen.click();
        waitForElementToBeVisible(burgerMenuContents);
    }
    public LoginPage logOut() {
        openBurgerMenu();
        waitForElementToBeVisible(burgerMenuLogout);
        burgerMenuLogout.click();
        waitForElementToBeInvisible(burgerMenuContents);
        return new LoginPage(driver);
    }

}

