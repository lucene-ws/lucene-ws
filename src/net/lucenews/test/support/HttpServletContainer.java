package net.lucenews.test.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews.http.HttpRequest;
import net.lucenews.http.HttpResponse;

public interface HttpServletContainer {
	
	public void service(HttpRequest request, HttpResponse response) throws Exception;
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
