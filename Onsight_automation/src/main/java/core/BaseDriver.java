package core;

import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;

// Appium Java Client imports
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.options.WindowsOptions;

// Logging imports
import lombok.extern.log4j.Log4j2;

import org.apache.http.params.CoreConnectionPNames;
import org.zaproxy.clientapi.core.ClientApi;

import static org.openqa.selenium.chrome.ChromeOptions.LOGGING_PREFS;

@Log4j2
public class BaseDriver implements ApiDriver, webDriver, DesktopDriver, MobileDriver {

    // Static variable to store a process
    public static Process p;
    // ThreadLocal variable for ZAP Scanner instance
    public ThreadLocal<ClientApi> ZapScanner = new ThreadLocal<ClientApi>();

    // Method to initialize WebDriver for web automation
    public WebDriver webInit(String browser, String baseURL, Boolean Grid, Boolean proxyRequired) throws Exception {
        org.openqa.selenium.WebDriver dr = null;

        // Configure log4j2 configuration file path
        System.setProperty("log4j.configurationFile", System.getProperty("user.dir") + "/log4j2.xml");

        System.out.println("In Web Initiator");

        // Check if running on Grid or locally
        if (!Grid) {
            if (browser.equalsIgnoreCase("chrome")) {
                // Initialize ChromeOptions and DesiredCapabilities
                DesiredCapabilities capabilities = new DesiredCapabilities();
                ChromeOptions options = new ChromeOptions();

                // Handle proxy configuration if required
                if (proxyRequired) {
                    Proxy proxy = new Proxy();
                    proxy.setAutodetect(false);
                    proxy.setHttpProxy("localhost:8081");
                    proxy.setSslProxy("localhost:8081");

                    // ZAP proxy configuration
                    final String ZAP_PROXYHOST = "localhost";
                    final int ZAP_PROXYPORT = 8081;
                    final String[] policyNames = {"directory-browsing", "cross-site-scripting", "sql-injection",
                            "path-traversal", "remote-file-inclusion", "server-side-include", "script-active-scan-rules",
                            "server-side-code-injection", "external-redirect", "crlf-injection"};

                    // Initialize ZAP Scanner if not already set
                    if (ZapScanner.get() == null) {
                        ZapScanner.set(new ClientApi(ZAP_PROXYHOST, ZAP_PROXYPORT, "7ast5q3osjonr6f0nrk070l9fh"));
                    }
                    System.out.println("Zap Scanner" + ZapScanner);

                    // Set proxy capabilities for ChromeOptions
                    options.setCapability(CapabilityType.PROXY, proxy);
                    options.addArguments("--ignore-certificate-errors");
                }

                // Configure Chrome options
                Map<String, Object> prefs = new HashMap<String, Object>();
                prefs.put("profile.default_content_setting_values.notifications", 2);
                prefs.put("profile.default_content_setting_values.popups", 1);
                prefs.put("download.default_directory", System.getProperty("user.dir") + "\\src\\Data\\Downloads");

                options.setExperimentalOption("prefs", prefs);
                options.addArguments("--start-maximized");
                options.addArguments("disable-infobars");
                options.addArguments("--disable-popup-blocking");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-extensions");
                options.addArguments("--disable-gpu");
                options.addArguments("--no-sandbox");

                // Set additional capabilities for Chrome WebDriver
                options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                capabilities.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "none");

                // Enable logging for Chrome WebDriver
                LoggingPreferences logs = new LoggingPreferences();
                logs.enable(LogType.DRIVER, Level.ALL);
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                capabilities.setCapability(LOGGING_PREFS, logs);
                options.merge(capabilities);
                options.addArguments("--remote-allow-origins=*");

                try {
                    dr = new ChromeDriver();
                } catch (WebDriverException e) {
                    // Handle WebDriverException
                    System.out.println(e.getAdditionalInformation());
                    System.out.println(e.getMessage());
                    System.out.println(e.getRawMessage());
                }

                // Configure WebDriver timeouts and window settings
                dr.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                dr.manage().deleteAllCookies();
                dr.manage().window().maximize();
                //log.info("Chrome Browser Launched");

            } else if (browser.equalsIgnoreCase("edge")) {
                // Initialize EdgeOptions for Edge browser
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");

                // Initialize EdgeDriver
                dr = new EdgeDriver();
                dr.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                dr.manage().deleteAllCookies();
                dr.manage().window().maximize();
                //log.info("Edge Browser Launched");

            } else {
                // Handle other browsers (e.g., IE)
                System.out.println("For IE Browser");
            }
        }
        dr.navigate().to(baseURL);
        return dr;
    }

    // Method to initialize headless WebDriver for web automation
    public WebDriver webInit(String browser, String BaseURL, Boolean Grid, Boolean proxyRequired, Boolean headless)
            throws Exception {
        org.openqa.selenium.WebDriver dr = null;

        // Configure log4j2 configuration file path
        System.setProperty("log4j.configurationFile", System.getProperty("user.dir") + "/log4j2.xml");

        // Check if running on Grid or locally
        if (!Grid) {
            if (browser.equalsIgnoreCase("chrome")) {
                // Configure ChromeOptions for headless mode
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments(Arrays.asList("--log-level=OFF", "--silent", "--headless=true"));

                // Initialize ChromeDriver in headless mode
                dr = new ChromeDriver(chromeOptions);
                dr.manage().window().fullscreen();
                //log.info("Chrome Browser Launched in Headless Mode");
            } else {
                // Handle other browsers (e.g., IE) for headless mode
                System.out.println("For IE Browser (Headless Mode)");
            }
        }

        return dr;
    }

    // Method to initialize WindowsDriver for desktop automation
    public WindowsDriver desktopInit(String Application) throws Exception {
        // Configure log4j2 configuration file path
        System.setProperty("log4j.configurationFile", System.getProperty("user.dir") + "/log4j2.xml");

        // Initialize WindowsDriver session for desktop application
        WindowsDriver desktopSession = null;
        WindowsOptions capabilities = new WindowsOptions();
        capabilities.setApp(Application);
        capabilities.setCapability("app", Application);
        capabilities.setPlatformName("Windows");
        capabilities.setCapability("deviceName", "WindowsPC");
        capabilities.setWaitForAppLaunch(Duration.ofSeconds(20));
        capabilities.setExperimentalWebDriver(false);

        // Establish connection to WindowsDriver
        desktopSession = new WindowsDriver(new URL("http://127.0.0.1:4783"), capabilities);
        Thread.sleep(10000);
        desktopSession.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        //log.info("Desktop App Launched");

        return desktopSession;
    }

    // Method to initialize AppiumDriver for mobile automation
    public AppiumDriver mobileInit(String env, String device, String appPackage, String platform,
                                   String automationName, String version, String grantPermission, String appium_url, Boolean installApp) throws Exception {

        System.setProperty("log4j.configurationFile", System.getProperty("user.dir") + "/log4j2.xml");

        // Initialize AppiumDriver session for mobile application
        AppiumDriver mobileDriver = null;
        try {
            UiAutomator2Options uiAutomator2Options = new UiAutomator2Options();
            XCUITestOptions xcuiTestOptions = new XCUITestOptions();
            if (platform.equalsIgnoreCase("iOS")) {
                xcuiTestOptions.setPlatformName(platform);
                xcuiTestOptions.setDeviceName(device);
                xcuiTestOptions.setNewCommandTimeout(Duration.ofMinutes(3));
                xcuiTestOptions.setAutomationName(automationName);
                xcuiTestOptions.setPlatformVersion(version);
                xcuiTestOptions.setNoReset(false);
                xcuiTestOptions.setFullReset(false);
                xcuiTestOptions.setCapability("appium:autoLaunch", false);
                xcuiTestOptions.setCapability("adbExecTimeout", 60000);
                mobileDriver = new IOSDriver(new URL(appium_url), xcuiTestOptions);
            } else {
                uiAutomator2Options.setPlatformName(platform);
                uiAutomator2Options.setDeviceName(device);
                uiAutomator2Options.setNewCommandTimeout(Duration.ofMinutes(3));
                uiAutomator2Options.setAutomationName(automationName);
                uiAutomator2Options.setPlatformVersion(version);
                uiAutomator2Options.setNoReset(false);
                uiAutomator2Options.setFullReset(false);
                uiAutomator2Options.setUiautomator2ServerInstallTimeout(Duration.ofMinutes(2));
                uiAutomator2Options.setAutoGrantPermissions(Boolean.parseBoolean(grantPermission));
                uiAutomator2Options.setCapability("appium:autoLaunch", false);
                uiAutomator2Options.setCapability("adbExecTimeout", 60000);
                uiAutomator2Options.setCapability("uiautomator2ServerLaunchTimeout", 60000);
                uiAutomator2Options.setCapability("uiautomator2ServerInstallTimeout", 60000);
                uiAutomator2Options.setCapability("ignoreHiddenApiPolicyError", true);
                mobileDriver = new AndroidDriver(new URL(appium_url), uiAutomator2Options);
            }

            if (platform.equalsIgnoreCase("iOS")) {
                xcuiTestOptions.setCapability("appium:bundleId", appPackage);
                xcuiTestOptions.setCapability("adbExecTimeout", 60000);
                String permissionsJson = String.format(
                        "{\"%s\": {\"location\": \"YES\", \"microphone\": \"YES\", \"motion\": \"YES\", \"notifications\":\"YES\"}}",
                        appPackage
                );

                xcuiTestOptions.setCapability("permissions", permissionsJson);
                xcuiTestOptions.setCapability("permissions", permissionsJson);

                mobileDriver = new IOSDriver(new URL(appium_url), xcuiTestOptions);
            } else {
                uiAutomator2Options.setAppPackage(appPackage);
                mobileDriver = new AndroidDriver(new URL(appium_url), uiAutomator2Options);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR WHILE INITIALIZING THE APP: " + e.getMessage() + "\n" + e.getLocalizedMessage() + "\n" + e.getCause());
        }

        return mobileDriver;
    }

    // Method to initialize RequestSpecification for API testing using RestAssured
    public RequestSpecification apiInit(String baseurl) {
        // Configure log4j2 configuration file path
        System.setProperty("log4j.configurationFile", System.getProperty("user.dir") + "/log4j2.xml");

        // Initialize RestAssured for API testing with provided base URL
        RequestSpecification httpRequest;
        RestAssured.baseURI = baseurl;
        @SuppressWarnings("deprecation")
        RestAssuredConfig config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 100000)
                        .setParam(CoreConnectionPNames.SO_TIMEOUT, 100000));

        // Create HTTP request instance
        httpRequest = RestAssured.given().config(config);
        //log.info("API Instance initiated");

        return httpRequest;
    }

    // Method to return the ThreadLocal ZapScanner instance
    public ClientApi returnZapScanner() {
        System.out.println("Zap Scanner Return method" + ZapScanner.get());
        return ZapScanner.get();
    }

    // Method to return the static process instance
    public Process ReturncurrentProcess() {
        System.out.println("Zap Scanner Return method" + ZapScanner);
        return p;
    }

    public void killProcessOnPort(int port) {
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            try {
                String[] cmd = {"/bin/bash", "-c", "lsof -ti:" + port + " | xargs kill -9"};
                Process proc = Runtime.getRuntime().exec(cmd);
                proc.waitFor();
                System.out.println("Cleared port: " + port);
            } catch (Exception e) {
//                e.printStackTrace();
            }
        } else {
            try {
                String findPid = "for /f \"tokens=5\" %a in ('netstat -aon ^| findstr :" + port + "') do taskkill /PID %a /F";
                Process proc = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", findPid});
                proc.waitFor();
                System.out.println("Port " + port + " cleared.");
            } catch (Exception e) {
            }
        }
    }
}
