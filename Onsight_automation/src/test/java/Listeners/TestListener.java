package Listeners;



import com.relevantcodes.extentreports.LogStatus;


import utils.*;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import Reporter.ExtentManager;
import Reporter.ExtentTestManager;
 
public class TestListener extends Driver implements ITestListener  {
 
	
	String Filename;


    PropertyReader pr=new PropertyReader();
    PdfReader pd = new PdfReader();
    ReadAnnotation Rd = new ReadAnnotation();
    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    //Before starting all tests, below method runs.
    @SuppressWarnings("static-access")
	public void onStart(ITestContext iTestContext) {

        Log.info("I am on Start Test " + iTestContext.getName());
        try {
            if(pr.readProperty("InternetConnection").equalsIgnoreCase("Y"))
            {
                pd.CheckInternetConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("I am on Start Test " + iTestContext.getName());

        try {
            iTestContext.setAttribute("WebDriver", this.getWebDriver());
            System.out.println("Driver instance in Listemer" + this.getWebDriver());
        }
        catch(NullPointerException e)
        {

        }

        pd.onStartTest();
        //Test2.ResetFailedTestDetailes();
    }
 
    //After ending all tests, below method runs.
    public void onFinish(ITestContext iTestContext) {
        Log.info("I am on Finish method " + iTestContext.getName());
        System.out.println("I am on Finish method " + iTestContext.getName());
        //Do tier down operations for extentreports reporting!
     
       
        ExtentTestManager.endTest();
        ExtentManager.getReporter().flush();
        System.out.println("In the end");
//        PdfReader.onFinishTest();
       // Test2.ResetFailedTestExecued();
        //Test2.RunRandom();
        //Test2.RunFailedTestinCurrentExecution();


//        byte[] htmlReport = scanner.getHtmlReport()
//        Path pathToFile = Paths.get(path)
//        Files.createDirectories(pathToFile.getParent())
//        Files.write(pathToFile, htmlReport)
//        org.zaproxy.zap.


    }

    public void onTestStart(ITestResult iTestResult) {
        Log.info("I am on TestStart method " +  getTestMethodName(iTestResult) + " start");
        System.out.println("I am on TestStart method " +  getTestMethodName(iTestResult) + " start");
        //Start operation for extentreports.
        //ExtentTestManager.  TestName
        Log.info("I am in onStart method " + iTestResult.getTestContext().getAttribute("testName"));
        System.out.println("I am on TestStart method " +  getTestMethodName(iTestResult) + " start");
        Log.info("I am in onStart method " + TestName.get());
        System.out.println("I am in onStart method " + TestName.get());
        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmm", Locale.ENGLISH);
        Filename=iTestResult.getName()+ "-" + df.format(new Date());
      //  Filename=iTestResult.getTestContext().getAttribute("testName").toString();
        System.out.println("Test name from Exfel"+Filename);
//        ExtentTestManager.startTest(iTestResult.getTestContext().getSuite().getXmlSuite().getName().toString()+"-"+iTestResult.getTestContext().getCurrentXmlTest().getName().toString()+"-"+iTestResult.getTestContext().getAttribute("testName").toString()+"-"+TestName.get().toString()," THIs is Demo Test");

        ExtentTestManager.startTest("API Test "+"_ "+ iTestResult.getMethod().getMethodName()," THIs is Demo Test");
    }
 
    public void onTestSuccess(ITestResult iTestResult) {
        Log.info("I am on TestSuccess method " +  getTestMethodName(iTestResult) + " succeed");
        //Extentreports log operation for passed tests.
      ExtentTestManager.getTest().log(LogStatus.PASS, getTestMethodName(iTestResult)+" : Test Method has been passed");
      ExtentTestManager.endTest();
        System.out.println(iTestResult.getMethod().getDescription());
        ExtentManager.getReporter().flush();

       String classname= iTestResult.getMethod().getRealClass().getSimpleName();
//        try {
//
//            pd.onSuccessTest(Rd.GetAnnotation(iTestResult.getMethod().getMethod(),iTestResult.getMethod().getDescription()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }
 
    public void onTestFailure(ITestResult iTestResult) {
       // Log.info("I am on TestFailure method " +  getTestMethodName(iTestResult) + " failed");
        //iTestResult.setStatus(0);
        //Get driver from BaseTest and assign to local webdriver variable.
    	ExtentTestManager.getTest().log(LogStatus.FAIL, getTestMethodName(iTestResult)+" : Test Method has been Failed");
        Object testClass = iTestResult.getInstance();
        //iTestResult.setStatus(arg0);
        @SuppressWarnings("static-access")
        WebDriver webDriver;
        WebDriver DesktopDriver;
        WebDriver MobileDriver;
        String base64Screenshot = "";
        String Screenshotdata=null;
        String Message="";
                try {
                    webDriver = getWebDriver();

                        Screenshotdata=((TakesScreenshot)webDriver).
                                getScreenshotAs(OutputType.BASE64);
                        base64Screenshot = "data:image/png;base64,"+Screenshotdata;
                }

                catch(Exception e) {

                    try {
                        DesktopDriver = getDesktopDriver();

                        Screenshotdata = ((TakesScreenshot) DesktopDriver).
                                getScreenshotAs(OutputType.BASE64);
                        base64Screenshot = "data:image/png;base64," + Screenshotdata;

                    } catch (Exception e2) {
                        try {
                            MobileDriver = getMobileDriver();
                            Screenshotdata = ((TakesScreenshot) MobileDriver).
                                    getScreenshotAs(OutputType.BASE64);
                            base64Screenshot = "data:image/png;base64," + Screenshotdata;
                        }
                        catch (Exception e3) {
                            base64Screenshot="";
                        }
                    }
                }

        //Take base64Screenshot screenshot.


        try{
       Message=iTestResult.getThrowable().getMessage() != null ? iTestResult.getThrowable().getMessage() :
        iTestResult.getThrowable().getCause().toString();
        }
        catch(Exception e) {
         Message="Intenational Failed";
        }
       
        System.out.println("Result Messages"+Message);
        //Extentreports log and screenshot operations for failed tests.
       if(base64Screenshot.contains("data:image/png;base64,")) {
        ExtentTestManager.getTest().log(LogStatus.FAIL,Message+
               ExtentTestManager.getTest().addBase64ScreenShot(base64Screenshot));
       }
       else
       {
    	   ExtentTestManager.getTest().log(LogStatus.FAIL,Message);
       }
        ExtentTestManager.endTest();
        ExtentManager.getReporter().flush();


//        try {
//            pd.onFailedTest(Rd.GetAnnotation(iTestResult.getMethod().getMethod(),iTestResult.getMethod().getDescription()),Message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        ///////////////////Getting Failed test
        //Test2.GettingFailedTestDetails(iTestResult);
        //////////////////////////////////////////////
        //GetFailedTestDetails(iTestResult);
    }
 
    @SuppressWarnings({ "static-access", "unused" })
	public void onTestError(ITestResult iTestResult) {
        Log.info("I am on TestSuccess method " +  getTestMethodName(iTestResult) + " errored");
        //Extentreports log operation for passed tests.
        	ExtentTestManager.getTest().log(LogStatus.ERROR, getTestMethodName(iTestResult)+" : Test Method has been errored");
            Object testClass = iTestResult.getInstance();
            //iTestResult.setStatus(arg0);
            WebDriver webDriver = ((Driver) testClass).getWebDriver();
     
            //Take base64Screenshot screenshot.
            String base64Screenshot = "data:image/png;base64,"+((TakesScreenshot)getWebDriver()).
                    getScreenshotAs(OutputType.BASE64);
            
           
            ExtentManager.getReporter().flush();
            //Extentreports log and screenshot operations for failed tests.
          //  ExtentTestManager.getTest().log(LogStatus.ERROR,"Test Errored",
           //         ExtentTestManager.getTest().addBase64ScreenShot(base64Screenshot));
    }

    public void onTestSkipped(ITestResult iTestResult) {
        Log.info("I am in onTestSkipped method "+  getTestMethodName(iTestResult) + " skipped");
       
        ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
        
       
        
        ExtentManager.getReporter().flush();
    }
 
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        Log.info("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
        ExtentManager.getReporter().flush();
    }



 public void GetFailedTestDetails(ITestResult iTestResult )
 {
    int length = iTestResult.getThrowable().getStackTrace().length;

        for(int i=0;i<length;i++) {
            String className = iTestResult.getThrowable().getStackTrace()[i].getClassName();
            if (className.contains("pageHelper")) {
                System.out.println("class Name :" + className);
                String MethodName = iTestResult.getThrowable().getStackTrace()[i].getMethodName();
                System.out.println("Method Name :" + MethodName);
                int Linenumber = iTestResult.getThrowable().getStackTrace()[i].getLineNumber();
                System.out.println("Line Number :" + Linenumber);
                String Filename = iTestResult.getThrowable().getStackTrace()[i].getFileName();
                System.out.println("File name :" + Filename);
                break;
            }
        }
    }

}