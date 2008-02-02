package net.lucenews.test.support;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;

import net.lucenews.http.DefaultHttpConversation;
import net.lucenews.http.DefaultHttpRequest;
import net.lucenews.http.DefaultHttpResponse;
import net.lucenews.http.HttpCommunication;
import net.lucenews.http.HttpConversation;
import net.lucenews.http.HttpRequest;
import net.lucenews.http.HttpResponse;

public class HttpUtility {
	
	private String defaultMethod;
	private HttpServletContainer container;

	public HttpUtility(final HttpServletContainer container) {
		this.container = container;
		this.defaultMethod = "GET";
	}
	
	public URL toUrl(final String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public HttpRequest buildRequest() {
		return new DefaultHttpRequest();
	}

	public HttpRequest buildRequest(final String url) {
		return buildRequest(toUrl(url));
	}
	
	public HttpRequest buildRequest(final URL url) {
		return buildRequest(defaultMethod, url);
	}
	
	public HttpRequest buildRequest(final String method, final String url) {
		return buildRequest(method, toUrl(url));
	}
	
	public HttpRequest buildRequest(final String method, final URL url) {
		final HttpRequest request = buildRequest();
		request.setMethod(method);
		request.setHost(url.getHost());
		Integer port = url.getPort();
		if (port != null && port >= 0) {
			request.setPort(port);
		}
		request.setResource(url.getPath());
		return request;
	}
	
	public HttpResponse buildResponse() {
		return new DefaultHttpResponse();
	}
	
	public HttpConversation sendRequest(final String url) {
		return sendRequest(defaultMethod, toUrl(url));
	}
	
	public HttpConversation sendRequest(final String method, final String url) {
		return sendRequest(method, toUrl(url));
	}
	
	public HttpConversation sendRequest(final String method, final URL url) {
		return sendRequest(buildRequest(method, url));
	}
	
	public HttpConversation sendRequest(final HttpRequest request) {
		final HttpResponse response = buildResponse();
		try {
			container.service(request, response);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new DefaultHttpConversation(request, response);
	}
	
	public HttpConversation send(final HttpRequest request) {
		return sendRequest(request);
	}
	
	/**
	 * Attempts to populate the body of the given communication using the
	 * given object.
	 * @param communication
	 * @param content
	 */
	public void populateBody(final HttpCommunication communication, final Object content) {
		try {
			this.getClass().getMethod("populateBuffer", ByteBuffer.class, content.getClass()).invoke(this, communication.getBody(), content);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void populateBuffer(final ByteBuffer buffer, final byte[] bytes) {
		buffer.put(bytes);
	}
	
	public void populateBuffer(final ByteBuffer buffer, final String string) {
		buffer.put(string.getBytes());
	}
	
	public void populateBuffer(final ByteBuffer buffer, final StringBuffer string) {
		buffer.put(string.toString().getBytes());
	}
	
}
