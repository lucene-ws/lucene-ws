package net.lucenews3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

public class ResourceHandlerMapping implements HandlerMapping {

	private Logger logger;
	private HandlerMapping serviceHandlerMapping;
	private HandlerMapping servicePropertiesHandlerMapping;
	private HandlerMapping indexHandlerMapping;
	private HandlerMapping indexPropertiesHandlerMapping;
	private HandlerMapping openSearchDescriptionHandlerMapping;
	private HandlerMapping documentHandlerMapping;

	public ResourceHandlerMapping() {
		BasicConfigurator.configure();
		this.logger = Logger.getLogger(getClass());
		this.logger.setLevel(Level.ALL); // FIXME
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
	 * @param req
	 */
	@Override
	public HandlerExecutionChain getHandler(HttpServletRequest req) throws Exception {
		Iterable<String> path = getPath(req);
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
				return servicePropertiesHandlerMapping.getHandler(req);
			} else {
				req.setAttribute("index", token0);
				
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
						return indexPropertiesHandlerMapping.getHandler(req);
					} else if (token1.equals("opensearchdescription.xml")) {
						logger.debug("Requested OpenSearch description");
						return openSearchDescriptionHandlerMapping.getHandler(req);
					} else {
						req.setAttribute("document", token1);
						
						if (logger.isDebugEnabled()) {
							logger.debug("Requested document: \"" + token1 + "\"");
						}
						
						if (i.hasNext()) {
							String version = i.next();
							req.setAttribute("version", version);
						}
						
						if (logger.isDebugEnabled()) {
							logger.debug("Requested document, handler mapping: " + documentHandlerMapping);
						}
						
						return documentHandlerMapping.getHandler(req);
					}
				} else {
					logger.debug("Requested index");
					return indexHandlerMapping.getHandler(req);
				}
			}
		} else {
			logger.debug("Requested service");
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
