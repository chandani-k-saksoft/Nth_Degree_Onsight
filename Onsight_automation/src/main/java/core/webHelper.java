package core;

import java.io.IOException;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public interface webHelper {
    // Closes all open browser windows.
    void closeAllWindows();

    // Switches to the last opened browser tab.
    void switchToLastTab();

    // Waits for an iframe specified by the frame locator to be fully loaded.
    void getIframeLoaded(String framelocator);

    // Waits for the page to be ready for interactions.
    void waitforPageToBeReady() throws Exception;

    // Retrieves the title of the current page.
    String getTitle();

    // Opens the specified URL based on the environment.
    void openURL(String environment);

    // Opens the specified URL with an additional exception handling.
    void openURLV2(String environment) throws Exception;

    // Retrieves the current URL of the page.
    void getURL(String URL);

    // Refreshes the current page.
    void pageRefresh() throws Exception;

    // Retrieves the current URL of the page.
    String currentURL();

    // Captures a screenshot of a specific web element.
    String captureScreenShotForElement(WebElement ele);

    // Captures a full-page screenshot.
    String captureFullScreenShot(WebDriver driver);

    // Clicks on the specified web element.
    void clickOn(WebElement ele) throws Exception;

    // Sends a specified value to a web element and handles possible interruptions and IO exceptions.
    void sendKeys(WebElement el, String value) throws InterruptedException, IOException;

    // Verifies that the page title matches the expected title.
    void verifyTitle(String Expectedtitle);

    // Verifies that the text of a web element matches the expected text.
    void verifyText(WebElement el, String Expectedtext);

    // Retrieves a web element based on a specified locator.
    WebElement getWebElement(String locator) throws InterruptedException;

    List<WebElement> getWebElements(String locator) throws Exception;

    // Retrieves advertisements on the page based on a given string.
    String getAdsOnPage(String string);

    // Gets the value of a specified attribute of a web element.
    String getAttribute(WebElement el, String attributename);

    // Validates the data tracking research page for accuracy.
    boolean validateDataTrackResearchPage();

    // Retrieves the text from a web element.
    String getText(WebElement wl);

    // Clicks on a web element using JavaScript.
    void javascriptButtonClick(WebElement webElement);

    // Checks if an element specified by a locator is present on the page.
    boolean isElementPresent(WebElement locator) throws Exception;

    // Sends a keyboard key event to a web element.
    void sendKeyboardKeys(WebElement el, Keys k) throws InterruptedException;

    // Polls the current time based on a class name, timestamp, and timeout period.
    String pollCurrentTime(String className, String timeStamp, String timeOut) throws InterruptedException;

    // Moves to a specified web element.
    void moveOn(WebElement el);

    // Handles waits for Angular-specific elements.
    void handleAngularWait();

    // Handles Angular-specific main page waits.
    void handleAngularMain();

    // Waits for an element specified by a locator to become clickable.
    void waitforElementToBeClickable(String locator) throws InterruptedException;

    // Verifies that an element specified by a locator is present on the page.
    void verifyElementToBePresent(String Locators) throws InterruptedException;

    // Verifies that an element specified by a locator is not present on the page.
    void verifyElementNotToBePresent(String Locators) throws InterruptedException;

    // Waits for the entire page to load.
    void waitForCompletePageLoad();

    // Waits for a specific element to load, based on a timeout value.
    void waitForElementLoad(int x);

    // Waits for a web element to no longer be visible.
    void waitForElementNotToBeVisible(WebElement wl);

    // Waits for a frame specified by the frame name to be available and switches to it.
    void waitForFrameToBeAvailableAndSwitchToIt(String frameName);

    // Waits for the specified text to be present in a web element.
    void waitForTextToBePresentInElement(WebElement wl, String Textanme);

    // Waits until a web element is visible.
    void waitUntilTheElementIsVisible(WebElement wl);

    // Waits until a web element is no longer selected.
    void waitUntilTheElementIsNotSelected(WebElement wl);

    // Waits until a web element's attribute value changes to the specified value.
    void waitUntilTheAttributeValueChangeToAnElement(WebElement wl, String AttributeName, String AttributeValue);

    // Waits until an alert is not displayed.
    void waitUntilAlertNotDisplaying() throws InterruptedException;

    // Waits until an alert is displayed.
    void waitUntilTheAlertIsDisplaying();

    // Waits until a new browser window is opened.
    void waitUntilANewWindowOpen();

    // Unchecks a specified checkbox element.
    void uncheckTheCheckBox(WebElement el);

    // Verifies that the current URL matches the expected URL.
    void verifyCurrentURL(String ExpectedURL);

    // Checks if an alert is present.
    boolean verifyAlertPresent();

    // Retrieves the message from an alert.
    String returnAlertMessage();

    // Waits until all images on the page are fully loaded.
    void waitUntilAllImagesAreLoaded();

    // Checks if at least one option in a list is selected.
    boolean returnListHasSomeOptionSelected(WebElement wl);

    // Returns the number of options in a list.
    int returnListHasNumberOfOptions(WebElement wl);

    // Checks if a specific option in a list has the given value selected.
    boolean returnListHasOptionWithValueSelected(WebElement wl, String Value);

    // Returns a flag indicating if multiple options can be selected for a given WebElement (typically a multi-select dropdown)
    boolean returnAllowSelectionOfMultipleOptionFlag(WebElement wl);

    // Returns a flag indicating if a given WebElement is empty
    boolean returnElementEmptyValueFlag(WebElement el);

    // Returns the tag name of the given WebElement
    String returnElementTagName(WebElement el);

    // Returns the value of a specified attribute for the given WebElement
    String returnElementAttrubuteValue(WebElement el, String AttributeName);

    // Returns the value of a specified CSS property for the given WebElement
    String returnCssPropertyName(WebElement el, String Propertyname);

    // Returns the checked status (true/false) of the given WebElement (e.g., a checkbox or radio button)
    boolean ReturnElementCheckStatus(WebElement el);

    // Returns the value selected in a list element (e.g., a dropdown)
    String returnValueSelectedInList(WebElement wl);

    // Returns the count of options in a list element
    int returnOptionsCountsInList(WebElement wl);

    // Returns the count of web elements located by a specific locator, with potential waiting involved
    int getWebElementsCount(String locator) throws InterruptedException;

    // Selects an option by its value attribute in a dropdown or list element
    void selectOptionByValue(WebElement el, String value);

    // Selects an option by its visible text in a dropdown or list element
    void selectOptionByText(WebElement el, String Text);

    // Selects an option by its index in a dropdown or list element
    void selectOptionByIndex(WebElement el, String index);

    // Performs a specified action on the given WebElement
    void actionWithElement(WebElement el, String ActionType);

    // Drags an element and drops it onto another element
    void dragAndDropElement(WebElement el, WebElement el1);

    // Switches to a window with a specified title
    void switchWindowTitled(String windowTitle);
    void closeWindowTitled(String windowTitle);

    // Switches to a window by its index (e.g., 0 for the first window)
    void switchWindowByIndex(String index);

    // Switches to the parent window from a child window
    void switchToParentWindow();

    // Switches to the parent page if within an iframe
    void switchToParentPage();

    // Closes the current window
    void closeCurrentWindows();

    // Closes a window with a specific title
    void closeWindowByTitle(String windowTitle);

    // Closes a window by its index
    void closeWindowByIndex(String index);

    // Navigates to a specified URL
    void goTo(String url);

    // Switches to the parent frame from a nested iframe
    void switchToParentFrame();

    // Switches to a specific iframe by its name
    void switchToFrame(String FrameName);

    // Switches to an iframe by its index
    void switchToFrameByIndex(String index);

    // Switches to an iframe using a WebElement reference
    void switchToFrameByElement(WebElement wl);

    // Deletes cookies with a specific name
    void deleteCookiesWithName(String Cookiesname);

    // Deletes all cookies
    void deleteAllCookies();

    // Refreshes the current page
    void refresh();

    // Navigates forward in the browser history
    void forward();

    // Navigates backward in the browser history
    void back();

    // Accepts an alert dialog
    void acceptAlert();

    // Cancels (dismisses) an alert dialog
    void cancelAlert();

    // Clicks on an element if it is visible, throwing an exception if the element is not visible
    void clickOnIfElementVisible(WebElement el) throws Exception;

    // Clears the value of an attribute using JavaScript
    void clearAttributeValue(WebElement element);

    // Clears the text content of an element
    void clearText(WebElement element);

    // Verifies if clicking a link opens a new tab or window
    boolean verifyLinksOpenNewTanOrWindows(WebElement element);

    // Sets an attribute value using JavaScript
    void setAttributeUsingJS(WebElement element, String attName, String attValue);

    // Verifies if the value of an element is less than a specified expected value
    boolean verifyElementValueLessThan(WebElement element, int ExpValue);

    // Verifies if the value of an element is greater than a specified expected value
    boolean verifyElementValueGreaterThan(WebElement element, int ExpValue);

    // Verifies if the value of an element is less than or equal to a specified expected value
    boolean verifyElementValueLessThanOrEqualTo(WebElement element, int ExpValue);

    // Verifies if the value of an element is greater than or equal to a specified expected value
    boolean verifyElementValueGreaterThanOrEqualTo(WebElement element, int ExpValue);

    // Sends keys to an element using JavaScript, with potential exceptions for waiting and IO operations
    void sendKeysUsingJS(WebElement el, String value) throws InterruptedException, IOException;

    // Clicks on an element to open it in a new window and switches to that window
    void clickOnElementToOpenInNewWindowAndSwitchToIt(WebElement element);

    // Sends keyboard keys to the browser using the Actions class
    void sendKeyBoardKeysUsingActions(Keys key) throws Exception;

    // Presses the Shift and Delete keys simultaneously
    void pressShiftDelete() throws Exception;

    public void checkTheCheckBox(WebElement el);

    // Retrieves a web element with a relative locator based on a reference WebElement
    WebElement getWebElementWithRelative(String locator, WebElement wl, String relative) throws InterruptedException;

    void scrollIntoView(WebElement el) throws InterruptedException;

    boolean returnElementEmptyTextFlag(WebElement el);

    void waitForTitleContains(String Title);

    void waitForElementBeVisible(WebElement wl, int timeoutInSeconds) throws Exception;

    WebDriver getWebDriver();
}
