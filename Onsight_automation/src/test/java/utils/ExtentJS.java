package utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExtentJS {
		public static void ExtentJS() throws IOException, InvalidFormatException {
			String reportpath = "target\\cucumber-reports\\DIT\\report.html";

			String jsString = new String(Files.readAllBytes(Paths.get("lib\\extent.txt")));
			String completeString = new String(Files.readAllBytes(Paths.get(reportpath)));

			String v1 = "https://cdn.rawgit.com/anshooarora/extentreports-java";
			String v2 = "/dist/js/extent.js' type='text/javascript'></script>";
			String valueToReplace = completeString.substring(completeString.lastIndexOf(v1),completeString.lastIndexOf(v2)+v2.length());
			System.out.println(valueToReplace);
			completeString = completeString.replace("<script src='"+valueToReplace, "<script>"+jsString+"</script>");
			Files.write(Paths.get(reportpath), completeString.getBytes());

		}


}
