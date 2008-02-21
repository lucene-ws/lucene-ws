package net.lucenews3.test;

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
	 * Creates an index locally, then starts up the service.
	 * @throws IOException 
	 * @throws LockObtainFailedException 
	 * @throws CorruptIndexException 
	 * @throws SAXException 
	 * @throws XPathExpressionException 
	 * @throws ServletException 
	 */
	@Test
	public void testLocalPreInitCreation() throws Exception {
		File temp = fileSystem.getTemporaryDirectory();
		
		String indexName = "testindex";
		
		File indexDir = new File(temp, indexName);
		IndexWriter writer = lucene.getTemporaryIndexWriter(indexDir);
		writer.addDocument(lucene.buildDocument(toMap("id", 5)));
		writer.close();
		
		container.setInitialParameter("directory", temp.getCanonicalPath());
		
		HttpResponse response = http.sendRequest("http://localhost/lucene").getResponse();
		
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
	public void testRemoteCreation() throws Exception {
		File temp = fileSystem.getTemporaryDirectory();
		
		container.setInitialParameter("directory", temp.getCanonicalPath());
		
		StringBuffer body = new StringBuffer();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		body.append("<entry xmlns=\"http://www.w3.org/2005/Atom\">");
		body.append("<title>testindex04</title>");
		body.append("<content type=\"xhtml\">");
		body.append("<div xmlns=\"http://www.w3.org/1999/xhtml\">");
		body.append("<dl class=\"xoxo\">");
		body.append("<dt>index.title</dt>");
		body.append("<dd>Yippee!</dd>");
		body.append("</dl>");
		body.append("</div>");
		body.append("</content>");
		body.append("</entry>");
		
		HttpRequest request = http.buildRequest("POST", "http://localhost/lucene");
		http.populateBody(request, body);
		HttpResponse response = http.send(request).getResponse();
		
		org.w3c.dom.Document document = toDocument(response);
		
		if (strict) {
			entryAsserter.assertEntry(document.getDocumentElement());
		}
		
		Assert.assertEquals("response status", HttpStatus.SC_CREATED, response.getStatus());
		
		String location = response.getHeaders().byKey().get("Location").only();
		
		Assert.assertEquals("index location", "http://localhost/lucene/testindex04", location);
		
		HttpResponse getRes = http.sendRequest("http://localhost/lucene/testindex04").getResponse();
		
		Assert.assertEquals("found index page", HttpStatus.SC_OK, getRes.getStatus());
		
		org.w3c.dom.Document getDoc = toDocument(getRes);
		Element feed = getDoc.getDocumentElement();
		
		feedAsserter.assertFeed(feed);
		
		String title = dom.innerText(dom.elementByPath(feed, "./title"));
		
		Assert.assertEquals("index title", "Yippee!", title);
		
		String id = dom.innerText(dom.elementByPath(feed, "./id"));
		
		Assert.assertEquals("index id", "http://localhost/lucene/testindex04", id);
	}
	
	@Test
	public void testRemoteConflictingCreation() throws Exception {
		File temp = fileSystem.getTemporaryDirectory();
		
		String indexName = "testindex";
		
		File indexDir = new File(temp, indexName);
		IndexWriter writer = lucene.getTemporaryIndexWriter(indexDir);
		writer.addDocument(lucene.buildDocument(toMap("id", 5)));
		writer.close();
		
		StringBuffer body = new StringBuffer();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		body.append("<entry xmlns=\"http://www.w3.org/2005/Atom\">");
		body.append("<title>" + indexName + "</title>");
		body.append("<content type=\"xhtml\">");
		body.append("<div xmlns=\"http://www.w3.org/1999/xhtml\">");
		body.append("<dl class=\"xoxo\">");
		body.append("<dt>index.title</dt>");
		body.append("<dd>Yippee!</dd>");
		body.append("</dl>");
		body.append("</div>");
		body.append("</content>");
		body.append("</entry>");

		HttpRequest request = http.buildRequest("POST", "http://localhost/lucene");
		http.populateBody(request, body);
		HttpResponse response = http.send(request).getResponse();
		
		Assert.assertEquals("response status", HttpStatus.SC_CONFLICT, response.getStatus());
		
	}
	
	public GetMethodWebRequest getIndexRequest(String indexName) {
		return new GetMethodWebRequest("http://localhost/lucene/" + indexName);
	}
	
}
