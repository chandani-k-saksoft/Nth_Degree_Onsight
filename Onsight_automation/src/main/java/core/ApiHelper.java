package core;

import com.google.gson.JsonElement;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.dom4j.DocumentException;
import org.json.simple.JSONObject;
import org.xml.sax.SAXException;

public interface ApiHelper {

	int RESPONSE_CODE_200 = 200;
	int RESPONSE_CODE_201 = 201;
	int RESPONSE_CODE_400 = 400;
	int RESPONSE_CODE_401 = 401;
	int RESPONSE_CODE_204 = 204;
	int RESPONSE_CODE_404 = 404;
	int RESPONSE_CODE_202 = 202;
	int RESPONSE_CODE_500 = 500;

	/**
	 * Method to authenticate requests.
	 *
	 * @param type     - Type of authentication (e.g., "Basic", "preemptive").
	 * @param username - Username for authentication.
	 * @param password - Password or token for authentication.
	 */
	void authentication(String type, String username, String password);

	/**
	 * Method to update request header attributes.
	 *
	 * @param HeaderKey - The name of the header key to be updated.
	 * @param Value     - The new value for the header key.
	 */
	void updateRequestHeader(String HeaderKey, String Value);

	/**
	 * Method to add request parameters from a map.
	 *
	 * @param paramsMap - Map of parameters to be added to the request.
	 */
	void addRequestParameters(Map<String, String> paramsMap);

	/**
	 * Method to set the content type of the request.
	 *
	 * @param contentType - The content type to be set (e.g., "application/json").
	 */
	void setContentType(String contentType);

	/**
	 * Method to set content type for RestAssured.
	 *
	 * @param contentType - The content type to be set.
	 */
	void setContentTypeRestAssured(ContentType contentType);

	/**
	 * Method to read the request template from a file.
	 *
	 * @param path - Path to the request template file.
	 * @return The content of the request template.
	 * @throws IOException If an I/O error occurs.
	 */
	String readRequestTemplate(String path) throws IOException;

	/**
	 * Method to update an attribute in the request body.
	 *
	 * @param Filename - Path to the request template file.
	 * @param Key      - Key or node name in the request body.
	 * @param Value    - New value for the attribute.
	 * @throws IOException                  If an I/O error occurs.
	 * @throws SAXException                If a SAX error occurs.
	 * @throws ParserConfigurationException If a parser configuration error occurs.
	 * @throws Exception                   If a general error occurs.
	 */
	void updateAttributeInRequestBody(String Filename, String Key, String Value) throws IOException, SAXException, ParserConfigurationException, Exception;

	/**
	 * Method to update multiple attributes in the request body.
	 *
	 * @param Filename - Path to the request template file.
	 * @param values   - Map of attributes to be updated.
	 * @throws IOException                  If an I/O error occurs.
	 * @throws SAXException                If a SAX error occurs.
	 * @throws ParserConfigurationException If a parser configuration error occurs.
	 * @throws Exception                   If a general error occurs.
	 */
	void updateAttributeInRequestBody(String Filename, HashMap<String, String> values) throws IOException, SAXException, ParserConfigurationException, Exception;

	/**
	 * Method to generate the final payload with updated input data.
	 */
	void generatePayLoad();
	void addPayloadToRequest(org.json.JSONObject requestBody);

	/**
	 * Method to submit a request to the server.
	 *
	 * @param method - HTTP method to be used (e.g., GET, POST).
	 * @param URI    - Service URI (e.g., "/Create").
	 */
	void submitRequest(Method method, String URI);

	/**
	 * Method to validate the JSON schema of the response.
	 *
	 * @param path - Path to the JSON schema file.
	 */
	void validateResponseJsonSchema(String path);

	/**
	 * Method to validate the XML schema of the response.
	 *
	 * @param path - Path to the XML schema file.
	 */
	void validateResponseXMLSchema(String path);

	/**
	 * Method to assert that a specific string is present in the response body.
	 *
	 * @param ExpectedData - Expected string to be contained in the response body.
	 */
	void assertStringInResponseBody(String ExpectedData);

	/**
	 * Method to assert the status code of the response.
	 *
	 * @param ExpectedStatusCode - Expected status code returned by the service call.
	 */
	void assertStatusCode(int ExpectedStatusCode);

	/**
	 * Method to assert the status line of the response.
	 *
	 * @param ExpectedStatusLine - Expected status line returned by the service call.
	 */
	void assertStatusLine(String ExpectedStatusLine);

	/**
	 * Method to assert the value of a specific header key in the response.
	 *
	 * @param HeaderName          - Name of the header key to be verified.
	 * @param ExpectedheaderValue - Expected value for the header key.
	 */
	void assertHeaderattribute(String HeaderName, String ExpectedheaderValue);

	/**
	 * Method to assert the value of a specific body key in the response.
	 *
	 * @param Node          - Name of the body key to be verified.
	 * @param Expectedvalue - Expected value for the body key.
	 * @throws SAXException                If a SAX error occurs.
	 * @throws IOException                 If an I/O error occurs.
	 * @throws ParserConfigurationException If a parser configuration error occurs.
	 * @throws DocumentException           If a document error occurs.
	 */
	void assertResponseBodyAttribute(String Node, String Expectedvalue) throws SAXException, IOException, ParserConfigurationException, DocumentException;

	/**
	 * Method to save the value of a specific body key from the response.
	 *
	 * @param Node - Name of the body key to be saved.
	 * @return The value of the specified body key.
	 * @throws DocumentException If a document error occurs.
	 */
	String SaveAttributevalue(String Node) throws DocumentException;

	/**
	 * Method to generate JSON input based on a template.
	 *
	 * @param templatefile - JSON input template.
	 * @param Node         - Name of the node to be updated.
	 * @param Value        - New value for the node.
	 * @return Updated JSON object.
	 */
	JSONObject Inputgenerator(JSONObject templatefile, String Node, String Value);

	/**
	 * Method to append parameters to a URI.
	 *
	 * @param uri        - Base URI.
	 * @param headerMap  - Map of parameters to be appended.
	 * @return Updated URI with parameters.
	 */
	String appendUriWithParameters(String uri, Map<String, String> headerMap);

	/**
	 * Method to get the base URI.
	 *
	 * @param uri - Base URI to be returned.
	 * @return The base URI.
	 */
	String getBaseURI(String uri);

	/**
	 * Method to read the payload from a file.
	 *
	 * @param Requestpath - Path to the payload file.
	 */
	void readPayload(String Requestpath);

	/**
	 * Method to get a single value from the response based on a node name.
	 *
	 * @param node - Name of the node.
	 * @return Value of the specified node.
	 */
	String getSingleValueFromResponse(String node);

	/**
	 * Method to compare two strings.
	 *
	 * @param actual   - Actual string value.
	 * @param expected - Expected string value.
	 */
	void Stringcomparator(String actual, String expected);

	/**
	 * Method to update an attribute in an XML payload.
	 *
	 * @param Node  - Name of the node to be updated.
	 * @param Value - New value for the node.
	 * @throws DocumentException                If a document error occurs.
	 * @throws IOException                      If an I/O error occurs.
	 * @throws SAXException                    If a SAX error occurs.
	 * @throws ParserConfigurationException    If a parser configuration error occurs.
	 */
	void UpdatedAttributeinxmlPayload(String Node, String Value) throws DocumentException, IOException, SAXException, ParserConfigurationException;

	/**
	 * Method to update an attribute in a JSON payload.
	 *
	 * @param Node    - Name of the node to be updated.
	 * @param Value   - New value for the node.
	 * @param isstring - Indicates if the value is a string.
	 * @throws DocumentException                If a document error occurs.
	 * @throws IOException                      If an I/O error occurs.
	 * @throws SAXException                    If a SAX error occurs.
	 * @throws ParserConfigurationException    If a parser configuration error occurs.
	 */
	void UpdatedAttributeinJsonPayload(String Node, String Value, boolean isstring) throws DocumentException, IOException, SAXException, ParserConfigurationException;

	/**
	 * Method to submit a request with headers.
	 *
	 * @param method - HTTP method to be used (e.g., GET, POST).
	 * @param URI    - Service URI (e.g., "/Create").
	 */
	void submitRequestWithHeader(Method method, String URI);

	/**
	 * Method to assert the presence of a node in the response.
	 *
	 * @param Node    - Name of the node to be checked.
	 * @param expected - Expected presence (true/false) of the node.
	 * @throws SAXException                If a SAX error occurs.
	 * @throws IOException                 If an I/O error occurs.
	 * @throws ParserConfigurationException If a parser configuration error occurs.
	 * @throws DocumentException           If a document error occurs.
	 */
	void assertNodeIsPresent(String Node, boolean expected) throws SAXException, IOException, ParserConfigurationException, DocumentException;

	/**
	 * Method to compare two boolean values.
	 *
	 * @param actual   - Actual boolean value.
	 * @param expected - Expected boolean value.
	 */
	void Booleancomparator(Boolean actual, Boolean expected);

	/**
	 * Method to encode data into Base64 format.
	 *
	 * @param data - Data to be encoded.
	 * @return Base64 encoded data.
	 * @throws SAXException                If a SAX error occurs.
	 * @throws IOException                 If an I/O error occurs.
	 * @throws ParserConfigurationException If a parser configuration error occurs.
	 * @throws DocumentException           If a document error occurs.
	 */
	String Base64Encoder(String data) throws SAXException, IOException, ParserConfigurationException, DocumentException;

	/**
	 * Method to return the current payload.
	 *
	 * @return Current payload.
	 * @throws SAXException                If a SAX error occurs.
	 * @throws IOException                 If an I/O error occurs.
	 * @throws ParserConfigurationException If a parser configuration error occurs.
	 * @throws DocumentException           If a document error occurs.
	 */
	String ReturnPaylod() throws SAXException, IOException, ParserConfigurationException, DocumentException;

	/**
	 * Method to validate date format.
	 *
	 * @param dateStr     - Date string to be validated.
	 * @param dateFormat  - Expected date format.
	 * @return True if the date format is valid, otherwise false.
	 */
	boolean isValidDateFormat(String dateStr, String dateFormat);

	/**
	 * Method to perform partial comparison of two strings.
	 *
	 * @param actual   - Actual string value.
	 * @param expected - Expected string value.
	 */
	void Partialstringcomparator(String actual, String expected);

	/**
	 * Method to perform exact comparison of two JSON objects.
	 *
	 * @param actual   - Actual JSON object.
	 * @param expected - Expected JSON object.
	 */
	void Jsoncomparator(JsonElement actual, JsonElement expected);

	/**
	 * Method to generate multipart form data.
	 *
	 * @param key   - Name of the form data key.
	 * @param Value - Value of the form data key.
	 */
	void generateMultipart(String key, String Value);

	/**
	 * Method to return the current response.
	 *
	 * @return Current response.
	 */
	String getResponse();

	/**
	 * Method to get today's date in a specified format.
	 *
	 * @param dateFormat - Format of the date.
	 * @return Today's date in the specified format.
	 */
	String getTodaysDateinFormat(String dateFormat);

	/**
	 * Method to create an order number.
	 *
	 * @return Generated order number.
	 */
	String createOrderNumber();
	String getResponseBody();

}
