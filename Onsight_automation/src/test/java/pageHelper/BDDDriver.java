package pageHelper;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.aventstack.extentreports.service.ExtentService;
import com.epam.reportportal.listeners.LogLevel;
import com.epam.reportportal.service.ReportPortal;
import core.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.windows.WindowsDriver;
import io.cucumber.core.backend.TestCaseState;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.plugin.event.Result;
import io.github.sridharbandi.Accessibility;
import io.github.sridharbandi.AccessibilityRunner;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.zaproxy.clientapi.core.ClientApi;
import utils.*;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class BDDDriver {

    public static final ThreadLocal<HashMap<String, String>> commonVariables = new InheritableThreadLocal<>();
    public static final ThreadLocal<org.openqa.selenium.WebDriver> WEB_DRIVER_THREAD_LOCAL = new InheritableThreadLocal<>();
    public static final ThreadLocal<RequestSpecification> API_DRIVER_THREAD_LOCAL = new InheritableThreadLocal<>();
    public static final ThreadLocal<Response> API_RESPONSE_THREAD_LOCAL = new InheritableThreadLocal<>();
    public static final ThreadLocal<WindowsDriver> DESKTOP_DRIVER_THREAD_LOCAL = new InheritableThreadLocal<>();
    public static final ThreadLocal<AppiumDriver> MOBILE_DRIVER_THREAD_LOCAL = new InheritableThreadLocal<>();
    public static final ThreadLocal<ClientApi> ZAP_SCANNER_THREAD_LOCAL = new InheritableThreadLocal<>();
    private static final ThreadLocal<IOSPopupWatcher> watcher = new ThreadLocal<>();
    private static final ThreadLocal<Thread> watcherThread = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> install_app = new ThreadLocal<Boolean>();
    public static int itr;
    public static boolean Accessibilityflag;
    public static AccessibilityRunner accessibilityRunner;
    public static ThreadLocal<String> random = new InheritableThreadLocal<>();
    public static ThreadLocal<String> featureName = new InheritableThreadLocal<>();
    public static ThreadLocal<Long> startTime = new InheritableThreadLocal<>();
    PropertyReader pr = new PropertyReader();
    String StepName;
    String classname;
    String MethodName;
    String ExceptionName;
    Date date1;
    Date date2;
    int count = 0;
    int linestart = 0;
    int exampleLine = 0;
    int lineend = 0;

    // Get the WebDriver instance
    public static org.openqa.selenium.WebDriver getWebDriver() {
        return WEB_DRIVER_THREAD_LOCAL.get();
    }

    // Get the WindowsDriver instance
    public static WindowsDriver getDesktopDriver() {
        return DESKTOP_DRIVER_THREAD_LOCAL.get();
    }

    // Get the AppiumDriver instance
    public static AppiumDriver getMobileDriver() {
        return MOBILE_DRIVER_THREAD_LOCAL.get();
    }

    /**
     * Runs an accessibility test on the current page.
     *
     * @throws Throwable if an error occurs during the test
     */
    @Given("^Scan the Page against Accessibility Complainces$")
    public static void Run_AccessibilityTest() throws Throwable {
        Accessibilityflag = true;
        accessibilityRunner.execute();
    }

    /**
     * Calculates the difference between two dates in a specified time unit.
     *
     * @param date1 the start date
     * @param date2 the end date
     * @return the difference between the two dates in the specified time unit
     */
    public static long getDateDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return diffInMillies / 1000;
    }

    /**
     * Logs the payload for the report.
     */
    public static void logPayload() {
        ExtentCucumberAdapter.addTestStepLog("<textarea readonly>" + BaseDriverHelper.ReportPayload + "</textarea>");
    }

    /**
     * Logs a message to the test report.
     *
     * @param message the message to log
     */
    public static void log(String message) {
//        System.out.println("Added Variables: "+message);
        ExtentCucumberAdapter.addTestStepLog(message);
    }

    public static void Reportlog(String message, File file) {
        ReportPortal.emitLog(message, LogLevel.INFO.name(), new Date(), file);

    }

    public static void WriteExcelData(String TestCaseID, String ExecutionTime, String Status, String Error) throws IOException {
        String TestCase;
        int Rownum = 0;
        boolean flag = false;
        Row row;

        FileInputStream file = new FileInputStream(new File("src\\test\\resources\\dataSource\\Execution_Sheet.xlsx"));
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            try {
                TestCase = sheet.getRow(i).getCell(0).getStringCellValue().trim();
                if (TestCase.equalsIgnoreCase(TestCaseID)) {
                    flag = true;
                    Rownum = i;
                }
            } catch (Exception e) {
                flag = false;
            }
        }

        if (flag) {
            row = sheet.createRow(Rownum);
        } else {
            row = sheet.createRow(sheet.getLastRowNum() + 1);
        }
        // Create cells and write data
        Cell cell1 = row.createCell(0);
        cell1.setCellValue(TestCaseID);

        Cell cell2 = row.createCell(1);
        cell2.setCellValue(ExecutionTime);

        Cell cell3 = row.createCell(2);
        cell3.setCellValue(Status);

        Cell cell4 = row.createCell(3);
        cell4.setCellValue(Error);

        try (FileOutputStream fileOut = new FileOutputStream("src\\test\\resources\\dataSource\\Execution_Sheet.xlsx")) {
            workbook.write(fileOut);
            System.out.println("Excel file has been updated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the workbook
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static String getRandomValue() {
        if (random.get() != null)
            return String.valueOf(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("platform").toCharArray()[0]).toLowerCase() + random.get();
        else
            return null;
    }

    public static void setRandomValue(String value) {
        if (value.equalsIgnoreCase("-1"))
            random.remove();
        else
            random.set(value);
    }

    private static void reActivateApp(String appPackage) {
        if (MOBILE_DRIVER_THREAD_LOCAL.get().toString().contains("Android")) {
            ((AndroidDriver) MOBILE_DRIVER_THREAD_LOCAL.get()).terminateApp(appPackage);
            ((AndroidDriver) MOBILE_DRIVER_THREAD_LOCAL.get()).activateApp(appPackage);
        } else {
            ((IOSDriver) MOBILE_DRIVER_THREAD_LOCAL.get()).terminateApp(appPackage);
            ((IOSDriver) MOBILE_DRIVER_THREAD_LOCAL.get()).activateApp(appPackage);
        }
    }

    // Method to set up test start time and configuration
    @Before
    public void startTime(Scenario s) throws IOException {
        String ScenarioName = s.getName();
        // Check if the current scenario is different from the stored one
//		if(!propertyreader.readproperty("currentScenario").equalsIgnoreCase(ScenarioName)) {
//			// Update the current scenario and reset the test number
//			propertyreader.updateproprty("currentScenario", ScenarioName);
//			propertyreader.updateproprty("CurrentTest", "1");
//		}
//		// Determine the total number of tests based on the feature file and scenario name
//		String path = s.getUri().toString().split("/")[s.getUri().toString().split("/").length - 1];
//		int Total_test = countExamples1(System.getProperty("user.dir") + "/src/test/resources/FeatureFile/" + path, ScenarioName);
//		Test = propertyreader.readproperty("CurrentTest") + "/" + Total_test;
//
//		// Update the current test number if the scenario matches
//		if(propertyreader.readproperty("currentScenario").equalsIgnoreCase(ScenarioName)) {
//			int number = Integer.parseInt(propertyreader.readproperty("CurrentTest")) + 1;
//			propertyreader.updateproprty("CurrentTest", String.valueOf(number));
//		}
    }

    // Method to set up API-related configurations
    @Before("@API")
    public void apiSetup(Scenario s) throws Exception {

        if (featureName.get() != null) {
            if (!featureName.get().equalsIgnoreCase(getFeatureName(s))) {
                if (WEB_DRIVER_THREAD_LOCAL.get() != null)
                    WEB_DRIVER_THREAD_LOCAL.remove();
                featureName.set(getFeatureName(s));
            }
        } else
            featureName.set(getFeatureName(s));

        String platform = "", appPackage = "";

        platform = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("platform");

        String env = new PropertyReader().readProperty("env");
        appPackage = new PropertyReader().readProperty("appPackage") + env;

        setCommonVariables("platform", platform);
        if (!platform.equalsIgnoreCase("Android"))
            appPackage = appPackage.replace("." + env, "-" + env);

        if (env.equalsIgnoreCase("prod")) {
            appPackage = appPackage.replace("." + env, "");
            appPackage = appPackage.replace("-" + env, "");
        }

        setCommonVariables("appPackage", appPackage);

        ApiDriver ApiDriver = new BaseDriver();
        API_DRIVER_THREAD_LOCAL.set(ApiDriver.apiInit(""));
        API_RESPONSE_THREAD_LOCAL.set(null);
    }

    @After("@API")
    public void APITearDown(Scenario s) throws IOException, NoSuchFieldException, IllegalAccessException {
        // Handle API test result and update test case ID
        random.remove();
        API_DRIVER_THREAD_LOCAL.remove();
        API_RESPONSE_THREAD_LOCAL.remove();

//        if (s.isFailed()) {
//            String ErrorMessage = getErrors(s);
//            String Defect = DataReader.ReadDefectID(classname, MethodName, ExceptionName);
////			PdfReader.onFailedTest(getTestDetails(s), ErrorMessage, Defect);
//            DataReader.WriteExcelData(classname, MethodName, ExceptionName);
//        } else {
////			PdfReader.onSuccessTest(getTestDetails(s));
//        }
    }

    // Method to set up web-related configurations
    @Before("@WEB")
    public void webSetup(Scenario s) throws Exception {

        if (featureName.get() != null) {
            if (!featureName.get().equalsIgnoreCase(getFeatureName(s))) {
                if (WEB_DRIVER_THREAD_LOCAL.get() != null)
                    WEB_DRIVER_THREAD_LOCAL.remove();
                featureName.set(getFeatureName(s));
            }
        } else
            featureName.set(getFeatureName(s));

        String platform = "", appPackage = "";

        platform = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("platform");

        String env = new PropertyReader().readProperty("env");
        appPackage = new PropertyReader().readProperty("appPackage") + env;

        setCommonVariables("platform", platform);
        if (!platform.equalsIgnoreCase("Android"))
            appPackage = appPackage.replace("." + env, "-" + env);

        if (env.equalsIgnoreCase("prod")) {
            appPackage = appPackage.replace("." + env, "");
            appPackage = appPackage.replace("-" + env, "");
        }

        setCommonVariables("appPackage", appPackage);

        String browser = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("Browser");
        if (browser == null)
            browser = pr.readProperty("Browser");
        System.out.println(browser);
        webDriver webDriver = new BaseDriver();

        boolean SecuryScan = Boolean.valueOf(pr.readProperty("SecurityScan" + browser));
        String app = pr.readProperty("portal_admin_url");


        WEB_DRIVER_THREAD_LOCAL.set(webDriver.webInit(browser, app, false, SecuryScan));
        try {
            // Initialize and configure ZAP scanner
            ZAP_SCANNER_THREAD_LOCAL.set(webDriver.returnZapScanner());
            System.out.println("Zap Scanner in Driver.java" + ZAP_SCANNER_THREAD_LOCAL.get());
            ZAP_SCANNER_THREAD_LOCAL.get().spider.scan(app, null, null, null, null);
        } catch (Exception e) {
            System.out.println("No ZapCanner is not setup");
        }

        Accessibility.REPORT_PATH = System.getProperty("user.dir") + "/Reports/Accessibility/Report";

        System.out.println(Accessibility.REPORT_PATH + Accessibility.STANDARD);

        accessibilityRunner = new AccessibilityRunner(WEB_DRIVER_THREAD_LOCAL.get());
        date1 = new Date();
        log("<b>Running Test on " + platform + " Platform. </b>");
        startTime.set(System.currentTimeMillis());
    }

    @After("@WEB")
    public void webTearDown(Scenario s) throws IOException, NoSuchFieldException, IllegalAccessException {
        if (startTime.get() != null) {
            long duration = System.currentTimeMillis() - startTime.get();
            long seconds = (duration / 1000) % 60;
            long minutes = (duration / (1000 * 60)) % 60;

            String formattedDuration = String.format("%02d:%02d", minutes, seconds);
            log("<b>Execution completed in: " + formattedDuration + " time. </b>");
            startTime.remove();
        }
        // Log the test case ID and handle the test result
        random.remove();
        date2 = new Date();
        String TestCaseID = s.getSourceTagNames().iterator().next();

        if (s.isFailed()) {
            // Capture screenshot and log failure details
            String ErrorMessage = getErrors(s);
            try {
                String Defect = DataReader.ReadDefectID(classname, MethodName, ExceptionName);
                String sourcePath = "data:image/png;base64," + ((TakesScreenshot) WEB_DRIVER_THREAD_LOCAL.get()).
                        getScreenshotAs(OutputType.BASE64);
                ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromBase64String(sourcePath).build());
                File scrFile = ((TakesScreenshot) WEB_DRIVER_THREAD_LOCAL.get()).getScreenshotAs(OutputType.FILE);
                Reportlog("Failed Scenario - " + s, scrFile);
//			PdfReader.onFailedTest(getTestDetails(s), ErrorMessage, Defect);
                DataReader.WriteExcelData(classname, MethodName, ExceptionName);
            } catch (Exception ignored) {
            }
        }
        // Handle ZAP security scanner report
        if (!(ZAP_SCANNER_THREAD_LOCAL.get() == null)) {
            String FILEPATH = "Reports/Security_Report.html";
            File file = new File(FILEPATH);
            try {
                OutputStream os = new FileOutputStream(file);
                os.write(ZAP_SCANNER_THREAD_LOCAL.get().core.htmlreport());
                System.out.println("Successfully byte inserted");
                os.close();
            } catch (Exception e) {
                System.out.println("Exception: " + e);
            }
//			ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, "<a href='Security_Report.html'>Please See the Detail of Security Scan by Clicking this link</a>");
            ZAP_SCANNER_THREAD_LOCAL.set(null);
        }

        // Handle accessibility report
        if (Accessibilityflag) {
            accessibilityRunner.generateHtmlReport();
            ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, "<a href='" + Accessibility.REPORT_PATH + "/report/html/index.html'>Please See the Detail of Accessibility Scan by Clicking this link</a>");
        }
        Accessibilityflag = false;
        System.out.println("\n\nTestCaseID is : " + s.getId());

        if (s.isFailed()) {
            String sourcePath = "data:image/png;base64," + ((TakesScreenshot) WEB_DRIVER_THREAD_LOCAL.get()).
                    getScreenshotAs(OutputType.BASE64);
            ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromBase64String(sourcePath).build());
        }

        WEB_DRIVER_THREAD_LOCAL.get().quit();
    }

    @Before("@DESKTOP")
    public void desktopSetup(Scenario s) throws Exception {
        // Log that desktop setup is starting
        System.out.println("Desktop Before method");

        // Retrieve the application name from test parameters
        String app = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("App");
        System.out.println(app);

        // Initialize desktop driver and set it for the current thread
        DesktopDriver desktopDriver = new BaseDriver();
        DESKTOP_DRIVER_THREAD_LOCAL.set(desktopDriver.desktopInit(app));
    }

    @After("@DESKTOP")
    public void DesktopTearDown(Scenario s) throws IOException {
        random.remove();
        // Handle desktop test results and report status
        String TestCaseID = s.getSourceTagNames().iterator().next();
        System.out.println("\n\nTestCaseID is" + s.getId());

        if (s.isFailed()) {
            String sourcePath = "data:image/png;base64," + ((TakesScreenshot) DESKTOP_DRIVER_THREAD_LOCAL.get()).
                    getScreenshotAs(OutputType.BASE64);
            ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromBase64String(sourcePath).build());
            Rally_Updator rlu = new Rally_Updator();
            rlu.Create("QABuild", "413450412168", "Fail");
        } else {
            Rally_Updator rlu = new Rally_Updator();
            rlu.Create("QABuild", "413450412168", "Pass");
        }
        DESKTOP_DRIVER_THREAD_LOCAL.get().quit();
    }

    @Before("@MOBILE")
    public void mobileSetup(Scenario s) throws Exception {
        if (featureName.get() != null) {
            if (!featureName.get().equalsIgnoreCase(getFeatureName(s))) {
                if (MOBILE_DRIVER_THREAD_LOCAL.get() != null) {
                    MOBILE_DRIVER_THREAD_LOCAL.remove();
                    commonVariables.get().remove(getCommonVariables("platform") + "_skipFlow");
                }
                featureName.set(getFeatureName(s));
//                sendReport();

            }
        } else
            featureName.set(getFeatureName(s));

        install_app.set(Boolean.parseBoolean(System.getProperty("appInstalled")));

        String device = "", version = "", automator = "", platform = "", appPackage = "", grantPermission = "", appium_url = "";

        device = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("device");
        version = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("version");
        automator = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("automator");
        platform = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("platform");
        appium_url = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("appium_url");
        grantPermission = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("grantPermission");

        String env = new PropertyReader().readProperty("env");
        appPackage = new PropertyReader().readProperty("appPackage") + env;

        setCommonVariables("platform", platform);
        if (!platform.equalsIgnoreCase("Android"))
            appPackage = appPackage.replace("." + env, "-" + env);

        if (env.equalsIgnoreCase("prod")) {
            appPackage = appPackage.replace("." + env, "");
            appPackage = appPackage.replace("-" + env, "");
        }

        setCommonVariables("appPackage", appPackage);

        if (MOBILE_DRIVER_THREAD_LOCAL.get() == null)
            AppiumSessionManager.startAppiumSession(appium_url);
        try {
            initializeMobileDriver(env, device, version, automator, platform, appPackage, grantPermission, appium_url);
        } catch (Exception e) {
            MOBILE_DRIVER_THREAD_LOCAL.remove();
            AppiumSessionManager.startAppiumSession(appium_url);
            initializeMobileDriver(env, device, version, automator, platform, appPackage, grantPermission, appium_url);
        }

        System.out.println("SESSION ID: " + MOBILE_DRIVER_THREAD_LOCAL.get().getSessionId().toString());
        log("<b>Running Test on " + getCommonVariables("platform") + " Platform. </b>");
        startTime.set(System.currentTimeMillis());

        startWatcher(MOBILE_DRIVER_THREAD_LOCAL.get());
    }

    public void initializeMobileDriver(String env, String device, String version, String automator, String platform, String appPackage, String grantPermission, String appium_url) throws Exception {
        MobileDriver mobileDriver = new BaseDriver();
        if (MOBILE_DRIVER_THREAD_LOCAL.get() == null)
            MOBILE_DRIVER_THREAD_LOCAL.set(mobileDriver.mobileInit(env, device, appPackage, platform, automator, version, grantPermission, appium_url, install_app.get()));

        if (MOBILE_DRIVER_THREAD_LOCAL.get() != null)
            reActivateApp(appPackage);
        else
            MOBILE_DRIVER_THREAD_LOCAL.set(mobileDriver.mobileInit(env, device, appPackage, platform, automator, version, grantPermission, appium_url, install_app.get()));


    }

    @After("@MOBILE")
    public void MobileTearDown(Scenario s) throws Exception {
        stopWatcher();

        if (startTime.get() != null) {
            long duration = System.currentTimeMillis() - startTime.get();
            long seconds = (duration / 1000) % 60;
            long minutes = (duration / (1000 * 60)) % 60;

            String formattedDuration = String.format("%02d:%02d", minutes, seconds);
            log("<b>Execution completed in: " + formattedDuration + " time. </b>");
            startTime.remove();
        }
        // Handle mobile test results and report status
        String TestCaseID = s.getSourceTagNames().iterator().next();
        System.out.println("\n\nTestCaseID is : " + s.getId());
        String env = new PropertyReader().readProperty("env");
        String appPackage = new PropertyReader().readProperty("appPackage") + env;

        if (MOBILE_DRIVER_THREAD_LOCAL.get() != null)
            if (!MOBILE_DRIVER_THREAD_LOCAL.get().toString().contains("Android"))
                appPackage = appPackage.replace("." + env, "-" + env);

        if (env.equalsIgnoreCase("prod")) {
            appPackage = appPackage.replace("." + env, "");
            appPackage = appPackage.replace("-" + env, "");
        }

        if (MOBILE_DRIVER_THREAD_LOCAL.get() != null) {
            if (s.isFailed()) {
                Thread.sleep(2000);
                String sourcePath = "data:image/png;base64," + ((TakesScreenshot) MOBILE_DRIVER_THREAD_LOCAL.get()).
                        getScreenshotAs(OutputType.BASE64);
                ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromBase64String(sourcePath).build());
                MOBILE_DRIVER_THREAD_LOCAL.get().quit();
                MOBILE_DRIVER_THREAD_LOCAL.remove();
                commonVariables.get().remove(getCommonVariables("platform") + "_skipFlow");
            } else {
                try {
                    if (MOBILE_DRIVER_THREAD_LOCAL.get() != null)
                        if (MOBILE_DRIVER_THREAD_LOCAL.get().toString().contains("Android"))
                            ((AndroidDriver) MOBILE_DRIVER_THREAD_LOCAL.get()).terminateApp(appPackage);
                        else
                            ((IOSDriver) MOBILE_DRIVER_THREAD_LOCAL.get()).terminateApp(appPackage);
                } catch (Exception e) {
                    MOBILE_DRIVER_THREAD_LOCAL.get().quit();
                    MOBILE_DRIVER_THREAD_LOCAL.remove();
                }
            }
        }
//        MOBILE_DRIVER_THREAD_LOCAL.remove();
    }

    public void startWatcher(AppiumDriver driver) {
        stopWatcher(); // Clean up previous thread if any

        IOSPopupWatcher popupWatcher = new IOSPopupWatcher(driver);
        Thread thread = new Thread(popupWatcher);
        thread.setDaemon(true);
        thread.start();

        watcher.set(popupWatcher);
        watcherThread.set(thread);
    }

    public void stopWatcher() {
        IOSPopupWatcher popupWatcher = watcher.get();
        if (popupWatcher != null) {
            popupWatcher.stop();
            watcher.remove();
        }

        Thread thread = watcherThread.get();
        if (thread != null) {
            try {
                thread.join(1000); // Optional: wait for it to stop
            } catch (InterruptedException ignored) {
            }
            watcherThread.remove();
        }
    }

    // Method to retrieve error messages from a failed scenario
    public String getErrors(Scenario scenario) throws NoSuchFieldException, IllegalAccessException {
        String message = null;

        // Access private fields of the scenario to get test results
        Field delegate = scenario.getClass().getDeclaredField("delegate");
        delegate.setAccessible(true);
        TestCaseState tcs = (TestCaseState) delegate.get(scenario);

        Field stepResults = tcs.getClass().getDeclaredField("stepResults");
        stepResults.setAccessible(true);

        ArrayList<Result> results = (ArrayList<Result>) stepResults.get(tcs);
        for (Result result : results) {
            if (result.getError() != null) {
                message = result.getError().getMessage();
                ExceptionName = result.getError().toString().split(":")[0];
                ExceptionName = ExceptionName.split("\\.")[ExceptionName.split("\\.").length - 1];
            }
        }
        return message;
    }

    @AfterStep
    public void stepName() {
        // Capture step details after each step
        StepName = StepDetails.stepName;
        String ClassTest = StepDetails.ClassName;
//		MethodName = ClassTest.split("\\.")[ClassTest.split("\\.").length - 1];
//		classname = ClassTest.split("\\.")[ClassTest.split("\\.").length - 2];
    }

    @After
    public void afterScenario() {
        ExtentService.getInstance().flush();
    }

    @BeforeSuite
    public void BeforeSuite() {
        // Initialize test setup
        itr = 0;
        // Configure logging
        DOMConfigurator.configure("log4j.xml");
        // Prepare PDF reader for the test
//		PdfReader.onStartTest();
    }

    @AfterSuite
    public void AfterSuite() {
        // Finalize PDF reader after the test
//		PdfReader.onFinishTest();
    }

    // Get the RequestSpecification instance for API requests
    public RequestSpecification getApiDriver() {
        return API_DRIVER_THREAD_LOCAL.get();
    }

    /**
     * Runs JMeter performance scripts and logs the output.
     *
     * @throws Throwable if an error occurs during the performance test
     */
    @Given("^Running the JMeter Performance Scripts$")
    public void performance() throws Throwable {
        FileUtils.deleteDirectory(new File(System.getProperty("user.dir") + "/Reports/Performance"));
        Path path = Paths.get(System.getProperty("user.dir") + "/Reports/Performance/");
        Files.createDirectory(path);

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", System.getProperty("user.dir") + "/src/main/java/core/Perfomance/bin/Jmeter.bat -n -t " + System.getProperty("user.dir") + "/src/test/resources/PerformanceScripts/saksoft.jmx -l " + System.getProperty("user.dir") + "/src/test/resources/PerformanceScripts/test.jtl & " + System.getProperty("user.dir") + "/src/main/java/core/Perfomance/bin/Jmeter.bat -g " + System.getProperty("user.dir") + "/src/test/resources/PerformanceScripts/test.jtl -o " + System.getProperty("user.dir") + "/Reports/Performance");

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs a link to the detailed performance report.
     *
     * @throws Throwable if an error occurs during logging
     */
    @Given("^Generate the Detailed Report$")
    public void performanceReportLogger() throws Throwable {
        ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, "<a href='Performance/index.html'>Please See the Detail of Performance by Clicking this link</a>");
    }

    public void setCommonVariables(String key, String value) {

        HashMap<String, String> args = new HashMap<>();
        if (commonVariables.get() != null)
            args.putAll(commonVariables.get());
        args.put(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("platform") + "_" + key, value);
        commonVariables.set(args);
    }

    /**
     * Gets the variables from the context.
     *
     * @return a HashMap of variables
     */
    public String getCommonVariables(String key) {
//        log("Added Variables: " + (HashMap<String, String>) commonVariables.get());
//        System.out.println("=============================================================================");
//        System.out.println(getCommonVariables());
//        System.out.println("=============================================================================");
        return commonVariables.get().get(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("platform") + "_" + key);
    }

    public Map<String, String> getCommonVariables() {
        return commonVariables.get();
    }

    /**
     * Gets the starting line number of the scenario.
     *
     * @param featureFilePath the path to the feature file
     * @param scenarioName    the name of the scenario
     * @return the starting line number of the scenario
     * @throws IOException if an error occurs while reading the feature file
     */
    public int getStartCount(String featureFilePath, String scenarioName) throws IOException {
        LineIterator it = IOUtils.lineIterator(new BufferedReader(new FileReader(featureFilePath)));
        for (int lineNumber = 1; it.hasNext(); lineNumber++) {
            String line = it.next();
            if (line.trim().startsWith("Scenario") && line.contains(scenarioName)) {
                linestart = lineNumber;
                break;
            }
        }
        return linestart;
    }

    /**
     * Gets the ending line number of the scenario.
     *
     * @param featureFilePath the path to the feature file
     * @param scenarioName    the name of the scenario
     * @return the ending line number of the scenario
     * @throws IOException if an error occurs while reading the feature file
     */
    public int getEndCount(String featureFilePath, String scenarioName) throws IOException {
        LineIterator it = IOUtils.lineIterator(new BufferedReader(new FileReader(featureFilePath)));
        for (int lineNumber = 1; it.hasNext(); lineNumber++) {
            String line = it.next();
            if (lineNumber > getStartCount(featureFilePath, scenarioName)) {
                if (line.trim().startsWith("Scenario")) {
                    lineend = lineNumber;
                    break;
                }
            }
        }
        return lineend;
    }

    /**
     * Gets the line number where examples start in a scenario outline.
     *
     * @param featureFilePath the path to the feature file
     * @param scenarioName    the name of the scenario
     * @return the line number where examples start
     * @throws IOException if an error occurs while reading the feature file
     */
    public int getExampleCount(String featureFilePath, String scenarioName) throws IOException {
        LineIterator it = IOUtils.lineIterator(new BufferedReader(new FileReader(featureFilePath)));
        for (int lineNumber = 1; it.hasNext(); lineNumber++) {
            String line = it.next();
            if (lineNumber > getStartCount(featureFilePath, scenarioName) && lineNumber < getEndCount(featureFilePath, scenarioName)) {
                if (line.trim().startsWith("Examples:")) {
                    exampleLine = lineNumber;
                    break;
                }
            }
        }
        return exampleLine;
    }

    public String getFeatureName(Scenario scenario) {
        String uri = scenario.getUri().toString();

        String featureFile = uri.substring(uri.lastIndexOf("/") + 1);
        String featureName = featureFile.replace(".feature", "");

        return featureName;
    }
}

class IOSPopupWatcher implements Runnable {

    private final AppiumDriver driver;
    private volatile boolean running = true;

    public IOSPopupWatcher(AppiumDriver driver) {
        this.driver = driver;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
//                if (driver.toString().toLowerCase().contains("ios")) {
                String popupLocator = "//*[@name='Not Now'] | " +
                        "//*[contains(@name,'An SSL error has occurred')]/../../..//XCUIElementTypeButton[@name='OK'] | //*[@text='Close app'] | //XCUIElementTypeStaticText[@name='Turn On All']";

                List<WebElement> buttons = driver.findElements(By.xpath(popupLocator));
                if (!buttons.isEmpty()) {
                    buttons.get(0).click();
                    System.out.println("[Watcher] Handled popup.");

                    Thread.sleep(2000); // Avoid double-click

                    String followUpLocator = "//XCUIElementTypeTextField[@value='Height' or @name='Height']/../../..//XCUIElementTypeButton[@name='Sign Up'] | " +
                            "//XCUIElementTypeStaticText[@name='Please login to your account.']/..//XCUIElementTypeButton[@name='Login'] | " +
                            "//XCUIElementTypeButton[@name='Submit'] | //XCUIElementTypeButton[@name='UIA.Health.AuthSheet.DoneButton' and @label='Allow']";

                    buttons = driver.findElements(By.xpath(followUpLocator));
                    if (!buttons.isEmpty()) {
                        buttons.get(0).click();
                        System.out.println("[Watcher] Clicked follow-up button.");
                    }
                }
//                }
            } catch (Exception e) {
                // Log once or debug
                System.err.println("[Watcher] Exception: " + e.getMessage());
            }

            try {
                Thread.sleep(2000); // 2 seconds between checks
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Good practice
            }
        }
    }
}
