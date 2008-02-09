package net.lucenews3.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.atom.Collection;
import net.lucenews3.atom.CollectionImpl;
import net.lucenews3.atom.CollectionList;
import net.lucenews3.atom.IntrospectionDocument;
import net.lucenews3.atom.IntrospectionDocumentImpl;
import net.lucenews3.atom.Service;
import net.lucenews3.atom.ServiceImpl;
import net.lucenews3.atom.Workspace;
import net.lucenews3.atom.WorkspaceImpl;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class ViewIntrospectionDocumentController implements Controller {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final IntrospectionDocument introspection = new IntrospectionDocumentImpl();
		
		final Service service = new ServiceImpl();
		introspection.setService(service);
		
		final Workspace workspace = new WorkspaceImpl();
		service.getWorkspaces().add(workspace);
		
		// Build the collections
		final CollectionList collections = workspace.getCollections();
		List<Object> indexes = null;
		for (Object index : indexes) {
			Collection collection = new CollectionImpl();
			collection.getAccepts().add("application/atom+xml;type=entry");
			collections.add(collection);
		}
		
		// TODO Auto-generated method stub
		return null;
	}

}
