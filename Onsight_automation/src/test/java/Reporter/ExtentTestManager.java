package Reporter;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import java.util.HashMap;
import java.util.Map;
 
public class ExtentTestManager {
    @SuppressWarnings("rawtypes")
	static Map extentTestMap = new HashMap();
   // ExtentManager.loadConfig(new File(workingDir+"/config-report.xml"));
    static ExtentReports extent = ExtentManager.getReporter();

    public static synchronized ExtentTest getTest() {
    	
        return (ExtentTest)extentTestMap.get((int) Thread.currentThread().getId());
    }
 
    public static synchronized void endTest() {
        extent.endTest((ExtentTest)extentTestMap.get((int) Thread.currentThread().getId()));
    }
 
    @SuppressWarnings("unchecked")
	public static synchronized ExtentTest startTest(String testName, String desc) {
    	
    	ExtentTest test = extent.startTest(testName, desc);
        extentTestMap.put((int) Thread.currentThread().getId(), test);
        return test;
    }
}