package net.lucenews.test;

import com.meterware.httpunit.GetMethodWebRequest;

public class IndexCreationTest extends ClientTest {

	public GetMethodWebRequest getIndexRequest(String indexName) {
		return new GetMethodWebRequest("http://localhost/lucene/" + indexName);
	}
	
}
