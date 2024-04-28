package org.djd;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class BaseTest {
    protected WebDriver driver;
    private String browser;
    private String baseURL;
    private Boolean isHeadless;

    @BeforeTest(alwaysRun = true) // Always run so don't get skipped over if using TestNG Groups
    public void launchApp() throws IOException {
        parseConfig();
        setUpDriver();
        driver.get(baseURL);
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {driver.quit();} // Nulls the driver object

    // One way to set up browser and baseURL. Could also use TestNG parameters
    private void parseConfig() throws IOException {
        // In IntelliJ, user.dir is like the project/module directory
        String currentDirectory = System.getProperty("user.dir"); // C:\Users\david\coding\java\Udemy_Practice\SeleniumE2E
        Properties props = new Properties();
        FileReader reader = new FileReader(currentDirectory + "\\src\\test\\java\\org\\djd\\Test.properties"); // Double backslash because single backslash is escape character
        props.load(reader);
        browser = props.getProperty("browser");
        baseURL = props.getProperty("testURL");
        isHeadless = Boolean.valueOf(props.getProperty("headless"));
    }

    private void setUpDriver() {
        if (browser.equalsIgnoreCase("Chrome")) {
            // https://peter.sh/experiments/chromium-command-line-switches/
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--guest");
            if (isHeadless) {options.addArguments("--headless=new");}
            driver = new ChromeDriver(options);
        }
        else if (browser.equalsIgnoreCase("Edge")) {
            // https://peter.sh/experiments/chromium-command-line-switches/
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--guest"); // Need to add this so Edge doesn't show random popups
            if (isHeadless) {options.addArguments("--headless=new");}
            driver = new EdgeDriver(options);
        }
        else if (browser.equalsIgnoreCase("Firefox")) {
            // https://wiki.mozilla.org/Firefox/CommandLineOptions
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("-private");
            if (isHeadless) {options.addArguments("-headless");}
            driver = new FirefoxDriver(options);
        }
        driver.manage().window().maximize();
    }

    @DataProvider
    public Object[][] getTestData() throws IOException {
        String filepath = System.getProperty("user.dir") + "\\src\\test\\java\\org\\djd\\Data.json";
        List<HashMap<String, String>> data = getJsonTestDataAsHashMap(filepath);
        return new Object[][]{{data.get(0)}, {data.get(1)}};
    }

    public List<HashMap<String, String>> getJsonTestDataAsHashMap(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper(); // Use Jackson API to convert JSON to a HashMap
        return mapper.readValue(new File(path), new TypeReference<>() {});
    }
}
