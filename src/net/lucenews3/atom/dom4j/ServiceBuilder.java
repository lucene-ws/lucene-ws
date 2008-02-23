package net.lucenews3.atom.dom4j;

import net.lucenews3.atom.Service;
import net.lucenews3.atom.Workspace;
import net.lucenews3.atom.WorkspaceList;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class ServiceBuilder extends AbstractBuilder {

	private WorkspaceBuilder workspaceBuilder;
	
	public ServiceBuilder() {
		this.workspaceBuilder = new WorkspaceBuilder();
	}
	
	public Element build(Service service) {
		final Element element = DocumentHelper.createElement("service");
		build(service, element);
		return element;
	}

	private void build(Service service, Element element) {
		element.addAttribute("xmlns", "http://www.w3.org/2007/app");
		element.addAttribute("xmlns:atom", "http://www.w3.org/2005/Atom");
		
		final WorkspaceList workspaces = service.getWorkspaces();
		for (Workspace workspace : workspaces) {
			final Element workspaceElement = workspaceBuilder.build(workspace);
			element.add(workspaceElement);
		}
	}
	
}
