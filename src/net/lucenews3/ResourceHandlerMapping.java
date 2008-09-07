package net.lucenews3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

public class ResourceHandlerMapping implements HandlerMapping, Ordered {

	public static final String DEFAULT_INDEX_ATTRIBUTE_NAME    = "index";
	public static final String DEFAULT_DOCUMENT_ATTRIBUTE_NAME = "document";
	
	private Logger logger;
	private HandlerMapping serviceHandlerMapping;
	private HandlerMapping servicePropertiesHandlerMapping;
	private HandlerMapping indexHandlerMapping;
	private HandlerMapping indexPropertiesHandlerMapping;
	private HandlerMapping openSearchDescriptionHandlerMapping;
	private HandlerMapping documentHandlerMapping;
	private String indexAttributeName;
	private String documentAttributeName;

	public ResourceHandlerMapping() {
		this.logger = Logger.getLogger(getClass());
		this.serviceHandlerMapping               = new NullHandlerMapping();
		this.servicePropertiesHandlerMapping     = new NullHandlerMapping();
		this.indexHandlerMapping                 = new NullHandlerMapping();
		this.indexPropertiesHandlerMapping       = new NullHandlerMapping();
		this.openSearchDescriptionHandlerMapping = new NullHandlerMapping();
		this.documentHandlerMapping              = new NullHandlerMapping();
		this.indexAttributeName    = DEFAULT_INDEX_ATTRIBUTE_NAME;
		this.documentAttributeName = DEFAULT_DOCUMENT_ATTRIBUTE_NAME;
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
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
		String[] tokens = req.getPathInfo().substring(1).split("/");
		
		List<String> path = new ArrayList<String>(tokens.length);
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i];
			if ((i == (tokens.length - 1)) && "".equals(token)) {
				// Do not add!
			} else {
				path.add(token);
			}
		}
		
		return path;
	}

	/**
	 * Parses the request path, determining the resource being requested.
	 * Delegates handler mapping onto one of the sub-mappings according
	 * to which type of resource was requested (i.e. - service, index, document, etc...)
	 * 
	 * @param request
	 */
	@Override
	public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		Iterable<String> path = getPath(request);
		Iterator<String> i = path.iterator();
		
		if (logger.isDebugEnabled()) {
			logger.debug("requested path: " + path);
		}
		
		if (i.hasNext()) {
			String token0 = i.next();
			
			if (logger.isDebugEnabled()) {
				logger.debug("token 0: \"" + token0 + "\"");
			}
			
			if (token0.equals("service.properties")) {
				logger.debug("Requested service properties");
				return servicePropertiesHandlerMapping.getHandler(request);
			} else {
				request.setAttribute(indexAttributeName, token0);
				
				if (logger.isDebugEnabled()) {
					logger.debug("Requested index: \"" + token0 + "\"");
				}
				
				if (i.hasNext()) {
					String token1 = i.next();
					
					if (logger.isDebugEnabled()) {
						logger.debug("token 1: " + token1);
					}
					
					if (token1.equals("index.properties")) {
						logger.debug("Requested index properties");
						return indexPropertiesHandlerMapping.getHandler(request);
					} else if (token1.equals("opensearchdescription.xml")) {
						logger.debug("Requested OpenSearch description");
						return openSearchDescriptionHandlerMapping.getHandler(request);
					} else {
						request.setAttribute(documentAttributeName, token1);
						
						if (logger.isDebugEnabled()) {
							logger.debug("Requested document: \"" + token1 + "\"");
						}
						
						if (i.hasNext()) {
							String version = i.next();
							request.setAttribute("version", version);
						}
						
						if (logger.isDebugEnabled()) {
							logger.debug("Requested document, handler mapping: " + documentHandlerMapping);
						}
						
						return documentHandlerMapping.getHandler(request);
					}
				} else {
					logger.debug("Requested index");
					return indexHandlerMapping.getHandler(request);
				}
			}
		} else {
			logger.debug("Requested service");
			return serviceHandlerMapping.getHandler(request);
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

	public String getIndexAttributeName() {
		return indexAttributeName;
	}

	public void setIndexAttributeName(String indexAttributeName) {
		this.indexAttributeName = indexAttributeName;
	}

	public String getDocumentAttributeName() {
		return documentAttributeName;
	}

	public void setDocumentAttributeName(String documentAttributeName) {
		this.documentAttributeName = documentAttributeName;
	}

}
