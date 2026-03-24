package utils;

import org.testng.Reporter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {


    public String readProperty(String key) throws IOException {
        String St = null;
        String config_env = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("environment");
        if (key.equalsIgnoreCase("env"))
            return config_env;
        Properties pr = new Properties();
        String filePath = "config.properties";
        InputStream file = new FileInputStream(filePath);
        pr.load(file);
        St = pr.getProperty(key);
        file.close();
        return St;
    }
}