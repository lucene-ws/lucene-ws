package net.lucenews3.model;

import java.util.ArrayDeque;
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
import net.lucenews3.controller.ViewStaticResourceController;
import net.lucenews3.http.Url;
import net.lucenews3.http.UrlParser;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

public class WebServiceHandlerMapping implements HandlerMapping {

	private Logger logger;
	private UrlParser<HttpServletRequest> urlParser;
	private UrlParser<HttpServletRequest> baseUrlParser;
	private ViewServiceController<?, ?> viewServiceController;
	private ViewServicePropertiesController<?, ?> viewServicePropertiesController;
	private CreateIndexController<?, ?> createIndexController;
	private SearchIndexController<?, ?> searchIndexController;
	private RemoveIndexController<?, ?> removeIndexController;
	private ViewDocumentController<?, ?> viewDocumentController;
	private CreateDocumentController<?, ?> createDocumentController;
	private UpdateDocumentController<?, ?> updateDocumentController;
	private RemoveDocumentController<?, ?> removeDocumentController;
	private ViewStaticResourceController viewStaticResourceController;
	
	public WebServiceHandlerMapping() {
		this.logger = Logger.getLogger(getClass());
	}
	
	public HandlerExecutionChain getHandler(HttpServletRequest request)
			throws Exception {
		final Url url = urlParser.parse(request);
		final Url baseUrl = baseUrlParser.parse(request);
		
		final Deque<String> tokens = new ArrayDeque<String>();
		tokens.addAll(url.getPath());
		
		int basePathSize = baseUrl.getPath().size();
		for (int i = 0; i < basePathSize; i++) {
			tokens.removeFirst();
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Handler tokens: " + tokens);
		}
		
		if (tokens.isEmpty()) {
			return getServiceHandler(request);
		} else {
			String token0 = tokens.remove();
			if (token0.equals("service.properties")) {
				return getServicePropertiesHandler(request);
			} else if (token0.equals("static")) {
				request.setAttribute("resourcePath", tokens);
				return new HandlerExecutionChain(viewStaticResourceController);
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

	public UrlParser<HttpServletRequest> getUrlParser() {
		return urlParser;
	}

	public void setUrlParser(UrlParser<HttpServletRequest> urlParser) {
		this.urlParser = urlParser;
	}

	public UrlParser<HttpServletRequest> getBaseUrlParser() {
		return baseUrlParser;
	}

	public void setBaseUrlParser(UrlParser<HttpServletRequest> baseUrlParser) {
		this.baseUrlParser = baseUrlParser;
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

	public ViewStaticResourceController getViewStaticResourceController() {
		return viewStaticResourceController;
	}

	public void setViewStaticResourceController(
			ViewStaticResourceController viewStaticResourceController) {
		this.viewStaticResourceController = viewStaticResourceController;
	}

}
