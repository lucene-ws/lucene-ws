package net.lucenews.test;

import java.io.File;
import java.util.List;
import java.util.Map;

import net.lucenews.http.HttpResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class IndexGetTest extends ClientTest {

	@Test
	public void testEmptyIndex() throws Exception {
		doTestIndexListing("testindex01");
	}
	
	@Test
	public void testSingleDocumentIndex() throws Exception {
		doTestIndexListing("testindex01", toMap("foo", "bar"), toMap("cat", "dog"));
	}
	
	public void doTestIndexListing(final String indexName, final Map<?, ?>... documents) throws Exception {
		final File root = fileSystem.getTemporaryDirectory();
		
		final File index = new File(root, indexName);
		lucene.buildIndex(index, documents);
		
		container.setInitialParameter("directory", root.getCanonicalPath());
		
		final HttpResponse response = http.sendRequest("http://localhost/lucene/" + indexName).getResponse();
		final Document document = toDocument(response);
		final Element feed = dom.elementByPath(document, "/feed");
		
		Assert.assertEquals("response status", HttpStatus.SC_OK, response.getStatus());
		
		feedAsserter.assertFeed(feed);
		
		List<Element> entries = dom.elementsByPath(document, "/feed/entry");
		
		Assert.assertEquals("# of entries", documents.length, entries.size());
		
		for (int i = documents.length - 1; i >= 0; i--) {
			// TODO: final Map<?, ?> doc = documents[i];
		}
	}
	
}
