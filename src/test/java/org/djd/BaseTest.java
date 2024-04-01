package org.djd;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class BaseTest {
    protected WebDriver driver;

    @BeforeTest
    public WebDriver initDriver() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        return driver;
    }

    @BeforeTest(dependsOnMethods = {"initDriver"})
    public void launchApp() {
        try {
            driver.get("https://www.rahulshettyacademy.com/loginpagePractise/");
        } catch (Exception e) {
            System.out.println(e);

        }
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}