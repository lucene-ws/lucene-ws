package net.lucenews.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.junit.Before;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ClientTest {

	protected URL serviceUrl;
	
	protected HttpClient client;
	
	protected DocumentBuilder documentBuilder;
	
	@Before
	public void setup() throws MalformedURLException, ParserConfigurationException {
		serviceUrl = new URL("http://localhost:8080/lucene/");
		client = new HttpClient();
		documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	}
	
	protected String toUrl(String relativePath) {
		return serviceUrl.toString() + relativePath;
	}
	
	protected Document toDocument(HttpMethod method) throws SAXException, IOException {
		return documentBuilder.parse(method.getResponseBodyAsStream());
	}
	
}
