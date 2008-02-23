package net.lucenews3.atom.dom4j;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import net.lucenews3.atom.Collection;
import net.lucenews3.atom.CollectionList;
import net.lucenews3.atom.Workspace;

public class WorkspaceBuilder extends AbstractBuilder {

	private CollectionBuilder collectionBuilder;
	
	public WorkspaceBuilder() {
		this.collectionBuilder = new CollectionBuilder();
	}
	
	public Element build(Workspace workspace) {
		final Element element = DocumentHelper.createElement("workspace");
		build(workspace, element);
		return element;
	}

	private void build(Workspace workspace, Element element) {
		final CollectionList collections = workspace.getCollections();
		
		addPropertyNS(element, "http://www.w3.org/2005/Atom", "atom:title", workspace.getTitle());
		
		for (Collection collection : collections) {
			final Element collectionElement = collectionBuilder.build(collection);
			element.add(collectionElement);
		}
	}
	
}
