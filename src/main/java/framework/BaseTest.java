package framework;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class BaseTest {
    private final static ThreadLocal<ExtentTest> testMethod = new ThreadLocal<>();
    private static ExtentReports report;
    private static String env;
    private static Instant suiteStartInstant;
    private static Instant suiteEndInstant;
    private final String FS = File.separator; // System dependent file separator (so tests can run on windows or unix)

    // Returns working/project directory. In my case, the path to SeleniumE2E
    private final String USER_DIR = System.getProperty("user.dir");
    private final String PATH_TO_TEST_SOURCES_ROOT = USER_DIR+FS+"src"+FS+"test"+FS+"java";
    private static String REPORT_SAVE_PATH;

    /** Load properties from maven run config and create the shell of the results report */
    @BeforeSuite()
    protected void setUp() {
        // System props can come from the maven command line arguments or the POM file
        // I'm getting these from the maven command line arguments
        try {
            getSuiteName();
            BaseDriver.browser = getBrowser();
            BaseDriver.env = getEnv();
            BaseDriver.url = getUrl();
            BaseDriver.FS = FS;
            BaseDriver.USER_DIR = USER_DIR;
            BaseDriver.REPORT_SAVE_PATH = REPORT_SAVE_PATH;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        createReport();
        suiteStartInstant = LocalDateTime.now().toInstant(ZoneOffset.ofHours(0));
    }

    @BeforeMethod()
    protected void launchApp() {
        BaseDriver.configureDriver();
        BaseDriver.startDriver();
    }

    @BeforeMethod(dependsOnMethods = {"launchApp"})
    protected void createTestEntry(ITestResult result) {
        // I consider each test method a "Test"
        String className = result.getMethod().getRealClass().getName(); // The real class where the test method was declared
        String methodName = result.getMethod().getMethodName();

        // Each thread gets its own Test
        testMethod.set(report.createTest(className + "_" + methodName));
    }

    @AfterMethod()
    protected void listenForResult(ITestResult result) throws IOException {
        ExtentTest testMethod = getTestMethod();
        if (result.getStatus() == ITestResult.FAILURE) {
            String relativePathToScreenshot = BaseDriver.saveErrorScreenshot(result); // Needs relative path from project directory
            testMethod.fail(result.getThrowable()).addScreenCaptureFromPath(relativePathToScreenshot);
        }
        else {testMethod.log(Status.PASS, "Success");}
    }

    /** Null the thread specific driver object */
    @AfterMethod(dependsOnMethods = {"listenForResult"})
    protected void tearDown() {BaseDriver.quitDriver();}

    @AfterSuite()
    protected void saveReport() {
        suiteEndInstant = LocalDateTime.now().toInstant(ZoneOffset.ofHours(0));
        report.setSystemInfo("Total run time", getDurationOfTestSuite());
        report.flush();
        if (!"PRD".equals(env)) {
            System.out.println("\n=====Test results can be found here: " + REPORT_SAVE_PATH + "======\n");
        }
    }

    // Provide methods for each thread to get their thread specific variables
    private ExtentTest getTestMethod() {return testMethod.get();}

    private static String getBrowser() {
        String browser = System.getProperty("browser");
        if (browser == null) {throw new PropertyNotSpecifiedException("browser");}
        return browser.toUpperCase();
    }

    private String getUrl() {
        String url = System.getProperty("url");
        if (url == null) {throw new PropertyNotSpecifiedException("url");}
        return url;
    }

    /** Get the value in the XML file's <suite> tag name attribute. This does not refer to the XML filename. */
    private String getSuiteName() {
        String suiteName = SuiteListener.suiteName;
        if (suiteName.contains("/") || suiteName.isEmpty()) {throw new InvalidXmlSuiteNameException();}
        return suiteName;
    }

    private String getEnv() {
        return System.getProperty("env");
    }

    private void createReport() {
        String suiteName = getSuiteName();
        String browser = getBrowser();
        String filename = suiteName + " " + getBrowser();
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
        String filePath = PATH_TO_TEST_SOURCES_ROOT + FS + "data" + FS + filename;
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
