package net.lucenews3.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lucenews3.http.HttpRequest;
import net.lucenews3.http.HttpResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.lucene.index.IndexReader;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ServicePostTest extends ClientTest {

	private final Object NAME_KEY;
	
	public ServicePostTest() {
		this.NAME_KEY = client.INDEX_NAME_KEY;
	}
	
	/**
	 * Creates an index with no index properties.
	 * @throws Exception
	 */
	@Test
	public void testNamedIndexCreation() throws Exception {
		doTestIndexCreation("testindex01");
	}
	
	@Test
	public void testMultipleIndexCreation() throws Exception {
		doTestIndexesCreation(toMap(NAME_KEY, "index01"), toMap(NAME_KEY, "index02"), toMap(NAME_KEY, "index03"));
	}

	/**
	 * Creates an index with a few index properties.
	 * @throws Exception
	 */
	@Test
	public void testTitledIndexCreation() throws Exception {
		doTestIndexCreation("testindex01", toMap("index.title",
				"My test index 01", "random.properties", "random value!"));
	}

	/**
	 * Attempts to create an index which already exists.
	 * @throws Exception
	 */
	@Test
	public void testConflictingIndexCreation() throws Exception {
		File root = fileSystem.getTemporaryDirectory();

		String indexName = "testindex01";
		File index1 = new File(root, indexName);
		lucene.buildIndex(index1);

		container.setInitialParameter("directory", root.getCanonicalPath());

		HttpRequest request = buildIndexCreationRequest(toMap(NAME_KEY, indexName));
		HttpResponse response = http.send(request).getResponse();

		Assert.assertEquals("response status", HttpStatus.SC_CONFLICT, response
				.getStatus());
	}

	/**
	 * An index of the same previously exists, but is deleted via the API. A new
	 * index of the same name is created.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testResolvedConflictingIndexCreation() throws Exception {
		File root = fileSystem.getTemporaryDirectory();

		String indexName = "testindex01";
		File index1 = new File(root, indexName);
		lucene.buildIndex(index1);

		container.setInitialParameter("directory", root.getCanonicalPath());

		HttpRequest deleteRequest = buildIndexDeletionRequest(indexName);
		HttpResponse deleteResponse = http.send(deleteRequest).getResponse();

		Assert.assertEquals("delete response status", HttpStatus.SC_OK,
				deleteResponse.getStatus());

		doTestIndexCreation(indexName, toMap("foo", "bar", "cat", "dog"));
	}

	public void doTestIndexCreation(String indexName) throws Exception {
		doTestIndexCreation(indexName, toMap());
	}

	public HttpRequest buildIndexDeletionRequest(String indexName) {
		HttpRequest request = http.buildRequest("DELETE", "http://localhost/lucene/"
				+ indexName);
		return request;
	}

	public HttpRequest buildIndexCreationRequest(Map<?, ?>... indexes) {
		final int count = indexes.length;
		
		final StringBuffer body = new StringBuffer();
		body.append("<?xml version=\"1.0\"?>");
		
		if (count > 1) {
			body.append("<feed>");
		}
		
		for (Map<?, ?> index : indexes) {
			final String indexName = index.get(NAME_KEY).toString();
			Map<Object, Object> properties = new HashMap<Object, Object>();
			properties.putAll(index);
			properties.remove(NAME_KEY);
			
			body.append("<entry xmlns=\"http://www.w3.org/2005/Atom\">");
			body.append("<title>" + indexName + "</title>");
			if (properties != null) {
				body.append("<content type=\"xhtml\">");
				body.append("<div xmlns=\"http://www.w3.org/1999/xhtml\">");
				body.append("<dl class=\"xoxo\">");
				for (Object key : properties.keySet()) {
					Object value = properties.get(key);
					body.append("<dt>" + key + "</dt>");
					body.append("<dd>" + value + "</dd>");
				}
				body.append("</dl>");
				body.append("</div>");
				body.append("</content>");
			}
			body.append("</entry>");
		}
		
		if (count > 1) {
			body.append("</feed>");
		}

		HttpRequest postRequest = http.buildRequest("POST", "http://localhost/lucene");
		http.populateBody(postRequest, body);
		return postRequest;
	}
	
	public void doTestIndexCreation(String indexName, Map<?, ?> properties)
			throws Exception {
		Map<Object, Object> copy = new HashMap<Object, Object>();
		copy.putAll(properties);
		copy.put(NAME_KEY, indexName);
		doTestIndexesCreation(copy);
	}
	
	public void doTestIndexesCreation(Map<?, ?>... indexes) throws Exception {
		final int count = indexes.length;
		
		File root = fileSystem.getTemporaryDirectory();

		container.setInitialParameter("index.directory", root
				.getCanonicalPath());

		HttpRequest postRequest = buildIndexCreationRequest(indexes);
		HttpResponse postResponse = http.send(postRequest).getResponse();
		List<String> locations = new ArrayList<String>(postResponse.getHeaders().byKey().get("Location"));

		// TODO: Does this make sense?
		if (count == 1) {
			Assert.assertEquals("# of location headers", indexes.length, locations.size());
		}
		
		for (int i = 0; i < indexes.length; i++) {
			Map<?, ?> index = indexes[i];
			String indexName = index.get(NAME_KEY).toString();
			Map<Object, Object> properties = new HashMap<Object, Object>();
			properties.putAll(index);
			properties.remove(NAME_KEY);
			
			Assert.assertEquals("response status", HttpStatus.SC_CREATED,
					postResponse.getStatus());
	
			File file = new File(root, indexName);
	
			Assert.assertTrue("index directory exists", file.exists());
	
			Assert.assertTrue("index directory contains a valid Lucene index",
					IndexReader.indexExists(file));
	
			if (count == 1) {
				String location = locations.get(i);
				Assert.assertEquals("index URL",
						"http://localhost/lucene/" + indexName, location);
			}
			
			assertIndexProperties(indexName, properties);
		}
	}

	/**
	 * Asserts that the given index properties are the ones associated with it
	 * according to the web service.
	 * 
	 * @param indexName
	 * @param expectedProperties
	 * @throws Exception
	 */
	public void assertIndexProperties(String indexName,
			Map<?, ?> expectedProperties) throws Exception {
		HttpResponse response = http.sendRequest("http://localhost/lucene/" + indexName
				+ "/index.properties").getResponse();

		Document document = toDocument(response);
		Element entry = dom.elementByPath(document, "/entry");

		entryAsserter.assertEntry(entry);

		Element dl = dom.elementByPath(document, "/entry/content/div/dl");
		Map<String, String> actualProperties = xoxo.toMap(dl);

		Assert.assertEquals("index properties", expectedProperties,
				actualProperties);
	}

}
