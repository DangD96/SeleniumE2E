package org.djd;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
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
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class BaseTest {
    protected WebDriver driver;

    /* TestNG will create a new instance of the test class for you per each test.
    If you want to share your variables - make them static (addresses the "this.<variable> is null" error)
    https://stackoverflow.com/questions/69721031/lateinit-variable-is-not-initialized-in-testngs-beforesuite */
    private static ExtentReports report;
    private static String browser;
    private static String baseURL;
    private static Boolean isHeadless;

    private ExtentTest testMethod;

    // C:\Users\david\coding\java\Udemy_Practice\SeleniumE2E
    private final String USER_DIR = System.getProperty("user.dir");

    // System dependent file separator (so tests can run on windows or unix)
    private final String FS = File.separator;

    private final String PATH_TO_TEST_SOURCES_ROOT = USER_DIR+FS+"src"+FS+"test"+FS+"java"+FS;

    /* Will vary depending on which SUBCLASS is running the test (TestNG creates an object of the subclass)
    PackageName uses the "." separator like "org.djd.Something"
    Use Reflection API to get info about class name and package name */
    private final String PACKAGE_NAME = this.getClass().getPackageName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    // Convert to file path
    private final String PATH_TO_PACKAGE = PATH_TO_TEST_SOURCES_ROOT + PACKAGE_NAME.replace(".", FS);

    private final String OS_NAME = System.getProperty("os.name");

    /* Always run so don't get skipped over if using TestNG Groups
    Parameters set in the Intellij Run Configuration. Get injected into setUp method's parameters.
    Could also set in the XML file */
    @BeforeSuite(alwaysRun = true)
    @Parameters({"testURL", "testBrowser", "headlessMode", "runName"})
    protected void setUp(String testURL, String testBrowser, String headlessMode, String runName) {
        baseURL = testURL;
        browser = testBrowser.toUpperCase();
        isHeadless = Boolean.parseBoolean(headlessMode);
        createReport(browser, headlessMode, runName);
    }

    // Using native dependency injection https://testng.org/#_native_dependency_injection
    // "Test" = <Test/> tag defined in XML
    @BeforeTest(alwaysRun = true)
    protected void launchApp() {setUpDriver(baseURL, browser, isHeadless);}

    // "Method" = Method with @Test annotation
    @BeforeMethod(alwaysRun = true)
    protected void createTest (ITestResult result) {
        testMethod = report.createTest(result.getMethod().getMethodName());
    }

    @AfterMethod(alwaysRun = true)
    protected void listenForResult(ITestResult result) throws IOException {
        // Doing this because ITestResult's onTestFailure listener keeps logging twice
        if (result.getStatus() == ITestResult.FAILURE) {
            String path = getScreenshot();
            // Won't work with absolute path for some reason. Needs relative path from project directory
            testMethod.fail(result.getThrowable()).addScreenCaptureFromPath(path);
        } else {testMethod.log(Status.PASS, "Success");}
    }

    @AfterTest(alwaysRun = true)
    protected void tearDown() {driver.quit();} // Nulls driver object

    @AfterSuite(alwaysRun = true)
    protected void saveReport() {report.flush();}

    @DataProvider
    protected Object[] getTestData() throws IOException {
        String pathToDataFile = PATH_TO_PACKAGE + FS + CLASS_NAME + ".json";
        ArrayList<HashMap<String, String>> data = deserializeJSON(pathToDataFile);
        int size = data.size();
        Object[] objAry = new Object[size]; // Object array to store the hashmaps
        for (int i = 0; i < size; i++) {objAry[i] = data.get(i);}
        return objAry;
    }

    private void setUpDriver(String url, String browser, boolean isHeadless) {
        switch (browser) {
            case "EDGE":
                // https://peter.sh/experiments/chromium-command-line-switches/
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--guest"); // Need to add this so Edge doesn't show random popups
                if (isHeadless) {edgeOptions.addArguments("--headless=new");}
                driver = new EdgeDriver(edgeOptions);
                break;
            case "FIREFOX":
                // https://wiki.mozilla.org/Firefox/CommandLineOptions
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("-private");
                if (isHeadless) {firefoxOptions.addArguments("-headless");}
                driver = new FirefoxDriver(firefoxOptions);
                break;
            default:
                // https://peter.sh/experiments/chromium-command-line-switches/
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--guest");
                if (isHeadless) {chromeOptions.addArguments("--headless=new");}
                driver = new ChromeDriver(chromeOptions);
                break;
        }
        driver.manage().window().maximize();
        driver.get(url);
    }

    protected void createReport(String browser, String headlessMode, String runName) {
        // directory where output is to be printed
        ExtentSparkReporter reporter = new ExtentSparkReporter(USER_DIR + FS + "test-results" + FS + runName.replace(" ", "_") + ".html");
        reporter.config().setReportName("Test Run Results");
        reporter.config().setDocumentTitle("DJD Automation");
        reporter.config().setTheme(Theme.DARK);
        reporter.config().setTimelineEnabled(true);

        report = new ExtentReports();
        report.setSystemInfo("OS Used During Runtime", OS_NAME);
        report.setSystemInfo("Test Run", runName);
        report.setSystemInfo("Browser", browser);
        report.setSystemInfo("Headless?", headlessMode);
        report.attachReporter(reporter);
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
        File destFile = new File(USER_DIR + FS + "test-results" + FS + "screenshot.png");
        FileUtils.copyFile(tempFile, destFile);
        String absolutePath = destFile.getAbsolutePath();
        return getRelativePath(absolutePath);
    }

    private String getRelativePath(String absolutePath) {
        // Need to double escape for regex
        String[] results = USER_DIR.split("\\\\"); // Split on "\"
        String projectName = results[results.length-1];
        return absolutePath.split(projectName)[1]; // In my case, get everything that comes after "SeleniumE2E"
    }
}
