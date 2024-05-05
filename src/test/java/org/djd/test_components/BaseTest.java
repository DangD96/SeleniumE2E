package org.djd.test_components;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
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

    // System.getProperty("user.dir") = C:\Users\david\coding\java\Udemy_Practice\SeleniumE2E
    private final String USER_DIR = System.getProperty("user.dir");

    // system dependent file separator
    private final String FS = File.separator;

    private final String PATH_TO_TEST_SOURCES_ROOT = USER_DIR + FS + "src" + FS + "test" + FS + "java" + FS;

    // PackageName will vary depending on which SUBCLASS is running the test
    // PackageName uses the "." separator like "org.djd.Something"
    // Use Reflection API to get info about class name and package name
    private final String PACKAGE_NAME = this.getClass().getPackageName();

    // Convert to file path
    private final String PATH_TO_PACKAGE = PATH_TO_TEST_SOURCES_ROOT + PACKAGE_NAME.replace(".", FS);

    ExtentReports report;
    ExtentTest test;

    // "Test" = The <Test/> tag in XML file (if running a test suite)
    @BeforeClass(alwaysRun = true) // Always run so don't get skipped over if using TestNG Groups
    public void launchApp() throws IOException {
        report = createTestReport();
        parseConfig();
        setUpDriver();
        driver.get(baseURL);
    }

    // "Method" = Method with @Test annotation
    @BeforeMethod(alwaysRun = true)
    public void createTest (ITestResult result){
        test = report.createTest(result.getMethod().getMethodName());
    }

    @AfterMethod(alwaysRun = true)
    public void flush(ITestResult result) {
        System.out.println("method done");
        int status = result.getStatus(); // Status of 1 = Pass / 2 = Fail
        report.flush();
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        System.out.println("test done");
        driver.quit();
    } // Nulls the driver object

    @DataProvider
    public Object[] getTestData() throws IOException {
        String pathToDataFile = PATH_TO_PACKAGE + FS + "Data.json";
        ArrayList<HashMap<String, String>> data = deserializeJSON(pathToDataFile);
        int size = data.size();
        Object[] objAry = new Object[size]; // Object array to store the hashmaps
        for (int i = 0; i < size; i++) {objAry[i] = data.get(i);}
        return objAry;
    }

    // One way to set up browser and baseURL. Could also use TestNG parameters
    private void parseConfig() throws IOException {
        // Every Test should have its own Setup.properties file living under its own package
        String pathToSetupFile = PATH_TO_PACKAGE + FS + "Setup.properties";
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

    private ArrayList<HashMap<String, String>> deserializeJSON(String path) throws IOException {
        /* Use Jackson API to convert JSON objects to HashMaps.
        Return as ArrayList because getTestData will need to retrieve data from it
        and that is faster compared to LinkedList */
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path), new TypeReference<>(){});
    }

    private File takeScreenshot() throws IOException {
        TakesScreenshot screenshotMode = (TakesScreenshot) driver;
        File tempFile = screenshotMode.getScreenshotAs(OutputType.FILE);
        File destFile = new File(PATH_TO_PACKAGE+FS+"screenshots"+FS+"screenshot.png");
        FileUtils.copyFile(tempFile, destFile);
        return destFile;
    }

    ExtentReports createTestReport() {
        String name = this.getClass().getSimpleName();

        // filename where output is to be printed
        ExtentSparkReporter reporter = new ExtentSparkReporter(USER_DIR + FS + "test-results" + FS + name + FS + "results.html");
        reporter.config().setReportName("Results for " + name);
        reporter.config().setDocumentTitle("Test Results");

        ExtentReports report = new ExtentReports();
        report.attachReporter(reporter);
        return report;
    }
}
