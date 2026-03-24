package utils;

import java.io.File;

import org.dom4j.*;
import org.dom4j.io.SAXReader;
import pageHelper.BDDDriver;


public class XmlReader {
    public static String name;
    public static Document doc;
    public String fileLoaction;

    public XmlReader(String filePath) {
        fileLoaction = filePath;
    }

    public String getMobileLocator(String nodePath) throws Exception {
        String locator = null;
        File Inputfile = new File(fileLoaction);
        SAXReader xmlReader = new SAXReader();
        Document doc = xmlReader.read(Inputfile);

        if (!fileLoaction.toLowerCase().contains("portal"))
            if (BDDDriver.MOBILE_DRIVER_THREAD_LOCAL.get() != null)
                if (BDDDriver.MOBILE_DRIVER_THREAD_LOCAL.get().toString().contains("Android"))
                    nodePath = nodePath.replace("//locators", "//locators/android");
                else
                    nodePath = nodePath.replace("//locators", "//locators/ios");

        name = doc.selectSingleNode(nodePath).valueOf("@name");
        locator = doc.selectSingleNode(nodePath).getText();
        String env = new PropertyReader().readProperty("env");

        String packageName = new PropertyReader().readProperty("appPackage") + env;

        if (env.equalsIgnoreCase("prod"))
            packageName = packageName.replace("." + env, "");
        if (!fileLoaction.toLowerCase().contains("portal"))
            if (locator.startsWith("id="))
                locator = locator.replace("id=", "id=" + packageName + ":id/");

        String logoExt = "";
        if (env.equalsIgnoreCase("staging"))
            logoExt = "-Stage";

        locator = locator.replace("{{PACKAGE}}", packageName);
        locator = locator.replace("{{ENV}}", logoExt);

        return locator.trim();
    }
}