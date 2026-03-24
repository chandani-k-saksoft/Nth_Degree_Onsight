package utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebDriver;


public class DriverController {

	static String TestType="BDD";
	WebDriver driver;
	WindowsDriver DesktopDriver;
	AppiumDriver MobileDriver;
	public void setDriver(WebDriver dr)
	{
		driver=dr;
		TestType="TDD";
	}
	public WebDriver getDriver()
	{
		return driver;
	}

	public void setDesktopDriver(WindowsDriver dr)
	{
		DesktopDriver=dr;
		TestType="TDD";
	}
	public WindowsDriver getDesktopDriver()
	{
		return DesktopDriver;
	}
	public void setMobileDriver(AppiumDriver dr)
	{
		MobileDriver=dr;
		TestType="TDD";
	}
	public AppiumDriver getMobileDriver()
	{
		return MobileDriver;
	}
	public String getTestType()
	{
		return TestType;
	}
}