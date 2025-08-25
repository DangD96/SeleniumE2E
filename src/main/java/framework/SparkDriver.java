package framework;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.function.Function;

// Custom static wrapper around common WebDriver actions
public class SparkDriver {
    // Threadsafe variables because tests methods run in parallel
    static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    static ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();

    static final int MAX_RETRIES = 10;
    static final long WAIT_MS = 1000;
    static String browser;
    static String url;
    static String env;
    static String FS;
    static String USER_DIR;
    static String REPORT_SAVE_PATH;

    // get thread specific variables
    public static WebDriver getDriver() {return driver.get();}
    public static WebDriverWait getWait() {return wait.get();}

    public static void configureDriver() {
        switch (browser) {
            case "EDGE":
                // https://peter.sh/experiments/chromium-command-line-switches/
                EdgeOptions edgeOptions = new EdgeOptions();
                if ("PRD".equals(env)) {
                    edgeOptions.addArguments("--no-sandbox"); // sandboxing has potential to cause issues in automated testing envs
                    edgeOptions.addArguments("--disable-dev-shm-usage");
                    edgeOptions.addArguments("--headless"); // PRD tests will run in GitHub Runner environment, which is headless
                }
                edgeOptions.addArguments("--guest"); // Need to add this so Edge doesn't show random popups
                driver.set(new EdgeDriver(edgeOptions));
                break;
            case "FIREFOX":
                // https://wiki.mozilla.org/Firefox/CommandLineOptions
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if ("PRD".equals(env)) {
                    firefoxOptions.addArguments("-headless");
                }
                firefoxOptions.addArguments("-private");
                driver.set(new FirefoxDriver(firefoxOptions));
                break;
            case "CHROME":
                // https://peter.sh/experiments/chromium-command-line-switches/
                ChromeOptions chromeOptions = new ChromeOptions();
                if ("PRD".equals(env)) {
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--headless");
                }
                chromeOptions.addArguments("--guest");
                driver.set(new ChromeDriver(chromeOptions)); // set driver variable in current thread
                break;
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }
    }

    public static void startDriver() {
        WebDriver driver = getDriver();
        wait.set(new WebDriverWait(driver, Duration.ofSeconds(5)));
        driver.manage().window().maximize();
        try {
            driver.get(url);
        }
        catch (Exception e) {
            driver.quit();
            throw new RuntimeException(e);
        }
    }

    public static void quitDriver() {
        getDriver().quit();
    }

    public static void pauseForEffect() {
        sleep(500);
    }

    public static String saveErrorScreenshot(ITestResult result) throws IOException {
        String className = result.getMethod().getRealClass().getName(); // The real class where the test method was declared
        String methodName = result.getMethod().getMethodName();

        TakesScreenshot screenshotMode = (TakesScreenshot) getDriver();
        File tempFile = screenshotMode.getScreenshotAs(OutputType.FILE);

        File destFile = new File(USER_DIR + FS + "error-screenshots" + FS + className + "_" + methodName + ".png");
        FileUtils.copyFile(tempFile, destFile);
        String absolutePath = destFile.getAbsolutePath();
        return getPathRelativeToUserDir(absolutePath);
    }

    public static String getPathRelativeToUserDir(String absolutePath) {
        String OS = System.getProperty("os.name");
        String[] results;

        if (OS.contains("Windows")) {
            // Because backslash is a special character in both regex AND string literals,
            // need to double escape so the resulting string evalutates as "\\"
            // i.e. match on a literal single backslash
            results = USER_DIR.split("\\\\"); // Split on "\"
        } else {
            results = USER_DIR.split("/");   // Split on "/"
        }

        String userDirAKAProjectName = results[results.length-1];
        return absolutePath.split(userDirAKAProjectName)[1]; // Get everything that comes after the project name
    }

    // Function = takes something, returns something
    public static <R> R actionWithRetry(By locator, Function<By, R> function, boolean isInteractiveAction) {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                if (isInteractiveAction) {
                    waitForElementToBeInteractable(locator); // for actions that need element to be interactable
                } else {
                    waitForElementToBeVisible(locator); // for actions like that just need element to be visible
                }
                return function.apply(locator);
            } catch (StaleElementReferenceException | ElementClickInterceptedException retry) {
                retries++;
                sleep(WAIT_MS);
            }
        }
        throw new RuntimeException(String.format("Action failed after %d retries for locator: %s", MAX_RETRIES, locator));
    }

    public static void click(By locator) {
        // passing locator into the lambda function as an argument
        actionWithRetry(locator, loc -> {getDriver().findElement(loc).click(); return null;}, true);
        pauseForEffect();
    }

    public static void typeText(By locator, String text) {
        actionWithRetry(locator, loc -> {getDriver().findElement(loc).sendKeys(text); return null;}, true);
        pauseForEffect();
    }

    public static void clear(By locator) {
        actionWithRetry(locator, loc -> {getDriver().findElement(loc).clear(); return null;}, true);
        pauseForEffect();
    }

    public static WebElement getElement(By locator) {
        return actionWithRetry(locator, loc -> getDriver().findElement(loc), false);
    }

    public static String getText(By locator) {
        return actionWithRetry(locator, loc -> getDriver().findElement(loc).getText(), false);
    }

    public static int getCount(By locator) {
        try {
            return getDriver().findElements(locator).size();
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getURL() {
        return getDriver().getCurrentUrl();
    }

    public static void waitForElementToBeInteractable(By locator) {
        getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void waitForElementToBeVisible(By locator) {
        getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void waitForElementsToBeVisible(By locator) {
        getWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static void waitForElementToBeInvisible(By locator) {
        getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // to help with building locators via composition (parent-child elements in repeatable UI)
    public static By appendToXpath(By baseLocator, String relative) {
        if (!baseLocator.toString().startsWith("By.xpath: ")) {
            throw new IllegalArgumentException("This method only supports XPath By objects");
        }
        String xpathAsString = baseLocator.toString().replace("By.xpath: ", "");
        return By.xpath(xpathAsString + relative);
    }
}
