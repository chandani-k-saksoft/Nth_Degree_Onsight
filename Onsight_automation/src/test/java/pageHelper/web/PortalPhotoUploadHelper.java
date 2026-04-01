package pageHelper.web;

import core.BaseDriverHelper;
import core.webHelper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pageHelper.BDDDriver;
import utils.CommonVariables;
import utils.DriverController;
import utils.PropertyReader;
import utils.XmlReader;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utils.AccessibilitySniffer.driver;


public class PortalPhotoUploadHelper {
    private static List<String> messages = new ArrayList<>();
    private static PropertyReader prop = new PropertyReader();
    public webHelper webDriver;
    public XmlReader locators;
    int retryIndex = 3;
    WebDriverWait wait;
    private BDDDriver driverInstance;

    public PortalPhotoUploadHelper(BDDDriver contextSteps, DriverController driverController) throws Exception {
        this.driverInstance = contextSteps;
        if (BDDDriver.getWebDriver() != null)
            webDriver = new BaseDriverHelper(BDDDriver.getWebDriver());
        locators = new XmlReader("src//test//resources//locators//Portal_PhotoUploadScreen.xml");
    }

    // Constructor initializing WebActions with driver controller only
    public PortalPhotoUploadHelper(DriverController driverController) {
        webDriver = new BaseDriverHelper(driverController.getDriver());
        locators = new XmlReader("src//test//resources//locators//Portal_PhotoUploadScreen.xml");
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

    @And("I enter show number {string}")
    public void iEnterShowNumber(String showNumber) throws Exception {
        webDriver.sendKeys(webDriver.getWebElement("//input[@class='k-input-inner']"), showNumber);
        webDriver.clickOn(webDriver.getWebElement("//li[@role='option']/span[text()='"+showNumber+"']"));

    }

    @And("I select {string} from select report")
    public void iSelectFromSelectReport(String reportName) throws Exception {

        By dropdown = By.xpath("selectReportField");
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(dropdown));

        Select select = new Select(element);
        select.selectByVisibleText(reportName);
    }

    @And("I click on {string} login button")
    public void iClickOnLoginButton(String buttonText) throws Exception {
        webDriver.clickOn(getPortalLocator("submitBtn"));

    }

    @And("I select {string} as {string} dropdown field")
    public void iSelectAsDropdownField(String value, String dropdown) throws Exception {
            webDriver.clickOn(getPortalLocator(dropdown));
            webDriver.clickOn(webDriver.getWebElement("//li[@role='option']/span[text()='"+value+"']"));
    }

    @And("I click on {string} image count of job number {string}")
    public void iClickOnImageCountOfJobNumber(String column, String jobNumber) throws Exception {
        Thread.sleep(5000);
//        String locator = "//tr[td[normalize-space()='"+jobNumber.trim()+"']]/td[@aria-colindex='{{index}}']";
        String locator = locators.getMobileLocator("//locators/portal/jobPhototableCol");
        locator = locator.replace("{{jobNumber}}", jobNumber);
        String index = "1";
        switch (column) {
            case "Install Freight":
                index = "3";
                break;
            case "Install Freight Content":
                index = "4";
                break;
            case "Install Progress":
                index = "5";
                break;
            case "Show Ready":
                index = "6";
                break;
            case "Dismantle Freight Content":
                index = "7";
                break;
            case "Dismantle Freight":
                index = "8";
                break;
            case "Outbound BOL":
                index = "9";
                break;
            case "Client Request":
                index = "10";
                break;
            case "Special Handling":
                index = "11";
                break;
            case "Accident":
                index = "12";
                break;
            case "Damage":
                index = "13";
                break;
            default:
                break;
        }
        locator = locator.replace("{{index}}", index);
        webDriver.clickOn(webDriver.getWebElement(locator));
    }

    @And("I get {string} image count as {string} in web")
    public void iGetImageCountAsInWeb(String column, String jobNumber) throws Exception {
        Thread.sleep(5000);
        String locator = locators.getMobileLocator("//locators/portal/jobPhototableCol");
        locator=locator.replace("{{jobNumber}}",jobNumber);
        String index = "1";
        switch (column) {
            case "Install Freight":
                index = "3";
                break;
            case "Install Freight Content":
                index = "4";
                break;
            case "Install Progress":
                index = "5";
                break;
            case "Show Ready":
                index = "6";
                break;
            case "Dismantle Freight Content":
                index = "7";
                break;
            case "Dismantle Freight":
                index = "8";
                break;
            case "Outbound BOL":
                index = "9";
                break;
            case "Client Request":
                index = "10";
                break;
            case "Special Handling":
                index = "11";
                break;
            case "Accident":
                index = "12";
                break;
            case "Damage":
                index = "13";
                break;
            default:
                break;
        }
        locator = locator.replace("{{index}}", index);
        String count = webDriver.getText(webDriver.getWebElement(locator));
        driverInstance.setCommonVariables(jobNumber+"_"+column,count);
        System.out.println(driverInstance.getCommonVariables(jobNumber+"_"+column));
    }

    @And("I switch to {string} tab")
    public void iSwitchToTab(String title) throws InterruptedException {
Thread.sleep(5000);
        webDriver.switchWindowTitled(title);
    }


    @And("I close the {string} tab")
    public void iCloseTheTab(String title) {
        webDriver.closeWindowTitled(title);
    }

    @And("I verify {int} uploaded photos")
    public void iVerifyUploadedPhotos(int numberOfPhotos) throws Exception {

        webDriver.clickOn(getPortalLocator("imageClick"));
        for(int i=0;i<numberOfPhotos;i++)
        {
            Thread.sleep(3000);
            webDriver.clickOn(getPortalLocator("imageNextClick"));
        }
    }

    @And("I navigate to securityLevel screen")
    public void iNavigateToSecurityLevelScreen() throws Exception {
        webDriver.clickOn(getPortalLocator("securityLevelsBtn"));

    }
}
