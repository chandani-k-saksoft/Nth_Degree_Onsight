package utils;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.log4j.Logger;
import Reporter.ExtentTestManager;
//import cucumber.Reporter;


public class Log {

// Initialize Log4j logs
	 
	 private static final Logger Log = Logger.getLogger(Log.class.getName());//

 // This is to print log for the beginning of the test case, as we usually run so many test cases as a test suite

 public static void startTestCase(String sTestCaseName){

	Log.info("****************************************************************************************");

	Log.info("****************************************************************************************");

	Log.info("$$$$$$$$$$$$$$$$$$$$$                 "+sTestCaseName+ "       $$$$$$$$$$$$$$$$$$$$$$$$$");

	Log.info("****************************************************************************************");

	Log.info("****************************************************************************************");

	}

	//This is to print log for the ending of the test case

 public static void endTestCase(String sTestCaseName){

	Log.info("XXXXXXXXXXXXXXXXXXXXXXX             "+"-E---N---D-"+"             XXXXXXXXXXXXXXXXXXXXXX");

	Log.info("X");

	Log.info("X");

	Log.info("X");

	Log.info("X");

	}
	// Need to create these methods, so that they can be called  

public static void info(String message) {

		Log.info(message);

		}

	 public static void report(String message) {

		 Log.info(message);
		 try{
			 if(message.contains("<") ||message.contains("{") ) {
//				 Reporter.addStepLog("Response Body After Request");
//				 Reporter.addStepLog("<textarea readonly>" + message + "</textarea>");
			 } else {
//				 Reporter.addStepLog(message);
			 }

		 }
		 catch (NullPointerException e)
		 {
			 if(message.contains("<") ||message.contains("{") ) {
				 ExtentTestManager.getTest().log(LogStatus.INFO, "<textarea readonly>" + message + "</textarea>");
			 }
			 else {
				 ExtentTestManager.getTest().log(LogStatus.INFO, message);
			 }
		 }
		 catch (Exception e)
		 {
			 System.out.println("Error in writing the reporting and none of reporter found");
		 }


	 }


	 public static void warn(String message) {

 Log.warn(message);

	}


public static void error(String message) {

 Log.error(message);

	}

public static void fatal(String message) {

 Log.fatal(message);

	}

public static void debug(String message) {

 Log.debug(message);

	}

 }
