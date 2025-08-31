package framework;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.function.*;

// Custom static wrapper around common WebDriver behaviors
public class SparkDriver {
    /** === Thread safe variables and their getters (tests methods run in parallel) === */
    static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    static ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();
    public static WebDriver getDriver() {return driver.get();}
    public static WebDriverWait getWait() {return wait.get();}

    /** === Constants === */
    static final int MAX_RETRIES = 10;
    static final long WAIT_MS = 1000;

    /** === Driver === */
    // set driver variable in current thread
    public static void configureDriver() {
        switch (SparkTest.browser) {
            case "CHROME" -> driver.set(new ChromeDriver(getChromeOptions()));
            case "EDGE" -> driver.set(new EdgeDriver(getEdgeOptions()));
            case "FIREFOX" -> driver.set(new FirefoxDriver(getFirefoxOptions()));
            default -> throw new IllegalArgumentException("Browser not supported: " + SparkTest.browser);
        }
    }

    private static ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        return addArguments(chromeOptions);
    }

    private static EdgeOptions getEdgeOptions() {
        EdgeOptions edgeOptions = new EdgeOptions();
        return addArguments(edgeOptions);
    }

    private static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        return addArguments(firefoxOptions);
    }

    @SuppressWarnings("PatternVariableCanBeUsed")
    private static <T extends AbstractDriverOptions<?>> T addArguments(T options) {
        // https://peter.sh/experiments/chromium-command-line-switches/
        if (options instanceof ChromiumOptions<?>) {
            ChromiumOptions<?> chromiumOptions = (ChromiumOptions<?>) options;
            if (Helpers.isPRD()) {
                // PRD tests will run in GitHub Runner environment, which is headless
                // Sandboxing has potential to cause issues in CI/CD envs
                chromiumOptions.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
            }
            chromiumOptions.addArguments("--guest");
            return options; // chromiumOptions and options point to the same object
        }

        // https://wiki.mozilla.org/Firefox/CommandLineOptions
        if (options instanceof FirefoxOptions) {
            FirefoxOptions firefoxOptions = (FirefoxOptions) options;
            if (Helpers.isPRD()) {
                firefoxOptions.addArguments("-headless");
            }
            firefoxOptions.addArguments("-private");
            return options;
        }

        throw new IllegalArgumentException("Options type not supported");
    }

    public static void startDriver() {
        WebDriver driver = getDriver();
        wait.set(new WebDriverWait(driver, Duration.ofSeconds(5)));
        driver.manage().window().maximize();
        try {
            driver.get(SparkTest.url);
        }
        catch (Exception e) {
            driver.quit();
            throw new RuntimeException(e);
        }
    }

    public static void quitDriver() {
        getDriver().quit();
    }

    public static String takeErrorScreenshot(ITestResult result) throws IOException {
        String FS = File.separator;
        String className = result.getMethod().getRealClass().getName(); // The real class where the test method was declared
        String methodName = result.getMethod().getMethodName();

        TakesScreenshot screenshotMode = (TakesScreenshot) getDriver();
        File tempFile = screenshotMode.getScreenshotAs(OutputType.FILE);
        File destFile = new File(SparkTest.USER_DIR + FS + "error-screenshots" + FS + className + "_" + methodName + ".png");
        FileUtils.copyFile(tempFile, destFile);

        String absolutePath = destFile.getAbsolutePath();
        return Helpers.getPathRelativeToUserDir(absolutePath);
    }

    public static <R> R doAction(By locator, Function<By, R> function, boolean isInteractiveAction) {
        try {
            if (isInteractiveAction) {
                waitForElementToBeInteractable(locator); // for performer-type actions that need element to be interactable
                function.apply(locator); // don't care about function's return value here
                pauseForEffect();
                return null;
            } else {
                waitForElementToBeVisible(locator); // for getter-type actions that don't need the element to be interactible
                return function.apply(locator);
            }
        } catch (StaleElementReferenceException retry) {
            if (isInteractiveAction) {
                waitForElementToBeInteractable(locator);
                function.apply(locator);
                pauseForEffect();
                return null;
            } else {
                waitForElementToBeVisible(locator);
                return function.apply(locator);
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("Action failed on locator %s with error: %s", locator, e));
        }
    }

    /** Mainly for interactive actions (performers) like click(). Not meant to be used by getters
     * @param locator1  locator to find first element
     * @param function  function that acts on element found via locator1
     * @param locator2  locator to find second element
     * @param condition predicate that tests element found via locator2
     */
    public static <R> void repeatUntil(By locator1, Function<By, R> function, By locator2, Predicate<By> condition) {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            doAction(locator1, function, true);
            if (condition.test(locator2)) {
                return; // success
            } else {
                retries++;
            }
        }
        throw new RuntimeException(String.format("Action failed after %d retries", retries));
    }

    /** === Performing === */
    // member that is a lambda expresssion
    public static Function<By, Void> Click = loc -> {
        getDriver().findElement(loc).click();
        return null;
    };

    public static void click(By locator) {
        doAction(locator, Click, true);
    }

    /** Click element found by locator1 until element found by locator2 is visible */
    public static void clickUntilElementIsVisible(By locator1, By locator2) {
        repeatUntil(locator1, Click, locator2, Is.Displayed);
    }

    public static void clickUntilElementIsInteractable(By locator1, By locator2) {
        repeatUntil(locator1, Click, locator2, Is.Enabled);
    }

    public static void typeText(By locator, String text) {
        doAction(locator, loc -> {getDriver().findElement(loc).sendKeys(text); return null;}, true);
    }

    public static void clear(By locator) {
        doAction(locator, loc -> {getDriver().findElement(loc).clear();return null;}, true);
    }

    /** === Getting === */
    public static WebElement getElement(By locator) {
        return doAction(locator, loc -> getDriver().findElement(loc), false);
    }

    public static String getText(By locator) {
        return doAction(locator, loc -> getDriver().findElement(loc).getText(), false);
    }

    public static int getCount(By locator) {
        try {
            return getDriver().findElements(locator).size();
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    public static String getURL() {
        return getDriver().getCurrentUrl();
    }

    /** === Waiting === */
    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void pauseForEffect() {
        sleep(WAIT_MS);
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
}
