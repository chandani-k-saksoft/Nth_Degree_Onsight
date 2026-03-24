package utils;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import io.restassured.response.Response;

/**
 * 
 * @author Sadik Ali
 *
 */
public class XMLUtils {
		
	/**
	 * This function read all values from response and return list of string
	 * @param response : API response 
	 * @param nodePath : node xpath
	 * @return : return list of node values
	 * @throws DocumentException
	 */
	public static List<String> getNodeValues(Response response, String nodePath) throws DocumentException {
		SAXReader xmlreader=new SAXReader();
		Document doc = xmlreader.read(response.asInputStream());
		@SuppressWarnings("unchecked")
		List<Node> nodes = doc.selectNodes(nodePath);
		List<String> nodeValues = new ArrayList<String>();
		for(Node node : nodes ) {
			String value = node.getText();
			nodeValues.add(value);
		}
		return nodeValues;
	}
	
	/**
	 * This function read single node value from response and return string
	 * @param response : API response 
	 * @param nodePath : node xpath
	 * @return : return string value of node
	 * @throws DocumentException
	 */
	public static String getNodeValue(Response response, String nodePath) throws DocumentException {
		SAXReader xmlreader=new SAXReader();
		Document doc = xmlreader.read(response.asInputStream());
		Node node = doc.selectSingleNode(nodePath); 
		String value = node.getText();
		return value;
	}

}
