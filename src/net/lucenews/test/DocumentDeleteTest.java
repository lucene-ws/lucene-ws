package net.lucenews.test;

import net.lucenews.http.HttpResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

public class DocumentDeleteTest extends ClientTest {
	
	@Test
	public void testDeletion() {
		String indexName = "testindex01";
		client.createIndex(indexName);
		client.createDocument(indexName, map.toMap("id", 5));
		HttpResponse response = client.deleteDocument(indexName, String.valueOf(5)).getResponse();
		
		Assert.assertEquals("response status", HttpStatus.SC_OK, response.getStatus());
		
		String location = response.getHeaders().byKey().get("Location").only();
		Assert.assertEquals("location header", "http://localhost/lucene/" + indexName + "/5", location);
	}

}
