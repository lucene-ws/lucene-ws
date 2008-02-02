package net.lucenews.test;

import java.util.Map;

import net.lucenews.http.HttpResponse;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DocumentGetTest extends ClientTest {

	@Test
	public void testDocumentRetrieval() throws Exception {
		String indexName = client.getRandomIndexName();
		client.createIndex(indexName);
		
		Map<?, ?> document = toMap("id", 5);
		client.createDocument(indexName, document);
		
		HttpResponse response = http.sendRequest("http://localhost/lucene/" + indexName + "/5").getResponse();
		
		http.assertOk(response);
		
		Document domdoc = toDocument(response);
		Element entry = dom.elementByPath(domdoc, "/entry");
		//entryAsserter.assertEntry(entry);
	}
	
	@Test
	public void testMissingDocumentRetrieval() throws Exception {
		String indexName = client.getRandomIndexName();
		client.createIndex(indexName);
		
		Map<?, ?> document = toMap("id", 5);
		client.createDocument(indexName, document);
		
		HttpResponse response = http.sendRequest("http://localhost/lucene/" + indexName + "/456").getResponse();
		
		http.assertNotFound(response);
	}
	
	@Test
	public void testMissingIndexDocumentRetrieval() throws Exception {
		String indexName = client.getRandomIndexName();
		client.createIndex(indexName);
		
		Map<?, ?> document = toMap("id", 5);
		client.createDocument(indexName, document);
		
		HttpResponse response = http.sendRequest("http://localhost/lucene/bsafadfasdfadfs/5").getResponse();
		
		http.assertNotFound(response);
	}
	
}
