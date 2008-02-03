package net.lucenews3;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.atom.IntrospectionDocument;
import net.lucenews3.atom.IntrospectionDocumentImpl;

public class WebService extends HttpServlet {

	private static final long serialVersionUID = -5476182566760996923L;

	@Override
	public void init(ServletConfig configuration) {
		
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		IntrospectionDocument introspection = new IntrospectionDocumentImpl();
		System.out.println(request.getPathTranslated());
	}
	
}
