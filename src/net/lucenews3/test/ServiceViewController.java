package net.lucenews3.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class ServiceViewController implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
		//IntrospectionDocument introspection = new IntrospectionDocument();
		//introspection.getWorkspaces().add(new Workspace("Lucene Web Service"));
		return null;
	}
	/**
	
	public IntrospectionDocument buildIntrospectionDocument() {
		IntrospectionDocument introspection = new IntrospectionDocument();
		Workspace workspace = buildWorkspace();
		introspection.getWorkspaces().add(workspace);
		return introspection;
	}
	
	public Workspace buildWorkspace() {
		Workspace workspace = new Workspace();
		return workspace;
	}
	
	public Collection buildCollection() {
		Collection collection = new Collection();
		return collection;
	}
	*/
}
