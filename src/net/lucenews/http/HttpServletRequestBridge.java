package net.lucenews.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * An implementation of <code>HttpServletRequest</code> which wraps method
 * calls to a <code>HttpRequest</code> object.
 * 
 */
public class HttpServletRequestBridge implements HttpServletRequest {

	private HttpRequest request;
	private String authType;
	private String contextPath;
	private Cookie[] cookies;

	public HttpServletRequestBridge(HttpRequest request) {
		this.request = request;
	}

	@Override
	public String getAuthType() {
		return authType;
	}

	@Override
	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	@Override
	public Cookie[] getCookies() {
		return cookies;
	}

	@Override
	public long getDateHeader(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getHeader(String name) {
		String result;
		KeyValueMap<String, String> headersByName = request.getHeaders()
				.byKey();
		if (headersByName.containsKey(name)) {
			result = headersByName.get(name).first();
		} else {
			result = null;
		}
		return result;
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return new IteratorEnumeration<String>(request.getHeaders().byKey()
				.keySet().iterator());
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		Enumeration<String> result;
		final KeyValueMap<String, String> headersByName = request.getHeaders()
				.byKey();
		if (headersByName.containsKey(name)) {
			result = new IteratorEnumeration<String>(headersByName.get(name)
					.iterator());
		} else {
			result = new EmptyEnumeration<String>();
		}
		return result;
	}

	@Override
	public int getIntHeader(String name) {
		int result;
		final KeyValueMap<String, String> headersByName = request.getHeaders()
				.byKey();
		if (headersByName.containsKey(name)) {
			result = Integer.parseInt(headersByName.get(name).first());
		} else {
			result = -1;
		}
		return result;
	}

	@Override
	public String getMethod() {
		return request.getMethod();
	}

	@Override
	public String getPathInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQueryString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestURI() {
		return request.getResource();
	}

	@Override
	public StringBuffer getRequestURL() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("http://");
		buffer.append(request.getHost());
		Integer port = request.getPort();
		if (port != null) {
			buffer.append(":" + port);
		}
		String resource = request.getResource();
		if (resource != null) {
			buffer.append(resource);
		}
		return buffer;
	}

	@Override
	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSession getSession(boolean create) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUserInRole(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getContentLength() {
		return request.getBody().limit();
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		ByteBuffer body = request.getBody();
		return new DefaultServletInputStream(new ByteBufferInputStream(body));
	}

	@Override
	public String getLocalAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLocalPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<Locale> getLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameter(String name) {
		String result;
		final KeyValueMap<String, String> parametersByName = request
				.getParameters().byKey();
		if (parametersByName.containsKey(name)) {
			result = parametersByName.get(name).first();
		} else {
			result = null;
		}
		return result;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		final Map<String, String[]> result = new HashMap<String, String[]>();
		final KeyValueMap<String, String> parametersByName = request
				.getParameters().byKey();
		for (String name : parametersByName.keySet()) {
			String[] values = parametersByName.get(name).toArray(
					new String[] {});
			result.put(name, values);
		}
		return result;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		final KeyValueMap<String, String> parametersByName = request
				.getParameters().byKey();
		return new IteratorEnumeration<String>(parametersByName.keySet()
				.iterator());
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] result;
		final KeyValueMap<String, String> parametersByName = request
				.getParameters().byKey();
		if (parametersByName.containsKey(name)) {
			result = parametersByName.get(name).toArray(new String[] {});
		} else {
			result = null;
		}
		return result;
	}

	@Override
	public String getProtocol() {
		return request.getProtocol();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		final InputStream inputStream = new ByteBufferInputStream(request
				.getBody());
		final Reader reader = new InputStreamReader(inputStream);
		return new BufferedReader(reader);
	}

	@Override
	public String getRealPath(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRemotePort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScheme() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getServerPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAttribute(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAttribute(String name, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCharacterEncoding(String env)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

	}

}
