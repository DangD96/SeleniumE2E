package framework;

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
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Listeners({SuiteListener.class}) // Add listener to get name of the XML test suite
public abstract class BaseTest {
    // Create threadsafe variables
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> testMethod = new ThreadLocal<>();

    private static String suiteName;
    private static ExtentReports report;
    private static String browser;
    private static String url;
    private static Instant suiteStartInstant;
    private static Instant suiteEndInstant;

    // System dependent file separator (so tests can run on windows or unix)
    private final String FS = File.separator;

    // Returns working/project directory. In my case, C:\Users\david\coding\java\SeleniumE2E
    private final String USER_DIR = System.getProperty("user.dir");
    private final String PATH_TO_TEST_SOURCES_ROOT = USER_DIR+FS+"src"+FS+"test"+FS+"java";
    private static String REPORT_SAVE_PATH;

    /** Load properties from maven run config and start up the results report */
    @BeforeSuite()
    protected void setUp() {
        // System props can come from the maven command line arguments or the POM file
        // I'm getting these from the maven command line arguments
        try {getSetupProperties();}
        catch (Exception e) {throw new RuntimeException(e);}
        createReport();
        suiteStartInstant = LocalDateTime.now().toInstant(ZoneOffset.ofHours(0));
    }

    @BeforeMethod()
    protected void launchApp() {setUpDriver();}

    @BeforeMethod(dependsOnMethods = {"launchApp"})
    protected void createTestEntry(ITestResult result) {
        // I consider each test a method a "Test"
        // Each thread gets its own Test
        testMethod.set(report.createTest(result.getMethod().getMethodName()));
    }

    @AfterMethod()
    protected void listenForResult(ITestResult result) throws IOException {
        ExtentTest testMethod = getTestMethod();
        if (result.getStatus() == ITestResult.FAILURE) {
            String relativePathToScreenshot = saveErrorScreenshot(result); // Needs relative path from project directory
            testMethod.fail(result.getThrowable()).addScreenCaptureFromPath(relativePathToScreenshot);
        }
        else {testMethod.log(Status.PASS, "Success");}
    }

    /** Null the thread specific driver object */
    @AfterMethod(dependsOnMethods = {"listenForResult"})
    protected void tearDown() {getDriver().quit();}

    @AfterSuite()
    protected void saveReport() {
        suiteEndInstant = LocalDateTime.now().toInstant(ZoneOffset.ofHours(0));
        report.setSystemInfo("Total run time", getDurationOfTestSuite());
        report.flush();
        System.out.println("\n=====Test results can be found here: " + REPORT_SAVE_PATH + "======\n");
    }

    // Provide methods for each thread to get their thread specific variables
    public WebDriver getDriver() {return driver.get();}
    public ExtentTest getTestMethod() {return testMethod.get();}

    private void getSetupProperties() {
        getSuiteName();
        getBrowser();
        getUrl();
    }

    private void getBrowser() {
        browser = System.getProperty("browser");
        if (browser == null) {throw new PropertyNotSpecifiedException("browser");}
        browser = browser.toUpperCase();
    }

    private void getUrl() {
        url = System.getProperty("url");
        if (url == null) {throw new PropertyNotSpecifiedException("url");}
    }

    /** Get the value in the XML file's <suite> tag name attribute. This does not refer to the XML filename. */
    private void getSuiteName() {
        suiteName = SuiteListener.suiteName;
        if (suiteName.contains("/") || suiteName.isEmpty()) {throw new InvalidXmlSuiteNameException();}
    }

    private void createReport() {
        String filename = suiteName + " " + browser;
        String reportName = suiteName + " - " + browser;
        REPORT_SAVE_PATH = USER_DIR + FS + "test-results" + FS + filename.replace(" ", "_") + ".html"; // directory where output is to be printed
        ExtentSparkReporter reporter = new ExtentSparkReporter(REPORT_SAVE_PATH);
        reporter.config().setReportName(reportName);
        reporter.config().setDocumentTitle("DJD Automation Report");
        reporter.config().setTheme(Theme.DARK);
        reporter.config().setTimelineEnabled(true);
        report = new ExtentReports();
        report.attachReporter(reporter);
    }

    private void setUpDriver() {
        switch (browser) {
            case "EDGE":
                // https://peter.sh/experiments/chromium-command-line-switches/
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--guest"); // Need to add this so Edge doesn't show random popups
                driver.set(new EdgeDriver(edgeOptions));
                break;
            case "FIREFOX":
                // https://wiki.mozilla.org/Firefox/CommandLineOptions
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("-private");
                driver.set(new FirefoxDriver(firefoxOptions));
                break;
            default:
                // https://peter.sh/experiments/chromium-command-line-switches/
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--guest");
                driver.set(new ChromeDriver(chromeOptions)); // set driver variable in current thread
                break;
        }
        WebDriver driver = getDriver();
        driver.manage().window().maximize();
        try {driver.get(url);}
        catch (Exception e) {
            driver.quit();
            throw new RuntimeException(e);
        }
    }

    private String saveErrorScreenshot(ITestResult result) throws IOException {
        String className = result.getMethod().getRealClass().getName(); // The real class where the test method was declared
        String methodName = result.getMethod().getMethodName();

        TakesScreenshot screenshotMode = (TakesScreenshot) getDriver();
        File tempFile = screenshotMode.getScreenshotAs(OutputType.FILE);

        File destFile = new File(USER_DIR + FS + "error-screenshots" + FS + className + "_" + methodName + ".png");
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

    private String getDurationOfTestSuite() {
        Duration duration = Duration.between(suiteStartInstant, suiteEndInstant);
        int minutes = duration.toMinutesPart();
        double minutesFraction = (double) duration.toSecondsPart() / 60;
        String runTimeStr = String.valueOf(minutes + minutesFraction);
        int runTime = (int) Math.round(Double.parseDouble(runTimeStr));
        if (runTime < 1) {return "1 min";}
        return runTime + " mins";
    }

    @SuppressWarnings("SameParameterValue")
    protected Object[] getTestData(String filename) throws IOException {
        String filePath = PATH_TO_TEST_SOURCES_ROOT + FS + filename;
        ArrayList<HashMap<String, String>> data = deserializeJSON(filePath);
        int size = data.size();
        Object[] objAry = new Object[size]; // Object array to store the hashmaps
        for (int i = 0; i < size; i++) {objAry[i] = data.get(i);}
        return objAry;
    }

    private ArrayList<HashMap<String, String>> deserializeJSON(String path) throws IOException {
        /* Use Jackson API to convert JSON objects to HashMaps.
        Return as ArrayList because getTestData will need to retrieve data from it
        and that is faster compared to LinkedList */
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(path), new TypeReference<>(){});
    }
}
