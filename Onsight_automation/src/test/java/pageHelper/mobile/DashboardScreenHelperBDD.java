package pageHelper.mobile;

import core.BaseDriverHelper;
import core.MobileHelper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pageHelper.BDDDriver;
import utils.DriverController;
import utils.PropertyReader;
import utils.XmlReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardScreenHelperBDD {
    static String containerColor;
    public MobileHelper mobileDriver;
    private BDDDriver driverInstance;
    private XmlReader locators;
    private PropertyReader prop;

    public DashboardScreenHelperBDD(BDDDriver contextSteps, DriverController drivercontroller) throws Exception {
        this.driverInstance = contextSteps;
        System.out.println(this.driverInstance);
        mobileDriver = new BaseDriverHelper(BDDDriver.getMobileDriver());
        locators = new XmlReader("src//test//resources//locators//DashboardScreen.xml");
        prop = new PropertyReader();
    }

    public DashboardScreenHelperBDD(DriverController drivercontroller) {
        mobileDriver = new BaseDriverHelper(drivercontroller.getMobileDriver());
        locators = new XmlReader("src//test//resources//locators//DashboardScreen.xml");
        prop = new PropertyReader();
    }

    private WebElement getMobileLocator(String locator) throws Exception {
        locator = locators.getMobileLocator("//locators/dashboard/" + locator);
        WebElement el = mobileDriver.getMobileElement(locator);
        if (el == null)
            el = mobileDriver.getMobileElement(locator);
        return el;
    }

    private WebElement getMobileLocator(String locator, String replaceBy, String replaceFrom) throws Exception {
        locator = locators.getMobileLocator("//locators/dashboard/" + locator);
        locator = locator.replace(replaceBy, replaceFrom);
        WebElement el = mobileDriver.getMobileElement(locator);
        if (el == null)
            el = mobileDriver.getMobileElement(locator);
        return el;
    }

    private void addEmail(String email) throws Exception {
        Thread.sleep(3000);
//        if (getMobileLocator("enterEmailTextField") != null) {
        mobileDriver.clickOn(getMobileLocator("enterEmailTextField"));
        mobileDriver.sendKeys(getMobileLocator("enterEmailTextField"), email);
//            mobileDriver.clickOn(getMobileLocator("addEmailIcon"));

//        }
    }

    @And("I verify the navigation list as follows")
    public void iVerifyTheNavigationListAsFollows(DataTable dataTable) throws Exception {
//        mobileDriver.clickOn(getMobileLocator("hamburgerIcon"));
        List<List<String>> rows = dataTable.asLists(String.class);
        for (List<String> row : rows) {
            String rowVal=row.get(0).trim();
            for(String row_value : rowVal.split(",")) {
                row_value=row_value.trim();
                String screenTitleLoc = locators.getMobileLocator("//locators/dashboard/screenTitle").replace("{{NAVIGATION_MENU}}", row_value);
                String navigationMenuLoc = locators.getMobileLocator("//locators/dashboard/navigationMenu").replace("{{NAVIGATION_MENU}}", row_value);

                String finalLoc = screenTitleLoc +" | "+navigationMenuLoc;
                boolean found = mobileDriver.isElementPresent(mobileDriver.getMobileElement(finalLoc));
                if (!(found)) {
                    mobileDriver.scrollScreenDown(1);
                    found = mobileDriver.isElementPresent(mobileDriver.getMobileElement(finalLoc));
                }
                Assert.assertTrue(found , row_value + " is not found");
            }
        }
    }

    @And("I navigate to {string} module")
    public void iNavigateToModule(String menuName) throws Exception {
        mobileDriver.scrollScreen(708, 384, 726, 123);
        if (!mobileDriver.isElementPresent(getMobileLocator("version")))
            mobileDriver.clickOn(getMobileLocator("hamBurgerIcon"));
        mobileDriver.clickOn(getMobileLocator("navigationMenu", "{{NAVIGATION_MENU}}", menuName));
    }

    @And("I navigate to {string} sub module")
    public void iNavigateToSubModule(String menuName) throws Exception {
        mobileDriver.clickOn(getMobileLocator("navigationMenu", "{{NAVIGATION_MENU}}", menuName));
    }

    @Then("I verify I am at {string} screen")
    public void iVerifyIAmAtScreen(String menuName) throws Exception {
        mobileDriver.scrollScreenUp(1);
        Assert.assertEquals(mobileDriver.getAttribute(getMobileLocator("screenTitle", "{{NAVIGATION_MENU}}", menuName), "content-desc"), menuName);
    }

    @And("I press device Back button")
    public void iPressDeviceBackButton() throws Exception {
        mobileDriver.pressDeviceBackButton();
    }

    @And("I navigate to job Updates screen")
    public void iNavigateToJobUpdatesScreen() throws Exception {
        mobileDriver.clickOn(getMobileLocator("jobUpdate"));
    }

    @And("I enter {string} job number")
    public void iEnterJobNumber(String jobNumber) throws Exception {
        mobileDriver.clickOn(getMobileLocator("jobUpdateField"));
        mobileDriver.sendKeys(getMobileLocator("jobUpdateField"), jobNumber);
    }

    @And("I land to Job number screen")
    public void iLandToJobNumberScreen() throws Exception {
        mobileDriver.isElementPresent(getMobileLocator("jobPhotoBtn"));
        mobileDriver.isElementPresent(getMobileLocator("evaluationBtn"));
    }

    @And("I add email address")
    public void iAddEmailAddress() throws Exception {
//        addEmail("chandani.kumari1@saksoft.com");
        mobileDriver.scrollScreenDown(2);
        addEmail("ckgupta438@gmail.com");
//       mobileDriver.clickOn(getMobileLocator("emailField"));
//       mobileDriver.sendKeys(getMobileLocator("emailField"),"chandani.kumari1@saksoft.com1");
    }

    @And("I scroll the screen down {int} times")
    public void iScrollTheScreenDownTimes(int count) throws Exception {
        mobileDriver.scrollScreenDown(count);

    }

    @And("I expand list")
    public void iExpandList() throws Exception {
        mobileDriver.clickOn(getMobileLocator("expandEmailAddBtn"));
    }

    @And("I click on add email button")
    public void iClickOnAddEmailButton() throws Exception {
        mobileDriver.clickOn(getMobileLocator("addEmailIcon"));

    }

    @And("I verify I got the Job number is invalid message")
    public void iVerifyIGotTheJobNumberIsInvalidMessage() throws Exception {
        Thread.sleep(5000);
        mobileDriver.isElementPresent(getMobileLocator("invalidJobNumberText"));
    }

    @Then("I verify show info details for work order")
    public void iVerifyShowInfoDetailsForWorkOrder(DataTable dataTable) throws Exception {
        List<Map<String, String>> dataLists = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> map : dataLists) {
            String title = map.get("Title");
            String value = map.get("Value");
            boolean found = mobileDriver.isElementPresent(getMobileLocator("infoTitle", "{{INFO_DETAILS}}", title));
            if (!(found)) {
                mobileDriver.scrollScreenDown(1);
                found = mobileDriver.isElementPresent(getMobileLocator("screenTitle", "{{NAVIGATION_MENU}}", value));
            }
            Assert.assertTrue(found, title + " is not found");
        }
    }

    @And("I click on Ellipsis button")
    public void iClickOnEllipsisButton() throws Exception {
        mobileDriver.clickOn(getMobileLocator("elipsisBtn"));
    }

    @And("I click on Logout button")
    public void iClickOnLogoutButton() {
    }
}