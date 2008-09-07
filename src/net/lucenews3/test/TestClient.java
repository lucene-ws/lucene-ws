package net.lucenews3.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

public class TestClient {

	private HttpClient httpClient;
	private AtomParser atomParser;
	private XMLInputFactory xmlInputFactory;
	private XMLOutputFactory xmlOutputFactory;

	public TestClient() {
		this.httpClient = new HttpClient();
		this.atomParser = new AtomParser();
		this.xmlInputFactory = XMLInputFactory.newInstance();
		this.xmlOutputFactory = XMLOutputFactory.newInstance();
	}

	protected XMLStreamReader toXMLStreamReader(HttpMethod method) throws XMLStreamException, IOException {
		return xmlInputFactory.createXMLStreamReader(method.getResponseBodyAsStream());
	}

	protected boolean isException(HttpMethod method) {
		return method.getStatusCode() != 200;
	}

	protected void handleException(HttpMethod method) throws XMLStreamException, IOException {
		atomParser.parse(toXMLStreamReader(method));
	}

	@Test
	public void testCreateIndex() throws HttpException, IOException, XMLStreamException, URISyntaxException {
		createIndex(new URI("http://localhost:8080/lucene/"), "test", new Properties());
	}

	public void createIndex(URI serviceURI, String indexName, Properties indexProperties) throws HttpException, IOException, XMLStreamException {
		PostMethod method = new PostMethod(serviceURI.toString());
		
		httpClient.executeMethod(method);
		
		if (isException(method)) {
			handleException(method);
		} else {
			byte[] responseBody = method.getResponseBody();
			System.out.write(responseBody);
			
			atomParser.parseEntry(xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(responseBody)));
		}
	}

}
