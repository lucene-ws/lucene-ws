package net.lucenews3.atom.dom4j;

import net.lucenews3.atom.Collection;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class CollectionBuilder extends AbstractBuilder {
	
	public Element build(Collection collection) {
		final Element element = DocumentHelper.createElement("collection");
		build(collection, element);
		return element;
	}

	public void build(Collection collection, Element element) {
		addAttribute(element, "href", collection.getHref());
		addPropertyNS(element, "http://www.w3.org/2005/Atom", "atom:title", collection.getTitle());
	}

}
