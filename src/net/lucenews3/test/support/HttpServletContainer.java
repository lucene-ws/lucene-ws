package net.lucenews3.test.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.http.HttpRequest;
import net.lucenews3.http.HttpResponse;

public interface HttpServletContainer {
	
	public void service(HttpRequest request, HttpResponse response) throws Exception;
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
