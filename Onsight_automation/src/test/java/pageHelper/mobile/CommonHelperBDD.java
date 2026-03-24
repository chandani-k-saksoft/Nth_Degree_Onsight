package pageHelper.mobile;

import io.cucumber.java.en.And;
import pageHelper.BDDDriver;
import utils.CommonVariables;
import utils.DriverController;
import utils.EmailUtils;
import utils.PropertyReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonHelperBDD {
    private BDDDriver driverInstance;
    private PropertyReader prop;

    public CommonHelperBDD(BDDDriver contextSteps, DriverController drivercontroller) throws Exception {
        this.driverInstance = contextSteps;
        System.out.println(this.driverInstance);
        prop = new PropertyReader();
    }

    public CommonHelperBDD(DriverController drivercontroller) {
        prop = new PropertyReader();
    }

    public static String extractValue(String text, String start, String end) {
        Pattern pattern = Pattern.compile(start + "\\s*(.*?)\\s*" + end);
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "";
    }

    @And("I store {string} to {string} common variable")
    public void iStoreToCommonVariable(String variable, String variableToUpdate) throws Exception {
        String value = prop.readProperty(variable) != null ? prop.readProperty(variable)
                : driverInstance.getCommonVariables(variable) != null ? driverInstance.getCommonVariables(variable) : variable;
        driverInstance.setCommonVariables(variableToUpdate, value);

    }

    @And("I receive mail from gmail having subject as {string}")
    public void iReceiveMailFromGmailHavingSubjectAs(String subject) throws Throwable {
        String text = EmailUtils.getEmail(subject);
        System.out.println(text);
        // Extract Link
        Pattern linkPattern = Pattern.compile("https?://\\S+");
        Matcher linkMatcher = linkPattern.matcher(text);
        String link = linkMatcher.find() ? linkMatcher.group() : "";

        // Extract Freight Show
        String freightShow = extractValue(text, "Show:", "Exhibitor:");

        // Extract Exhibitor
        String exhibitor = extractValue(text, "Exhibitor:", "Booth Number:");

        // Extract Venue
        String venue = extractValue(text, "Venue:", "Work Order:");

        // Extract Work Order
        Pattern woPattern = Pattern.compile("Work Order:\\s*(\\S+)");
        Matcher woMatcher = woPattern.matcher(text);
        String workOrder = woMatcher.find() ? woMatcher.group(1) : "";

        // Store in List
        List<String> details = new ArrayList<>();
        details.add("Freight Show: " + freightShow);
        details.add("Exhibitor: " + exhibitor);
        details.add("Venue: " + venue);
        details.add("Work Order: " + workOrder);
        details.add("Link: " + link);

        // Print List
        for (String item : details) {
            System.out.println(item);
        }
    }
}