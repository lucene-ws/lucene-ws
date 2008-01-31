package net.lucenews.test;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.httpclient.HttpException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebResponse;

public class ServiceTest extends ClientTest {

	private IntrospectionDocumentAsserter introspectionDocumentAsserter;

	public ServiceTest() {
		introspectionDocumentAsserter = new IntrospectionDocumentAsserter();
	}

	@Before
	public void setup() throws Exception {
		super.setup();
	}

	/**
	 * Test that the XML produced by the web service is a valid Atom Publishing
	 * Protocol introspection document.
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */
	@Test
	public void testXml() throws MalformedURLException, IOException,
			SAXException, XPathExpressionException {
		GetMethodWebRequest request = getServiceRequest();
		WebResponse response = client.getResponse(request);
		Document document = toDocument(response);
		introspectionDocumentAsserter.assertIntrospectionDocument(document);
	}

	/**
	 * The initial state of the web service should be empty.
	 * 
	 * @throws IOException
	 * @throws HttpException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 * 
	 */
	@Test
	public void testWorkspaceCount() throws HttpException, IOException,
			SAXException, XPathExpressionException {
		GetMethodWebRequest request = getServiceRequest();
		WebResponse response = client.getResponse(request);
		Document document = toDocument(response);
		Assert.assertEquals("# of workspaces", 1, dom.elementsByPath(document,
				"/service/workspace").size());
	}

	/**
	 * The initial web service should have no collections as of yet.
	 * 
	 * @throws HttpException
	 * @throws IOException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 */
	@Test
	public void testCollectionCount() throws HttpException, IOException,
			SAXException, XPathExpressionException {
		GetMethodWebRequest request = getServiceRequest();
		WebResponse response = client.getResponse(request);
		Document document = toDocument(response);
		Assert.assertEquals("# of collections", 0, dom.elementsByPath(document,
				"/service/workspace/collection").size());
	}

	public GetMethodWebRequest getServiceRequest() {
		return new GetMethodWebRequest("http://localhost/lucene");
	}

}
