package net.lucenews3.test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.lucenews3.http.HttpRequest;
import net.lucenews3.http.HttpResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class IndexPostTest extends ClientTest {

	public final Object CLASS = new Object();
	public final Object NAME = new Object();
	public final Object VALUE = new Object();
	
	@Test
	public void test() throws Exception {
		//doTestDocumentCreation(toMap("foo", "bar"));
	}
	
	public List<String> getCustomDocumentIdenfierPropertyKeys() {
		return Arrays.asList(new String[]{"field.<identifier>.name", "field.<identifier>", "field.identifier", "identifier"});
	}
	
	@Test
	public void testCustomIdentifiedDocumentCreation1() throws Exception {
		doTestCustomIdentifiedDocumentCreation(lists.filter(getCustomDocumentIdenfierPropertyKeys(), 1));
	}
	
	@Test
	public void testCustomIdentifiedDocumentCreation2() throws Exception {
		doTestCustomIdentifiedDocumentCreation(lists.filter(getCustomDocumentIdenfierPropertyKeys(), 2));
	}
	
	@Test
	public void testCustomIdentifiedDocumentCreation3() throws Exception {
		doTestCustomIdentifiedDocumentCreation(lists.filter(getCustomDocumentIdenfierPropertyKeys(), 3));
	}
	
	@Test
	public void testCustomIdentifiedDocumentCreation4() throws Exception {
		doTestCustomIdentifiedDocumentCreation(lists.filter(getCustomDocumentIdenfierPropertyKeys(), 4));
	}
	
	@Test
	public void testCustomIdentifiedDocumentCreation5() throws Exception {
		doTestCustomIdentifiedDocumentCreation(lists.filter(getCustomDocumentIdenfierPropertyKeys(), 5));
	}
	
	@Test
	public void testCustomIdentifiedDocumentCreation6() throws Exception {
		doTestCustomIdentifiedDocumentCreation(lists.filter(getCustomDocumentIdenfierPropertyKeys(), 6));
	}
	
	@Test
	public void testCustomIdentifiedDocumentCreation7() throws Exception {
		doTestCustomIdentifiedDocumentCreation(lists.filter(getCustomDocumentIdenfierPropertyKeys(), 7));
	}
	
	@Test
	public void testCustomIdentifiedDocumentCreation8() throws Exception {
		doTestCustomIdentifiedDocumentCreation(lists.filter(getCustomDocumentIdenfierPropertyKeys(), 8));
	}
	
	@Test
	public void testCustomIdentifiedDocumentCreation9() throws Exception {
		doTestCustomIdentifiedDocumentCreation(lists.filter(getCustomDocumentIdenfierPropertyKeys(), 9));
	}
	
	@Test
	public void testCustomIdentifiedDocumentCreation10() throws Exception {
		doTestCustomIdentifiedDocumentCreation(lists.filter(getCustomDocumentIdenfierPropertyKeys(), 10));
	}
	
	@Test
	public void testCustomIdentifiedDocumentCreation11() throws Exception {
		doTestCustomIdentifiedDocumentCreation(lists.filter(getCustomDocumentIdenfierPropertyKeys(), 11));
	}
	
	@Test
	public void testCustomIdentifiedDocumentCreation12() throws Exception {
		doTestCustomIdentifiedDocumentCreation(lists.filter(getCustomDocumentIdenfierPropertyKeys(), 12));
	}
	
	@Test
	public void testCustomIdentifiedDocumentCreation13() throws Exception {
		doTestCustomIdentifiedDocumentCreation(lists.filter(getCustomDocumentIdenfierPropertyKeys(), 13));
	}
	
	@Test
	public void testCustomIdentifiedDocumentCreation14() throws Exception {
		doTestCustomIdentifiedDocumentCreation(lists.filter(getCustomDocumentIdenfierPropertyKeys(), 14));
	}
	
	@Test
	public void testCustomIdentifiedDocumentCreation15() throws Exception {
		doTestCustomIdentifiedDocumentCreation(lists.filter(getCustomDocumentIdenfierPropertyKeys(), 15));
	}
	
	/**
	 * 
	 * @param propertyNames
	 * @throws Exception
	 */
	public void doTestCustomIdentifiedDocumentCreation(List<String> identityFieldNamePropertyKeys) throws Exception {
		String primaryFieldName = null;
		
		final int size = identityFieldNamePropertyKeys.size();
		for (int i = 0; i < size; i++) {
			String fieldName = "field" + string.padLeft(i + 1, '0', 2);
			if (i == 0) {
				primaryFieldName = fieldName;
			}
		}
		
		Map<?, ?> indexProperties = toMap(identityFieldNamePropertyKeys.get(0), primaryFieldName);
		String indexName = client.getRandomIndexName();
		client.createIndex(indexName, indexProperties);
		
		Map<?, ?> document = toMap(primaryFieldName, "123");
		HttpResponse response = client.createDocument(indexName, document).getResponse();
		
		http.assertCreated(response);
	}
	
	@Test
	public void testUnidentifiedDocumentCreation() throws Exception {
		String indexName = "testindex01";
		client.createIndex(indexName);
		Map<?, ?> document = toMap("sound", "meow");
		HttpResponse response = client.createDocument(indexName, document).getResponse();
		
		http.assertStatus(response, HttpStatus.SC_BAD_REQUEST);
	}
	
	@Test
	public void testAmbiguouslyIdentifiedDocumentCreation() throws Exception {
		String indexName = "testindex01";
		client.createIndex(indexName);
		Map<?, ?> document = toMap("id", new int[]{ 5, 6 }, "sound", "meow");
		HttpResponse response = client.createDocument(indexName, document).getResponse();
		
		http.assertBadRequest(response);
	}
	
	@Test
	public void testRedundantlyIdentifiedDocumentCreation() throws Exception {
		String indexName = "testindex01";
		client.createIndex(indexName);
		Map<?, ?> document = toMap("id", new int[]{5, 5, 5, 5}, "sound", "woof");
		HttpResponse response = client.createDocument(indexName, document).getResponse();
		
		http.assertBadRequest(response);
	}
	
	@Test
	public void testUnknownIndexDocumentCreation() throws Exception {
		String indexName = "testindex01";
		Map<?, ?> document = toMap("id", 5);
		HttpResponse response = client.createDocument(indexName, document).getResponse();
		
		http.assertNotFound(response);
	}
	
	@Test
	public void testDocumentCreationConflict() throws Exception {
		String indexName = "testindex01";
		client.createIndex(indexName);
		Map<?, ?> document = toMap("id", 5);
		client.createDocument(indexName, document);
		
		HttpResponse response = client.createDocument(indexName, document).getResponse();
		
		Assert.assertEquals("response status", HttpStatus.SC_CONFLICT, response.getStatus());
	}
	
	protected void doTestDocumentCreation(Map<?, ?> fields) throws Exception {
		File root = fileSystem.getTemporaryDirectory();
		
		String indexName = "testindex01";
		File index = new File(root, indexName);
		lucene.buildIndex(index);
		
		container.setInitialParameter("directory", root.getCanonicalPath());
		
		StringBuffer body = new StringBuffer();
		appendDocument(body, fields);
		HttpRequest request = http.buildRequest("POST", "http://localhost/lucene/" + indexName);
		http.populateBody(request, body);
		HttpResponse response = http.send(request).getResponse();
		
		Assert.assertEquals("response status", HttpStatus.SC_CREATED, response.getStatus());
		
		String id = "1";
		String location = response.getHeaders().byKey().get("Location").only();
		Assert.assertEquals("location header", "http://localhost/lucene/" + indexName + "/" + id, location);
		
		Document document = toDocument(response);
		Element entry = dom.elementByPath(document, "/entry");
		entryAsserter.assertEntry(entry);
		
		
		
	}
	
	public void appendDocument(Appendable appendable, Map<?, ?>... fields) throws Exception {
		appendable.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		appendable.append("<entry>");
		appendable.append("<title>New Document</title>");
		appendable.append("<content type=\"xhtml\">");
		appendable.append("<div xmlns=\"http://www.w3.org/1999/xhtml\">");
		appendable.append("<dl class=\"xoxo\">");
		for (Map<?, ?> field : fields) {
			appendField(appendable, field);
		}
		appendable.append("</dl>");
		appendable.append("</div>");
		appendable.append("</content>");
		appendable.append("</entry>");
	}
	
	public void appendField(Appendable appendable, Map<?, ?> field) throws Exception {
		Object fieldClass = field.get(CLASS);
		Object fieldName = field.get(NAME);
		Object fieldValue = field.get(VALUE);
		
		appendable.append("<dt");
		if (fieldClass != null) {
			appendable.append(" class=\"" + fieldClass + "\"");
		}
		appendable.append(">");
		appendable.append(fieldName.toString());
		appendable.append("</dt>");
		appendable.append("<dd>");
		appendable.append(fieldValue.toString());
		appendable.append("</dd>");
	}
	
}
