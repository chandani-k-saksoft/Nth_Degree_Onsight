package core;

import io.appium.java_client.windows.WindowsDriver;

/**
 * This interface defines a method for initializing a WindowsDriver for desktop applications.
 */
public interface DesktopDriver {
	/**
	 * Initializes the WindowsDriver with the given base URL.
	 *
	 * @param baseUrl the URL to use for initializing the driver
	 * @return a WindowsDriver instance
	 * @throws Exception if there is an issue during initialization
	 */
	WindowsDriver desktopInit(String baseUrl) throws Exception;
}
