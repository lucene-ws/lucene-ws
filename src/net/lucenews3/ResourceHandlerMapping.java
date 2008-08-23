package net.lucenews3;

import java.util.Arrays;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

public class ResourceHandlerMapping implements HandlerMapping {

	private HandlerMapping serviceHandlerMapping;
	private HandlerMapping servicePropertiesHandlerMapping;
	private HandlerMapping indexHandlerMapping;
	private HandlerMapping indexPropertiesHandlerMapping;
	private HandlerMapping openSearchDescriptionHandlerMapping;
	private HandlerMapping documentHandlerMapping;

	public ResourceHandlerMapping() {
		this.serviceHandlerMapping               = new NullHandlerMapping();
		this.servicePropertiesHandlerMapping     = new NullHandlerMapping();
		this.indexHandlerMapping                 = new NullHandlerMapping();
		this.indexPropertiesHandlerMapping       = new NullHandlerMapping();
		this.openSearchDescriptionHandlerMapping = new NullHandlerMapping();
		this.documentHandlerMapping              = new NullHandlerMapping();
	}

	/**
	 * Provides a sequence of strings representing the directories (path) requested
	 * in the given request.
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	protected Iterable<String> getPath(HttpServletRequest req) throws Exception {
		return Arrays.asList(req.getServletPath().split("/"));
	}

	/**
	 * Parses the request path, determining the resource being requested.
	 * Delegates handler mapping onto one of the sub-mappings according
	 * to which type of resource was requested (i.e. - service, index, document, etc...)
	 * 
	 * @param req
	 */
	@Override
	public HandlerExecutionChain getHandler(HttpServletRequest req) throws Exception {
		Iterable<String> path = getPath(req);
		Iterator<String> i = path.iterator();
		
		if (i.hasNext()) {
			String token0 = i.next();
			if (token0.equals("service.properties")) {
				return servicePropertiesHandlerMapping.getHandler(req);
			} else {
				req.setAttribute("index", token0);
				
				if (i.hasNext()) {
					String token1 = i.next();
					if (token1.equals("index.properties")) {
						return indexPropertiesHandlerMapping.getHandler(req);
					} else if (token1.equals("opensearchdescription.xml")) {
						return openSearchDescriptionHandlerMapping.getHandler(req);
					} else {
						req.setAttribute("document", token1);
						
						if (i.hasNext()) {
							String version = i.next();
							req.setAttribute("version", version);
						}
						
						return documentHandlerMapping.getHandler(req);
					}
				} else {
					return indexHandlerMapping.getHandler(req);
				}
			}
		} else {
			return serviceHandlerMapping.getHandler(req);
		}
	}

	public HandlerMapping getServiceHandlerMapping() {
		return serviceHandlerMapping;
	}

	public void setServiceHandlerMapping(HandlerMapping serviceHandlerMapping) {
		this.serviceHandlerMapping = serviceHandlerMapping;
	}

	public HandlerMapping getServicePropertiesHandlerMapping() {
		return servicePropertiesHandlerMapping;
	}

	public void setServicePropertiesHandlerMapping(HandlerMapping servicePropertiesHandlerMapping) {
		this.servicePropertiesHandlerMapping = servicePropertiesHandlerMapping;
	}

	public HandlerMapping getIndexHandlerMapping() {
		return indexHandlerMapping;
	}

	public void setIndexHandlerMapping(HandlerMapping indexHandlerMapping) {
		this.indexHandlerMapping = indexHandlerMapping;
	}

	public HandlerMapping getIndexPropertiesHandlerMapping() {
		return indexPropertiesHandlerMapping;
	}

	public void setIndexPropertiesHandlerMapping(HandlerMapping indexPropertiesHandlerMapping) {
		this.indexPropertiesHandlerMapping = indexPropertiesHandlerMapping;
	}

	public HandlerMapping getOpenSearchDescriptionHandlerMapping() {
		return openSearchDescriptionHandlerMapping;
	}

	public void setOpenSearchDescriptionHandlerMapping(HandlerMapping openSearchDescriptionHandlerMapping) {
		this.openSearchDescriptionHandlerMapping = openSearchDescriptionHandlerMapping;
	}

	public HandlerMapping getDocumentHandlerMapping() {
		return documentHandlerMapping;
	}

	public void setDocumentHandlerMapping(HandlerMapping documentHandlerMapping) {
		this.documentHandlerMapping = documentHandlerMapping;
	}

}
