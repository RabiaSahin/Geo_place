package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Driver {

    private static WebDriver driver;

    private Driver() {
    }

    public synchronized static WebDriver getDriver(String browser) {
        // String browser ==>  it originally comes from xml file to test base class, from test base it comes here. (for TestNG)
        if (driver == null) {
            // first we check if the value from xml file is null or not
            // if the value from xml file NOT null we use
            // the value from xml file IS null, we get the browser from properties file
            browser = Objects.requireNonNullElse(browser, ConfigurationReader.getProperty("browser"));

            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions options = new ChromeOptions();
                    TestConfiguration.configureChromeOptions(options);
                    driver = new ChromeDriver(options);

                    break;
                case "chromeHeadless":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions headless = new ChromeOptions();
                    headless.addArguments("--headless");
                    driver = new ChromeDriver(headless);
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                case "firefoxHeadless":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions headlessOptions = new FirefoxOptions();
                    headlessOptions.addArguments("--headless");
                    driver = new FirefoxDriver(headlessOptions);
                    break;
                case "ie":
                    if (System.getProperty("os.name").toLowerCase().contains("mac"))
                        throw new WebDriverException("You are operating Mac OS which doesn't support Internet Explorer");
                    WebDriverManager.iedriver().setup();
                    driver = new InternetExplorerDriver();
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions opt = new EdgeOptions();
                    opt.addArguments("--start-maximized");
                    driver = new EdgeDriver(opt);
                    break;
                case "safari":
                    if (System.getProperty("os.name").toLowerCase().contains("windows"))
                        throw new WebDriverException("You are operating Windows OS which doesn't support Safari");
                    WebDriverManager.getInstance(SafariDriver.class).setup();
                    driver = new SafariDriver();
                    break;

                default:
                    throw new RuntimeException("Illegal browser type!");
            }
        }
        return driver;
    }

    public static WebDriver getDriver() {
        return getDriver(null);
    }

    /**
     * Gets the JavascriptExecutor instance for the current WebDriver.
     *
     * @return The JavascriptExecutor instance.
     */
    public static JavascriptExecutor getJsExecutor() {
        return (JavascriptExecutor) getDriver();
    }

    public static void closeDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public static WebDriverWait getWaiter() {
        return getWaiter(Long.parseLong(ConfigurationReader.getProperty("SHORT_WAIT")));
    }

    public static WebDriverWait getWaiter(long timeout) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
    }

    public static Actions getAction() {
        return new Actions(Driver.getDriver());
    }

    public static ZonedDateTime time() {
        return Instant.now().atZone(ZoneId.systemDefault());
    }

    public static TakesScreenshot ssTaker() {
        return (TakesScreenshot) getDriver();
    }

    public static boolean isActive() {
        return driver != null;
    }

    public static String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }
}