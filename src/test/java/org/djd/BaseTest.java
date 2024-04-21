package org.djd;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.testng.annotations.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class BaseTest {
    protected WebDriver driver;
    private Properties props;
    
    @BeforeMethod(alwaysRun = true) // Always run so don't get skipped over if using TestNG Groups
    public void launchApp() throws IOException {
        parseConfig();
        setUpDriver();
        try {
            driver.get(props.getProperty("testURL"));
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driver.quit(); // Nulls the driver object
    }

    // One way to set up browser and baseURL. Could also use TestNG parameters
    private void parseConfig() throws IOException {
        // In IntelliJ, it's like the project/module directory
        String currentDirectory = System.getProperty("user.dir"); // C:\Users\david\coding\java\Udemy_Practice\SeleniumE2E
        props = new Properties();
        FileReader reader = new FileReader(currentDirectory + "\\src\\test\\java\\org\\djd\\Test.properties"); // Double backslash because single backslash is escape character
        props.load(reader);
    }

    private void setUpDriver() {
        String browser = props.getProperty("browser");
        if (browser.equalsIgnoreCase("Chrome")) {
            driver = new ChromeDriver();
        }
        else if (browser.equalsIgnoreCase("Edge")) {
            driver = new EdgeDriver();
        }
        else if (browser.equalsIgnoreCase("Firefox")) {
            driver = new FirefoxDriver();
        }
        driver.manage().window().maximize();
    }
}
