package net.lucenews.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.xml.xpath.XPathExpressionException;

import net.lucenews.http.HttpRequest;
import net.lucenews.http.HttpResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.LockObtainFailedException;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;

public class IndexCreationTest extends ClientTest {

	/**
	 * Creates an index manually, then starts up the service.
	 * @throws IOException 
	 * @throws LockObtainFailedException 
	 * @throws CorruptIndexException 
	 * @throws SAXException 
	 * @throws XPathExpressionException 
	 * @throws ServletException 
	 */
	@Test
	public void testStartupManualCreation() throws Exception {
		File temp = fileSystem.getTemporaryDirectory();
		
		String indexName = "testindex";
		
		File indexDir = new File(temp, indexName);
		IndexWriter writer = lucene.getTemporaryIndexWriter(indexDir);
		writer.addDocument(lucene.buildDocument(toMap("id", 5)));
		writer.close();
		
		container.setInitialParameter("directory", temp.getCanonicalPath());
		
		HttpRequest request = getRequest("http://localhost/lucene");
		HttpResponse response = getResponse(request);
		
		org.w3c.dom.Document document = toDocument(response);
		
		if (strict) {
			introspectionDocumentAsserter.assertIntrospectionDocument(document);
		}
		
		List<Element> collections = dom.elementsByPath(document, "/service/workspace/collection");
		Assert.assertEquals("# of collections", 1, collections.size());
		Element collection = collections.get(0);
		
		List<Element> titles = dom.elementsByPath(collection, "./title");
		Assert.assertEquals("# of titles", 1, titles.size());
		Element title = titles.get(0);
		
		Assert.assertEquals("collection title", indexName, dom.innerText(title));
	}
	
	@Test
	public void testAutomaticCreation() throws Exception {
		File temp = fileSystem.getTemporaryDirectory();
		
		container.getInitialParameters().put("directory", temp.getCanonicalPath());
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffer.append("<entry xmlns=\"http://www.w3.org/2005/Atom\">");
		buffer.append("<title>testindex04</title>");
		buffer.append("<content type=\"xhtml\">");
		buffer.append("<div xmlns=\"http://www.w3.org/1999/xhtml\">");
		buffer.append("<dl class=\"xoxo\">");
		buffer.append("<dt>index.title</dt>");
		buffer.append("<dd>Yippee!</dd>");
		buffer.append("</dl>");
		buffer.append("</div>");
		buffer.append("</content>");
		buffer.append("</entry>");
		
		HttpRequest request = postRequest("http://localhost/lucene");
		request.getBody().put(buffer.toString().getBytes());
		HttpResponse response = getResponse(request);
		
		org.w3c.dom.Document document = toDocument(response);
		
		if (strict) {
			entryAsserter.assertEntry(document.getDocumentElement());
		}
		
		Assert.assertEquals("response status", HttpStatus.SC_CREATED, response.getStatus());
	}
	
	@Test
	public void testAutomaticConflictingCreation() throws Exception {
		File temp = fileSystem.getTemporaryDirectory();
		
		String indexName = "testindex";
		
		File indexDir = new File(temp, indexName);
		IndexWriter writer = lucene.getTemporaryIndexWriter(indexDir);
		writer.addDocument(lucene.buildDocument(toMap("id", 5)));
		writer.close();
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffer.append("<entry xmlns=\"http://www.w3.org/2005/Atom\">");
		buffer.append("<title>" + indexName + "</title>");
		buffer.append("<content type=\"xhtml\">");
		buffer.append("<div xmlns=\"http://www.w3.org/1999/xhtml\">");
		buffer.append("<dl class=\"xoxo\">");
		buffer.append("<dt>index.title</dt>");
		buffer.append("<dd>Yippee!</dd>");
		buffer.append("</dl>");
		buffer.append("</div>");
		buffer.append("</content>");
		buffer.append("</entry>");

		HttpRequest request = postRequest("http://localhost/lucene");
		request.getBody().put(buffer.toString().getBytes());
		HttpResponse response = getResponse(request);
		
		Assert.assertEquals("response status", HttpStatus.SC_CONFLICT, response.getStatus());
		
	}
	
	public GetMethodWebRequest getIndexRequest(String indexName) {
		return new GetMethodWebRequest("http://localhost/lucene/" + indexName);
	}
	
}
