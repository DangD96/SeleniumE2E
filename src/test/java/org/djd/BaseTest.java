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
import java.util.*;

public class BaseTest {
    protected WebDriver driver;
    private String browser;
    private String baseURL;
    private Boolean isHeadless;

    /* In IntelliJ, user.dir is like the project/module directory
    System.getProperty("user.dir") = C:\Users\david\coding\java\Udemy_Practice\SeleniumE2E
    Double backslash below because single backslash is escape character */
    private final String PATH_TO_TEST_SOURCES_ROOT = System.getProperty("user.dir") + "\\src\\test\\java\\";

    // PackageName will vary depending on which subclass is running the test
    // PackageName uses the "." separator like "org.djd.Something"
    private final String PACKAGE_NAME = this.getClass().getPackageName();

    // Convert to file path using "\" like "org\djd\Something"
    private final String PATH_TO_PACKAGE = PATH_TO_TEST_SOURCES_ROOT + PACKAGE_NAME.replace(".", "\\");

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
        // Every Test should have its own Setup.properties file living under its own package
        String pathToSetupFile = PATH_TO_PACKAGE + "\\Setup.properties";
        FileReader reader = new FileReader(pathToSetupFile);
        Properties props = new Properties();
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

    public Object[] getTestData(String filepath) throws IOException {
        ArrayList<HashMap<String, String>> data = deserializeJSON(filepath);
        int size = data.size();
        Object[] objAry = new Object[size]; // Object array to store the hashmaps
        for (int i = 0; i < size; i++) {objAry[i] = data.get(i);}
        return objAry;
    }

    public ArrayList<HashMap<String, String>> deserializeJSON(String path) throws IOException {
        /* Use Jackson API to convert JSON objects to HashMaps.
        Return as ArrayList because getTestData will need to retrieve data from it
        and that is faster compared to LinkedList */
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path), new TypeReference<>(){});
    }
}
