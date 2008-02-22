package net.lucenews3.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseBridge extends HttpServletCommunicationBridge implements HttpServletResponse {

	private HttpResponse response;
	
	public HttpServletResponseBridge(HttpResponse response) {
		super(response);
		this.response = response;
	}
	
	@Override
	public void addCookie(Cookie cookie) {
		// TODO Auto-generated method stub
		
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
		response.getHeaders().add(new HeaderImpl("Location", location));
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
		return new ServletOutputStreamImpl(new ByteBufferOutputStream(response.getBody()));
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
	public void setCharacterEncoding(String characterEncoding) {
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
