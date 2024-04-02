package org.djd;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class BaseTest {
    protected WebDriver driver;
    private Properties props;

    public void parseProps() throws IOException {
        // In IntelliJ, it's like the project/module directory
        String currentDirectory = System.getProperty("user.dir"); // C:\Users\david\coding\java\Udemy_Practice\SeleniumE2E
        props = new Properties();
        FileReader reader = new FileReader(currentDirectory + "\\src\\test\\java\\org\\djd\\Test.properties"); // Double backslash because single backslash is escape character
        props.load(reader);
    }

    @BeforeTest
    public void initDriver() throws IOException {
        parseProps();
        String browser = props.getProperty("browser");
        if (browser.equalsIgnoreCase("Chrome")) {driver = new ChromeDriver();}
        else if (browser.equalsIgnoreCase("Edge")) {driver = new EdgeDriver();}
        else if (browser.equalsIgnoreCase("Firefox")) {driver = new FirefoxDriver();}
        driver.manage().window().maximize();
    }

    @BeforeTest(dependsOnMethods = {"initDriver"})
    public void launchApp() {
        try {
            driver.get(props.getProperty("testURL"));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}