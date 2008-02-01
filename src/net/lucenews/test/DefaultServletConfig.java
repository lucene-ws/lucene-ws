package net.lucenews.test;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import net.lucenews.http.IteratorEnumeration;

public class DefaultServletConfig implements ServletConfig {

	private String servletName;
	private Map<String, String> initialParameters;
	
	public DefaultServletConfig(String servletName) {
		this(servletName, new HashMap<String, String>());
	}
	
	public DefaultServletConfig(String servletName, Map<String, String> initialParameters) {
		this.servletName = servletName;
		this.initialParameters = initialParameters;
	}
	
	@Override
	public String getInitParameter(String name) {
		return initialParameters.get(name);
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		return new IteratorEnumeration<String>(initialParameters.keySet().iterator());
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletName() {
		return servletName;
	}

}
