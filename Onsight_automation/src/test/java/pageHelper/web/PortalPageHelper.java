package pageHelper.web;

import core.BaseDriverHelper;
import core.webHelper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.text.StringEscapeUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pageHelper.BDDDriver;
import utils.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import static utils.AccessibilitySniffer.driver;


public class PortalPageHelper {
    private static List<String> messages = new ArrayList<>();
    private static PropertyReader prop = new PropertyReader();
    public webHelper webDriver;
    public XmlReader locators;
    int retryIndex = 3;
    private BDDDriver driverInstance;

    public PortalPageHelper(BDDDriver contextSteps, DriverController driverController) throws Exception {
        this.driverInstance = contextSteps;
        if (BDDDriver.getWebDriver() != null)
            webDriver = new BaseDriverHelper(BDDDriver.getWebDriver());
        locators = new XmlReader("src//test//resources//locators//Portal_LoginScreen.xml");
    }

    // Constructor initializing WebActions with driver controller only
    public PortalPageHelper(DriverController driverController) {
        webDriver = new BaseDriverHelper(driverController.getDriver());
        locators = new XmlReader("src//test//resources//locators//Portal_LoginScreen.xml");
    }

    private static boolean isInteger(String str) {
        return str != null && str.matches("-?\\d+");
    }

    private WebElement getPortalLocator(String locator) throws Exception {
        return webDriver.getWebElement(locators.getMobileLocator("//locators/portal/" + locator));
    }

    private WebElement getPortalLocator(String locator, String byReplace, String toReplace) throws Exception {
        locator = locators.getMobileLocator("//locators/portal/" + locator);
        locator = locator.replace(byReplace, toReplace);
        return webDriver.getWebElement(locator);
    }


    @Given("I am at OnSight portal login screen")
    public void i_am_at_on_sight_portal_login_screen() throws Exception {
        Assert.assertTrue(webDriver.isElementPresent(getPortalLocator("portalWelcomeText")));
    }

    @Given("I verify Email field available on the page")
    public void i_verify_email_field_available_on_the_page() throws Exception {
        Assert.assertTrue(webDriver.isElementPresent(getPortalLocator("emailField")));
    }

    @Given("I login to the application with valid user")
    public void i_login_to_the_application_with_valid_user() throws Exception {
        webDriver.sendKeys(getPortalLocator("emailField"), prop.readProperty("SystemAdminUsername"));
        webDriver.sendKeys(getPortalLocator("passwordField"), prop.readProperty("orgCorePassword"));
        webDriver.clickOn(getPortalLocator("loginButton"));
    }

    @Given("I verify I am at dashboard page")
    public void i_verify_i_am_at_dashboard_page() throws Exception {
     Assert.assertTrue(webDriver.isElementPresent(getPortalLocator("dashboardButton")));
        Assert.assertTrue(webDriver.isElementPresent(getPortalLocator("showNumberText")));
        Assert.assertTrue(webDriver.isElementPresent(getPortalLocator("selectReportText")));
    }

    @Given("I verify Password field available on the page")
    public void i_verify_password_field_available_on_the_page() throws Exception {
        Assert.assertTrue(webDriver.isElementPresent(getPortalLocator("passwordField")));
    }


    @Given("I verify Login button available on the page")
    public void i_verify_login_button_available_on_the_page() throws Exception {
        Assert.assertTrue(webDriver.isElementPresent(getPortalLocator("loginButton")));
    }

    @And("I navigate to manage user screen")
    public void iNavigateToManageUserScreen() throws Exception {
        webDriver.clickOn(getPortalLocator("manageUserButton"));
    }

    @Given("I Verify I am at manage user list")
    public void i_verify_i_am_at_manage_user_list() throws Exception {
       Assert.assertTrue(webDriver.isElementPresent(getPortalLocator("securityLevelsBtn")));
        Assert.assertTrue(webDriver.isElementPresent(getPortalLocator("importBtn")));
        Assert.assertTrue(webDriver.isElementPresent(getPortalLocator("exportBtn")));
    }

    @And("I navigate to add user screen")
    public void iNavigateToAddUserScreen() throws Exception {
        webDriver.clickOn(getPortalLocator("addButton"));
    }

    @Given("I check IsActive status")
    public void i_check_is_active_status() throws Exception{
        System.out.println("SELECTED: "+getPortalLocator("onToggleBtn").isSelected());
        Assert.assertTrue(getPortalLocator("onToggleBtn").isSelected());
    }


    @And("I enter given detail for user")
    public void iEnterGivenDetailForUser(DataTable dataTable) throws Exception {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            for (String id : row.keySet()) {
                String value = row.get(id);
                if (value.contains("RANDOM"))
                    value = value.replace("RANDOM", new CommonVariables().getRandomNumber());
                if (id.equalsIgnoreCase("email"))
                    driverInstance.setCommonVariables("ADDED_USER", value);
                webDriver.sendKeys(getPortalLocator(id), value);
            }
        }
    }


    @And("I select {string} as {string} dropdown")
    public void iSelectAsDropdown(String value, String dropdown) throws Exception{
        webDriver.clickOn(getPortalLocator(dropdown));
        webDriver.clickOn(webDriver.getWebElement("//li[@role='option']/span[text()='"+value+"']"));
    }

    @And("I click on add user button")
    public void iClickOnAddUserButton() throws Exception{
        webDriver.clickOn(getPortalLocator("addBtn"));
    }

    @And("I apply {string} filter for {string} value")
    public void iApplyFilterForValue(String filter, String value) throws Exception {
        webDriver.clickOn(webDriver.getWebElement("//a[@title='" + filter + " Filter Menu']"));
        value = driverInstance.getCommonVariables(value);
        webDriver.sendKeys(webDriver.getWebElement("//input[@aria-label='" + filter + " Filter']"), value);
        webDriver.clickOn(getPortalLocator("filterBtn"));
    }

    @And("I scroll left side of the screen")
    public void iScrollLeftSideOfTheScreen() throws Exception {
        webDriver.scrollIntoView(getPortalLocator("editBtn"));
    }

}
