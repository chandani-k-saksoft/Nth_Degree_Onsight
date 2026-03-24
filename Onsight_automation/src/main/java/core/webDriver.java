package core;

import org.openqa.selenium.WebDriver;
import org.zaproxy.clientapi.core.ClientApi;

public interface webDriver {
	/**
	 * Initializes the URL on the specified browser and optionally runs Selenium Grid and Node if Grid Mode is enabled.
	 *
	 * @param browser Name of the browser (e.g., Chrome, Firefox, IE, Edge).
	 * @param BaseURL URL of the application to be tested (e.g., https://360logica.com).
	 * @param Grid Boolean option to enable the Grid Mode (True or False). Default value is False.
	 * @param proxy Boolean option to enable proxy settings.
	 * @return WebDriver instance for the specified browser.
	 * @throws Exception if an error occurs during initialization.
	 */
	WebDriver webInit(String browser, String BaseURL, Boolean Grid, Boolean proxy) throws Exception;

	/**
	 * Initializes the URL on the specified browser with the option to run in headless mode and optionally runs Selenium Grid and Node if Grid Mode is enabled.
	 *
	 * @param browser Name of the browser (e.g., Chrome, Firefox, IE, Edge).
	 * @param BaseURL URL of the application to be tested (e.g., https://360logica.com).
	 * @param Grid Boolean option to enable the Grid Mode (True or False). Default value is False.
	 * @param proxy Boolean option to enable proxy settings.
	 * @param headless Boolean option to run the browser in headless mode.
	 * @return WebDriver instance for the specified browser.
	 * @throws Exception if an error occurs during initialization.
	 */
	org.openqa.selenium.WebDriver webInit(String browser, String BaseURL, Boolean Grid, Boolean proxy, Boolean headless) throws Exception;

	/**
	 * Returns an instance of the ClientApi for interacting with the ZAP scanner.
	 *
	 * @return ClientApi instance.
	 */
	ClientApi returnZapScanner();
}
