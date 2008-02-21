package net.lucenews3.test.support;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.lucenews.http.HttpConversation;
import net.lucenews.http.HttpRequest;

public class ClientUtility {
	
	public final Object INDEX_NAME_KEY = new Object();
	public final Object FIELD_NAME_KEY = new Object();
	public final Object FIELD_CLASS_KEY = new Object();
	public final Object FIELD_VALUE_KEY = new Object();
	
	@SuppressWarnings("unused")
	private HttpServletContainer container;
	private HttpUtility http;
	private MapUtility map;
	private Random random;
	
	public ClientUtility(final HttpServletContainer container) {
		this(container, new HttpUtility(container));
	}
	
	public ClientUtility(final HttpServletContainer container, final HttpUtility http) {
		this.container = container;
		this.http = http;
		this.map = new MapUtility();
		this.random = new Random();
	}
	
	public String getRandomIndexName() {
		return "index-" + Math.abs(random.nextInt());
	}
	
	public HttpConversation createIndex(final String indexName) {
		return createIndex(indexName, map.toMap());
	}
	
	public HttpConversation createIndex(final String indexName, final Map<?, ?> indexProperties) {
		return http.send(buildIndexCreationRequest(indexName, indexProperties));
	}
	
	public HttpRequest buildIndexCreationRequest(final String indexName, final Map<?, ?> indexProperties) {
		final Map<Object, Object> index = new HashMap<Object, Object>();
		index.putAll(indexProperties);
		index.put(INDEX_NAME_KEY, indexName);
		return buildIndexCreationRequest(index);
	}
	
	public HttpConversation createIndexes(final Map<?, ?>... indexes) {
		return http.send(buildIndexCreationRequest(indexes));
	}
	
	public HttpRequest buildIndexCreationRequest(final Map<?, ?>... indexes) {
		final int count = indexes.length;
		final StringBuffer body = new StringBuffer();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		if (count != 1) {
			body.append("<feed>");
		}
		for (final Map<?, ?> index : indexes) {
			final String indexName = index.get(INDEX_NAME_KEY).toString();
			final Map<Object, Object> indexProperties = new HashMap<Object, Object>();
			indexProperties.putAll(index);
			indexProperties.remove(INDEX_NAME_KEY);
			try {
				buildIndexCreationEntry(indexName, indexProperties, body);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if (count != 1) {
			body.append("</feed>");
		}
		
		final HttpRequest request = http.buildRequest("POST", "http://localhost/lucene");
		http.populateBody(request, body);
		return request;
	}
	
	public String buildIndexCreationEntry(final String indexName, final Map<?, ?> indexProperties) {
		final StringBuffer body = new StringBuffer();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		try {
			buildIndexCreationEntry(indexName, indexProperties, body);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return body.toString();
	}
	
	public void buildIndexCreationEntry(final String indexName, final Map<?, ?> indexProperties, final Appendable body) throws IOException {
		body.append("<entry xmlns=\"http://www.w3.org/2005/Atom\">");
		body.append("<title>" + indexName + "</title>");
		body.append("<content type=\"xhtml\">");
		body.append("<div xmlns=\"http://www.w3.org/1999/xhtml\">");
		body.append("<dl class=\"xoxo\">");
		for (final Object key : indexProperties.keySet()) {
			final Object value = indexProperties.get(key);
			body.append("<dt>" + http.escape(key) + "</dt>");
			body.append("<dd>" + http.escape(value) + "</dd>");
		}
		body.append("</dl>");
		body.append("</div>");
		body.append("</content>");
		body.append("</entry>");
	}
	
	public HttpConversation createDocument(final String indexName, final Map<?, ?> document) {
		return createDocuments(indexName, document);
	}
	
	public HttpConversation createDocuments(final String indexName, final Map<?, ?>... documents) {
		return http.send(buildDocumentCreationRequest(indexName, documents));
	}
	
	public HttpRequest buildDocumentCreationRequest(final String indexName, final Map<?, ?>... documents) {
		final StringBuffer body = new StringBuffer();
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		
		final int count = documents.length;
		final boolean isFeed = (count != 1);
		
		if (isFeed) {
			body.append("<feed>");
		}
		
		for (Map<?, ?> document : documents) {
			body.append("<entry>");
			body.append("<title>New Document</title>");
			body.append("<content type=\"xhtml\">");
			body.append("<div xmlns=\"http://www.w3.org/1999/xhtml\">");
			body.append("<dl class=\"xoxo\">");
			for (final Object key : document.keySet()) {
				final Object value = document.get(key);
				if (value.getClass().isArray()) {
					final int length = Array.getLength(value);
					for (int i = 0; i < length; i++) {
						appendField(body, key.toString(), Array.get(value, i).toString(), null);
					}
				} else {
					appendField(body, key.toString(), value.toString(), null);
				}
			}
			body.append("</dl>");
			body.append("</div>");
			body.append("</content>");
			body.append("</entry>");
		}
		
		if (isFeed) {
			body.append("</feed>");
		}
		
		final HttpRequest request = http.buildRequest("POST", "http://localhost/lucene/" + indexName);
		http.populateBody(request, body);
		return request;
	}
	
	public void appendField(StringBuffer body, String fieldName, String fieldValue, String fieldClass) {
		body.append("<dt");
		if (fieldClass != null) {
			body.append(" class=\"" + fieldClass + "\"");
		}
		body.append(">");
		body.append(fieldName.toString());
		body.append("</dt>");
		body.append("<dd>");
		body.append(fieldValue.toString());
		body.append("</dd>");
	}
	
	public HttpConversation deleteDocument(final String indexName, final String documentId) {
		return http.sendRequest("DELETE", "http://localhost/lucene/" + indexName + "/" + documentId);
	}
	
	public HttpRequest buildIndexDeletionRequest(final String indexName) {
		return http.buildRequest("DELETE", "http://localhost/lucene/" + indexName);
	}
	
	public HttpConversation deleteIndex(final String indexName) {
		return http.send(buildIndexDeletionRequest(indexName));
	}
	
}
