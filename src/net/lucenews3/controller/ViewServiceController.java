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
import net.lucenews3.model.Index;
import net.lucenews3.model.IndexIdentity;

import org.dom4j.DocumentHelper;
import org.springframework.web.servlet.ModelAndView;

public class ViewServiceController<I, O> implements Controller<I, O> {

	private ServiceBuilder serviceBuilder;
	private Map<IndexIdentity, Index> indexesByIdentity;
	
	public ViewServiceController() {
		this.serviceBuilder = new ServiceBuilder();
	}
	
	@Override
	public ModelAndView handleRequest(I input, O output) throws Exception {
		final Service service = new ServiceImpl();
		final WorkspaceList workspaces = service.getWorkspaces();
		final Workspace workspace = new WorkspaceImpl();
		workspaces.add(workspace);
		
		workspace.setTitle("Lucene Web Service");
		
		final CollectionList collections = workspace.getCollections();
		
		for (IndexIdentity indexIdentity : indexesByIdentity.keySet()) {
			final Collection collection = new CollectionImpl();
			collection.setTitle(indexIdentity.toString());
			collection.setHref("http://localhost:8080/lucene3/" + indexIdentity);
			collections.add(collection);
		}
		
		final org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
		Map<Object, Object> data = new HashMap<Object, Object>();
		data.put("type", "text/xsl");
		data.put("href", "/lucene3/static/introspection.xslt");
		final org.dom4j.ProcessingInstruction pi = DocumentHelper.createProcessingInstruction("xml-stylesheet", data);
		final org.dom4j.Element element = serviceBuilder.build(service);
		document.add(pi);
		document.add(element);
		//final org.dom4j.ProcessingInstruction processingInstruction = document.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"http://localhost:8080/examples/jsp/XMLProject/quiz1_xsl.xsl\"");
		//document.appendChild(element);
		//document.insertBefore(document.createComment("Hello!"), document.getDocumentElement());
		
		final ModelAndView result = new ModelAndView("service/view");
		result.addObject("service", service);
		result.addObject("document", document);
		//result.addObject("element", element);
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

}
