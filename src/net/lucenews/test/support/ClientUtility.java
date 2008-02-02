package net.lucenews.test.support;

import java.io.IOException;
import java.util.Map;

import net.lucenews.http.HttpConversation;
import net.lucenews.http.HttpRequest;

public class ClientUtility {
	
	@SuppressWarnings("unused")
	private HttpServletContainer container;
	private HttpUtility http;
	private MapUtility map;
	
	public ClientUtility(final HttpServletContainer container) {
		this(container, new HttpUtility(container));
	}
	
	public ClientUtility(final HttpServletContainer container, final HttpUtility http) {
		this.container = container;
		this.http = http;
		this.map = new MapUtility();
	}
	
	public HttpConversation createIndex(final String indexName) {
		return createIndex(indexName, map.toMap());
	}
	
	public HttpConversation createIndex(final String indexName, final Map<?, ?> indexProperties) {
		final HttpRequest request = http.buildRequest();
		http.populateBody(request, buildIndexCreation(indexName, indexProperties));
		return http.send(request);
	}
	
	public String buildIndexCreation(final String indexName, final Map<?, ?> indexProperties) {
		final StringBuffer body = new StringBuffer();
		buildIndexCreation(indexName, indexProperties);
		return body.toString();
	}
	
	public void buildIndexCreation(final String indexName, final Map<?, ?> indexProperties, final Appendable body) throws IOException {
		body.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		body.append("<entry xmlns=\"http://www.w3.org/2005/Atom\">");
		body.append("<title>" + indexName + "</title>");
		body.append("<content type=\"xhtml\">");
		body.append("<div xmlns=\"http://www.w3.org/1999/xhtml\">");
		body.append("<dl class=\"xoxo\">");
		for (final Object key : indexProperties.keySet()) {
			final Object value = indexProperties.get(key);
			body.append("<dt>" + key + "</dt>");
			body.append("<dd>" + value + "</dd>");
		}
		body.append("</dl>");
		body.append("</div>");
		body.append("</content>");
		body.append("</entry>");
	}
	
}
