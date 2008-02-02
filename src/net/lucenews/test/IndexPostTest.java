package net.lucenews.test;

import java.io.File;
import java.util.Map;

import net.lucenews.http.HttpRequest;
import net.lucenews.http.HttpResponse;

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
		doTestDocumentCreation(toMap(NAME, "foo", CLASS, "indexed", VALUE, "bar"));
	}
	
	protected void doTestDocumentCreation(Map<?, ?> fields) throws Exception {
		File root = fileSystem.getTemporaryDirectory();
		
		String indexName = "testindex01";
		File index = new File(root, indexName);
		lucene.buildIndex(index);
		
		container.setInitialParameter("directory", root.getCanonicalPath());
		
		StringBuffer body = new StringBuffer();
		appendDocument(body, fields);
		HttpRequest request = postRequest("http://localhost/lucene/" + indexName);
		populateBody(request, body);
		HttpResponse response = getResponse(request);
		
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
