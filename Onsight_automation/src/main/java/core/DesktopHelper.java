package core;
import org.openqa.selenium.WebElement;
import java.io.IOException;
/**
 * This interface defines the methods for interacting with desktop application elements.
 */
public interface DesktopHelper {

	/**
	 * Clicks on a specified web element.
	 * @param ele The web element to be clicked.
	 * @throws Exception If an error occurs during the click operation.
	 */
	void clickOn(WebElement ele) throws Exception;

	/**
	 * Enters a value into a specified input field.
	 * @param el The web element representing the input field.
	 * @param value The value to be entered into the input field.
	 * @throws InterruptedException If the thread is interrupted during the operation.
	 * @throws IOException If an IO error occurs during the operation.
	 */
	void sendKeys(WebElement el, String value) throws InterruptedException, IOException;

	/**
	 * Verifies the title of the current web page.
	 * @param Expectedtitle The expected title of the web page.
	 */
	void verifyTitle(String Expectedtitle);

	/**
	 * Verifies the visible text of a specified web element.
	 * @param el The web element whose text is to be verified.
	 * @param Expectedtext The expected text to be displayed.
	 */
	void verifyText(WebElement el, String Expectedtext);

	/**
	 * Retrieves a web element based on a locator.
	 * @param locator The locator used to find the web element.
	 * @return The web element instance.
	 * @throws InterruptedException If the thread is interrupted during the operation.
	 */
	WebElement getDesktopelement(String locator) throws InterruptedException;

	/**
	 * Enters a specified text.
	 * @param s The text to be entered.
	 */
	void enterText(String s);
}
