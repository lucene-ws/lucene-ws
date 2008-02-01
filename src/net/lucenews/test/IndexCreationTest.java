package net.lucenews.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import net.lucenews.LuceneWebService;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.LockObtainFailedException;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;

public class IndexCreationTest extends ClientTest {

	/**
	 * Creates an index manually, then starts up the service.
	 * @throws IOException 
	 * @throws LockObtainFailedException 
	 * @throws CorruptIndexException 
	 * @throws SAXException 
	 * @throws XPathExpressionException 
	 */
	@Test
	public void testStartupManualCreation() throws CorruptIndexException, LockObtainFailedException, IOException, SAXException, XPathExpressionException {
		File temp = fileSystem.getTemporaryDirectory();
		
		String indexName = "testindex";
		
		File indexDir = new File(temp, indexName);
		IndexWriter writer = lucene.getTemporaryIndexWriter(indexDir);
		writer.addDocument(lucene.buildDocument(toMap("id", 5)));
		writer.close();
		
		runner = new ServletRunner();
		runner.registerServlet("lucene", LuceneWebService.class.getName(), toMap("directory", temp.getCanonicalPath()));
		client = runner.newClient();
		
		WebRequest request = new GetMethodWebRequest("http://localhost/lucene/testindex/opensearchdescription.xml");
		WebResponse response = client.getResponse(request);
		
		org.w3c.dom.Document document = toDocument(response);
		//introspectionDocumentAsserter.assertIntrospectionDocument(document);
		
		List<Element> collections = dom.elementsByPath(document, "/service/workspace/collection");
		Assert.assertEquals("# of collections", 1, collections.size());
		Element collection = collections.get(0);
		
		List<Element> titles = dom.elementsByPath(collection, "./title");
		Assert.assertEquals("# of titles", 1, titles.size());
		Element title = titles.get(0);
		
		Assert.assertEquals("collection title", indexName, dom.innerText(title));
	}
	
	@Test
	public void testAutomaticCreation() throws IOException, SAXException, XPathExpressionException {
		File temp = fileSystem.getTemporaryDirectory();
		
		runner = new ServletRunner();
		runner.registerServlet("lucene", LuceneWebService.class.getName(), toMap("directory", temp.getCanonicalPath()));
		client = runner.newClient();
		
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
		
		PostMethodWebRequest request = new PostMethodWebRequest("http://localhost/lucene", new ByteArrayInputStream(buffer.toString().getBytes()), "text/xml");
		WebResponse response = client.getResponse(request);
		
		org.w3c.dom.Document document = toDocument(response);
		entryAsserter.assertEntry(document.getDocumentElement());
	}
	
	public GetMethodWebRequest getIndexRequest(String indexName) {
		return new GetMethodWebRequest("http://localhost/lucene/" + indexName);
	}
	
}
