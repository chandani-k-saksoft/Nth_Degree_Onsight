package pageHelper.mobile;

import core.BaseDriverHelper;
import core.MobileHelper;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pageHelper.BDDDriver;
import utils.DriverController;
import utils.PropertyReader;
import utils.XmlReader;

import java.io.File;
import java.util.List;
import java.util.Map;

public class EvaluationScreenHelperBDD {
    static String containerColor;
    public MobileHelper mobileDriver;
    private BDDDriver driverInstance;
    private XmlReader locators;
    private PropertyReader prop;

    public EvaluationScreenHelperBDD(BDDDriver contextSteps, DriverController drivercontroller) throws Exception {
        this.driverInstance = contextSteps;
        System.out.println(this.driverInstance);
        mobileDriver = new BaseDriverHelper(BDDDriver.getMobileDriver());
        locators = new XmlReader("src//test//resources//locators//EvaluationScreen.xml");
        prop = new PropertyReader();
    }

    public EvaluationScreenHelperBDD(DriverController drivercontroller) {
        mobileDriver = new BaseDriverHelper(drivercontroller.getMobileDriver());
        locators = new XmlReader("src//test//resources//locators//EvaluationScreen.xml");
        prop = new PropertyReader();
    }

    private WebElement getMobileLocator(String locator) throws Exception {
        locator = locators.getMobileLocator("//locators/evaluation/" + locator);
        WebElement el = mobileDriver.getMobileElement(locator);
        if(el==null)
            el = mobileDriver.getMobileElement(locator);
        return el;
    }

    private WebElement getMobileLocator(String locator, String replaceBy, String replaceFrom) throws Exception {
        locator = locators.getMobileLocator("//locators/evaluation/" + locator);
        locator = locator.replace(replaceBy, replaceFrom);
        WebElement el = mobileDriver.getMobileElement(locator);
        if(el==null)
            el = mobileDriver.getMobileElement(locator);
        return el;
    }

    @And("I click on {string} evaluation")
    public void iClickOnEvaluation(String buttonText) throws Exception {
        mobileDriver.clickOn(getMobileLocator("installDismantleBtn", "{{BUTTON_TEXT}}", buttonText));
    }

    @And("I select answer of questions at the screen")
    public void iSelectAnswerOfQuestionsAtTheScreen(DataTable dataTable) throws Exception {
        List<Map<String, String>> dataLists = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> map : dataLists) {
            String question = map.get("question");
            String answer = map.get("answer");
            boolean textEnabled = map.get("textEnabled?").equalsIgnoreCase("YES")?true:false;


            String genericAnswerButton = locators.getMobileLocator("//locators/evaluation/genericAnswerButton");
            genericAnswerButton = genericAnswerButton.replace("{{question}}", question.trim());
            String answerToSelect = answer.equalsIgnoreCase("No")
                    ? genericAnswerButton + "/android.widget.RadioButton[2]"
                    : genericAnswerButton + "/android.widget.RadioButton[1]";
            String explaination = genericAnswerButton + "//android.widget.EditText";

            int i = 0;
            WebElement el = null;
            while (i++ < 10) {
                el = mobileDriver.getMobileElement(answerToSelect);
                if (el != null)
                    break;
                else
                    mobileDriver.scrollScreenDown(1);
            }
            mobileDriver.clickOn(el);
            if (textEnabled) {
                WebElement elx = mobileDriver.getMobileElement(explaination);
                mobileDriver.clickOn(elx);
                mobileDriver.sendKeys(elx, "Automated Answer for " + question);
                mobileDriver.clickOn(el);
            }
        }
        mobileDriver.scrollScreenDown(2);
    }
}
