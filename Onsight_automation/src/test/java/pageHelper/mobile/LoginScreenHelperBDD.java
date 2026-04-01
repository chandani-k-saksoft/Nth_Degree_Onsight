package pageHelper.mobile;

import com.google.common.collect.ImmutableMap;
import core.BaseDriverHelper;
import core.MobileHelper;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import pageHelper.BDDDriver;
import utils.DriverController;
import utils.PropertyReader;
import utils.XmlReader;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utils.AccessibilitySniffer.driver;

public class LoginScreenHelperBDD {
    static String containerColor;
    public MobileHelper mobileDriver;
    private BDDDriver driverInstance;
    private XmlReader locators;
    private PropertyReader prop;

    public LoginScreenHelperBDD(BDDDriver contextSteps, DriverController drivercontroller) throws Exception {
        this.driverInstance = contextSteps;
        System.out.println(this.driverInstance);
        mobileDriver = new BaseDriverHelper(BDDDriver.getMobileDriver());
        locators = new XmlReader("src//test//resources//locators//Mobile_LoginScreen.xml");
        prop = new PropertyReader();
    }


    public LoginScreenHelperBDD(DriverController drivercontroller) throws Exception {
        mobileDriver = new BaseDriverHelper(drivercontroller.getMobileDriver());
        locators = new XmlReader("src//test//resources//locators//Mobile_LoginScreen.xml");
        prop = new PropertyReader();
    }

    public void OtpSteps() {
        this.mobileDriver = mobileDriver; // your driver init
    }

    private WebElement getMobileLocator(String locator) throws Exception {
        WebElement el = mobileDriver.getMobileElement(locators.getMobileLocator("//locators/login/" + locator));
        if(el==null)
            el = mobileDriver.getMobileElement(locator);
        return el;
    }

    private WebElement getMobileLocator(String locator, String replaceBy, String replaceFrom) throws Exception {
        locator = locators.getMobileLocator("//locators/login/" + locator);
        locator = locator.replace(replaceBy, replaceFrom);
        WebElement el = mobileDriver.getMobileElement(locator);
        if(el==null)
            el = mobileDriver.getMobileElement(locator);
        return el;
    }

    private void login(String email) throws Exception {
        if (getMobileLocator("emailAndCodeField") != null) {
            System.out.println("Username: " + email);
            mobileDriver.clickOn(getMobileLocator("emailAndCodeField"));
            mobileDriver.sendKeys(getMobileLocator("emailAndCodeField"), email);
            iClickOnButton("SEND CODE");
        }
    }

    @And("I turn {string} the wifi on the device")
    public void iTurnTheWifiOnTheDevice(String status) throws Exception {
        boolean wifi_status = status.equalsIgnoreCase("ON");
        System.out.println("ABC: " + mobileDriver.getDriverType());
        if (mobileDriver.getDriverType().equalsIgnoreCase("Android"))
            mobileDriver.setWifiStatus(wifi_status);
    }

    @And("I land to enter email screen")
    public void iLandToEnterEmailScreen() throws Exception {
        if (driverInstance.getCommonVariables("skipFlow") == null)
            mobileDriver.isElementPresent(getMobileLocator("emailAndCodeField"));
    }

    @Then("Marketing Page {int} should be displayed")
    public void marketingPageShouldBeDisplayed(int pageNumber) throws Exception {
        if (pageNumber < 3)
            mobileDriver.isElementPresent(getMobileLocator("skipBtn"));
        mobileDriver.isElementPresent(getMobileLocator("forwardBtn"));
    }

    @And("The app logo should be visible")
    public void theAppLogoShouldBeVisible() throws Exception {
        mobileDriver.isElementPresent(getMobileLocator("appLogo"));

    }

    @And("The marketing banner image should be displayed")
    public void theMarketingBannerImageShouldBeDisplayed() throws Exception {
        mobileDriver.isElementPresent(getMobileLocator("bannerImage"));
    }

    @And("The Next button should be enabled")
    public void theNextButtonShouldBeEnabled() throws Exception {
        mobileDriver.isElementPresent(getMobileLocator("forwardBtn"));
    }

    @And("The Skip button should be visible")
    public void theSkipButtonShouldBeVisible() throws Exception {
        mobileDriver.isElementPresent(getMobileLocator("skipBtn"));
    }

    @When("The user taps on the Next button on Marketing Page {int}")
    public void theUserTapsOnTheNextButtonOnMarketingPage(int arg0) throws Exception {
        mobileDriver.clickOn(getMobileLocator("forwardBtn"));
    }


    @When("The user taps on the skip button on Marketing Page {int}")
    public void theUserTapsOnTheSkipButtonOnMarketingPage(int arg0) throws Exception {
        if (driverInstance.getCommonVariables("skipFlow") == null)
            mobileDriver.clickOn(getMobileLocator("skipBtn"));
    }

    @When("I login with unregistered user")
    public void iLoginWithUnregisteredUser() throws Exception {
        login("test@nthdegree.com");
    }

    @And("I verify I got the {string} message")
    public void iVerifyIGotTheMessage(String errorMessage) throws Exception {
        String actual_text = mobileDriver.getText(getMobileLocator("genericMsg", "{MESSAGE_TEXT}", errorMessage));
        System.out.println("ACTUAL TEXT: " + actual_text);
        Assert.assertTrue(actual_text.contains(errorMessage));
    }

    @And("I edit email address")
    public void iEditEmailAddress() throws Exception {
        mobileDriver.clickOn(getMobileLocator("editBtn"));
    }

    @And("I click on {string} button")
    public void iClickOnButton(String buttonText) throws Exception {
        mobileDriver.clickOn(getMobileLocator("genericBtn", "{BUTTON_TEXT}", buttonText));
    }

    @Given("I login with registered user")
    public void iLoginWithRegisteredUser() throws Exception {
        if (driverInstance.getCommonVariables("skipFlow") == null) {
            Thread.sleep(3000);
            login("onsightdemo@saksoft.com");
            iClickOnButton("OK");
            iClickOnButton("VERIFY");
        }
    }
    @Given("I login as {string} user")
    public void iLoginAsdUser(String username) throws Exception {
        if (driverInstance.getCommonVariables("skipFlow") == null) {
            Thread.sleep(3000);
            login(username);
            iClickOnButton("OK");
            iClickOnButton("VERIFY");
        }
    }

    @Given("I login with registered added user")
    public void iLoginWithRegisteredAddedUser() throws Exception {
        if (driverInstance.getCommonVariables("skipFlow")!=null) {
            String username = driverInstance.getCommonVariables("ADDED_USER");
            if (username == null)
                username = "chandani.kumari@saksoft.com";

            login(username);
        }
    }

    @And("I verify autofill otp")
    public void iVerifyAutofillOtp() throws Exception {
        Thread.sleep(50000);
        mobileDriver.openNotificationPopup();
        String otp = captureOTP();
        mobileDriver.closeNotificationPopup();
        String otpField = driverInstance.getCommonVariables("emailAndCodeField");

        // Get OTP value
//        String otpValue = otpField.
//
//        // Validation
//        if (otpValue != null && !otpValue.trim().isEmpty()) {
//            System.out.println("OTP is auto-filled: " + otpValue);
//        } else {
//            System.out.println("OTP is NOT auto-filled");
//        }
    }

    public String captureOTP() throws Exception {
        List<WebElement> notifications = mobileDriver.getMobileElements("//*[@id='android:id/text']");
        String otp = null;
        for (WebElement notification : notifications) {
            String text = notification.getText();
            if (text.matches(".*\\b\\d{6}\\b.*")) {
                otp = text.replaceAll("\\D+", "");
                break;
            }
        }

        System.out.println("OTP is: " + otp);
        return otp;
    }

    @And("I land to dashboard screen")
    public void iLandToDashboardScreen() throws Exception {
        driverInstance.setCommonVariables("skipFlow", "true");
        Assert.assertTrue(mobileDriver.isElementPresent(getMobileLocator("dashboardText")));
        Thread.sleep(10000);
    }

    @And("I clear otp field")
    public void iClearOtpField() throws Exception {
        mobileDriver.clickOn(getMobileLocator("emailAndCodeField"));
        mobileDriver.sendKeys(getMobileLocator("emailAndCodeField"), "");
        System.out.println("Clear OTP");
    }

    @And("I clear otp field and enter invalid")
    public void iClearOtpFieldAndEnterInvalid() throws Exception {
        if (getMobileLocator("emailAndCodeField") != null) {
            mobileDriver.clickOn(getMobileLocator("emailAndCodeField"));
            mobileDriver.sendKeys(getMobileLocator("emailAndCodeField"), "567890");
        }
    }

    public String captureOTP1() throws Exception {
        List<WebElement> otpBoxes = mobileDriver.getMobileElements("//*[@id='android:id/text']");

        String newOtp = "123456";

        for (int i = 0; i < otpBoxes.size(); i++) {
            otpBoxes.get(i).click();
            otpBoxes.get(i).clear();
            otpBoxes.get(i).sendKeys(String.valueOf(newOtp.charAt(i)));
        }

        return null;
    }

    @And("I enter resend code")
    public void iEnterResendCode() throws Exception {
        mobileDriver.getMobileElements("emailAndCodeField").clear();
        List<WebElement> notifications = mobileDriver.getMobileElements("//*[@id='android:id/text']");
        // Extract 4–6 digit OTP from text
        Pattern pattern = Pattern.compile("\\b\\d{4,6}\\b");
        Matcher matcher = pattern.matcher((CharSequence) notifications);

        String otp = "";
        if (matcher.find()) {
            otp = matcher.group();
        }

        System.out.println("Captured OTP: " + otp);

    }

    @And("I verify otp from clipboard")
    public void iVerifyOtpFromClipboard() throws Exception {
//        Assert.assertTrue(
//                driver.findElement(By.xpath("//android.widget.Toast"))
//                        .isDisplayed()
//        );
//        mobileDriver.waitForMobileElement(mobileDriver.getMobileElement("//android.widget.Toast[1]"),30);
        WebDriverWait wait = new WebDriverWait(BDDDriver.getMobileDriver(), Duration.ofSeconds(20));

        WebElement toast = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//android.widget.Toast")
                )
        );
        String toastText = toast.getAttribute("text");
        String otp = toastText.replaceAll(
                ".*verification code is:\\s*(\\d{6}).*", "$1");
        System.out.println(toastText);
        System.out.println("Otp is "+otp);
        mobileDriver.clickOn(getMobileLocator("emailAndCodeField"));
        mobileDriver.sendKeys(getMobileLocator("emailAndCodeField"), otp);

    }

    @And("I click on resend code link")
    public void iClickOnResendCodeLink() throws Exception {
        mobileDriver.clickOn(getMobileLocator("resendCode"));
    }

    @And("I wait")
    @And("I wait for OTP")
    public void iWaitForOTP() throws Exception {
        Thread.sleep(10000);
    }


    }



