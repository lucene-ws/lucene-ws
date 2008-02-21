package net.lucenews3.test.support;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews.http.HttpRequest;
import net.lucenews.http.HttpResponse;
import net.lucenews.http.HttpServletRequestBridge;
import net.lucenews.http.HttpServletResponseBridge;

public class DefaultHttpServletContainer implements HttpServletContainer {

	private HttpServlet servlet;
	private Class<? extends HttpServlet> servletClass;
	private String servletClassName;
	private String servletName;
	private Map<String, String> initialParameters;
	private ServletConfig servletConfig;
	private boolean initialized;
	
	public DefaultHttpServletContainer() {
		this.initialParameters = new HashMap<String, String>();
	}

	public HttpServlet getServlet() {
		return servlet;
	}

	public void setServlet(HttpServlet servlet) {
		this.servlet = servlet;
	}

	public Class<? extends HttpServlet> getServletClass() {
		return servletClass;
	}

	public void setServletClass(Class<? extends HttpServlet> servletClass) {
		this.servletClass = servletClass;
	}

	public String getServletClassName() {
		return servletClassName;
	}

	public void setServletClassName(String servletClassName) {
		this.servletClassName = servletClassName;
	}

	public String getServletName() {
		return servletName;
	}

	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	public Map<String, String> getInitialParameters() {
		return initialParameters;
	}

	public void setInitialParameters(Map<String, String> initialParameters) {
		this.initialParameters = initialParameters;
	}
	
	public void setInitialParameter(String name, String value) {
		this.initialParameters.put(name, value);
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	
	public void initialize() throws Exception {
		if (!initialized) {
			if (servlet == null && servletClass != null) {
				servlet = servletClass.newInstance();
			}
			if (servlet == null && servletClassName != null) {
				servlet = (HttpServlet) Class.forName(servletClassName).newInstance();
			}
			servletConfig = new DefaultServletConfig(servletName, initialParameters);
			servlet.init(servletConfig);
		}
	}

	@Override
	public void service(HttpRequest request, HttpResponse response)
			throws Exception {
		HttpServletRequestBridge bridgedRequest = new HttpServletRequestBridge(request);
		HttpServletResponseBridge bridgedResponse = new HttpServletResponseBridge(response);
		bridgedRequest.setContextPath("/lucene");
		service(bridgedRequest, bridgedResponse);
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		initialize();
		servlet.service(request, response);
	}

}
