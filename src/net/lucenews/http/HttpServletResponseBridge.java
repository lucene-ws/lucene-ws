package net.lucenews.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseBridge implements HttpServletResponse {

	private HttpResponse response;
	
	public HttpServletResponseBridge(HttpResponse response) {
		this.response = response;
	}
	
	@Override
	public void addCookie(Cookie cookie) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDateHeader(String name, long date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addHeader(String name, String value) {
		final HeaderCollection headers = response.getHeaders();
		headers.add(new DefaultHeader(name, value));
	}

	@Override
	public void addIntHeader(String name, int value) {
		final HeaderCollection headers = response.getHeaders();
		headers.add(new DefaultHeader(name, String.valueOf(value)));
	}

	@Override
	public boolean containsHeader(String name) {
		boolean result;
		final KeyValueMap<String, String> headersByName = response.getHeaders().byKey();
		result = headersByName.containsKey(name);
		return result;
	}

	@Override
	public String encodeRedirectURL(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeRedirectUrl(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeURL(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeUrl(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendError(int statusCode) throws IOException {
		response.setStatus(statusCode);
	}

	@Override
	public void sendError(int statusCode, String message) throws IOException {
		response.setStatus(statusCode);
		response.setMessage(message);
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
		response.getHeaders().add(new DefaultHeader("Location", location));
	}

	@Override
	public void setDateHeader(String name, long date) {
		final HeaderCollection headers = response.getHeaders();
		final KeyValueMap<String, String> headersByName = headers.byKey();
		if (headersByName.containsKey(name)) {
			headersByName.remove(name);
		}
		// TODO
		headers.add(new DefaultHeader(name, String.valueOf(date)));
	}

	@Override
	public void setHeader(String name, String value) {
		final HeaderCollection headers = response.getHeaders();
		final KeyValueMap<String, String> headersByName = headers.byKey();
		if (headersByName.containsKey(name)) {
			headersByName.remove(name);
		}
		headers.add(new DefaultHeader(name, value));
	}

	@Override
	public void setIntHeader(String name, int value) {
		final HeaderCollection headers = response.getHeaders();
		final KeyValueMap<String, String> headersByName = headers.byKey();
		if (headersByName.containsKey(name)) {
			headersByName.remove(name);
		}
		headers.add(new DefaultHeader(name, String.valueOf(value)));
	}

	@Override
	public void setStatus(int statusCode) {
		response.setStatus(statusCode);
	}

	@Override
	public void setStatus(int statusCode, String message) {
		response.setStatus(statusCode);
		response.setMessage(message);
	}

	@Override
	public void flushBuffer() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getBufferSize() {
		return response.getBody().capacity();
	}

	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	protected String getHeader(String name) {
		String result;
		final KeyValueMap<String, String> headersByName = response.getHeaders().byKey();
		if (headersByName.containsKey(name)) {
			result = headersByName.get(name).first();
		} else {
			result = null;
		}
		return result;
	}
	
	@Override
	public String getContentType() {
		return getHeader("Content-Type");
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return new DefaultServletOutputStream(new ByteBufferOutputStream(response.getBody()));
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(new ByteBufferOutputStream(response.getBody()));
	}

	@Override
	public boolean isCommitted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reset() {
		response.setStatus(null);
		response.setMessage(null);
		response.getHeaders().clear();
	}

	@Override
	public void resetBuffer() {
		response.getBody().rewind();
	}

	@Override
	public void setBufferSize(int size) {
		response.setBody(ByteBuffer.allocate(size));
	}

	@Override
	public void setCharacterEncoding(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContentLength(int contentLength) {
		setIntHeader("Content-Length", contentLength);
	}

	@Override
	public void setContentType(String contentType) {
		setHeader("Content-Type", contentType);
	}

	@Override
	public void setLocale(Locale locale) {
		// TODO Auto-generated method stub
		
	}

}
