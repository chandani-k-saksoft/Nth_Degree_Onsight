package utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebDriver;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pageHelper.mobile.*;
import pageHelper.web.PortalPageHelper;


public class PageController {

    public static final ThreadLocal<LoginScreenHelperBDD> loginScreen = new InheritableThreadLocal<>();
    public static final ThreadLocal<CommonHelperBDD> commonHelperBDD = new InheritableThreadLocal<>();

    public void initPage(WebDriver driver) {
        DriverController driverController = new DriverController();
        driverController.setDriver(driver);
        PortalPageHelper PPH = new PortalPageHelper(driverController);

    }

    public void initPage(WindowsDriver driver) {
        DriverController driverController = new DriverController();
        driverController.setDesktopDriver(driver);
    }

    public void initPage(AppiumDriver driver) throws Exception {
        DriverController driverController = new DriverController();
        driverController.setMobileDriver(driver);

        LoginScreenHelperBDD LSH = new LoginScreenHelperBDD(driverController);
        CommonHelperBDD CHD = new CommonHelperBDD(driverController);

        loginScreen.set(LSH);
        commonHelperBDD.set(CHD);
    }

    public void initPage(RequestSpecification dr, Response response) throws Exception {
    }
}