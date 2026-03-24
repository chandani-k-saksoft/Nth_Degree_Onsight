package bddRunner;

import org.openqa.selenium.WebElement;
import utils.EmailUtils;
import utils.XmlReader;

import java.util.HashMap;

public class Demo {
    private static  String  getMobileLocator(String locator, String replaceBy, String replaceFrom) throws Exception {
        XmlReader locators = new XmlReader("src//test//resources//locators//DashboardScreen.xml");
        locator = locators.getMobileLocator("//locators/android/dashboard/" + locator);
        return locator;
    }
    public static void main(String... as) throws Exception {
        System.out.println("ll");
        getMobileLocator("navigationMenu", "{{NAVIGATION_MENU}}", "a");
//        EmailUtils emailUtilsUpdated = new EmailUtils();
//        HashMap<String, String> hm = (HashMap<String, String>) emailUtilsUpdated.getGmailSubjectAndBody("is:read");
////        HashMap<String, String> hm = emailUtilsUpdated.getGmailData("subject:Invited to join the study");
//        System.out.println(hm.size());
//        System.out.println(hm);
//        /*System.out.println(hm.get("subject"));
//        System.out.println("=================");
//        //System.out.println(hm.get("body"));
//        System.out.println("=================");
//        System.out.println(hm.get("link"));
//
//        System.out.println("=================");
//        System.out.println("Total count of emails is :"+getTotalCountOfMails());
//
//        System.out.println("=================");
//        boolean exist = isMailExist("Verification link");
//        System.out.println("title exist or not: " + exist);*/

    }
}
