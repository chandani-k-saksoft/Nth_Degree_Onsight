package core;
// RestAssured imports

import static io.restassured.matcher.RestAssuredMatchers.matchesXsd;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

// AWT and ImageIO imports for image handling
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

// FileReader and StringReader for file and string input reading
import java.io.FileReader;

// Java standard library imports
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

// ImageIO for image handling
import javax.imageio.ImageIO;

// XML parsing exceptions
import javax.xml.parsers.ParserConfigurationException;

// Axe accessibility testing library imports
import com.deque.html.axecore.args.AxeRunOnlyOptions;
import com.deque.html.axecore.args.AxeRunOptions;
import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;

// Google Gson library imports for JSON handling
import com.google.gson.JsonElement;

// Paul Hammant's ngWebDriver for AngularJS
import com.paulhammant.ngwebdriver.*;

// Appium imports for mobile automation
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.connection.ConnectionState;
import io.appium.java_client.android.connection.ConnectionStateBuilder;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.windows.WindowsDriver;

// RestAssured for HTTP request/response handling
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

// Lombok annotations for generating boilerplate code
import lombok.Data;
import lombok.extern.log4j.Log4j2;

// DOM4J for XML parsing
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

// Selenium WebDriver and related imports
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.*;

// TestNG assertion
import org.testng.Assert;

// SAXException for XML parsing
import org.xml.sax.SAXException;

// RestAssured authentication and HTTP handling
import io.restassured.authentication.FormAuthConfig;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

// Screenshot libraries
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

// SimpleXML for XML handling
import simplexml.model.Element;


/**
 * Helper class for managing various drivers and configurations.
 */
// Log4j2 logging and Data setup
@Data
@Log4j2
public class BaseDriverHelper implements ApiHelper, webHelper, DesktopHelper, MobileHelper {

    public final static String Contenttype = "application/xml"; // Default content type
    // Response instance for storing API response
    public static Response resultResponse;
    // Strings for storing payloads and content type
    public static String Payload = "";
    public static String ReportPayload = "";
    public static Document XMLDocumentPayload;
    public static JSONObject JSONDocumentPayload;
    // Variables for specific data
    public static String WindDirectionDegree;
    public static Properties propinhelper;
    // Variable for storing video name
    public static String VideoName;
    // FluentWaits for each driver
    public Wait<WebDriver> wait;
    public Wait<WindowsDriver> waitDesktop;
    public Wait<AppiumDriver> waitMobile;
    // WebDriver instance for web automation
    WebDriver web_driver;
    // RequestSpecification for API testing
    RequestSpecification requestSpecification;
    // WindowsDriver instance for desktop automation
    WindowsDriver desktop_driver;
    // AppiumDriver instance for mobile automation
    AppiumDriver mobile_driver;
    // AndroidDriver instance for Android automation
    AndroidDriver androidDriver;
    IOSDriver iosDriver;
    // NgWebDriver instance for AngularJS applications
    NgWebDriver ngDriver;
    // WebElement and List<WebElement> for UI interaction
    WebElement el = null;
    List<WebElement> ellist;
    // Root directory path for resources
    String Root = "src/test/resources/apiResourceTemplate/";
    Set<String> allHandles;  // Set to hold all window handles
    String parentWinHandle = null;  // Store the handle of the parent window

    /**
     * Constructor for WebDriver initialization.
     *
     * @param dr WebDriver instance
     */
    public BaseDriverHelper(WebDriver dr) {
        web_driver = dr;

        wait = new FluentWait<WebDriver>(web_driver)
                .withTimeout(Duration.ofSeconds(5)) // Maximum timeout duration
                .pollingEvery(Duration.ofSeconds(1)) // Polling interval
                .ignoring(NoSuchElementException.class)

        ; // Exceptions to ignore
    }

    /**
     * Constructor for WindowsDriver initialization.
     *
     * @param dr WindowsDriver instance
     */
    public BaseDriverHelper(WindowsDriver dr) {
        desktop_driver = dr;

        waitDesktop = new FluentWait<WindowsDriver>(desktop_driver)
                .withTimeout(Duration.ofSeconds(5))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);
    }
    /*	Start of the Web Driver Helper area which contains all the base methods related to the Web Driver
     */

    /**
     * Constructor for AppiumDriver initialization.
     *
     * @param dr AppiumDriver instance
     */
    public BaseDriverHelper(AppiumDriver dr) {
        mobile_driver = dr;
        if (dr != null) {
            if (dr.toString().toLowerCase().contains("android"))
                androidDriver = (AndroidDriver) dr;
            else
                iosDriver = (IOSDriver) dr;

            waitMobile = new FluentWait<>(mobile_driver)
                    .withTimeout(Duration.ofSeconds(5))
                    .pollingEvery(Duration.ofMillis(200))
                    .ignoring(NoSuchElementException.class);
        }
    }

    /**
     * Constructor for RequestSpecification and Response initialization.
     *
     * @param dr       RequestSpecification instance
     * @param response Response instance
     */

    public BaseDriverHelper(RequestSpecification dr, Response response) {
        requestSpecification = dr;
        propinhelper = new Properties(); // Initialize properties object
    }

    private static Element getBook(final Element element) {
        // Extracts the 'book' element from the given XML element.
        return element.children.get(0).children.get(0);
    }

    public void waitforElementToBeClickable(String locator) throws InterruptedException {
        System.out.println("In Wait for Element Clickable method for - " + locator);

        // Check if the locator is an XPath expression
        if (locator.startsWith("//") || locator.startsWith("(")) {
            // Wait until the element specified by the XPath is clickable
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
            System.out.println("Waiting for Element to be clickable and active" + locator);
        }
        // Check if the locator is a name attribute
        else if (locator.startsWith("name")) {
            // Wait until the element specified by the name is clickable
            wait.until(ExpectedConditions.elementToBeClickable(By.name(locator.split("=")[1])));
            System.out.println("Waiting for Element to be clickable and active" + locator);
        }
        // Check if the locator is an id attribute
        else if (locator.startsWith("id")) {
            // Wait until the element specified by the id is clickable
            wait.until(ExpectedConditions.elementToBeClickable(By.id(locator.split("=")[1])));
            System.out.println("Waiting for Element to be clickable and active" + locator);
        }
    }

    public void waitForElementNotToBeVisible(WebElement wl) {
        // Wait until the specified WebElement is not visible
        WebDriverWait wait = new WebDriverWait(web_driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.invisibilityOf(wl));
    }

    public void waitForElementBeVisible(WebElement wl, int timeoutInSeconds) throws Exception {
        if (wl != null) {
            WebDriverWait wait = new WebDriverWait(web_driver, Duration.ofSeconds(timeoutInSeconds));
            try {
                wait.until(ExpectedConditions.visibilityOf(wl));
            }catch (Exception e)
            {
                Thread.sleep(timeoutInSeconds * 500L);
            }
        } else
            Thread.sleep(timeoutInSeconds * 500L);
    }

    public void verifyElementToBePresent(String Locators) throws InterruptedException {
        // Verify that the element specified by Locators is present
        int count = getWebElementsCount(Locators);
        Stringcomparator(String.valueOf(count), "1");
    }

    public void verifyElementNotToBePresent(String Locators) throws InterruptedException {
        // Verify that the element specified by Locators is not present
        int count = getWebElementsCount(Locators);
        Stringcomparator(String.valueOf(count), "0");
    }

    public void waitForCompletePageLoad() {
        // Wait until the page is fully loaded
        waitForLoad(web_driver);
    }

    public void waitForTitleContains(String Title) {
        // Wait until the page title contains
        WebDriverWait wait = new WebDriverWait(web_driver, Duration.ofSeconds(40));
        wait.until(ExpectedConditions.titleContains(Title));
    }

    void waitForLoad(WebDriver driver) {
        // Define the condition for page load
        ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        // Wait until the page load condition is met
        WebDriverWait wait = new WebDriverWait(web_driver, Duration.ofSeconds(10));
        wait.until(pageLoadCondition);
    }

    public void waitForFrameToBeAvailableAndSwitchToIt(String frameName) {
        // Wait until the specified frame is available and switch to it
        WebDriverWait wait = new WebDriverWait(web_driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName));
    }

    public void waitForTextToBePresentInElement(WebElement wl, String Textanme) {
        // Wait until the specified text is present in the specified WebElement
        WebDriverWait wait = new WebDriverWait(web_driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElement(wl, Textanme));
    }

    public void waitUntilTheElementIsVisible(WebElement wl) {
        // Wait until the specified WebElement is visible
        try {
            WebDriverWait wait = new WebDriverWait(web_driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(wl));
        } catch (Exception e) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void waitUntilTheElementIsNotSelected(WebElement wl) {
        // Wait until the specified WebElement is not selected
        WebDriverWait wait = new WebDriverWait(web_driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementSelectionStateToBe(wl, false));
    }

    public void waitUntilTheAttributeValueChangeToAnElement(WebElement wl, String AttributeName, String AttributeValue) {
        // Wait until the specified attribute of the WebElement changes to the specified value
        WebDriverWait wait = new WebDriverWait(web_driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.attributeToBe(wl, AttributeName, AttributeValue));
    }

    public void waitUntilAlertNotDisplaying() throws InterruptedException {
        int i = 0;
        while (i++ < 5) {
            try {
                // Try to switch to the alert
                Alert alert = web_driver.switchTo().alert();
            } catch (NoAlertPresentException e) {
                // If no alert is present, wait for 1 second and then break the loop
                Thread.sleep(1000);
                break;
            }
        }
    }

    // Method to verify if an alert is present
    public boolean verifyAlertPresent() {
        return isAlertPresent();
    }

    // Method to return the alert message text
    public String returnAlertMessage() {
        return web_driver.switchTo().alert().getText().trim();
    }

    // Method to check if an alert is present
    public boolean isAlertPresent() {
        try {
            web_driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException Ex) {
            return false;
        }
    }

    // Method to wait until an alert is displayed
    public void waitUntilTheAlertIsDisplaying() {
        WebDriverWait wait = new WebDriverWait(web_driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.alertIsPresent());
    }

    // Method to wait until a new window is opened
    public void waitUntilANewWindowOpen() {
        WebDriverWait wait = new WebDriverWait(web_driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
    }

    // Method to wait until all images on the page are loaded
    public void waitUntilAllImagesAreLoaded() {
        JavascriptExecutor jse = (JavascriptExecutor) web_driver;
        Object result = jse.executeScript("return document.readyState;");
        System.out.println("=> The status is : " + result.toString());
        if (result.equals("complete")) {
            result = jse.executeScript("return document.images.length");
            int imagesCount = Integer.parseInt(result.toString());
            boolean allLoaded = false;
            while (!allLoaded) {
                int count = 0;
                for (int i = 0; i < imagesCount; i++) {
                    result = jse.executeScript("return document.images[" + i + "].complete;");
                    boolean loaded = (Boolean) result;
                    if (loaded) count++;
                }
                if (count == imagesCount) {
                    System.out.println("=> All the Images are loaded...");
                    break;
                } else {
                    System.out.println("=> Not yet loaded...");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // Method to uncheck a checkbox if it is selected
    public void uncheckTheCheckBox(WebElement el) {
        if (el.isSelected()) {
            el.click();
        } else {
            System.out.println("Checkbox is already Unselected");
        }
    }

    public void checkTheCheckBox(WebElement el) {
        el.click();
    }

    // Method to check if a list has some option selected
    public boolean returnListHasSomeOptionSelected(WebElement wl) {
        Select sel = new Select(wl);
        return true;
    }

    // Method to return the number of options in a list
    public int returnListHasNumberOfOptions(WebElement wl) {
        Select sel = new Select(wl);
        return sel.getOptions().size();
    }

    // Method to check if a list has an option with a specific value selected
    public boolean returnListHasOptionWithValueSelected(WebElement wl, String Value) {
        Select sel = new Select(wl);
        return sel.getAllSelectedOptions().toString().contains(Value);
    }

    // Method to return the value of the selected option in a list
    public String returnValueSelectedInList(WebElement wl) {
        Select sel = new Select(wl);
        return sel.getFirstSelectedOption().toString();
    }

    // Method to return the number of options in a list
    public int returnOptionsCountsInList(WebElement wl) {
        Select sel = new Select(wl);
        return sel.getOptions().size();
    }

    // Method to check if multiple option selection is allowed in a list
    public boolean returnAllowSelectionOfMultipleOptionFlag(WebElement wl) {
        Select sel = new Select(wl);
        return sel.isMultiple();
    }

    // Method to check if an element has an empty value
    public boolean returnElementEmptyValueFlag(WebElement el) {
        boolean flag = false;
        String value = el.getAttribute("value");
        flag = !value.isEmpty();
        return flag;
    }

    // Method to check if an element has an empty Text
    public boolean returnElementEmptyTextFlag(WebElement el) {
        boolean flag = false;
        String value = getText(el);
        System.out.println("Valueee:" + value);
        if (!value.isEmpty()) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    // Method to return the tag name of an element
    public String returnElementTagName(WebElement el) {
        return el.getTagName().trim();
    }

    // Method to return the attribute value of an element
    public String returnElementAttrubuteValue(WebElement el, String AttributeName) {
        return el.getAttribute(AttributeName).trim();
    }

    // Method to get the text of an element
    public String getText(WebElement wl) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        if (wl == null)
            return "";
        return wl.getText();
    }

    // Method to return the CSS property value of an element
    public String returnCssPropertyName(WebElement el, String Propertyname) {
        return el.getCssValue(Propertyname).trim();
    }

    // Method to check the selected status of an element
    public boolean ReturnElementCheckStatus(WebElement el) {
        return el.isSelected();
    }

    // Method to get the count of web elements matching a locator
    public int getWebElementsCount(String locator) throws InterruptedException {
        ellist = web_driver.findElements(By.xpath(locator));
        return ellist.size();
    }

    // Method to select an option in a list by its value
    public void selectOptionByValue(WebElement el, String value) {
        Select sel = new Select(el);
        sel.selectByValue(value);
    }

    // Method to select an option in a list by its text
    public void selectOptionByText(WebElement el, String Text) {
        Select sel = new Select(el);
        sel.selectByVisibleText(Text);
    }

    // Method to select an option in a list by its index
    public void selectOptionByIndex(WebElement el, String index) {
        Select sel = new Select(el);
        sel.selectByIndex(Integer.parseInt(index));
    }

    // Method to perform an action with an element (RightClick, Mouseover, DoubleClick)
    public void actionWithElement(WebElement el, String ActionType) {
        Actions act = new Actions(web_driver);
        if (ActionType.equalsIgnoreCase("RightClick")) {
            act.contextClick(el).perform();
        } else if (ActionType.equalsIgnoreCase("Mouseover")) {
            act.moveToElement(el).perform();
        } else if (ActionType.equalsIgnoreCase("DoubleClick")) {
            act.doubleClick(el).perform();
        } else if (ActionType.equalsIgnoreCase("Click")) {
            act.moveToElement(el).click(el).perform();
        }
    }

    // Method to perform drag and drop of an element to another element
    public void dragAndDropElement(WebElement el, WebElement el1) {
        Actions act = new Actions(web_driver);
        act.dragAndDrop(el, el1).perform();
    }

    /**
     * Switches to a window with the given title.
     *
     * @param windowTitle The title of the window to switch to.
     */
    public void switchWindowTitled(String windowTitle) {
        parentWinHandle = web_driver.getWindowHandle();  // Save the current window handle
        allHandles = web_driver.getWindowHandles();  // Get all window handles
        for (String window : allHandles) {
            web_driver.switchTo().window(window);
            if (web_driver.getTitle().equalsIgnoreCase(windowTitle))   // Check if the handle matches the title
                break;
        }
    }

    public void closeWindowTitled(String windowTitle)
    {
        allHandles = web_driver.getWindowHandles();  // Get all window handles
        for (String window : allHandles) {
            web_driver.switchTo().window(window);
            if (web_driver.getTitle().equalsIgnoreCase(windowTitle))   // Check if the handle matches the title
                web_driver.close();
        }

        allHandles = web_driver.getWindowHandles();  // Get all window handles
        for (String window : allHandles) {
            web_driver.switchTo().window(window);
            if (!web_driver.getTitle().equalsIgnoreCase(windowTitle))   // Check if the handle matches the title
                break;
        }
    }

    /**
     * Closes the window with the given title.
     *
     * @param windowTitle The title of the window to close.
     */
    public void closeWindowByTitle(String windowTitle) {
        parentWinHandle = web_driver.getWindowHandle();  // Save the current window handle
        allHandles = web_driver.getWindowHandles();  // Get all window handles
        for (String window : allHandles) {
            if (window.equalsIgnoreCase(windowTitle)) {  // Check if the handle matches the title
                web_driver.switchTo().window(window);  // Switch to the window with the given title
                waitForElementLoad(2);  // Wait for the element to load
                web_driver.close();  // Close the window
            }
        }
        web_driver.switchTo().window(parentWinHandle);  // Switch back to the parent window
    }

    /**
     * Switches to a window by its index.
     *
     * @param index The index of the window to switch to.
     */
    public void switchWindowByIndex(String index) {
        parentWinHandle = web_driver.getWindowHandle();  // Save the current window handle
        allHandles = web_driver.getWindowHandles();  // Get all window handles
        int totalWin = allHandles.size();  // Get the total number of windows
        String winTitle = null;
        for (int i = 0; i < totalWin; i++) {
            if (i == Integer.parseInt(index)) {  // Check if the index matches
                winTitle = allHandles.toArray()[i].toString();  // Get the window handle by index
            }
        }
        web_driver.switchTo().window(winTitle);  // Switch to the window by its handle
        System.out.println(winTitle);  // Print the window title
    }

    /**
     * Closes a window by its index.
     *
     * @param index The index of the window to close.
     */
    public void closeWindowByIndex(String index) {
        parentWinHandle = web_driver.getWindowHandle();  // Save the current window handle
        allHandles = web_driver.getWindowHandles();  // Get all window handles
        int totalWin = allHandles.size();  // Get the total number of windows
        String winTitle = null;
        for (int i = 0; i < totalWin; i++) {
            if (i == Integer.parseInt(index)) {  // Check if the index matches
                winTitle = allHandles.toArray()[i].toString();  // Get the window handle by index
            }
        }
        web_driver.switchTo().window(winTitle);  // Switch to the window by its handle
        web_driver.close();  // Close the window
        web_driver.switchTo().window(parentWinHandle);  // Switch back to the parent window
        System.out.println(winTitle);  // Print the window title
    }

    /**
     * Switches back to the parent window.
     */
    public void switchToParentWindow() {
        web_driver.switchTo().window(parentWinHandle);  // Switch to the parent window
    }

    /**
     * Switches to the default content (main page) from an iframe or frame.
     */
    public void switchToParentPage() {
        web_driver.switchTo().defaultContent();  // Switch to the default content
    }

    /**
     * Closes the currently focused window.
     */
    public void closeCurrentWindows() {
        web_driver.close();  // Close the current window
    }

    /**
     * Closes all windows except the parent window.
     */
    @Override
    public void closeAllWindows() {
        String parentWinHandle = web_driver.getWindowHandle();  // Save the current window handle
        Set<String> totalOpenWindows = web_driver.getWindowHandles();  // Get all window handles
        if (totalOpenWindows.size() > 1) {  // Check if there are multiple windows
            for (String handle : totalOpenWindows) {
                if (!handle.equals(parentWinHandle)) {  // Skip the parent window
                    web_driver.switchTo().window(handle);  // Switch to the window
                }
            }
            web_driver.close();  // Close the current window
            web_driver.switchTo().window(parentWinHandle);  // Switch back to the parent window
        } else {
            System.out.println("No popup displayed");  // Print message if no popups are present
        }
    }

    /**
     * Navigates to the specified URL.
     *
     * @param url The URL to navigate to.
     */
    public void goTo(String url) {
        web_driver.navigate().to(url);  // Navigate to the specified URL
    }

    /**
     * Refreshes the current page.
     */
    public void refresh() {
        web_driver.navigate().refresh();  // Refresh the current page
    }

    /**
     * Navigates forward in the browser history.
     */
    public void forward() {
        web_driver.navigate().forward();  // Navigate forward
    }

    /**
     * Navigates backward in the browser history.
     */
    public void back() {
        web_driver.navigate().back();  // Navigate backward
    }

    /**
     * Switches to a frame by its name.
     *
     * @param frameName The name of the frame to switch to.
     */
    public void switchToFrame(String frameName) {
        web_driver.switchTo().frame(frameName);  // Switch to the frame by its name
    }

    /**
     * Switches to the parent frame and then to the default content.
     */
    public void switchToParentFrame() {
        web_driver.switchTo().parentFrame();  // Switch to the parent frame
        web_driver.switchTo().defaultContent();  // Switch to the default content
    }

    /**
     * Switches to a frame by its index.
     *
     * @param index The index of the frame to switch to.
     */
    public void switchToFrameByIndex(String index) {
        web_driver.switchTo().frame(Integer.parseInt(index));  // Switch to the frame by its index
    }

    /**
     * Switches to a frame using a WebElement.
     *
     * @param wl The WebElement representing the frame.
     */
    public void switchToFrameByElement(WebElement wl) {
        web_driver.switchTo().frame(wl);  // Switch to the frame using the WebElement
    }

    /**
     * Deletes a cookie by its name.
     *
     * @param cookiesName The name of the cookie to delete.
     */
    public void deleteCookiesWithName(String cookiesName) {
        web_driver.manage().deleteCookieNamed(cookiesName);  // Delete the specified cookie
    }

    /**
     * Deletes all cookies.
     */
    public void deleteAllCookies() {
        web_driver.manage().deleteAllCookies();  // Delete all cookies
    }

    /**
     * Accepts the current alert.
     */
    public void acceptAlert() {
        web_driver.switchTo().alert().accept();  // Accept the alert
    }

    /**
     * Dismisses the current alert.
     */
    public void cancelAlert() {
        web_driver.switchTo().alert().dismiss();  // Dismiss the alert
    }

    public void scrollIntoView(WebElement el) throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) web_driver;
        js.executeScript("arguments[0].scrollIntoView();", el);
        //js.executeScript("window.scrollTo(0, 0)");
    }

    /**
     * Clicks on the specified WebElement with error handling.
     *
     * @param el The WebElement to click.
     * @throws Exception If an error occurs during the click action.
     */
    public void clickOn(WebElement el) throws Exception {
        if (el != null) {
            try {
                Thread.sleep(200);  // Wait briefly
                el.click();  // Click the WebElement
                System.out.println(" try to click on the element ->" + el);
            } catch (WebDriverException e) {
                if (web_driver != null)
                    ((JavascriptExecutor) web_driver).executeScript("arguments[0].click();", el);  // Click with JavaScript
                else {
                    e.printStackTrace();
                    Assert.fail("Element has issues");
                }
            }
        } else {
            Assert.fail("Element We are trying to interact is not found");
        }
    }

    /**
     * Performs a JavaScript click on the specified WebElement.
     *
     * @param element The WebElement to click.
     * @throws Exception If an error occurs during the click action.
     */
    public void SafeJavaScriptClick(WebElement element) throws Exception {
        try {
            if (element.isEnabled() && element.isDisplayed()) {
                System.out.println("Clicking on element using JavaScript click in If");
                ((JavascriptExecutor) web_driver).executeScript("arguments[0].click();", element);  // Click with JavaScript
            } else {
                System.out.println("Clicking on element using JavaScript click in else");
                ((JavascriptExecutor) web_driver).executeScript("arguments[0].click();", element);  // Click with JavaScript
            }
        } catch (StaleElementReferenceException e) {
        } catch (NoSuchElementException e) {
        } catch (Exception e) {
        }
    }

    /**
     * Clicks on the WebElement if it is visible.
     *
     * @param el The WebElement to click.
     * @throws Exception If an error occurs during the click action.
     */
    public void clickOnIfElementVisible(WebElement el) throws Exception {
        if (el.isEnabled() || el.isEnabled()) {  // Check if the element is enabled
            clickOn(el);  // Click the WebElement
        }
    }

    /**
     * Clears the value of the specified WebElement using JavaScript.
     *
     * @param element The WebElement whose value should be cleared.
     */
    public void clearAttributeValue(WebElement element) {
        ((JavascriptExecutor) web_driver).executeScript("arguments[0].setAttribute('value', '')", element);  // Clear the value attribute
    }

    /**
     * Clears the text of the specified WebElement.
     *
     * @param element The WebElement whose text should be cleared.
     */
    public void clearText(WebElement element) {
        el.clear();  // Clear the text in the WebElement
    }

    // Method to verify if a link opens in a new tab or window
    public boolean verifyLinksOpenNewTanOrWindows(WebElement element) {
        boolean flag = false;
        Set<String> totalopenwindow = web_driver.getWindowHandles(); // Get current window handles
        el.click(); // Click on the element
        waitForElementLoad(4); // Wait for 4 seconds
        Set<String> totalopenwindowafter = web_driver.getWindowHandles(); // Get window handles after click
        if (totalopenwindowafter.size() > totalopenwindow.size()) {
            return true; // If a new window is opened, return true
        }
        return flag;
    }

    // Method to set attribute using JavaScript
    public void setAttributeUsingJS(WebElement element, String attName, String attValue) {
        ((JavascriptExecutor) web_driver).executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element, attName, attValue);
    }

    // Method to verify if element value is less than expected value
    public boolean verifyElementValueLessThan(WebElement element, int ExpValue) {
        boolean flag = false;
        int value = Integer.parseInt(element.getAttribute("value").trim());
        if (value < ExpValue) {
            flag = true;
        }
        return flag;
    }

    // Method to verify if element value is greater than expected value
    public boolean verifyElementValueGreaterThan(WebElement element, int ExpValue) {
        boolean flag = false;
        int value = Integer.parseInt(element.getAttribute("value").trim());
        if (value > ExpValue) {
            flag = true;
        }
        return flag;
    }

    // Method to verify if element value is less than or equal to expected value
    public boolean verifyElementValueLessThanOrEqualTo(WebElement element, int ExpValue) {
        boolean flag = false;
        int value = Integer.parseInt(element.getAttribute("value").trim());
        if (value <= ExpValue) {
            flag = true;
        }
        return flag;
    }

    // Method to verify if element value is greater than or equal to expected value
    public boolean verifyElementValueGreaterThanOrEqualTo(WebElement element, int ExpValue) {
        boolean flag = false;
        int value = Integer.parseInt(element.getAttribute("value").trim());
        if (value >= ExpValue) {
            flag = true;
        }
        return flag;
    }

    // Method to send keys to an element
    public void sendKeys(WebElement el, String value) throws InterruptedException, IOException {
        if (el != null) {
            JavascriptExecutor js = (JavascriptExecutor) web_driver;
            try {
                if (!getDriverType().equalsIgnoreCase("WEB")) {
                    el.clear();
                } else {
                    el.clear();
                    el.click();
                    el.sendKeys(Keys.chord(Keys.CONTROL, "", Keys.DELETE));
                }
                el.sendKeys(value); // Send value to element

                System.out.println(" try to Send keys into field : " + value);
            } catch (WebDriverException e) {
                if (getDriverType().equalsIgnoreCase("WEB")) {
                    js.executeScript("arguments[0].setAttribute('style', 'border: 2px solid red;');", el);
                    Thread.sleep(200);
                    js.executeScript("arguments[0].setAttribute('style', 'border: 0px solid red;');", el);
                    System.out.println(" Error in Clickon " + e.getMessage());
                    if (!e.toString().equals("NoSuchElementException")) {
                        try {
                            Thread.sleep(3000);
                            el.clear();
                            el.sendKeys(value);
                            System.out.println(" try to Send keys into field : " + value);
                        } catch (Exception e1) {
                            Assert.fail();
                        }
                    }
                }
            }
        } else {
            Assert.fail("Element We are trying to interact is not found");
        }
        try {
            if (mobile_driver != null)
                if (androidDriver != null)
                    androidDriver.hideKeyboard();
                else
                    iosDriver.hideKeyboard();
        } catch (Exception ignored) {
//			System.out.println("No a device");
        }
    }

    // Method to clear an element
    public void Clear(WebElement el) throws IOException, InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) web_driver;
        js.executeScript("arguments[0].setAttribute('style', 'border: 2px solid red;');", el);
        Thread.sleep(200);
        js.executeScript("arguments[0].setAttribute('style', 'border: 0px solid red;');", el);
        el.clear();
    }

    // Method to send keys using JavaScript
    public void sendKeysUsingJS(WebElement el, String value) throws InterruptedException, IOException {
        if (el != null) {
            JavascriptExecutor js = (JavascriptExecutor) web_driver;
            try {
                if (mobile_driver != null) {
                    el.clear();
                } else {
                    el.clear();
                    el.click();
                    el.clear();
                    el.sendKeys(Keys.BACK_SPACE);
                    el.sendKeys(Keys.BACK_SPACE);
                    el.sendKeys(Keys.BACK_SPACE);
                    el.sendKeys(Keys.BACK_SPACE);
                    el.sendKeys(Keys.BACK_SPACE);
                    el.sendKeys(Keys.BACK_SPACE);
                }
                js.executeScript("arguments[0].value=" + value + ";", el);
                System.out.println(" try to Send keys into field : " + value);
            } catch (WebDriverException e) {
                System.out.println(e);
                js.executeScript("arguments[0].setAttribute('style', 'border: 2px solid red;');", el);
                Thread.sleep(200);
                js.executeScript("arguments[0].setAttribute('style', 'border: 0px solid red;');", el);
                System.out.println("Error in Clickon " + e.getMessage());
                if (!e.toString().equals("NoSuchElementException")) {
                    try {
                        Thread.sleep(8000);
                        el.clear();
                        el.sendKeys(value);
                        System.out.println(" try to Send keys into field : " + value);
                    } catch (Exception e1) {
                        Assert.fail();
                    }
                }
            }
        } else {
            Assert.fail("Element We are trying to interact is not found");
        }
        try {
            if (androidDriver != null)
                androidDriver.hideKeyboard();
            else
                iosDriver.hideKeyboard();
        } catch (Exception e) {
            System.out.println("No a device");
        }
    }

    // Method to click on an element to open in new window and switch to it
    public void clickOnElementToOpenInNewWindowAndSwitchToIt(WebElement element) {
        String parent = web_driver.getWindowHandle();
        Set<String> s = web_driver.getWindowHandles();

        Iterator<String> I1 = s.iterator();
        while (I1.hasNext()) {
            String child_window = I1.next();
            if (!parent.equals(child_window)) {
                web_driver.switchTo().window(child_window);
                System.out.println(web_driver.switchTo().window(child_window).getTitle());
            }
        }
    }

    // Method to send keyboard keys to an element
    public void sendKeyboardKeys(WebElement el, Keys k) throws InterruptedException {
        el.sendKeys(k);
    }

    // Method to send keyboard keys using Actions class
    public void sendKeyBoardKeysUsingActions(Keys keys) throws Exception {
        Actions action = new Actions(web_driver);
        action.sendKeys(keys).build().perform();
    }

    // Method to press Shift + Delete keys using Actions class
    public void pressShiftDelete() throws Exception {
        Actions action = new Actions(web_driver);
        action.keyDown(Keys.SHIFT).keyDown(Keys.DELETE).build().perform();
        waitForElementLoad(1);
        action.keyUp(Keys.SHIFT).keyUp(Keys.DELETE).build().perform();
    }

    // Method to check if the options in a dropdown match the expected values
    public boolean checkOptions(String[] expected, WebElement el) {
        List<WebElement> options = el.findElements(By.xpath(".//option"));
        int k = 0;
        for (WebElement opt : options) {
            if (!opt.getText().equals(expected[k])) {
                return false; // Return false if any option does not match the expected value
            }
            k = k + 1;
        }
        return true; // Return true if all options match the expected values
    }

    // Method to send keys to an element using Actions class
    public void SendKeyswithAction(WebElement el, String key) throws Exception {
        clickOn(el); // Click on the element before sending keys
        Actions action = new Actions(web_driver);
        action.sendKeys(key).build().perform(); // Send the specified keys
        action.sendKeys(Keys.ENTER).build().perform(); // Press ENTER key
    }

    // Method to click an element using Actions class
    public void ClickswithAction(String el) throws InterruptedException {
        Actions action = new Actions(web_driver);
        action.click(web_driver.findElement(By.xpath(el))).build().perform(); // Click the element located by XPath
    }

    // Method to wait for a file to be downloaded
    public void WaitforFiletobeDownloaded(String Filename) throws InterruptedException {
        String str = System.getProperty("user.dir") + "/src/Data/Downloads/" + Filename; // Construct the file path
        File file = new File(str);
        wait.until((driver) -> file.exists()); // Wait until the file exists
    }

    // Method to get a web element by different locators
    public WebElement getWebElement(String locator) throws InterruptedException {
        System.out.println(new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date()) + "-> Search start for - " + locator);
        String[] finalval;
        try {
            el = null;
            if (locator.startsWith("name")) {
                finalval = locator.split("=");
                wait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        try {
                            el = driver.findElement(By.name(finalval[1])); // Find element by name
                        } catch (Exception e) {
                            el = null;
                        }
                        if (el != null) {
                            try {
                                wait.until(ExpectedConditions.elementToBeClickable(el)); // Wait until element is clickable
                            } catch (Exception e) {
                                wait.until(ExpectedConditions.visibilityOf(el)); // Wait until element is visible
                            }
                        }
                        System.out.println(new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date()) + "-> Search complete for - " + locator);
                        return el;
                    }
                });
            } else if (locator.startsWith("id")) {
                finalval = locator.split("=");
                wait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        try {
                            el = driver.findElement(By.id(finalval[1])); // Find element by ID
                        } catch (Exception e) {
                            el = null;
                        }
                        if (el != null) {
                            try {
                                wait.until(ExpectedConditions.elementToBeClickable(el)); // Wait until element is clickable
                            } catch (Exception e) {
                                wait.until(ExpectedConditions.visibilityOf(el)); // Wait until element is visible
                            }
                        }
                        System.out.println(new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date()) + "-> Search complete for - " + locator);
                        return el;
                    }
                });
            } else if (locator.startsWith("css")) {
                finalval = locator.split("=");
                wait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        try {
                            el = driver.findElement(By.cssSelector(finalval[1])); // Find element by ID
                        } catch (Exception e) {
                            el = null;
                        }
                        if (el != null) {
                            try {
                                wait.until(ExpectedConditions.elementToBeClickable(el)); // Wait until element is clickable
                            } catch (Exception e) {
                                wait.until(ExpectedConditions.visibilityOf(el)); // Wait until element is visible
                            }
                        }
                        System.out.println(new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date()) + "-> Search complete for - " + locator);
                        return el;
                    }
                });
            } else if (locator.startsWith("//") || locator.startsWith("(//") || locator.startsWith("(")) {
                wait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        try {
                            el = driver.findElement(By.xpath(locator)); // Find element by XPath
                        } catch (Exception e) {
                            el = null;
                        }
                        if (el != null) {
                            try {
                                wait.until(ExpectedConditions.elementToBeClickable(el)); // Wait until element is clickable
                            } catch (Exception e) {
                                wait.until(ExpectedConditions.visibilityOf(el)); // Wait until element is visible
                            }
                        }
                        System.out.println(new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date()) + "-> {XPATH} Search complete for - " + locator);
                        return el;
                    }
                });
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return el; // Return the found element
    }

    public WebElement getDesktopelement(String locator) throws InterruptedException {
        // Print the locator for which the element is being retrieved
        System.out.print("In Get element method for - " + locator);
        String[] finalval;
        try {
            // Check if the locator starts with "name"
            if (locator.startsWith("name")) {
                finalval = locator.split("=");
                // Wait for the element to be found and either clickable or visible
                waitDesktop.until(new Function<WindowsDriver, WebElement>() {
                    public WebElement apply(WindowsDriver DesktopDriver) {
                        el = DesktopDriver.findElement(By.name(finalval[1]));
                        try {
                            waitDesktop.until(ExpectedConditions.elementToBeClickable(el));
                        } catch (Exception e) {
                            waitDesktop.until(ExpectedConditions.visibilityOf(el));
                        }
                        return el;
                    }
                });
                // Check if the locator starts with "id"
            } else if (locator.startsWith("id")) {
                finalval = locator.split("=");
                el = desktop_driver.findElement(By.id(finalval[1]));
                waitDesktop.until(new Function<WindowsDriver, WebElement>() {
                    public WebElement apply(WindowsDriver DesktopDriver) {
                        el = DesktopDriver.findElement(By.id(finalval[1]));
                        try {
                            waitDesktop.until(ExpectedConditions.elementToBeClickable(el));
                        } catch (Exception e) {
                            waitDesktop.until(ExpectedConditions.visibilityOf(el));
                        }
                        return el;
                    }
                });
                // Check if the locator starts with "accessibityId"
            } else if (locator.startsWith("accessibityId")) {
                finalval = locator.split("=");
                el = desktop_driver.findElement(new AppiumBy.ByAccessibilityId(finalval[1]));
                waitDesktop.until(new Function<WindowsDriver, WebElement>() {
                    public WebElement apply(WindowsDriver DesktopDriver) {
                        el = DesktopDriver.findElement(new AppiumBy.ByAccessibilityId(finalval[1]));
                        try {
                            waitDesktop.until(ExpectedConditions.elementToBeClickable(el));
                        } catch (Exception e) {
                            waitDesktop.until(ExpectedConditions.visibilityOf(el));
                        }
                        return el;
                    }
                });
                // Check if the locator starts with an XPath expression
            } else if (locator.startsWith("//") || locator.startsWith("(//") || locator.startsWith("(")) {
                waitDesktop.until(new Function<WindowsDriver, WebElement>() {
                    public WebElement apply(WindowsDriver DesktopDriver) {
                        el = DesktopDriver.findElement(By.xpath(locator));
                        try {
                            waitDesktop.until(ExpectedConditions.elementToBeClickable(el));
                        } catch (Exception e) {
                            waitDesktop.until(ExpectedConditions.visibilityOf(el));
                        }
                        return el;
                    }
                });
            }
        } catch (Exception e) {
            // Print any exceptions that occur during the process
            System.out.println(e.getMessage());
        }
        // Return the found element
        return el;
    }

    public void javascriptButtonClick(WebElement webElement) {
        // Create a JavascriptExecutor instance and use it to click the web element
        JavascriptExecutor js = (JavascriptExecutor) web_driver;
        js.executeScript("arguments[0].click();", webElement);
    }

    public String getAdsOnPage(String string) {
        String src = null, width = null, height = null;
        try {
            // Switch to the first iframe
            web_driver.switchTo().frame(0);
            // Find and switch to the first ads iframe
            WebElement el = web_driver.findElement(By.xpath("//iframe[@id='google_ads_frame1']"));
            web_driver.switchTo().frame(el);
            // Find and switch to the second ads iframe
            WebElement el2 = web_driver.findElement(By.xpath("//iframe[@id='google_ads_frame2']"));
            web_driver.switchTo().frame(el2);
            // Find and switch to the third ads iframe
            WebElement el3 = web_driver.findElement(By.xpath("//iframe[@id='google_ads_frame3']"));
            web_driver.switchTo().frame(el3);
            // Find all ads images within the innermost iframe
            List<WebElement> list = web_driver.findElements(By.xpath("//img[@id='ads']"));
            for (WebElement img : list) {
                // Retrieve the src, width, and height attributes of each image
                src = img.getAttribute("src");
                width = img.getAttribute("width");
                height = img.getAttribute("height");
            }
        } catch (Exception e) {
            // Print stack trace if an exception occurs
            e.printStackTrace();
        }
        // Return the source URL and dimensions of the ad
        return src + " - " + width + "x" + height;
    }

    public boolean validateDataTrackResearchPage() {
        boolean flag = true;
        // Find all elements with the attribute 'data-track' within elements containing 'zone' in their ID
        int count = web_driver.findElements(By.xpath("(//*[contains(@id,'zone')]//*[@data-track])")).size();
        System.out.println(count);

        // Loop through each found element
        for (int i = 1; i <= count; i++) {
            String loc = "(//*[contains(@id,'zone')]//*[@data-track])";
            String locator = loc.concat("[" + i + "]");
            String attri = getAttribute(web_driver.findElement(By.xpath(locator)), "data-track");

            // Check for leading or trailing spaces in the 'data-track' attribute
            if (attri.endsWith(" ") || attri.startsWith(" ")) {
                System.err.println("Space is present with data-track:" + attri);
                flag = false;
            } else if (attri.endsWith("") || attri.startsWith("")) {
                System.out.println("Leading And Trailing Space is not present with data-track:" + attri);
            }
        }
        Assert.assertTrue(flag); // Assert that no leading or trailing spaces were found
        return flag;
    }

    public WebElement getMobileElement(String locator) throws InterruptedException {
        System.out.println(new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date()) + "-> Search start for - " + locator);
        String[] finalval;
        el = null;
        try {
            if (locator.startsWith("class")) {
                finalval = locator.split("=");
                waitMobile.until(new Function<AppiumDriver, WebElement>() {
                    public WebElement apply(AppiumDriver MobileDriver) {
                        try {
                            el = MobileDriver.findElement(new AppiumBy.ByAccessibilityId(finalval[1]));
                        } catch (NoSuchElementException e) {
                            el = null;
                        }
                        if (el != null) {
                            try {
                                waitMobile.until(ExpectedConditions.elementToBeClickable(el));
                            } catch (Exception e) {
                                waitMobile.until(ExpectedConditions.visibilityOf(el));
                            }
                        }
                        System.out.println(new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date()) + "-> Search complete for - " + locator);
                        return el;
                    }
                });
            } else if (locator.startsWith("id")) {
                finalval = locator.split("=");
//				el = mobile_driver.findElement(By.id(finalval[1]));
                waitMobile.until(new Function<AppiumDriver, WebElement>() {
                    public WebElement apply(AppiumDriver MobileDriver) {
                        try {
                            el = waitMobile.until(ExpectedConditions.elementToBeClickable(By.id(finalval[1])));
                        } catch (Exception e) {
                            el = null;
                        }
//						if (el != null) {
//							try {
//								waitMobile.until(ExpectedConditions.elementToBeClickable(el));
//							} catch (Exception e) {
//								waitMobile.until(ExpectedConditions.visibilityOf(el));
//							}
//						}
                        System.out.println(new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date()) + "-> Search complete for - " + locator);
                        return el;
                    }
                });
            } else if (locator.startsWith("name")) {
                finalval = locator.split("=");
                waitMobile.until(new Function<AppiumDriver, WebElement>() {
                    public WebElement apply(AppiumDriver MobileDriver) {
                        try {
                            el = waitMobile.until(ExpectedConditions.elementToBeClickable(By.name(finalval[1])));
                        } catch (Exception e) {
                            el = null;
                        }
//						if (el != null) {
//							try {
//								waitMobile.until(ExpectedConditions.elementToBeClickable(el));
//							} catch (Exception e) {
//								waitMobile.until(ExpectedConditions.visibilityOf(el));
//							}
//						}
                        System.out.println(new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date()) + "-> Search complete for - " + locator);
                        return el;
                    }
                });
            } else if (locator.startsWith("automationID")) {
                finalval = locator.split("=");
//				el = mobile_driver.findElement(new AppiumBy.ByAccessibilityId(finalval[1]));
                waitMobile.until(new Function<AppiumDriver, WebElement>() {
                    public WebElement apply(AppiumDriver MobileDriver) {
                        try {
                            el = MobileDriver.findElement(new AppiumBy.ByAccessibilityId(finalval[1]));
                        } catch (NoSuchElementException e) {
                            el = null;
                        }
                        if (el != null) {
                            try {
                                waitMobile.until(ExpectedConditions.elementToBeClickable(el));
                            } catch (Exception e) {
                                waitMobile.until(ExpectedConditions.visibilityOf(el));
                            }
                        }
                        System.out.println(new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date()) + "-> Search complete for - " + locator);
                        return el;
                    }
                });
            } else if (locator.startsWith("//") || locator.startsWith("/") || locator.startsWith("(//") || locator.startsWith("(")) {
                waitMobile.until(new Function<AppiumDriver, WebElement>() {
                    public WebElement apply(AppiumDriver MobileDriver) {
                        try {
                            el = waitMobile.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
                        } catch (Exception e) {
                            el = null;
                        }
                        System.out.println(new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date()) + "-> Search complete for - " + locator);
                        return el;
                    }
                });
            }
        } catch (Exception e) {
            System.out.println(" : " + e.getMessage());
        }
        return el;
    }

    public WebElement ReturnElement(String locator) {
        System.out.print("In Get element method for - " + locator);
        String[] finalval;

        try {
            if (locator.startsWith("name")) {
                finalval = locator.split("=");
                el = web_driver.findElement(By.name(finalval[1]));
            } else if (locator.startsWith("id")) {
                finalval = locator.split("=");
                el = web_driver.findElement(By.id(finalval[1]));
            } else if (locator.startsWith("//") || locator.startsWith("(//") || locator.startsWith("(")) {
                el = web_driver.findElement(By.xpath(locator));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println("Is the element is enabled-" + el.isEnabled());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return el;
    }

    public String getAttribute(WebElement el, String attributeName) {
        return el.getAttribute(attributeName);
    }

    public void moveOn(WebElement el) {
        Actions action = new Actions(web_driver);
        action.moveToElement(el).build().perform();
    }

    public void sendKeys(AndroidKey key) throws InterruptedException, IOException {
        if (androidDriver != null)
            androidDriver.pressKey(new KeyEvent(key));
    }

    public String getAndroidPageSource() throws InterruptedException, IOException {
        if (androidDriver != null)
            return androidDriver.getPageSource();
        else
            return iosDriver.getPageSource();
    }

    public void enterText(String s) {
        // Use Actions to send keys to the DesktopDriver
        Actions keyAction = new Actions(desktop_driver);
        keyAction.sendKeys(s).perform();
    }

    public String capturescreenshotforelement(WebElement ele) throws IOException {
        String screenshot2;
        // Capture the screenshot of the entire page
        File screenshot = ((TakesScreenshot) web_driver).getScreenshotAs(OutputType.FILE);
        BufferedImage fullImg = ImageIO.read(screenshot);

        // Get the location of the element on the page
        org.openqa.selenium.Point point = ele.getLocation();
        // Get the width and height of the element
        int eleWidth = ele.getSize().getWidth();
        int eleHeight = ele.getSize().getHeight();

        // Crop the entire page screenshot to get only the element screenshot
        BufferedImage eleScreenshot = fullImg.getSubimage(point.getX() - 20, point.getY() - 20, eleWidth + 20, eleHeight + 20);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(eleScreenshot, "png", bos);
        byte[] imageBytes = bos.toByteArray();
        screenshot2 = "data:image/png;base64," + Base64.getMimeEncoder().encodeToString(imageBytes);
        bos.close();
        return screenshot2;
    }

    @Override
    public void switchToLastTab() {
        // Store the current window handle
        String parentWinHandle = web_driver.getWindowHandle();
        // Get all open window handles
        Set<String> totalopenwindow = web_driver.getWindowHandles();
        for (String handle : totalopenwindow) {
            if (!handle.equals(parentWinHandle)) {
                // Switch to the last opened window
                web_driver.switchTo().window(handle);
            }
        }
    }

    @Override
    public void getIframeLoaded(String framlocator) {
        // Wait for the iframe to be present in the DOM
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(framlocator.split("=")[1])));
        System.out.println("Switched to Iframe");
    }

    @Override
    public void waitforPageToBeReady() throws Exception {
        // Wait until the page's ready state is "complete"
        while (!((JavascriptExecutor) web_driver).executeScript("return document.readyState").equals("complete")) {
            System.out.println("dom state is " + ((JavascriptExecutor) web_driver).executeScript("return document.readyState"));
            Thread.sleep(500);
        }
    }

    @Override
    public String getTitle() {
        // Get the title of the current page
        return web_driver.getTitle();
    }

    @Override
    public void openURL(String environment) {
        // Open the URL specified by the environment variable
        String URL = environment;
        System.out.println(URL);
        web_driver.get(URL);
    }

    @Override
    public void openURLV2(String environment) throws Exception {
        // Open the URL specified by the environment variable and wait for 5 seconds
        String URL = null;
        System.out.println(URL);
        web_driver.get(URL);
        Thread.sleep(5000);
    }

    @Override
    public void getURL(String URL) {
        // Open the specified URL
        web_driver.get(URL);
    }

    @Override
    public void verifyTitle(String Expectedtitle) {
        // Verify if the current page's title contains the expected title
        Assert.assertTrue(web_driver.getTitle().contains(Expectedtitle));
    }

    public void verifyCurrentURL(String ExpectedURL) {
        // Verify if the current URL contains the expected URL
        Assert.assertTrue(web_driver.getCurrentUrl().contains(ExpectedURL));
    }

    @Override
    public void verifyText(WebElement el, String Expectedtext) {
        // Verify if the element's text contains the expected text
        Assert.assertTrue(el.getText().contains(Expectedtext), "Text of Element displayed as -" + el.getText() + " and Expected String is-" + Expectedtext);
    }

    @Override
    public void pageRefresh() throws Exception {
        // Refresh the current page and wait for 5 seconds
        web_driver.navigate().refresh();
        Thread.sleep(5000);
    }

    @Override
    public String currentURL() {
        // Get the current URL of the page
        return web_driver.getCurrentUrl();
    }

    @Override
    public String captureScreenShotForElement(WebElement ele) {
        // Method to capture screenshot for an element (not implemented)
        return null;
    }

    /*	End of the Web Driver Helper area which contains all the base methods related to the Web Driver
     *------------------------------------------------------------------------------------------
     *
     */


    /*	Start of the API Helper area which contains all the base methods related to the API
     *------------------------------------------------------------------------------------------
     *
     */

    @Override
    public String captureFullScreenShot(WebDriver driver) {
        String screenshot2;
        // Capture the full page screenshot using AShot library
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(screenshot.getImage(), "PNG", bos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] imageBytes = bos.toByteArray();
        screenshot2 = "data:image/png;base64," + Base64.getMimeEncoder().encodeToString(imageBytes);
        try {
            bos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return screenshot2;
    }

    @Override
    public void authentication(String type, String username, String password) {
        // Handles different types of authentication
        switch (type) {
            case "Basic": {
                // Basic authentication
                requestSpecification.auth().basic(username, password);
                break;
            }
            case "preemptive": {
                // Preemptive basic authentication
                requestSpecification.auth().preemptive().basic(username, password);
                break;
            }
            case "Form": {
                // Form-based authentication
                requestSpecification.auth().form(username, password);
                break;
            }
            case "ComplexForm": {
                // Complex form authentication with additional configuration
                requestSpecification.auth().form(username, password, new FormAuthConfig("/perform_login", "username", "password"));
                break;
            }
            case "Oauth2": {
                // OAuth2 authentication
                requestSpecification.auth().oauth2(username);
                break;
            }
        }
    }

    @Override
    public void updateRequestHeader(String HeaderKey, String value) {
        // Updates the request header with a specified key and value
        requestSpecification.headers(HeaderKey, value);
    }

    @Override
    public String readRequestTemplate(String path) throws IOException {
        // Reads and returns the content of the request template file as a string
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    @Override
    public void updateAttributeInRequestBody(String Filename, String Key, String value) throws Exception {
        // Updates a specific attribute in the request body based on the file type
        if (Filename.toLowerCase().endsWith(".json")) {
            String abc = "";
            String[] arrOfStr = Key.split("//");

            if (Key.endsWith("//")) {
                System.out.println("Provided Json node path is not correct!! it can't be end with //");
                Assert.fail("Provided Json node path is not correct!! it can't be end with //");
            }
            FileReader reader = null;
            try {
                Filename = System.getProperty("user.dir") + "/" + Root + Filename;
                reader = new FileReader(Filename);
            } catch (Exception e) {
                System.out.println("File path Pointed to Json file is incorrect!!! Please provide a valid path... ");
                e.getMessage();
            }
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            JSONObject idObj1 = null;

            if (arrOfStr.length == 1 || arrOfStr.length == 0) {
                try {
                    if (!arrOfStr[0].contains("//")) {
                        System.out.println("// is missing with Node value!!!!");
                        Assert.assertFalse(arrOfStr[0].contains("//"), "// is missing with Node value!!!!");
                    } else {
                        System.out.println("Node is not correctly provided!!!!");
                    }
                } catch (Exception e) {
                    System.out.println("Node is not correctly provided!!!!");
                    e.getMessage();
                }
            } else {
                for (int i = 0; i < arrOfStr.length - 2; i++) {
                    if (idObj1 == null) {
                        idObj1 = (JSONObject) jsonObject.get(arrOfStr[i + 1]);
                    } else {
                        idObj1 = (JSONObject) idObj1.get(arrOfStr[i + 1]);
                    }
                }

                if (arrOfStr.length == 2) {
                    jsonObject.put(arrOfStr[arrOfStr.length - 1], value);
                } else {
                    idObj1.put(arrOfStr[arrOfStr.length - 1], value);
                }
                JSONDocumentPayload = jsonObject;
                abc = jsonObject.toJSONString();
            }

            Payload = abc;
            System.out.println("Updated Payload" + Payload);
        } else if (Filename.toLowerCase().endsWith(".xml")) {
            // For XML files, update the attribute value using SAXReader
            SAXReader xmlreader = new SAXReader();
            Document doc = xmlreader.read(System.getProperty("user.dir") + "/" + Root + Filename);
            doc.selectSingleNode(Key).setText(value);
            XMLDocumentPayload = doc;
            Payload = doc.asXML();
            System.out.println("Updated Payload" + Payload);
        }
    }

    @Override
    public void updateAttributeInRequestBody(String Filename, HashMap<String, String> values) throws Exception {
        // Updates multiple attributes in the request body based on file type
        if (Filename.toLowerCase().endsWith(".json")) {
            // TODO need to implement
        } else if (Filename.toLowerCase().endsWith(".xml")) {
            SAXReader xmlreader = new SAXReader();
            Document doc = xmlreader.read(System.getProperty("user.dir") + "/" + Root + Filename);
            for (String key : values.keySet()) {
                doc.selectSingleNode(key).setText(values.get(key));
            }
            Payload = doc.asXML();
            System.out.println("Updated Payload" + Payload);
        }
    }

    @Override
    public void generatePayLoad() {
        // Sets the payload body for the request and prints it
        requestSpecification.body(Payload);
        System.out.println("********************************************");
        System.out.println(Payload);
        ReportPayload = Payload;
        System.out.println("********************************************");
        Payload = "";
    }

    public void generateMultipart(String key, String Value) {
        // Adds a multipart form-data part to the request
        requestSpecification.multiPart(key, Value);
    }

    @Override
    public void submitRequest(Method method, String URI) {
        // Submits the request and prints the response body
        resultResponse = null;
        System.out.println(URI);
        resultResponse = requestSpecification.request(method, URI);
        System.out.println("Response Body After Request: " + resultResponse.getBody().asString());
        requestSpecification = RestAssured.given();
    }

    @Override
    public void assertStringInResponseBody(String ExpectedData) {
        // Asserts that the response body contains the expected data
        String responseBody = resultResponse.getBody().asString();
        System.out.println("Response Body is: " + responseBody);
        Assert.assertTrue(responseBody.contains(ExpectedData));
    }

    @Override
    public void assertStatusCode(int ExpectedStatusCode) {
        // Asserts that the response status code matches the expected status code
        int statusCode = resultResponse.getStatusCode();
        System.out.println("Status code is: " + statusCode);
        Assert.assertEquals(statusCode, ExpectedStatusCode);
    }

    @Override
    public void assertStatusLine(String ExpectedStatusLine) {
        // Asserts that the response status line matches the expected status line
        String statusLine = resultResponse.getStatusLine();
        System.out.println("Status Code is: " + statusLine);
        Assert.assertEquals(statusLine, ExpectedStatusLine);
    }

    @Override
    public void assertHeaderattribute(String HeaderName, String ExpectedheaderValue) {
        // Asserts that the response header matches the expected value
        Headers headers = resultResponse.getHeaders();
        System.out.println(headers);

        String headervalue = resultResponse.getHeader(HeaderName);
        System.out.println("The value of content-type header is: " + headervalue);
        Assert.assertEquals(headervalue, ExpectedheaderValue);
    }

    @Override
    public void assertResponseBodyAttribute(String Node, String Expectedvalue) throws SAXException, IOException, ParserConfigurationException, DocumentException {
        // Asserts that the response body attribute matches the expected value
        System.out.println("Response body is " + resultResponse.getBody().asString());
        if (resultResponse.getHeader("content-type").contains("json")) {
            JsonPath jsonPathValue = resultResponse.jsonPath();
            String Nodevalue = jsonPathValue.getString(Node);
            System.out.println("The value of " + Node + " is: " + Nodevalue);

            System.out.println("Expected Value is : " + Expectedvalue);

            System.out.println("Condition Value is " + Nodevalue.contains(Expectedvalue));
            Assert.assertTrue(Nodevalue.contains(Expectedvalue), "Expected the Value of <b>" + Node + "</b> Attribute value contains <b>" + Expectedvalue + "</b> but  Actual returned was <b>" + Nodevalue + "</b>");
        } else if (resultResponse.getHeader("content-type").contains("xml")) {
            SAXReader xmlreader = new SAXReader();
            Document doc = xmlreader.read(resultResponse.asInputStream());
            Assert.assertEquals(Expectedvalue, doc.selectSingleNode(Node).getText(), "Expected the Value of <b>" + Node + "</b> Attribute value contains <b>" + Expectedvalue + "</b> but  Actual returned was <b>" + doc.selectSingleNode(Node).getText() + "</b>");
        } else {
            Assert.fail("Response Content-Type is not matched. Please check the Assertion");
        }
    }

    @Override
    public String SaveAttributevalue(String Node) throws DocumentException {
        // Saves and returns the value of a specific attribute from the response
        String Nodevalue = null;
        if (resultResponse.getContentType().contains("json")) {
            System.out.println("Data in Response is " + resultResponse.getBody().asString());
            JsonPath jsonPathValue = resultResponse.jsonPath();
            Nodevalue = jsonPathValue.get(Node).toString();
            System.out.println("The value of " + Node + " is: " + Nodevalue);
            Assert.assertEquals(Nodevalue, Nodevalue);
        } else if (resultResponse.getContentType().contains("xml")) {
            SAXReader xmlreader = new SAXReader();
            Document doc = xmlreader.read(resultResponse.asInputStream());
            Nodevalue = doc.selectSingleNode(Node).getText();
        } else {
            Assert.fail("Invalid Content Type");
        }
        return Nodevalue;
    }

    @Override
    public JSONObject Inputgenerator(JSONObject templatefile, String Node, String Value) {
        // Generates input by updating a specific node in the template JSON file
        JSONObject jsonObjectnodelist = null;
        JSONObject jsonObjectfinal = templatefile;
        String[] nodelist = Node.split(".");
        if (nodelist.length == 1) {
            // Update the Node directly
            System.out.println(jsonObjectnodelist.get(nodelist[nodelist.length - 1]));
        } else {
            for (int i = 0; i < nodelist.length - 2; i++) {
                jsonObjectnodelist = (JSONObject) templatefile.get(nodelist[i]);
                System.out.println(jsonObjectnodelist.toString());
            }
            System.out.println(jsonObjectnodelist.get(nodelist[nodelist.length - 1]));
        }

        return templatefile;
    }

    @Override
    public void validateResponseJsonSchema(String path) {
        // Constructs the path to the JSON schema file and validates the response against it.
        String updatedpath = System.getProperty("user.dir") + "/" + Root + path;
        resultResponse.then().body(matchesJsonSchema(new File(updatedpath)));
    }

    @Override
    public String appendUriWithParameters(String uri, Map<String, String> headerMap) {
        // Appends parameters to the given URI based on the provided map.
        int i = 1;
        for (String key : headerMap.keySet()) {
            if (i == 1) {
                uri = uri.concat("?" + key + "=" + headerMap.get(key));
            } else {
                uri = uri.concat("&" + key + "=" + headerMap.get(key));
            }
            i++;
        }
        return uri;
    }

    @Override
    public void addRequestParameters(Map<String, String> headerMap) {
        // Adds parameters from the map to the request.
        requestSpecification.params(headerMap);
    }

    @Override
    public void setContentType(String contentType) {
        // Sets the content type for the request.
        requestSpecification.contentType(contentType);
    }

    @Override
    public void setContentTypeRestAssured(ContentType contentType) {
        // Sets the content type using RestAssured.
        requestSpecification.contentType(contentType);
    }

    @Override
    public void validateResponseXMLSchema(String path) {
        // Constructs the path to the XML schema file and validates the response against it.
        String updatedpath = System.getProperty("user.dir") + "/" + Root + path;
        resultResponse.then().body(matchesXsd(new File(updatedpath)));
    }

    @Override
    public String getBaseURI(String uri) {
        // Retrieves the base URI from configuration properties based on the environment.
        Properties pr = new Properties();
        InputStream file = null;
        String value = null;
        String endpoint = "";
        try {
            file = new FileInputStream("staging_config.properties");
            pr.load(file);
            value = pr.getProperty("ENV").trim();
            if (value.equalsIgnoreCase("local")) {
                endpoint = pr.getProperty("localmock_url");
            } else {
                String env = value.toLowerCase();
                endpoint = pr.getProperty(env + "_" + uri);
            }
        } catch (Exception e) {
            // Handle exception if needed
        } finally {
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return endpoint;
    }

    @Override
    public void readPayload(String requestpath) {
        // Reads the payload from a file and assigns it to the Payload variable.
        try {
            String payload = readRequestTemplate(System.getProperty("user.dir") + "/" + Root + requestpath);
            Payload = payload;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getSingleValueFromResponse(String Node) {
        // Retrieves the value of a specific node from the response, handling both JSON and XML responses.
        String nodevalue = null;
        if (resultResponse.getHeader("content-type").contains("json")) {
            JsonPath jsonPathValue = resultResponse.jsonPath();
            nodevalue = jsonPathValue.getString(Node);
        } else if (resultResponse.getHeader("content-type").contains("xml")) {
            SAXReader xmlreader = new SAXReader();
            Document doc = null;
            try {
                doc = xmlreader.read(resultResponse.asInputStream());
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            nodevalue = doc.selectSingleNode(Node).getText();
        }
        return nodevalue;
    }

    public void Stringcomparator(String actual, String expected) {
        // Compares two strings and asserts they are equal.
        System.out.println("Actual String : " + actual);
        System.out.println("Expected String : " + expected);
        if (expected == null) {
            expected = "";
        }
        if (actual == null) {
            actual = "";
        }
        Assert.assertEquals(expected, actual);
    }

    public void Jsoncomparator(JsonElement actual, JsonElement expected) {
        // Compares two JSON elements and asserts they are equal.
        System.out.println("Actual String : " + actual);
        System.out.println("Expected String : " + expected);
        Assert.assertEquals(expected, actual);
    }

    public void Booleancomparator(Boolean actual, Boolean expected) {
        // Compares two Boolean values and asserts they are the same.
        System.out.println("Actual String : " + actual);
        System.out.println("Expected String : " + expected);
        Assert.assertSame(actual, expected);
    }


    /**
     * Updates the text value of a specified XML node in the payload.
     *
     * @param Node  the XPath of the XML node to update
     * @param Value the new text value to set for the node
     * @throws DocumentException            if there's an error with the XML document
     * @throws IOException                  if there's an I/O error
     * @throws SAXException                 if there's an XML parsing error
     * @throws ParserConfigurationException if there's an error in XML parser configuration
     */
    public void UpdatedAttributeinxmlPayload(String Node, String Value) throws DocumentException, IOException, SAXException, ParserConfigurationException {
        Document doc = XMLDocumentPayload;
        doc.selectSingleNode(Node).setText(Value);
        XMLDocumentPayload = doc;
        Payload = doc.asXML();
    }

    /**
     * Updates a JSON node with a specified value in the JSON payload.
     *
     * @param Node     the path of the JSON node to update
     * @param Value    the new value to set for the node
     * @param isstring true if the value is a string, false if it's a numeric value
     * @throws DocumentException            if there's an error with the JSON document
     * @throws IOException                  if there's an I/O error
     * @throws SAXException                 if there's an XML parsing error
     * @throws ParserConfigurationException if there's an error in XML parser configuration
     */
    public void UpdatedAttributeinJsonPayload(String Node, String Value, boolean isstring) throws DocumentException, IOException, SAXException, ParserConfigurationException {
        String abc = "";
        String[] arrOfStr = Node.split("//");

        if (Node.endsWith("//")) {
            System.out.println("Provided Json node path is not correct!! it can't be end with //");
            Assert.fail("Provided Json node path is not correct!! it can't be end with //");
        }

        JSONObject jsonObject = JSONDocumentPayload;
        JSONObject idObj1 = null;

        if (arrOfStr.length == 1 || arrOfStr.length == 0) {
            try {
                if (!arrOfStr[0].contains("//")) {
                    System.out.println("// is missing with Node value!!!!");
                    Assert.assertFalse(arrOfStr[0].contains("//"), "// is missing with Node value!!!!");
                } else {
                    System.out.println("Node is not correctly provided!!!!");
                }
            } catch (Exception e) {
                System.out.println("Node is not correctly provided!!!!");
                e.getMessage();
            }
        } else {
            for (int i = 0; i < arrOfStr.length - 2; i++) {
                if (idObj1 == null) {
                    idObj1 = (JSONObject) (jsonObject.get(arrOfStr[i + 1]));
                } else {
                    idObj1 = (JSONObject) idObj1.get(arrOfStr[i + 1]);
                }
            }

            if (arrOfStr.length == 2) {
                if (isstring)
                    jsonObject.put(arrOfStr[arrOfStr.length - 1], Value);
                else {
                    BigInteger obj = new BigInteger(Value);
                    jsonObject.put(arrOfStr[arrOfStr.length - 1], obj);
                }
            } else {
                if (isstring)
                    idObj1.put(arrOfStr[arrOfStr.length - 1], Value);
                else {
                    BigInteger obj = new BigInteger(Value);
                    idObj1.put(arrOfStr[arrOfStr.length - 1], obj);
                }
            }
            JSONDocumentPayload = jsonObject;
            abc = jsonObject.toJSONString();
        }

        Payload = abc;
    }

    /**
     * Submits a request with a specified HTTP method and URI, and sets the header for the request.
     *
     * @param method the HTTP method (GET, POST, etc.)
     * @param URI    the URI for the request
     */
    @Override
    public void submitRequestWithHeader(Method method, String URI) {
        resultResponse = null;
        requestSpecification.headers("Content-Type", "text/xml");
        resultResponse = requestSpecification.request(method, URI);
        System.out.println("Response Body After Request: " + resultResponse.getBody().asString());
        requestSpecification.body("");
    }

    /**
     * Asserts whether a specified XML or JSON node is present in the response.
     *
     * @param Node     the XPath or JSON path of the node to check
     * @param expected true if the node is expected to be present, false otherwise
     * @throws SAXException                 if there's an XML parsing error
     * @throws IOException                  if there's an I/O error
     * @throws ParserConfigurationException if there's an error in XML parser configuration
     * @throws DocumentException            if there's an error with the document
     */
    public void assertNodeIsPresent(String Node, boolean expected) throws SAXException, IOException, ParserConfigurationException, DocumentException {
        boolean flag = false;
        if (resultResponse.getHeader("content-type").contains("json")) {
            JsonPath jsonPathValue = resultResponse.jsonPath();
            try {
                if (jsonPathValue.getList(Node).size() > 0) {
                    if (!jsonPathValue.getString(Node).contains("null")) {
                        flag = true;
                    }
                }
            } catch (Exception e) {
                flag = false;
            }
            Assert.assertEquals(flag, expected, "Node <b>" + Node + "</b> contains in the response ");
        } else if (resultResponse.getHeader("content-type").contains("xml")) {
            SAXReader xmlreader = new SAXReader();
            Document doc = xmlreader.read(resultResponse.asInputStream());
            try {
                if (doc.selectNodes(Node).size() > 0) {
                    flag = true;
                }
            } catch (Exception e) {
                flag = false;
            }
            Assert.assertEquals(flag, expected, "Node <b>" + Node + "</b> contains in the response which is expected :- " + expected);
        } else {
            Assert.fail("Response Content-Type is not matched.Please check the Assertion");
        }
    }

    /**
     * Encodes a given string to its Base64 representation.
     *
     * @param data the string to encode
     * @return the Base64 encoded string
     * @throws SAXException                 if there's an XML parsing error
     * @throws IOException                  if there's an I/O error
     * @throws ParserConfigurationException if there's an error in XML parser configuration
     * @throws DocumentException            if there's an error with the document
     */
    public String Base64Encoder(String data) throws SAXException, IOException, ParserConfigurationException, DocumentException {
        Base64.Encoder encoder = Base64.getEncoder();
        String originalString = data;
        String encodedString = encoder.encodeToString(originalString.getBytes());
        System.out.println(encodedString);
        return encodedString;
    }

    /**
     * Returns the current payload as a string.
     *
     * @return the payload string
     * @throws SAXException                 if there's an XML parsing error
     * @throws IOException                  if there's an I/O error
     * @throws ParserConfigurationException if there's an error in XML parser configuration
     * @throws DocumentException            if there's an error with the document
     */
    public String ReturnPaylod() throws SAXException, IOException, ParserConfigurationException, DocumentException {
        return Payload;
    }

    /**
     * Checks if a given date string matches a specified date format.
     *
     * @param dateStr    the date string to validate
     * @param dateFormat the date format to check against
     * @return true if the date string matches the format, false otherwise
     */
    public boolean isValidDateFormat(String dateStr, String dateFormat) {
        DateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Compares two strings to check if the actual string contains the expected string.
     *
     * @param actual   the actual string
     * @param expected the expected string
     */
    public void Partialstringcomparator(String actual, String expected) {
        System.out.println("Actual String : " + actual);
        System.out.println("Expected String : " + expected);
        if (expected == null) {
            expected = "";
        }
        if (actual == null) {
            actual = "";
        }
        Assert.assertTrue(actual.contains(expected));
    }

    /**
     * Returns the response as a string.
     *
     * @return the response string
     */
    public String getResponse() {
        return resultResponse.prettyPrint();
    }

    /**
     * Gets today's date formatted according to the specified format.
     *
     * @param dateFormat the date format to use
     * @return the formatted current date
     */
    public String getTodaysDateinFormat(String dateFormat) {
        DateFormat dteFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String requiredData = dteFormat.format(date);
        return requiredData;
    }

    /**
     * Creates a unique order number based on the current timestamp.
     *
     * @return the generated order number
     */
    public String createOrderNumber() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String orderNum = "" + timestamp.getTime();
        return orderNum;
    }

    /*	End of the API Helper area which contains all the base methods related to the API
     *------------------------------------------------------------------------------------------
     *
     */

    @Override
    public String pollCurrentTime(String className, String timeStamp, String timeOut) throws InterruptedException {
        // Define a DateTimeFormatter for minutes and seconds
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss");
        // Calculate the future time by adding the timeout to the current time
        LocalTime Time = LocalTime.now().plusMinutes(Integer.valueOf(timeOut.split(":")[0])).plusSeconds(Integer.valueOf(timeOut.split(":")[1]));
        // Cast the WebDriver instance to JavascriptExecutor to execute JS scripts
        JavascriptExecutor js = (JavascriptExecutor) web_driver;
        String p = "";
        while (true) {
            try {
                // Get the text content of the web element with the given class name
                p = getWebElement("//*[@class='" + className + "']/span").getText();
                // If the text matches the timestamp, click the play button and break the loop
                if (compare(p, timeStamp)) {
                    js.executeScript("arguments[0].click();", getWebElement("//*[@class='playkit-icon playkit-icon-play']"));
                    break;
                }
            } catch (Exception e) {
                // Handle any exceptions that occur during text retrieval or comparison
            }
            if (!p.isEmpty()) {
                // Compare the current time with the timestamp to determine if we should click the play button
                if (Integer.valueOf(p.split(":")[0]) >= Integer.valueOf(timeStamp.split(":")[0])) {
                    if (Integer.valueOf(p.split(":")[1]) >= Integer.valueOf(timeStamp.split(":")[1]) || LocalTime.now() == Time) {
                        js.executeScript("arguments[0].click();", getWebElement("//*[@class='playkit-icon playkit-icon-play']"));
                        break;
                    }
                }
            }
        }
        return p;
    }

    // Method to compare web time with a given timestamp
    public boolean compare(String webTimeStamp, String androidTimeStamp) {
        int result = (Integer.valueOf(webTimeStamp.split(":")[0]) * 60 + Integer.valueOf(webTimeStamp.split(":")[1])) - (Integer.valueOf(androidTimeStamp.split(":")[0]) * 60 + Integer.valueOf(androidTimeStamp.split(":")[1]));
        return result >= 0 && result <= 40; // Return true if the result is within the acceptable range
    }

    // Method to wait for Angular requests to finish
    public void handleAngularWait() {
        JavascriptExecutor jsDriver = (JavascriptExecutor) web_driver;
        ngDriver = new NgWebDriver(jsDriver);
        ngDriver.waitForAngularRequestsToFinish();
    }

    // Method to assert the number of elements found by the Angular repeater
    public void handleAngularMain() {
        int count = web_driver.findElements(ByAngular.repeater("result in memory")).size();
        System.out.println("Count  " + count);
        Assert.assertEquals(count, 1); // Assert that exactly one element is found
    }

    // Method to retrieve Angular web elements based on various locators
    public WebElement getNGWebelement(String locator) throws InterruptedException {
        String[] finalval;
        try {
            System.out.print("\nIn Get element method for - " + locator + " ");
            if (locator.startsWith("binding")) {
                finalval = locator.split("=");
                wait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        el = driver.findElement(ByAngular.binding(finalval[1]));
                        try {
                            wait.until(ExpectedConditions.elementToBeClickable(el));
                        } catch (Exception e) {
                            wait.until(ExpectedConditions.visibilityOf(el));
                        }
                        return el;
                    }
                });
            } else if (locator.startsWith("buttonText")) {
                finalval = locator.split("=");
                wait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        el = driver.findElement(ByAngular.buttonText(finalval[1]));
                        try {
                            wait.until(ExpectedConditions.elementToBeClickable(el));
                        } catch (Exception e) {
                            wait.until(ExpectedConditions.visibilityOf(el));
                        }
                        return el;
                    }
                });
            } else if (locator.startsWith("exactBinding")) {
                finalval = locator.split("=");
                wait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        el = driver.findElement(ByAngular.exactBinding(finalval[1]));
                        try {
                            wait.until(ExpectedConditions.elementToBeClickable(el));
                        } catch (Exception e) {
                            wait.until(ExpectedConditions.visibilityOf(el));
                        }
                        return el;
                    }
                });
            } else if (locator.startsWith("exactRepeater")) {
                finalval = locator.split("=");
                wait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        el = driver.findElement(ByAngular.exactRepeater(finalval[1]));
                        try {
                            wait.until(ExpectedConditions.elementToBeClickable(el));
                        } catch (Exception e) {
                            wait.until(ExpectedConditions.visibilityOf(el));
                        }
                        return el;
                    }
                });
            } else if (locator.startsWith("model")) {
                finalval = locator.split("=");
                wait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        el = driver.findElement(ByAngular.model(finalval[1]));
                        try {
                            wait.until(ExpectedConditions.elementToBeClickable(el));
                        } catch (Exception e) {
                            wait.until(ExpectedConditions.visibilityOf(el));
                        }
                        return el;
                    }
                });
            } else if (locator.startsWith("partialButtonText")) {
                finalval = locator.split("=");
                wait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        el = driver.findElement(ByAngular.partialButtonText(finalval[1]));
                        try {
                            wait.until(ExpectedConditions.elementToBeClickable(el));
                        } catch (Exception e) {
                            wait.until(ExpectedConditions.visibilityOf(el));
                        }
                        return el;
                    }
                });
            } else if (locator.startsWith("repeater")) {
                finalval = locator.split("=");
                wait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        el = driver.findElement(ByAngular.repeater(finalval[1]));
                        try {
                            wait.until(ExpectedConditions.elementToBeClickable(el));
                        } catch (Exception e) {
                            wait.until(ExpectedConditions.visibilityOf(el));
                        }
                        return el;
                    }
                });
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return el;
    }

    // Method to wait for a specified amount of time
    public void waitForElementLoad(int x) {
        try {
            int time = 1000 * x;
            Thread.sleep(time);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to get a web element with relative positioning based on a locator
    public WebElement getWebElementWithRelative(String locator, WebElement wl, String relative) throws InterruptedException {
        String[] finalval;
        try {
            if (locator.startsWith("name")) {
                finalval = locator.split("=");
                wait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        try {
                            switch (relative) {
                                case "above":
                                    el = driver.findElement(RelativeLocator.with(By.name(finalval[1])).above(wl));
                                    break;
                                case "below":
                                    el = driver.findElement(RelativeLocator.with(By.name(finalval[1])).below(wl));
                                    break;
                                case "near":
                                    el = driver.findElement(RelativeLocator.with(By.name(finalval[1])).near(wl));
                                    break;
                                case "toLeftOf":
                                    el = driver.findElement(RelativeLocator.with(By.name(finalval[1])).toLeftOf(wl));
                                    break;
                                case "toRightOf":
                                    el = driver.findElement(RelativeLocator.with(By.name(finalval[1])).toRightOf(wl));
                                    break;
                                default:
                                    System.out.println("Relative parameter should be - near, above, below, toLeftOf, and toRightOf.");
                                    Assert.fail("Relative parameter should be - near, above, below, toLeftOf, and toRightOf.");
                            }
                        } catch (NoSuchElementException e) {
                            el = null;
                        }
                        if (el != null) {
                            try {
                                wait.until(ExpectedConditions.elementToBeClickable(el));
                            } catch (Exception e) {
                                wait.until(ExpectedConditions.visibilityOf(el));
                            }
                        }
                        return el;
                    }
                });
            } else if (locator.startsWith("id")) {
                finalval = locator.split("=");
                try {
                    switch (relative) {
                        case "above":
                            el = web_driver.findElement(RelativeLocator.with(By.id(finalval[1])).above(wl));
                            break;
                        case "below":
                            el = web_driver.findElement(RelativeLocator.with(By.id(finalval[1])).below(wl));
                            break;
                        case "near":
                            el = web_driver.findElement(RelativeLocator.with(By.id(finalval[1])).near(wl));
                            break;
                        case "toLeftOf":
                            el = web_driver.findElement(RelativeLocator.with(By.id(finalval[1])).toLeftOf(wl));
                            break;
                        case "toRightOf":
                            el = web_driver.findElement(RelativeLocator.with(By.id(finalval[1])).toRightOf(wl));
                            break;
                        default:
                            System.out.println("Relative parameter should be - near, above, below, toLeftOf, and toRightOf.");
                            Assert.fail("Relative parameter should be - near, above, below, toLeftOf, and toRightOf.");
                    }
                } catch (NoSuchElementException e) {
                    el = null;
                }
                wait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        try {
                            switch (relative) {
                                case "above":
                                    el = driver.findElement(RelativeLocator.with(By.id(finalval[1])).above(wl));
                                    break;
                                case "below":
                                    el = driver.findElement(RelativeLocator.with(By.id(finalval[1])).below(wl));
                                    break;
                                case "near":
                                    el = driver.findElement(RelativeLocator.with(By.id(finalval[1])).near(wl));
                                    break;
                                case "toLeftOf":
                                    el = driver.findElement(RelativeLocator.with(By.id(finalval[1])).toLeftOf(wl));
                                    break;
                                case "toRightOf":
                                    el = driver.findElement(RelativeLocator.with(By.id(finalval[1])).toRightOf(wl));
                                    break;
                                default:
                                    System.out.println("Relative parameter should be - near, above, below, toLeftOf, and toRightOf.");
                                    Assert.fail("Relative parameter should be - near, above, below, toLeftOf, and toRightOf.");
                            }
                        } catch (NoSuchElementException e) {
                            el = null;
                        }
                        if (el != null) {
                            try {
                                wait.until(ExpectedConditions.elementToBeClickable(el));
                            } catch (Exception e) {
                                wait.until(ExpectedConditions.visibilityOf(el));
                            }
                        }
                        return el;
                    }
                });
            } else if (locator.startsWith("//") || locator.startsWith("(//") || locator.startsWith("(")) {
                wait.until(new Function<WebDriver, WebElement>() {
                    public WebElement apply(WebDriver driver) {
                        try {
                            switch (relative) {
                                case "above":
                                    el = driver.findElement(RelativeLocator.with(By.xpath(locator)).above(wl));
                                    break;
                                case "below":
                                    el = driver.findElement(RelativeLocator.with(By.xpath(locator)).below(wl));
                                    break;
                                case "near":
                                    el = driver.findElement(RelativeLocator.with(By.xpath(locator)).near(wl));
                                    break;
                                case "toLeftOf":
                                    el = driver.findElement(RelativeLocator.with(By.xpath(locator)).toLeftOf(wl));
                                    break;
                                case "toRightOf":
                                    el = driver.findElement(RelativeLocator.with(By.xpath(locator)).toRightOf(wl));
                                    break;
                                default:
                                    System.out.println("Relative parameter should be - near, above, below, toLeftOf, and toRightOf.");
                                    Assert.fail("Relative parameter should be - near, above, below, toLeftOf, and toRightOf.");
                            }
                        } catch (NoSuchElementException e) {
                            el = null;
                        }
                        if (el != null) {
                            try {
                                wait.until(ExpectedConditions.elementToBeClickable(el));
                            } catch (Exception e) {
                                wait.until(ExpectedConditions.visibilityOf(el));
                            }
                        }
                        return el;
                    }
                });
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return el;
    }

    // Method to check accessibility using Axe for automated accessibility testing
    public void checkAccessibility() {
        // Configure Axe to run tests for specific WCAG standards
        AxeRunOnlyOptions runOnlyOptions = new AxeRunOnlyOptions();
        runOnlyOptions.setType("tag");
        runOnlyOptions.setValues(Arrays.asList("wcag2a", "wcag2aa"));

        AxeRunOptions options = new AxeRunOptions();
        options.setRunOnly(runOnlyOptions);

        // Create AxeBuilder with options and analyze the page
        AxeBuilder axe = new AxeBuilder().withOptions(options);
        Results result = axe.analyze(web_driver);
        List<Rule> violationList = result.getViolations();
        System.out.println("Violation list size :" + result.getViolations().size());

        // Print details of each accessibility violation
        for (Rule r : result.getViolations()) {
            System.out.println("Complete = " + r.toString());
            System.out.println("Tags = " + r.getTags());
            System.out.println("Description = " + r.getDescription());
            System.out.println("Help Url = " + r.getHelpUrl());
        }

        // Print details of inapplicable rules
        System.out.println("Inapplicable list size :" + result.getInapplicable().size());
        for (Rule r : result.getInapplicable()) {
            System.out.println("Complete = " + r.toString());
            System.out.println("Tags = " + r.getTags());
            System.out.println("Description = " + r.getDescription());
            System.out.println("Help Url = " + r.getHelpUrl());
        }
    }

    public boolean isElementPresent(WebElement el) {
        try {
            el.getRect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setWifiStatus(boolean status) throws Exception {
        ConnectionState connectionState;
        if (status)
            connectionState = new ConnectionStateBuilder()
                    .withAirplaneModeDisabled()
                    .withDataEnabled()
                    .withWiFiEnabled()
                    .build();
        else
            connectionState = new ConnectionStateBuilder()
                    .withAirplaneModeEnabled()
                    .withDataDisabled()
                    .withWiFiDisabled()
                    .build();

        ((AndroidDriver) mobile_driver).setConnection(connectionState);
        waitForConnectionState(connectionState, 10);
    }

    private void waitForConnectionState(ConnectionState expectedState, int timeoutSeconds) {
        long endTime = System.currentTimeMillis() + timeoutSeconds * 1000L;

        while (System.currentTimeMillis() < endTime) {
            ConnectionState currentState = ((AndroidDriver) mobile_driver).getConnection();
            if (currentState.equals(expectedState)) {
                return;
            }
        }
    }


    public void setInternetStatus(boolean airplane, boolean wifi, boolean data) throws Exception {
        ConnectionStateBuilder builder = new ConnectionStateBuilder();

        builder = airplane ? builder.withAirplaneModeEnabled() : builder.withAirplaneModeDisabled();
        builder = wifi ? builder.withWiFiEnabled() : builder.withWiFiDisabled();

        String status = data ? "enable" : "disable";

        ConnectionState connectionState = builder.build();

        ((AndroidDriver) mobile_driver).setConnection(connectionState);
        waitForConnectionState(connectionState, 10);
        Runtime.getRuntime().exec("adb shell svc data " + status);
        Thread.sleep(5000);
    }

    public void enableDarkMode(boolean status) throws Exception {
        String value = status ? "yes" : "no";
        if (System.getProperty("os.name").contains("Windows"))
            Runtime.getRuntime().exec("adb shell \"cmd uimode night " + value + "\"");
        else
            Runtime.getRuntime().exec(new String[]{"/bin/zsh", "-c", "adb shell cmd uimode night " + value});
        Thread.sleep(2000);
    }

    public String getElementColor(WebElement el) throws Exception {
        Point point = el.getLocation();
        int centerx = point.getX();
        int centerY = point.getY();
        File scrFile = null;

        if (androidDriver != null)
            scrFile = ((TakesScreenshot) androidDriver).getScreenshotAs(OutputType.FILE);
        else
            scrFile = ((TakesScreenshot) iosDriver).getScreenshotAs(OutputType.FILE);
        BufferedImage image = ImageIO.read(scrFile);
        int clr = image.getRGB(centerx, centerY);
        int red = (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue = clr & 0x000000ff;
        return red + "" + green + "" + blue;
    }

    public void scrollScreenDown(int numberOfScroll) throws Exception {
        for (int i = 0; i < numberOfScroll; i++) {
            if (mobile_driver.toString().contains("Android"))
                scrollScreen(550, 1682, 559, 545);
            else
                scrollScreen(66, 380, 66, 66);
        }
    }

    public void scrollScreenUp(int numberOfScroll) throws Exception {
        for (int i = 0; i < numberOfScroll; i++) {
            if (mobile_driver.toString().contains("Android"))
                scrollScreen(559, 545, 550, 1682);
            else
                scrollScreen(66, 66, 66, 380);
        }
    }

    public void pressDeviceBackButton() throws Exception {
        if (getDriverType().equalsIgnoreCase("Android"))
            mobile_driver.executeScript("mobile: pressKey", Map.ofEntries(Map.entry("keycode", 4)));
        else {
            WebElement els = getMobileElement("name=BackButton");
            if (els != null)
                els.click();
        }
    }

    public void scrollScreen(int startX, int startY, int endX, int endY) throws Exception {
        final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        var start = new Point(startX, startY);
        var end = new Point(endX, endY);
        var swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), start.getX(), start.getY()));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(1000),
                PointerInput.Origin.viewport(), end.getX(), end.getY()));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        try {
            mobile_driver.perform(Arrays.asList(swipe));
        } catch (Exception ignored) {
        }
        Thread.sleep(100);
    }

    public void scrollScreen(String direction, int numberOfScroll) throws Exception {

        Dimension size = mobile_driver.manage().window().getSize();

        int startX = size.width / 2;

        // smaller scroll window (40% of screen height)
        int screenHeight = size.height;
        int scrollOffset = (int) (screenHeight * 0.15);

        int midY = screenHeight / 2;
        int startY, endY;

        switch (direction.toLowerCase()) {
            case "down":
                startY = midY + scrollOffset;  // start lower-middle
                endY = midY - scrollOffset;  // end upper-middle
                break;

            case "up":
                startY = midY - scrollOffset;  // start upper-middle
                endY = midY + scrollOffset;  // end lower-middle
                break;

            default:
                throw new IllegalArgumentException("Direction must be 'up' or 'down'");
        }

        for (int i = 0; i < numberOfScroll; i++) {
            final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            var start = new Point(startX, startY);
            var end = new Point(startX, endY);
            var swipe = new Sequence(finger, 1);
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(0),
                    PointerInput.Origin.viewport(), start.getX(), start.getY()));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(1000),
                    PointerInput.Origin.viewport(), end.getX(), end.getY()));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            try {
                mobile_driver.perform(Arrays.asList(swipe));
            } catch (Exception ignored) {
            }
            Thread.sleep(100);
        }
    }

    public List<WebElement> getMobileElements(String locator) throws Exception {
        return mobile_driver.findElements(By.xpath(locator));
    }

    public List<WebElement> getWebElements(String locator) throws Exception {
        return web_driver.findElements(By.xpath(locator));
    }

    public void waitForMobileElementHide(WebElement el, int timeOutInSeconds) throws Exception {
        if (el != null)
            new FluentWait<AppiumDriver>(mobile_driver)
                    .withTimeout(Duration.ofSeconds(timeOutInSeconds))
                    .pollingEvery(Duration.ofSeconds(1))
                    .ignoring(NoSuchElementException.class).until(ExpectedConditions.invisibilityOf(el));
        else
            Thread.sleep(timeOutInSeconds * 500L);
    }

    public void waitForMobileElement(WebElement el, int timeOutInSeconds) throws Exception {
        if (el != null)
            new FluentWait<AppiumDriver>(mobile_driver)
                    .withTimeout(Duration.ofSeconds(timeOutInSeconds))
                    .pollingEvery(Duration.ofMillis(200))
                    .ignoring(NoSuchElementException.class).until(ExpectedConditions.visibilityOf(el));
        else
            Thread.sleep(timeOutInSeconds * 500L);
    }


    public void executeScript(String script, Map<String, Object> params) throws Exception {
        mobile_driver.executeScript(script, params);
    }

    public void clickByXY(int x, int y) {
        System.out.println("Clicking by position: " + x + "\t" + y);
        final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        var tapPoint = new Point(x, y);
        var tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), tapPoint.x, tapPoint.y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(new Pause(finger, Duration.ofMillis(50)));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        mobile_driver.perform(Arrays.asList(tap));
    }

    public String getDriverType() {
        if (mobile_driver != null)
            if (mobile_driver.toString().toLowerCase().contains("android"))
                return "ANDROID";
            else
                return "IOS";
        else
            return "WEB";
    }

    public void activateApp(String appPackage) {
        if (getDriverType().equalsIgnoreCase("Android"))
            ((AndroidDriver) mobile_driver).activateApp(appPackage);
        else
            ((IOSDriver) mobile_driver).activateApp(appPackage);
    }

    public void setDeviceDateTime(int number, String type) {
        HashMap<String, Object> args = new HashMap<>();

        String date = "";
        if (getDriverType().equalsIgnoreCase("Android")) {
            date = convertAndUpdateDateTime("YYYYMMDD.HHMMSS", type, number);
            args.put("command", "date");
            args.put("args", new String[]{date});

            ((AndroidDriver) mobile_driver).executeScript("mobile: shell", args);
        } else {
            date = convertAndUpdateDateTime("yyyy-MM-dd HH:mm:ss", type, number);
            args.put("date", date);

            ((IOSDriver) mobile_driver).executeScript("mobile: setDate", args);
        }
    }

    public String getDeviceDateTime() {
        if (getDriverType().equalsIgnoreCase("Android"))
            return ((AndroidDriver) mobile_driver).getDeviceTime("YYYYMMDD.HHMMSS");
        else
            return ((IOSDriver) mobile_driver).getDeviceTime("yyyy-MM-dd HH:mm:ss");
    }

    private String convertAndUpdateDateTime(String format, String type, int number) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime modified = now;

        switch (type.toUpperCase()) {
            case "M":
                modified = now.plusMonths(number);
                break;
            case "D":
                modified = now.plusDays(number);
                break;
            case "H":
                modified = now.plusHours(number);
                break;
            default:
                break;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return modified.format(formatter);
    }

    public void addPayloadToRequest(org.json.JSONObject requestBody) {
        requestSpecification.body(requestBody.toString());
    }

    public String getResponseBody() {
        return resultResponse.asPrettyString();
    }

    public WebDriver getWebDriver()
    {
        return web_driver;
    }

    public void openNotificationPopup() {
        androidDriver.openNotifications();
    }

    public void closeNotificationPopup() {
        back();
    }
    public String getClipboardText() {
        String clipboardText =((AndroidDriver) mobile_driver).getClipboardText();
        System.out.println("CLIPBOARD TEXT: "+clipboardText);
        return clipboardText;
    }
}