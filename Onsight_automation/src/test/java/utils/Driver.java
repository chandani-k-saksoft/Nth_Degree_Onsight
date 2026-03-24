package utils;

import Reporter.ExtentTestManager;
import com.relevantcodes.extentreports.LogStatus;
import core.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.windows.WindowsDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

import io.github.sridharbandi.Accessibility;
import io.github.sridharbandi.AccessibilityRunner;
import io.github.sridharbandi.util.Standard;

import org.apache.log4j.xml.DOMConfigurator;
import org.testng.ITestContext;
import org.testng.annotations.*;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

public class Driver extends PageController
{
	public static int  itr;
	public static ThreadLocal<AccessibilityRunner>  accessibilityRunner=new ThreadLocal<AccessibilityRunner>();
	Process currentProcess;
	//public static boolean  Accessibilityflag;
	public static  ThreadLocal<Boolean> Accessibilityflag=new ThreadLocal<Boolean>();
	public static  ThreadLocal<Boolean> Zapreport=new ThreadLocal<Boolean>();

	PropertyReader pr=new PropertyReader();
	public static ThreadLocal<String> TestName= new InheritableThreadLocal<>();
	public static final ThreadLocal<org.openqa.selenium.WebDriver> WEB_DRIVER_THREAD_LOCAL = new InheritableThreadLocal<>();
	public static final ThreadLocal<RequestSpecification> API_DRIVER_THREAD_LOCAL = new InheritableThreadLocal<>();
	public static final ThreadLocal<Response> API_RESPONCE_THREAD_LOCAL = new InheritableThreadLocal<>();
	public static final ThreadLocal<WindowsDriver> DESKTOP_DRIVER_THREAD_LOCAL = new InheritableThreadLocal<>();
	public static final ThreadLocal<AppiumDriver> MOBILE_DRIVER_THREAD_LOCAL = new InheritableThreadLocal<>();
	public static final ThreadLocal<ClientApi> ZAP_CANNER_THREAD_LOCAL = new InheritableThreadLocal<>();

	@BeforeMethod(groups = { "api" })
	@Before
	public void APIsetup(Method method,ITestContext ctx ,Object[] data) throws Exception {
	
		Object[][] st1 = null;
		try 
		{
	 	st1=(Object[][]) data[0];
	 	System.out.println("Lenghth of Complete Data provided by DP"+st1.length);
		}
		catch(Exception e)
		{
			st1=new Object[][] {{""}};
		}
		Object[] st = null;
		try 
		
		{
	 	st= st1[0];
	 	System.out.println("Length of First Data provided by DP"+st.length);
		}
		catch(Exception e)
		{
			st=new Object[][] {{""}};
			System.out.println(e.getMessage());
		}
		
			try {
		    Log.info(st[st.length-2].toString());
		    TestName.set(st[st.length-2].toString());
		    ctx.setAttribute("testName", st[st.length-2].toString());
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				TestName.set("Default");
			    ctx.setAttribute("testName", "Default");
			
	      }
		
		ApiDriver ApiDriver=new BaseDriver();
		
		API_DRIVER_THREAD_LOCAL.set(ApiDriver.apiInit("https://google.com"));
		API_RESPONCE_THREAD_LOCAL.set(null);
		
		this.initPage(API_DRIVER_THREAD_LOCAL.get(),API_RESPONCE_THREAD_LOCAL.get());
	}

	//@BeforeMethod(groups = { "web" })
	//@Before
	public void websetup(Method method,ITestContext ctx ,Object[] data) throws Exception {
		Object[][] st1 = null;
		try {
			st1 = (Object[][]) data[0];
			System.out.println("Lenghth of Complete Data provided by DP" + st1.length);
		} catch (Exception e) {
			st1 = new Object[][]{{""}};
		}
		Object[] st = null;
		try {
			st = st1[0];
			System.out.println("Length of First Data provided by DP" + st.length);
		} catch (Exception e) {
			st = new Object[][]{{""}};
			System.out.println(e.getMessage());
		}

		try {
			Log.info(st[st.length - 2].toString());
			TestName.set(st[st.length - 2].toString());
			ctx.setAttribute("testName", st[st.length - 2].toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			TestName.set("Default");
			ctx.setAttribute("testName", "Default");

		}
		//baseDriver ZapScanner=new baseDriver();
		webDriver WebDriver = new BaseDriver();

		WEB_DRIVER_THREAD_LOCAL.set(WebDriver.webInit("chrome", "http://google.com", false, true));
		try{
		ZAP_CANNER_THREAD_LOCAL.set(WebDriver.returnZapScanner());
		System.out.println("Zap Scanner in Driver.java"+ZAP_CANNER_THREAD_LOCAL.get());

	    ZAP_CANNER_THREAD_LOCAL.get().spider.scan("http://google.com", null, null, null, null);
		}
		catch(Exception e){
			System.out.println("No ZapCanner is not setup");

		}
		this.initPage(WEB_DRIVER_THREAD_LOCAL.get());
		Accessibility.REPORT_PATH =  System.getProperty("user.dir")+"/ExtentReports/Accessibility/Report";

		setcompliences();
		System.out.println(Accessibility.REPORT_PATH +Accessibility.STANDARD);
		accessibilityRunner.set(new AccessibilityRunner(WEB_DRIVER_THREAD_LOCAL.get()));

//		  accessibilitySniffer=new AccessibilitySniffer(driver);

	}
	//@BeforeSuite(groups = { "web" })
	@BeforeMethod(groups = { "web" })
	//@Before
	public void websetupwithoutDataProvider(Method method,ITestContext ctx,Object[] dt) throws Exception {
//		Object[][] st1 = null;
//		try {
//			st1 = (Object[][]) data[0];
//			System.out.println("Lenghth of Complete Data provided by DP" + st1.length);
//		} catch (Exception e) {
//			st1 = new Object[][]{{""}};
//		}
//		Object[] st = null;
//		try {
//			st = (Object[]) st1[0];
//			System.out.println("Length of First Data provided by DP" + st.length);
//		} catch (Exception e) {
//			st = new Object[][]{{""}};
//			System.out.println(e.getMessage());
//		}

//		itr=0;
//		DOMConfigurator.configure("log4j.xml");


		String parameter=ctx.getCurrentXmlTest().getParameter("Browser");
		Boolean proxy = Boolean.valueOf(ctx.getCurrentXmlTest().getParameter("Proxy"));
		System.out.println("Proxy Parameter is"+proxy);
		System.out.println("Browse Passed From Testng is-"+dt[0].toString());
		try {
			Log.info("Generic Test");
			TestName.set("Parametrized Test From Suite");
			ctx.setAttribute("testName", "Parametrized Test From Suite");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			TestName.set("Default");
			ctx.setAttribute("testName", "Default");

		}
		//baseDriver ZapScanner=new baseDriver();
		webDriver WebDriver = new BaseDriver();

		WEB_DRIVER_THREAD_LOCAL.set(WebDriver.webInit(parameter, "http://google.com", false, proxy));
		try{
			ZAP_CANNER_THREAD_LOCAL.set(WebDriver.returnZapScanner());
			System.out.println("Zap Scanner in Driver.java"+ZAP_CANNER_THREAD_LOCAL.get());

			ZAP_CANNER_THREAD_LOCAL.get().spider.scan(WEB_DRIVER_THREAD_LOCAL.get().getCurrentUrl(), null, null, null, null);
		}
		catch(Exception e){
			System.out.println("No ZapCanner is not setup");

		}
		this.initPage(WEB_DRIVER_THREAD_LOCAL.get());
		Accessibility.REPORT_PATH =  System.getProperty("user.dir")+"/ExtentReports/Accessibility/Report";

		setcompliences();
		System.out.println(Accessibility.REPORT_PATH +Accessibility.STANDARD);
		accessibilityRunner.set(new AccessibilityRunner(WEB_DRIVER_THREAD_LOCAL.get()));

//		  accessibilitySniffer=new AccessibilitySniffer(driver);

	}


	@BeforeMethod(groups = { "webheadless" })
	//@Before
	public void websetupwithoutheadless(Method method,ITestContext ctx,Object[] dt) throws Exception {
//		Object[][] st1 = null;
//		try {
//			st1 = (Object[][]) data[0];
//			System.out.println("Lenghth of Complete Data provided by DP" + st1.length);
//		} catch (Exception e) {
//			st1 = new Object[][]{{""}};
//		}
//		Object[] st = null;
//		try {
//			st = (Object[]) st1[0];
//			System.out.println("Length of First Data provided by DP" + st.length);
//		} catch (Exception e) {
//			st = new Object[][]{{""}};
//			System.out.println(e.getMessage());
//		}

//		itr=0;
//		DOMConfigurator.configure("log4j.xml");


		String parameter=dt[0].toString();
		System.out.println("Browse Passed From Testng is-"+dt[0].toString());
		try {
			Log.info("Generic Test");
			TestName.set("Parametrized Test From Suite");
			ctx.setAttribute("testName", "Parametrized Test From Suite");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			TestName.set("Default");
			ctx.setAttribute("testName", "Default");

		}
		//baseDriver ZapScanner=new baseDriver();
		webDriver WebDriver = new BaseDriver();
	System.out.println("In Headless mode");
		WEB_DRIVER_THREAD_LOCAL.set(WebDriver.webInit(parameter, "http://google.com", false, false,true));
		try{
			ZAP_CANNER_THREAD_LOCAL.set(WebDriver.returnZapScanner());
			System.out.println("Zap Scanner in Driver.java"+ZAP_CANNER_THREAD_LOCAL.get());

			ZAP_CANNER_THREAD_LOCAL.get().spider.scan("http://google.com", null, null, null, null);
		}
		catch(Exception e){
			System.out.println("No ZapCanner is not setup");

		}
		this.initPage(WEB_DRIVER_THREAD_LOCAL.get());
		Accessibility.REPORT_PATH =  System.getProperty("user.dir")+"/ExtentReports/Accessibility/Report";

		setcompliences();
		System.out.println(Accessibility.REPORT_PATH +Accessibility.STANDARD);
		accessibilityRunner.set(new AccessibilityRunner(WEB_DRIVER_THREAD_LOCAL.get()));

//		  accessibilitySniffer=new AccessibilitySniffer(driver);

	}

	@BeforeMethod(groups = { "mobile" })
	//@Before
	public void Mobilesetup(Method method,ITestContext ctx ,Object[] data) throws Exception {


		Object[][] st1 = null;
		try
		{
			st1=(Object[][]) data[0];
			System.out.println("Lenghth of Complete Data provided by DP"+st1.length);
		}
		catch(Exception e)
		{
			st1=new Object[][] {{""}};
		}
		Object[] st = null;
		try

		{
			st= st1[0];
			System.out.println("Length of First Data provided by DP"+st.length);
		}
		catch(Exception e)
		{
			st=new Object[][] {{""}};
			System.out.println(e.getMessage());
		}

		try {
			Log.info(st[st.length-2].toString());
			TestName.set(st[st.length-2].toString());
			ctx.setAttribute("testName", st[st.length-2].toString());
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			TestName.set("Default");
			ctx.setAttribute("testName", "Default");

		}
		System.out.println("Parameters"+data[3].toString());
		MobileDriver mobileDriver=new BaseDriver();
		MOBILE_DRIVER_THREAD_LOCAL.set(mobileDriver.mobileInit("env", data[3].toString(),"Android","",System.getProperty ("user.dir")+"/lib/"+ctx.getCurrentXmlTest().getParameter("mApp"),"11", "true", "https://127.0.0.1:4723", true));
		this.initPage(MOBILE_DRIVER_THREAD_LOCAL.get());

	}



	@BeforeMethod(groups = { "desktop" })
	@Before
	public void Desktopsetup(Method method,ITestContext ctx ,Object[] data) throws Exception{
//		ProcessBuilder processBuilder = new ProcessBuilder();
//		processBuilder.command("cmd.exe", "/c", "C:\\Program Files (x86)\\Windows Application Driver\\WinAppDriver.exe");

		Object[][] st1 = null;
		try
		{
			st1=(Object[][]) data[0];
			System.out.println("Lenghth of Complete Data provided by DP"+st1.length);
		}
		catch(Exception e)
		{
			st1=new Object[][] {{""}};
		}
		Object[] st = null;
		try

		{
			st= st1[0];
			System.out.println("Length of First Data provided by DP"+st.length);
		}
		catch(Exception e)
		{
			st=new Object[][] {{""}};
			System.out.println(e.getMessage());
		}

		try {
			Log.info(st[st.length-2].toString());
			TestName.set(st[st.length-2].toString());
			ctx.setAttribute("testName", st[st.length-2].toString());
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			TestName.set("Default");
			ctx.setAttribute("testName", "Default");

		}
		System.out.println("Desktop Before method");

		DesktopDriver desktopDriver=new BaseDriver();
		//DESKTOP_DRIVER_THREAD_LOCAL.set(desktopDriver.desktopinit("Microsoft.WindowsCalculator_8wekyb3d8bbwe!App"));
		DESKTOP_DRIVER_THREAD_LOCAL.set(desktopDriver.desktopInit(ctx.getCurrentXmlTest().getParameter("App")));
		this.initPage(DESKTOP_DRIVER_THREAD_LOCAL.get());
		
	}
	@Before
	@BeforeMethod(groups = { "performance" })
	public void Perfprmance_Report_Setup(Method method,ITestContext ctx ,Object[] data) throws Exception{
	
		Object[][] st1 = null;
		try 
		{
	 	st1=(Object[][]) data[0];
	 	System.out.println("Lenghth of Complete Data provided by DP"+st1.length);
		}
		catch(Exception e)
		{
			st1=new Object[][] {{""}};
		}
		Object[] st = null;
		try 
		
		{
	 	st= st1[0];
	 	System.out.println("Length of First Data provided by DP"+st.length);
		}
		catch(Exception e)
		{
			st=new Object[][] {{""}};
			System.out.println(e.getMessage());
		}
		try {
		
		    Log.info(st[st.length-2].toString());
		    TestName.set(st[st.length-2].toString());
		    ctx.setAttribute("testName", st[st.length-2].toString());
	      }
	
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			TestName.set("Default");
		    ctx.setAttribute("testName", "Default");
		}

	}
	
	@BeforeSuite(alwaysRun = true)
	public void BeforeSuite() throws IOException {
	itr=0;
	DOMConfigurator.configure("log4j.xml");
//		try{
//			Process p = Runtime.getRuntime().exec("C:/Program Files/OWASP/Zed Attack Proxy/zap.bat -port 8777");
//			p.waitFor();
//
//		}catch( IOException ex ){
//			//Validate the case the file can't be accesed (not enought permissions)
//
//		}catch( InterruptedException ex ){
//			//Validate the case the process is being stopped by some external situation
//
//		}
//		MockServer ms=new MockServer();
//		ms.MockServerSetup();
	}
	@AfterSuite(alwaysRun = true)
	public void AfterSuite() throws IOException, ClientApiException {
//		if(Accessibilityflag==true) {
//			accessibilityRunner.generateHtmlReport();
//			ExtentTestManager.getTest().log(LogStatus.INFO, "<a href='Accessibility/Report/report/html/index.html'>Please See the Detail of Accessibility Scan by Clicking this link</a>");
//		}
//		Accessibilityflag=false;

		//WEB_DRIVER_THREAD_LOCAL.get().close();
		String message="<p>Hi All,</p><p>A Test Execution build was triggered and the execution has been completed.</p><p>For the detailed Information’s, please refer the attached html report.</p><p>Thanks,<br/>QA Team</p>";
		 //maiUtility ml=new maiUtility();
	    // ml.SendEmail(message,"ExtentReports//ExtentReportResults.html");
	}
	@AfterMethod(groups = { "web","webheadless" })
	//@After
	public static void Teardown() throws ClientApiException, IOException {
		//while (int(ZAP_CANNER_THREAD_LOCAL.get().spider.) < 100):
		System.out.println("In the end");
		if(!(ZAP_CANNER_THREAD_LOCAL.get()==null))
		{
			if(!(Zapreport.get()==null)) {
				if (Zapreport.get()) {
					System.out.println("Generating Zap report");
					String FILEPATH = "ExtentReports/Security_Report_"+Thread.currentThread()+".html";
					File file = new File(FILEPATH);
					try {

						// Initialize a pointer
						// in file using OutputStream
						OutputStream
								os
								= new FileOutputStream(file);

						// Starts writing the bytes in it
						os.write(ZAP_CANNER_THREAD_LOCAL.get().core.htmlreport());
						//ZAP_CANNER_THREAD_LOCAL.get().reports.generate("UNITE Security Report","Traditional HTML Report","","","Default","","","","",FILEPATH,"","","");

						System.out.println("Successfully"
								+ " byte inserted");

						// Close the file
						os.close();
					} catch (Exception e) {
						System.out.println("Exception: " + e);
					}
					ExtentTestManager.getTest().log(LogStatus.INFO, "<a href='Security_Report_"+Thread.currentThread()+".html'>Please See the Detail of Security Scan by Clicking this link</a>");
					//ZAP_CANNER_THREAD_LOCAL.set(null);
					Zapreport.set(false);
				}
				else{
					System.out.println("ZapReporting is not true");
				}
			}
			else
			{System.out.println("ZapReporting is not null");

			}

		}

		if(Accessibilityflag.get()!=null) {
			if (Accessibilityflag.get()) {
				accessibilityRunner.get().generateHtmlReport();
				ExtentTestManager.getTest().log(LogStatus.INFO, "<a href='Accessibility/Report/report/html/index.html'>Please See the Detail of Accessibility Scan by Clicking this link</a>");
			}
			Accessibilityflag.set(false);
		}
		if(WEB_DRIVER_THREAD_LOCAL.get()!=null)
		{
//			WEB_DRIVER_THREAD_LOCAL.get().close();
//			if(WEB_DRIVER_THREAD_LOCAL.get()!=null) {
				WEB_DRIVER_THREAD_LOCAL.get().quit();
		//	}
		}
		

	}
	@AfterTest(groups = { "web","webheadless" },alwaysRun = true)
	//@After
	public static void Teardowntest() throws ClientApiException, IOException {

//		if(WEB_DRIVER_THREAD_LOCAL.get()!=null)
//		{
//
//			WEB_DRIVER_THREAD_LOCAL.get().quit();
//		}


	}

	@AfterMethod(groups = { "mobile" })
	@After
	public static void MobileTeardown() throws ClientApiException, IOException {
		//while (int(ZAP_CANNER_THREAD_LOCAL.get().spider.) < 100):

//		MOBILE_DRIVER_THREAD_LOCAL.get().removeApp("org.wikipedia.alpha");
		MOBILE_DRIVER_THREAD_LOCAL.get().quit();

	}
	@AfterMethod(groups = { "desktop" })
	@After
	public static void DesktopTeardown() throws ClientApiException, IOException {
		//while (int(ZAP_CANNER_THREAD_LOCAL.get().spider.) < 100):


		DESKTOP_DRIVER_THREAD_LOCAL.get().quit();

	}
	@AfterMethod(groups = { "performance" })
	@After
	public static void PerformanceTeardown() throws ClientApiException, IOException {
		// Write the report

		ExtentTestManager.getTest().log(LogStatus.INFO, "<a href='"+System.getProperty("user.dir")+"/Reports/Performance/index.html'>Please See the Detail of Performance by Clicking this link</a>");



	}
	@AfterMethod(groups = { "api" })
	@After
	public static void APITeardown() throws ClientApiException, IOException {
		// Write the report


	}
	public static org.openqa.selenium.WebDriver getWebDriver(){
		return WEB_DRIVER_THREAD_LOCAL.get();
	}
	public static WindowsDriver getDesktopDriver(){
		return DESKTOP_DRIVER_THREAD_LOCAL.get();
	}
	public static AppiumDriver getMobileDriver(){
		return MOBILE_DRIVER_THREAD_LOCAL.get();
	}
	public RequestSpecification getApiDriver(){
		return API_DRIVER_THREAD_LOCAL.get();
	}
	public void PerformSecuityScan() throws InterruptedException, ClientApiException {
		System.out.println("--- Waiting for passive scan to complete --- "+ZAP_CANNER_THREAD_LOCAL.get());

		Zapreport.set(true);
		try {
			ZAP_CANNER_THREAD_LOCAL.get().pscan.enableAllScanners(); // enable passive scanner.

			ApiResponse response = ZAP_CANNER_THREAD_LOCAL.get().pscan.recordsToScan(); // getting a response

			//iterating till we get response as "0".
			while(!response.toString().equals("0")) {
				response =	ZAP_CANNER_THREAD_LOCAL.get().pscan.recordsToScan();
			}
		} catch (ClientApiException e1) {
			e1.printStackTrace();
		}
		System.out.println("--- Passive scan completed! ---");
	}
	public void setcompliences() throws IOException {
		System.out.println("In Accessibilty Test for:"+pr.readProperty("Accessibility_Comp"));
		switch(pr.readProperty("Accessibility_Comp"))
		{
			case "WCAG2.0A":
			{
				Accessibility.STANDARD = Standard.WCAG2A;
				break;
			}
			case "WCAG2AA":
			{
				Accessibility.STANDARD = Standard.WCAG2AA;
				break;
			}
			case "Section508":
			{
				Accessibility.STANDARD = Standard.Section508;
				break;
			}
			case "WCAG2AAA":
			default:
			{
				Accessibility.STANDARD = Standard.WCAG2AAA;
				break;
			}
		}
	}
}
