package core;

import io.appium.java_client.android.nativekey.AndroidKey;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MobileHelper {

    /**
     * Clicks on the specified web element.
     *
     * @param ele The web element to be clicked.
     * @throws Exception If there is an error during the click action.
     */
    void clickOn(WebElement ele) throws Exception;

    /**
     * Enters the specified value into the input field of the given web element.
     *
     * @param el    The web element representing the input field.
     * @param value The value to be entered into the input field.
     * @throws InterruptedException If the operation is interrupted.
     * @throws IOException          If there is an error during the operation.
     */
    void sendKeys(WebElement el, String value) throws InterruptedException, IOException;

    /**
     * Verifies that the current page title matches the expected title.
     *
     * @param Expectedtitle The expected page title.
     */
    void verifyTitle(String Expectedtitle);

    /**
     * Verifies that the text displayed in the specified web element matches the expected text.
     *
     * @param el           The web element under test.
     * @param Expectedtext The expected text to be displayed in the web element.
     */
    void verifyText(WebElement el, String Expectedtext);

    /**
     * Retrieves the web element based on the specified locator.
     *
     * @param locator The locator of the web element.
     * @return The web element instance.
     * @throws InterruptedException If the operation is interrupted.
     */
    WebElement getMobileElement(String locator) throws InterruptedException;

    /**
     * Sends the specified Android key event.
     *
     * @param key The Android key to be sent.
     * @throws InterruptedException If the operation is interrupted.
     * @throws IOException          If there is an error during the operation.
     */
    void sendKeys(AndroidKey key) throws InterruptedException, IOException;

    String getAndroidPageSource() throws InterruptedException, IOException;

    boolean isElementPresent(WebElement el) throws InterruptedException;

    void setWifiStatus(boolean status) throws Exception;

    String getText(WebElement el) throws Exception;

    String getAttribute(WebElement el, String attributeName);

    void enableDarkMode(boolean status) throws Exception;

    String getElementColor(WebElement el) throws Exception;

    void scrollScreenDown(int numberOfScroll) throws Exception;

    void scrollScreen(String direction, int numberOfScroll) throws Exception;

    void scrollScreenUp(int numberOfScroll) throws Exception;

    void pressDeviceBackButton() throws Exception;

    List<WebElement> getMobileElements(String locator) throws Exception;

    void scrollScreen(int startX, int startY, int endX, int endY) throws Exception;

    void waitForMobileElementHide(WebElement el, int timeOutInSeconds) throws Exception;

    void executeScript(String script, Map<String, Object> params) throws Exception;

    void clickByXY(int x, int y);

    String getDriverType();

    void setInternetStatus(boolean airplane, boolean wifi, boolean data) throws Exception;

    void waitForMobileElement(WebElement el, int timeOutInSeconds) throws Exception;

    void activateApp(String appPackage);

    void setDeviceDateTime(int number, String type);

    String getDeviceDateTime();

    void openNotificationPopup();

    void closeNotificationPopup();

    String getClipboardText();
}
