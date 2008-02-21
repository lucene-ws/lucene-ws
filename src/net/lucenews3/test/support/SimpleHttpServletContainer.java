package net.lucenews3.test.support;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.http.HttpRequest;
import net.lucenews3.http.HttpResponse;
import net.lucenews3.http.HttpServletRequestBridge;
import net.lucenews3.http.HttpServletResponseBridge;

public class SimpleHttpServletContainer implements HttpServletContainer {

	private HttpServlet servlet;
	
	public SimpleHttpServletContainer(Class<? extends HttpServlet> servletClass) throws InstantiationException, IllegalAccessException, ServletException {
		this.servlet = servletClass.newInstance();
		init();
	}
	
	@SuppressWarnings("unchecked")
	public SimpleHttpServletContainer(String servletClassName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ServletException {
		this((Class<HttpServlet>) Class.forName(servletClassName));
	}
	
	protected void init() throws ServletException {
		servlet.init();
	}
	
	public void service(HttpRequest request, HttpResponse response) throws ServletException, IOException {
		service(new HttpServletRequestBridge(request), new HttpServletResponseBridge(response));
	}
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		servlet.service(request, response);
	}
	
}
