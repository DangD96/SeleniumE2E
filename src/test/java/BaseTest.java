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
    // Create threadsafe variables
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> testMethod = new ThreadLocal<>();

    private static ExtentReports report;
    private static String browser;
    private static Boolean headless;
    private static String url;

    // Returns something like C:\Users\david\coding\java\SeleniumE2E
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

    private static String REPORT_PATH;

    // Provide methods for each thread to get their thread specific variables
    public WebDriver getDriver() {return driver.get();}

    public ExtentTest getTestMethod() {return testMethod.get();}

    @BeforeSuite(alwaysRun = true)
    protected void setUp() {
        // System props can come from the maven command line arguments or the POM file
        // I'm setting these from the maven command line arguments
        browser = System.getProperty("browser").toUpperCase();
        headless = Boolean.valueOf(System.getProperty("headless"));
        String runName = System.getProperty("runName");
        url = System.getProperty("url");
        createReport(runName);
    }

    @BeforeMethod(alwaysRun = true)
    protected void launchApp() {
        setUpDriver(browser, headless, url);
    }

    @BeforeMethod(alwaysRun = true, dependsOnMethods = {"launchApp"})
    protected void createTestEntry(ITestResult result) {
        // This implementation considers each test a method a "Test"
        // Each thread gets its own Test
        testMethod.set(report.createTest(result.getMethod().getMethodName()));
    }

    @AfterMethod(alwaysRun = true)
    protected void listenForResult(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            String relativePathToScreenshot = saveErrorScreenshot(); // Needs relative path from project directory
            getTestMethod().fail(result.getThrowable()).addScreenCaptureFromPath(relativePathToScreenshot);
        }
        else {
            getTestMethod().log(Status.PASS, "Success");
        }
    }

    @AfterMethod(alwaysRun = true, dependsOnMethods = {"listenForResult"})
    protected void tearDown() {
        getDriver().quit(); // Nulls thread specific driver object
    }

    @AfterSuite(alwaysRun = true)
    protected void saveReport() {
        report.flush();
        System.out.println("Test results can be found here: " + REPORT_PATH);
    }

    @DataProvider(parallel = true)
    protected Object[] getTestData() throws IOException {
        String pathToDataFile = PATH_TO_PACKAGE + FS + CLASS_NAME + ".json";
        ArrayList<HashMap<String, String>> data = deserializeJSON(pathToDataFile);
        int size = data.size();
        Object[] objAry = new Object[size]; // Object array to store the hashmaps
        for (int i = 0; i < size; i++) {objAry[i] = data.get(i);}
        return objAry;
    }

    private void setUpDriver(String browser, Boolean isHeadless, String url) {
        switch (browser) {
            case "EDGE":
                // https://peter.sh/experiments/chromium-command-line-switches/
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--guest"); // Need to add this so Edge doesn't show random popups
                if (isHeadless) {edgeOptions.addArguments("--headless=new");}
                driver.set(new EdgeDriver(edgeOptions));
                break;
            case "FIREFOX":
                // https://wiki.mozilla.org/Firefox/CommandLineOptions
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("-private");
                if (isHeadless) {firefoxOptions.addArguments("-headless");}
                driver.set(new FirefoxDriver(firefoxOptions));
                break;
            default:
                // https://peter.sh/experiments/chromium-command-line-switches/
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--guest");
                if (isHeadless) {chromeOptions.addArguments("--headless=new");}
                driver.set(new ChromeDriver(chromeOptions)); // set driver variable in current thread
                break;
        }
        getDriver().manage().window().maximize();
        getDriver().get(url);
    }

    protected void createReport(String runName) {
        // directory where output is to be printed
        REPORT_PATH = USER_DIR + FS + "test-results" + FS + runName.replace(" ", "_") + ".html";
        ExtentSparkReporter reporter = new ExtentSparkReporter(REPORT_PATH);
        reporter.config().setReportName(runName);
        reporter.config().setDocumentTitle("DJD Automation Report");
        reporter.config().setTheme(Theme.DARK);
        reporter.config().setTimelineEnabled(true);
        report = new ExtentReports();
        report.attachReporter(reporter);
    }

    private ArrayList<HashMap<String, String>> deserializeJSON(String path) throws IOException {
        /* Use Jackson API to convert JSON objects to HashMaps.
        Return as ArrayList because getTestData will need to retrieve data from it
        and that is faster compared to LinkedList */
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path), new TypeReference<>(){});
    }

    private String saveErrorScreenshot() throws IOException {
        TakesScreenshot screenshotMode = (TakesScreenshot) getDriver();
        File tempFile = screenshotMode.getScreenshotAs(OutputType.FILE);
        File destFile = new File(USER_DIR + FS + "screenshots" + FS + "screenshot.png");
        FileUtils.copyFile(tempFile, destFile);
        String absolutePath = destFile.getAbsolutePath();
        return getPathRelativeToUserDir(absolutePath);
    }

    private String getPathRelativeToUserDir(String absolutePath) {
        // Need to double escape for regex
        String[] results = USER_DIR.split("\\\\"); // Split on "\"
        String userDirAKAProjectName = results[results.length-1];
        return absolutePath.split(userDirAKAProjectName)[1]; // Get everything that comes after the project name
    }
}
