package org.djd;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;

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

    @BeforeMethod
    public void launchApp() throws IOException {
        parseProps();
        String browser = props.getProperty("browser");
        if (browser.equalsIgnoreCase("Chrome")) {driver = new ChromeDriver();}
        else if (browser.equalsIgnoreCase("Edge")) {driver = new EdgeDriver();}
        else if (browser.equalsIgnoreCase("Firefox")) {driver = new FirefoxDriver();}
        driver.manage().window().maximize();
        try {
            driver.get(props.getProperty("testURL"));
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    @AfterMethod
    public void tearDown() {
        driver.quit(); // Nulls the driver object
    }
}