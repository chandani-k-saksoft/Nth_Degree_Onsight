package core;

import io.appium.java_client.AppiumDriver;
/**
 * This interface defines a contract for initializing a mobile driver.
 */
public interface MobileDriver {
	/**
     * Initializes an AppiumDriver for a mobile application.
     *
     * @param env     environment
     * @param device     The device name or ID on which the app is to be tested.
     * @param appPackage The package name of the application.
     * @param platform   The platform (e.g., Android or iOS) on which the app is running.
     * @param installApp Install App
     * @return An instance of AppiumDriver for interacting with the mobile application.
     * @throws Exception If any error occurs during driver initialization.
     */
	AppiumDriver mobileInit(String env, String device, String appPackage, String platform, String automationName, String version, String grantPermission, String appium_url, Boolean installApp) throws Exception;
}
