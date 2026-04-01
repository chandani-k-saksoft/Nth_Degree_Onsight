package bddRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import org.testng.Reporter;
import org.testng.annotations.*;
import utils.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Made comments...
@CucumberOptions(
        features = {"src/test/resources/FeatureFile"}
        , glue = {"pageHelper"}
        , plugin = {"pretty", "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
        , tags = "@TestDemo3"
       , dryRun = false

)

public class RunnerTest extends AbstractTestNGCucumberTests {

    static String platform, environment, buildFolder = "";
    static boolean rerun = false;
    static boolean appInstalled = true;
    static String PAT = "";
    static String toEmails = "";

    @BeforeSuite
    public static void setupReportName() throws IOException {
        platform = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("platform");
        environment = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("environment");
        buildFolder = environment.toUpperCase() + "_" + platform;
        System.setProperty("appInstalled", String.valueOf(appInstalled));
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        Object[][] original = super.scenarios();
        if (platform.equalsIgnoreCase("Android")) {
            Arrays.sort(original, Comparator.comparing(o -> {
                PickleWrapper pickleWrapper = (PickleWrapper) o[0];
                String uri = pickleWrapper.getPickle().getUri().toString();
                String fileName = uri.substring(uri.lastIndexOf('/') + 1);

                Pattern p = Pattern.compile("^(\\d+)_");
                Matcher m = p.matcher(fileName);
                return m.find() ? Integer.parseInt(m.group(1)) : Integer.MAX_VALUE;
            }));
        } else {
            if (!Boolean.parseBoolean(System.getProperty("parallel"))) {
                Arrays.sort(original, Comparator.comparing(o -> {
                    PickleWrapper pickleWrapper = (PickleWrapper) o[0];
                    String uri = pickleWrapper.getPickle().getUri().toString();
                    String fileName = uri.substring(uri.lastIndexOf('/') + 1);

                    Pattern p = Pattern.compile("^(\\d+)_");
                    Matcher m = p.matcher(fileName);
                    return m.find() ? Integer.parseInt(m.group(1)) : Integer.MAX_VALUE;
                }));
            } else {
                Map<String, List<Object[]>> groupedByFile = new TreeMap<>(Comparator.comparingInt(fileName -> {
                    var matcher = Pattern.compile("^(\\d+)_").matcher(fileName);
                    return matcher.find() ? Integer.parseInt(matcher.group(1)) : Integer.MAX_VALUE;
                }));

                for (Object[] scenario : original) {
                    PickleWrapper pw = (PickleWrapper) scenario[0];
                    String uri = pw.getPickle().getUri().toString();
                    String fileName = uri.substring(uri.lastIndexOf('/') + 1);

                    groupedByFile.computeIfAbsent(fileName, k -> new ArrayList<>()).add(scenario);
                }

                List<Object[]> reversed = new ArrayList<>();
                List<String> reversedFileNames = new ArrayList<>(groupedByFile.keySet());
                Collections.reverse(reversedFileNames);

                for (String file : reversedFileNames) {
                    reversed.addAll(groupedByFile.get(file));
                }

                original = reversed.toArray(new Object[0][]);
            }
        }
        return original;
    }

    @Override
    @Test(dataProvider = "scenarios")
    public void runScenario(PickleWrapper pickle, FeatureWrapper feature) {
        super.runScenario(pickle, feature);
    }

    @BeforeTest
    public void setup() throws Exception {
        String tagsExecuting, tagsExecutingFinal, rerunStr = "";
        String today = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
        File report_folder = new File(System.getProperty("user.dir") + "/Reports");
        File report_folder_with_date = new File(System.getProperty("user.dir") + "/Reports/" + today);

        CucumberOptions options = RunnerTest.class.getAnnotation(CucumberOptions.class);
        tagsExecuting = "(" + options.tags() + ") and @" + platform;

        if (environment.equalsIgnoreCase("uat") || environment.equalsIgnoreCase("prod"))
            tagsExecuting = tagsExecuting + " and @" + environment.toUpperCase();

        LinkedHashSet<String> uniqueTags = new LinkedHashSet<>();
        for (String tag : tagsExecuting.split("and")) {
            uniqueTags.add(tag.trim());
        }
        tagsExecutingFinal = String.join(" and ", uniqueTags);
        String plugins = String.join(", ", options.plugin());

        System.setProperty("cucumber.filter.tags", tagsExecutingFinal);

        System.out.println("Executing Tags: " + System.getProperty("cucumber.filter.tags"));

        platform = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("platform");
        rerunStr = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("reRun");
        environment = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("environment");

        rerun = rerunStr == null ? rerun : Boolean.parseBoolean(rerunStr);

        if (!report_folder.exists())
            report_folder.mkdirs();
        if (!report_folder_with_date.exists())
            report_folder_with_date.mkdirs();

        File uniqueFile_with_date = ReportHelper.getUniqueFile(report_folder_with_date.getAbsolutePath(), buildFolder + "_Execution_Report", ".html");
        System.setProperty("extent.reporter.spark.out", uniqueFile_with_date.getAbsolutePath());

        AppiumSessionManager.startAppiumSession(Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("appium_url"));

    }

    @AfterTest
    public void stopAppiumSession() throws Exception {
        AppiumSessionManager.stopAppiumSession();
//        if (rerun) {
            String filePath = System.getProperty("extent.reporter.spark.out");
            File inputFile = new File(filePath);
    }
}