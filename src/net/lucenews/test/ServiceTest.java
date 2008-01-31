package net.lucenews.test;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ServiceTest extends ClientTest {

	/**
	 * The initial state of the web service should be empty.
	 * @throws IOException 
	 * @throws HttpException 
	 * @throws SAXException 
	 *
	 */
	@Test
	public void testEmptyWorkspace() throws HttpException, IOException, SAXException {
		GetMethod method = new GetMethod(toUrl("/"));
		
		int statusCode = client.executeMethod(method);
		Assert.assertEquals("Status code", HttpStatus.SC_OK, statusCode);
		
		Document document = toDocument(method);
	}
	
}
