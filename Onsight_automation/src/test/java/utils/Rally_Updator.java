package utils;

import java.sql.Date;

import org.apache.http.params.CoreConnectionPNames;
import org.json.simple.JSONObject;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class Rally_Updator
{
	static String base_url = "https://rally1.rallydev.com/slm/webservice/v2.0";
	public  void Create(String Buildnumber, String testcaseID, String status) {
		
				RestAssured.baseURI =base_url;
				RequestSpecification request = RestAssured.given();
				String payload = "{\r\n  \"TestCaseResult\": {\r\n    \"Build\": \"" + Buildnumber + "\",\r\n    \"Date\": \"" + new Date(0) + "\",\r\n    \"Project\": {\r\n      \"_rallyAPIMajor\": \"2\",\r\n      \"_rallyAPIMinor\": \"0\",\r\n      \"_ref\": \"null\",\r\n      \"_refObjectUUID\": \"null\",\r\n      \"_refObjectName\": \"SPQR\",\r\n      \"_type\": \"Project\"\r\n    },\r\n    \"TestCase\": {\r\n      \"_rallyAPIMajor\": \"2\",\r\n      \"_rallyAPIMinor\": \"0\",\r\n      \"_ref\": \"https://rally1.rallydev.com/slm/webservice/v2.0/testcase/" + testcaseID + "\",\r\n      \"_refObjectUUID\": \"null\",\r\n      \"_refObjectName\": \"null\",\r\n      \"_type\": \"TestCase\"\r\n    },\r\n    \"Tester\": {\r\n      \"_rallyAPIMajor\": \"2\",\r\n      \"_rallyAPIMinor\": \"0\",\r\n      \"_ref\": \"https://rally1.rallydev.com/slm/webservice/v2.0/user/410140620040\",\r\n      \"_refObjectUUID\": \"0fd56e2f-48ea-4d84-9518-3c3a7c4b9499\",\r\n      \"_refObjectName\": \"naved.a\",\r\n      \"_type\": \"User\"\r\n    },\r\n    \"Verdict\": \"" + status + "\"\r\n  }\r\n}";
				System.out.println(payload);
				request.header("Content-Type", "application/json");
				request.header("zsessionid", "_9rqq72ETkyc3nbrkdomDKTIq8tULt6GWobP4rgg");
				request.body(payload);
				 
				// Post the request and check the response
				Response response = request.post("/testcaseresult/create");
				
	}
	public static void main(String[] srgs) {
		Rally_Updator rlu=new Rally_Updator();
		rlu.Create("Buildnumber", "413450412168", "Pass");
		
		
}
}