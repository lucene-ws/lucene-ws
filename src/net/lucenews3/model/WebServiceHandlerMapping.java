package net.lucenews3.model;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import javax.servlet.http.HttpServletRequest;

import net.lucenews3.controller.CreateDocumentController;
import net.lucenews3.controller.CreateIndexController;
import net.lucenews3.controller.RemoveDocumentController;
import net.lucenews3.controller.RemoveIndexController;
import net.lucenews3.controller.SearchIndexController;
import net.lucenews3.controller.UpdateDocumentController;
import net.lucenews3.controller.ViewDocumentController;
import net.lucenews3.controller.ViewServiceController;
import net.lucenews3.controller.ViewServicePropertiesController;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

public class WebServiceHandlerMapping implements HandlerMapping {

	private ViewServiceController<?, ?> viewServiceController;
	private ViewServicePropertiesController<?, ?> viewServicePropertiesController;
	private CreateIndexController<?, ?> createIndexController;
	private SearchIndexController<?, ?> searchIndexController;
	private RemoveIndexController<?, ?> removeIndexController;
	private ViewDocumentController<?, ?> viewDocumentController;
	private CreateDocumentController<?, ?> createDocumentController;
	private UpdateDocumentController<?, ?> updateDocumentController;
	private RemoveDocumentController<?, ?> removeDocumentController;
	
	public HandlerExecutionChain getHandler(HttpServletRequest request)
			throws Exception {
		Deque<String> tokens = new ArrayDeque<String>();
		
		Logger logger = Logger.getLogger(this.getClass());
		logger.setLevel(Level.ALL);
		
		String path = request.getRequestURI();
		System.err.println("context path: " + path);
		if (path != null && !path.equals("/") && !path.equals("")) {
			tokens.addAll(Arrays.asList(path.split("/")));
			tokens.removeFirst();
			tokens.removeFirst();
		}
		
		System.err.println("Path tokens: " + tokens);
		
		if (tokens.isEmpty()) {
			return getServiceHandler(request);
		} else {
			String token0 = tokens.remove();
			if (token0.equals("service.properties")) {
				return getServicePropertiesHandler(request);
			} else {
				String indexName = token0;
				request.setAttribute("indexName", indexName);
				if (tokens.isEmpty()) {
					return getIndexHandler(request);
				} else {
					String token1 = tokens.remove();
					if (token1.equals("index.properties")) {
						return getIndexPropertiesHandler(request, indexName);
					} else {
						String documentId = token1;
						request.setAttribute("documentId", documentId);
						return getDocumentHandler(request, indexName, documentId);
					}
				}
			}
		}
	}

	private HandlerExecutionChain getDocumentHandler(HttpServletRequest request, String indexName, String documentId) {
		String method = request.getMethod();
		if (method.equalsIgnoreCase("GET")) {
			return new HandlerExecutionChain(viewDocumentController);
		} else if (method.equalsIgnoreCase("PUT")) {
			return new HandlerExecutionChain(updateDocumentController);
		} else if (method.equalsIgnoreCase("DELETE")) {
			return new HandlerExecutionChain(removeDocumentController);
		}
		return null;
	}

	private HandlerExecutionChain getIndexPropertiesHandler(HttpServletRequest request, String indexName) {
		// TODO Auto-generated method stub
		return null;
	}

	private HandlerExecutionChain getIndexHandler(HttpServletRequest request) {
		String method = request.getMethod();
		if (method.equalsIgnoreCase("GET")) {
			return new HandlerExecutionChain(searchIndexController);
		} else if (method.equalsIgnoreCase("POST")) {
			return new HandlerExecutionChain(createDocumentController);
		} else if (method.equalsIgnoreCase("DELETE")) {
			return new HandlerExecutionChain(removeIndexController);
		}
		return null;
	}

	private HandlerExecutionChain getServiceHandler(HttpServletRequest request) {
		String method = request.getMethod();
		if (method.equalsIgnoreCase("GET")) {
			return new HandlerExecutionChain(viewServiceController);
		} else if (method.equalsIgnoreCase("POST")) {
			return new HandlerExecutionChain(createIndexController);
		}
		// TODO Auto-generated method stub
		return null;
	}

	private HandlerExecutionChain getServicePropertiesHandler(HttpServletRequest request) {
		String method = request.getMethod();
		if (method.equalsIgnoreCase("GET")) {
			return new HandlerExecutionChain(viewServicePropertiesController);
		}
		return null;
	}

	public CreateDocumentController<?, ?> getCreateDocumentController() {
		return createDocumentController;
	}

	public void setCreateDocumentController(
			CreateDocumentController<?, ?> createDocumentController) {
		this.createDocumentController = createDocumentController;
	}

	public CreateIndexController<?, ?> getCreateIndexController() {
		return createIndexController;
	}

	public void setCreateIndexController(CreateIndexController<?, ?> createIndexController) {
		this.createIndexController = createIndexController;
	}

	public RemoveDocumentController<?, ?> getRemoveDocumentController() {
		return removeDocumentController;
	}

	public void setRemoveDocumentController(
			RemoveDocumentController<?, ?> removeDocumentController) {
		this.removeDocumentController = removeDocumentController;
	}

	public RemoveIndexController<?, ?> getRemoveIndexController() {
		return removeIndexController;
	}

	public void setRemoveIndexController(RemoveIndexController<?, ?> removeIndexController) {
		this.removeIndexController = removeIndexController;
	}

	public SearchIndexController<?, ?> getSearchIndexController() {
		return searchIndexController;
	}

	public void setSearchIndexController(SearchIndexController<?, ?> searchIndexController) {
		this.searchIndexController = searchIndexController;
	}

	public UpdateDocumentController<?, ?> getUpdateDocumentController() {
		return updateDocumentController;
	}

	public void setUpdateDocumentController(
			UpdateDocumentController<?, ?> updateDocumentController) {
		this.updateDocumentController = updateDocumentController;
	}

	public ViewDocumentController<?, ?> getViewDocumentController() {
		return viewDocumentController;
	}

	public void setViewDocumentController(
			ViewDocumentController<?, ?> viewDocumentController) {
		this.viewDocumentController = viewDocumentController;
	}

	public ViewServiceController<?, ?> getViewServiceController() {
		return viewServiceController;
	}

	public void setViewServiceController(ViewServiceController<?, ?> viewServiceController) {
		this.viewServiceController = viewServiceController;
	}

	public ViewServicePropertiesController<?, ?> getViewServicePropertiesController() {
		return viewServicePropertiesController;
	}

	public void setViewServicePropertiesController(
			ViewServicePropertiesController<?, ?> viewServicePropertiesController) {
		this.viewServicePropertiesController = viewServicePropertiesController;
	}

}
