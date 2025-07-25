package utilities;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.HashMap;
import java.util.Map;

public class TestConfiguration {
    private static final String headlessArgument = "headless";
    private static final String ScreenWidthArgument = "width";
    private static final String ScreenHeightArgument = "height";

    public static void configureChromeOptions(ChromeOptions options ) {
        final String headlessOption = System.getProperty(headlessArgument);
        final String screenWidth = System.getProperty(ScreenWidthArgument);
        final String screenHeight = System.getProperty(ScreenHeightArgument);
        final Map<String,Object> prefs = new HashMap<>();

        if ("true".equals(headlessOption))
            options.addArguments("--headless");

        prefs.put("download.directory_upgrade", true);
        prefs.put("safebrowsing.enabled", "false"); // Bypass warning message, keep file anyway (for .exe, .jar, etc.)
        prefs.put("profile.default_content_settings_.popups",0);
        prefs.put("download.prompt_for_download", "false");
        prefs.put("profile.default_content_settings.popups", 0);
        options.addArguments("--safebrowsing-disable-extension-blacklist");
        options.addArguments("--safebrowsing-disable-download-protection");
        options.addArguments("--start-maximized");
        options.addArguments("enable-automation"); // This option enables automation mode, for bot-safeguard purposes
        options.addArguments("--no-sandbox"); // This option disables the isolation of the browser processes for security purposes.
                options.addArguments("--disable-crash-reporter");
                options.addArguments("--disable-client-side-phishing-detection");
                options.addArguments("--disable-extensions"); // This option disables installed extensions in the browser.
                options.addArguments("--allow-running-insecure-content"); // Allow insecure content
                options.addArguments("--dns-prefetch-disable"); // This option disables DNS prefetching.
                options.addArguments("--disable-gpu"); //  This option disables GPU acceleration in the browser. Some tests may be incompatible with GPU acceleration.
                options.setExperimentalOption("prefs", prefs);
                options.setBrowserVersion("123.0.6312.122");
            }

            public static boolean isInt(String intParameter) {
                if (intParameter == null) {
                    return false;
                }
                try {
                    Integer.parseInt(intParameter);
                    return true;
                } catch (Throwable t) {
                    return false;
        }
      }

        }
