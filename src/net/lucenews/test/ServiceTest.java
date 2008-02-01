package net.lucenews.test;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.ServletException;
import javax.xml.xpath.XPathExpressionException;

import net.lucenews.http.HttpRequest;
import net.lucenews.http.HttpResponse;

import org.apache.commons.httpclient.HttpException;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ServiceTest extends ClientTest {

	private IntrospectionDocumentAsserter introspectionDocumentAsserter;

	public ServiceTest() {
		introspectionDocumentAsserter = new IntrospectionDocumentAsserter();
	}

	/**
	 * Test that the XML produced by the web service is a valid Atom Publishing
	 * Protocol introspection document.
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 * @throws ServletException 
	 */
	@Test
	public void testXml() throws Exception {
		HttpRequest request = getServiceRequest();
		HttpResponse response = getResponse(request);
		Document document = toDocument(response);
		if (strict) {
			introspectionDocumentAsserter.assertIntrospectionDocument(document);
		}
	}

	/**
	 * The initial state of the web service should be empty.
	 * 
	 * @throws IOException
	 * @throws HttpException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 * @throws ServletException 
	 * 
	 */
	@Test
	public void testWorkspaceCount() throws Exception {
		HttpRequest request = getServiceRequest();
		HttpResponse response = getResponse(request);
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
	 * @throws ServletException 
	 */
	@Test
	public void testCollectionCount() throws Exception {
		HttpRequest request = getServiceRequest();
		HttpResponse response = getResponse(request);
		Document document = toDocument(response);
		Assert.assertEquals("# of collections", 0, dom.elementsByPath(document,
				"/service/workspace/collection").size());
	}

	public HttpRequest getServiceRequest() {
		return getRequest("http://localhost/lucene");
	}

}
