package net.lucenews3.controller;

import java.util.HashMap;
import java.util.Map;

import net.lucenews3.atom.Collection;
import net.lucenews3.atom.CollectionImpl;
import net.lucenews3.atom.CollectionList;
import net.lucenews3.atom.Service;
import net.lucenews3.atom.ServiceImpl;
import net.lucenews3.atom.Workspace;
import net.lucenews3.atom.WorkspaceImpl;
import net.lucenews3.atom.WorkspaceList;
import net.lucenews3.atom.dom4j.ServiceBuilder;
import net.lucenews3.http.Url;
import net.lucenews3.http.UrlParser;
import net.lucenews3.model.Index;
import net.lucenews3.model.IndexIdentity;

import org.dom4j.DocumentHelper;
import org.springframework.web.servlet.ModelAndView;

public class ViewServiceController<I, O> implements Controller<I, O> {

	private ServiceBuilder serviceBuilder;
	private Map<IndexIdentity, Index> indexesByIdentity;
	private UrlParser<I> baseUrlParser;
	
	public ViewServiceController() {
		this.serviceBuilder = new ServiceBuilder();
	}
	
	@Override
	public ModelAndView handleRequest(I input, O output) throws Exception {
		final Url baseUrl = baseUrlParser.parse(input);
		
		final Service service = new ServiceImpl();
		final WorkspaceList workspaces = service.getWorkspaces();
		final Workspace workspace = new WorkspaceImpl();
		workspaces.add(workspace);
		
		workspace.setTitle("Lucene Web Service");
		
		final CollectionList collections = workspace.getCollections();
		
		for (IndexIdentity indexIdentity : indexesByIdentity.keySet()) {
			final Collection collection = new CollectionImpl();
			collection.setTitle(indexIdentity.toString());
			
			final Url collectionUrl = baseUrl.clone();
			collectionUrl.getPath().add(indexIdentity.toString());
			collection.setHref(collectionUrl.toString());
			collections.add(collection);
		}
		
		final org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
		Map<Object, Object> data = new HashMap<Object, Object>();
		data.put("type", "text/xsl");
		
		final Url stylesheetUrl = baseUrl.clone();
		stylesheetUrl.getPath().add("static");
		stylesheetUrl.getPath().add("introspection.xslt");
		data.put("href", stylesheetUrl.toString());
		final org.dom4j.ProcessingInstruction pi = DocumentHelper.createProcessingInstruction("xml-stylesheet", data);
		final org.dom4j.Element element = serviceBuilder.build(service);
		document.add(pi);
		document.add(element);
		
		final ModelAndView result = new ModelAndView("service/view");
		result.addObject("service", service);
		result.addObject("document", document);
		return result;
	}

	public ServiceBuilder getServiceBuilder() {
		return serviceBuilder;
	}

	public void setServiceBuilder(ServiceBuilder serviceBuilder) {
		this.serviceBuilder = serviceBuilder;
	}

	public Map<IndexIdentity, Index> getIndexesByIdentity() {
		return indexesByIdentity;
	}

	public void setIndexesByIdentity(Map<IndexIdentity, Index> indexesByIdentity) {
		this.indexesByIdentity = indexesByIdentity;
	}

	public UrlParser<I> getBaseUrlParser() {
		return baseUrlParser;
	}

	public void setBaseUrlParser(UrlParser<I> baseUrlParser) {
		this.baseUrlParser = baseUrlParser;
	}

}
