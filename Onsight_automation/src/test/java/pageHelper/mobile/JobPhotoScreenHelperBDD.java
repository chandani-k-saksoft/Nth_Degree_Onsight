package pageHelper.mobile;

import core.BaseDriverHelper;
import core.MobileHelper;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pageHelper.BDDDriver;
import utils.DriverController;
import utils.PropertyReader;
import utils.XmlReader;

import java.io.File;
import java.time.Duration;
import java.util.List;

import static utils.AccessibilitySniffer.driver;

public class JobPhotoScreenHelperBDD {
    static String containerColor;
    public MobileHelper mobileDriver;
    private BDDDriver driverInstance;
    private XmlReader locators;
    private PropertyReader prop;

    public JobPhotoScreenHelperBDD(BDDDriver contextSteps, DriverController drivercontroller) throws Exception {
        this.driverInstance = contextSteps;
        System.out.println(this.driverInstance);
        mobileDriver = new BaseDriverHelper(BDDDriver.getMobileDriver());
        locators = new XmlReader("src//test//resources//locators//JobPhotoScreen.xml");
        prop = new PropertyReader();
    }

    public JobPhotoScreenHelperBDD(DriverController drivercontroller) {
        mobileDriver = new BaseDriverHelper(drivercontroller.getMobileDriver());
        locators = new XmlReader("src//test//resources//locators//JobPhotoScreen.xml");
        prop = new PropertyReader();
    }

    private WebElement getMobileLocator(String locator) throws Exception {
        locator = locators.getMobileLocator("//locators/jobPhoto/" + locator);
        WebElement el = mobileDriver.getMobileElement(locator);
        if(el==null)
            el = mobileDriver.getMobileElement(locator);
        return el;
    }

    private WebElement getMobileLocator(String locator, String replaceBy, String replaceFrom) throws Exception {
        locator = locators.getMobileLocator("//locators/jobPhoto/" + locator);
        locator = locator.replace(replaceBy, replaceFrom);
        WebElement el = mobileDriver.getMobileElement(locator);
        if(el==null)
            el = mobileDriver.getMobileElement(locator);
        return el;
    }

    private void addEmail(String email) throws Exception {
        if (getMobileLocator("additionalEmailText") != null) {
            mobileDriver.clickOn(getMobileLocator("additionalEmailText"));
            mobileDriver.sendKeys(getMobileLocator("additionalEmailText"), email);

        }
    }

    @And("I click on checkbox available on {int} row")
    public void iClickOnCheckboxAvailableOnRow(int index) throws Exception {
        mobileDriver.clickOn(getMobileLocator("installFreightCheckbox", "{{index}}", String.valueOf(index)));
    }

    @And("I click on add icon for install Freight")
    public void iClickOnAddIconForInstallFreight() throws Exception {
        mobileDriver.getMobileElements("installFreightAddBtn");

    }

    @And("I navigate to {string} screen")
    public void iNavigateToScreen(DataTable dataTable) throws Exception {
        List<List<String>> rows = dataTable.asLists(String.class);
        for (List<String> row : rows)
            Assert.assertTrue(mobileDriver.isElementPresent(getMobileLocator("jobPhoto", "{{NAVIGATION_MENU}}", row.get(0))));
    }

    @And("I click on add photo plus button")
    public void iClickOnAddPhotoPlusButton() throws Exception {
        mobileDriver.clickOn(getMobileLocator("installFreightAddBtn"));
    }

    @And("I navigate to {string} page")
    public void iNavigateToPage(String menuName) throws Exception {
//        mobileDriver.scrollScreen(708, 384, 726, 123);
//        if (!mobileDriver.isElementPresent(getMobileLocator("version")))
//            mobileDriver.clickOn(getMobileLocator("hamBurgerIcon"));
        mobileDriver.clickOn(getMobileLocator("navigationMenu", "{{NAVIGATION_MENU}}", menuName));
    }

    @And("I verify the navigation list as follows options")
    public void iVerifyTheNavigationListAsFollowsOptions() {
    }

    @And("I verify I am at job Photo page")
    public void iVerifyIAmAtJobPhotoPage() throws Exception {
        mobileDriver.isElementPresent(getMobileLocator("chooseCategoryTitle"));
        mobileDriver.isElementPresent(getMobileLocator("installFreightBtn"));

    }

    @And("I click on job photo button")
    public void iClickOnJobPhotoButton() throws Exception {
        System.out.println("test");
        mobileDriver.clickOn(getMobileLocator("jobPhotoBtn"));
    }

    @And("I select photo from camera")
    public void iSelectPhotoFromCamera() throws Exception {
//        mobileDriver.clickOn(getMobileLocator("cameraClickBtn"));
        mobileDriver.clickOn(getMobileLocator("cameraClickBtn"));
        mobileDriver.clickOn(getMobileLocator("cameraPhotoSendBtn"));
        System.out.println("Photo Confirmed");
    }

    @And("I navigate to camera")
    public void iNavigateToCamera() throws Exception {
        mobileDriver.isElementPresent(getMobileLocator("addPhototextTitle"));
        mobileDriver.clickOn(getMobileLocator("cameraBtn"));
        mobileDriver.isElementPresent(getMobileLocator("cameraClickBtn"));
    }

    @And("I navigate to gallery")
    public void iNavigateToGallery() throws Exception {
        mobileDriver.clickOn(getMobileLocator("galleryBtn"));
    }

    @And("I select photo from gallery")
    public void iSelectPhotoFromGallery() throws Exception {
        androidPushImages("capture1");
        androidPushImages("capture2");
        androidPushImages("capture3");
        Thread.sleep(5000);
        List<WebElement> images = mobileDriver.getMobileElements(locators.getMobileLocator("//locators/jobPhoto/commonGalleryPhotoSelect"));

// Select first 2 images
        for (int i = 0; i < 2; i++) {
            images.get(i).click();
        }
        mobileDriver.clickOn(getMobileLocator("galleryDoneBtn"));
        System.out.println("image uploaded");

    }

    private void androidPushImages(String fileName) throws Exception
    {
        ((AndroidDriver) BDDDriver.getMobileDriver())
                .pushFile("/storage/emulated/0/Download/Capture.png",
                        new File(System.getProperty("user.dir")+"/src/test/resources/"+fileName+".png"));
    }

    @And("I get {string} image count")
    public void iGetImageCount(String name) throws Exception {
        System.out.println("Get below count details");
        String count = mobileDriver.getAttribute(getMobileLocator("countButton", "{{ITEM}}", name),"content-desc");
        driverInstance.setCommonVariables("oldImageCount", count);
    }
    @And("I get {string} image count as {string}")
    public void iGetImageCount(String name, String var) throws Exception {
        System.out.println("Get below count details");
        String count = mobileDriver.getAttribute(getMobileLocator("countButton", "{{ITEM}}", name),"content-desc");
        driverInstance.setCommonVariables(var, count);
    }

    @Then("I verify {string} image count increase")
    public void iVerifyImageCountIncrease(String name) throws Exception {
        Thread.sleep(3000);
        String count = mobileDriver.getAttribute(getMobileLocator("countButton", "{{ITEM}}", name),"content-desc");
        System.out.println("New Image count: "+count);
        System.out.println("OLD Image count: "+driverInstance.getCommonVariables("oldImageCount"));
        Assert.assertTrue(Integer.parseInt(count) > Integer.parseInt(driverInstance.getCommonVariables("oldImageCount")));
    }

    @Then("I verify {string} image count increase by {string}")
    public void iVerifyImageCountIncreaseBy(String name,String var) throws Exception {
        String count = mobileDriver.getAttribute(getMobileLocator("countButton", "{{ITEM}}", name),"content-desc");
        System.out.println("New Image count: "+count);
        System.out.println("OLD Image count: "+driverInstance.getCommonVariables(var));
        Assert.assertTrue(Integer.parseInt(count) > Integer.parseInt(driverInstance.getCommonVariables(var)));
    }

    @And("I click on add photo button")
    public void iClickOnAddPhotoButton() throws Exception {
        mobileDriver.clickOn(getMobileLocator("galleryAddTextBtn"));
    }

    @And("I click on {string} check box")
    public void iClickOnCheckBox(String name) throws Exception {
        for(int i=0;i<10;i++)
        {
            WebElement el = getMobileLocator("installFreightCheckbox", "{{NAME}}", name);
            if(!mobileDriver.isElementPresent(el))
                mobileDriver.scrollScreenDown(1);
            else
                break;
        }
        mobileDriver.clickOn(getMobileLocator("installFreightCheckbox", "{{NAME}}", name));
    }

    @And("I get {string} add photo")
    public void iGetAddPhoto(String name) throws Exception {
        //Test new test case
        for(int i=0;i<10;i++)
        {
            WebElement el = getMobileLocator("jobPhotoAddBtn", "{{NAME}}", name);
            if(!mobileDriver.isElementPresent(el))
                mobileDriver.scrollScreenDown(1);
            else
                break;
        }
        mobileDriver.clickOn(getMobileLocator("jobPhotoAddBtn", "{{NAME}}", name));
    }

    @And("I click on Install Freight Content send mail check box")
    public void iClickOnInstallFreightContentSendMailCheckBox() throws Exception {
        mobileDriver.clickOn(getMobileLocator("sendEmailInstallFreightContentBox"));
    }

    @And("I verify {string} image count click")
    public void iVerifyImageCountClick(String name) throws Exception {
        Thread.sleep(10000);
        mobileDriver.clickOn(getMobileLocator("countButton", "{{ITEM}}", name));
    }

    @And("I click on Install Freight send mail check box")
    public void iClickOnInstallFreightSendMailCheckBox() throws Exception {
        mobileDriver.clickOn(getMobileLocator("sendEmailInstallBox"));
    }


    @And("I scroll the screen up {int} times")
    public void iScrollTheScreenUpTimes(int count) throws Exception {
        mobileDriver.scrollScreenUp(count);

    }
    }
