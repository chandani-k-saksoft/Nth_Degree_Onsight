package Reporter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.relevantcodes.extentreports.ExtentReports;

//OB: ExtentReports extent instance created here. That instance can be reachable by getReporter() method.

public class ExtentManager {

  private static ExtentReports extent;

  public synchronized static ExtentReports getReporter(){
      if(extent == null){
          //Set HTML reporting file location
          String workingDir = System.getProperty("user.dir");
          DateFormat df = new SimpleDateFormat("dd-MM-yyyy-HH_mm", Locale.ENGLISH);
          DateFormat df1 = new SimpleDateFormat("(dd-MM-yyyy)", Locale.ENGLISH);
          //File config=new File();
          extent = new ExtentReports(workingDir+"\\ExtentReports\\UNITE"+df1.format(new Date())+"\\ExtentReportResults-"+df.format(new Date())+".html", true);

        //  extent = new ExtentReports(workingDir+"/ExtentReports/ExtentReportResults.html", true);
          extent.loadConfig(new File(workingDir+"/config-report.xml"));
      }
      return extent;
  }
}
