package org.djd;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
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
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BaseTest implements ITestListener {
    // TestNG will create a new instance of BaseTest class for you per each test.
    // If you want to share your variables - make them static
    // https://stackoverflow.com/questions/69721031/lateinit-variable-is-not-initialized-in-testngs-beforesuite
    protected static WebDriver driver;
    static ExtentReports report;
    static ExtentTest testMethod;

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

    // Always run so don't get skipped over if using TestNG Groups
    @BeforeSuite(alwaysRun = true)
    public void setUp() {
        report = new ExtentReports();
    }

    // Using native dependency injection https://testng.org/#_native_dependency_injection
    @BeforeTest(alwaysRun = true)
    public void launchApp(ITestContext context) throws IOException {
        attachReporter(context);
        parseConfig();
        setUpDriver();
        driver.get(baseURL);
    }

    // "Method" = Method with @Test annotation
    @BeforeMethod(alwaysRun = true)
    public void createTest (ITestResult result) {
        testMethod = report.createTest(result.getMethod().getMethodName());
    }

    @AfterMethod(alwaysRun = true)
    public void listenForResult(ITestResult result) throws IOException {
        // Doing this because onTestFailure listener keeps logging twice
        if (result.getStatus() == ITestResult.FAILURE) {
            String path = getScreenshot();
            testMethod.fail(result.getThrowable());
            testMethod.addScreenCaptureFromPath(path);
        }
        else {
            testMethod.log(Status.PASS, "Success");
        }
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        driver.quit();
    }

    @AfterSuite(alwaysRun = true)
    public void saveReport() {
        report.flush();
    }

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

    private String getScreenshot() throws IOException {
        TakesScreenshot screenshotMode = (TakesScreenshot) driver;
        File tempFile = screenshotMode.getScreenshotAs(OutputType.FILE);
        File destFile = new File(PATH_TO_PACKAGE+FS+"screenshots"+FS+"screenshot.png");
        FileUtils.copyFile(tempFile, destFile);
        return destFile.getAbsolutePath();
    }

    private void attachReporter(ITestContext context) {
        String suiteName = context.getSuite().getName();

        // directory where output is to be printed
        ExtentSparkReporter reporter = new ExtentSparkReporter(USER_DIR + FS + "test-results" + FS + suiteName.replace(" ", "_") + ".html");

        reporter.config().setReportName("Results for " + suiteName);
        reporter.config().setDocumentTitle(suiteName);
        report.attachReporter(reporter);
    }
}
