package net.lucenews3.atom.dom4j;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import net.lucenews3.atom.Link;

public class LinkBuilder extends AbstractBuilder {

	public Element build(Link link) {
		final Element element = DocumentHelper.createElement("link");
		build(link, element);
		return element;
	}

	public void build(Link link, Element element) {
		addAttribute(element, "href", link.getHref(), true);
		addAttribute(element, "rel", link.getRel(), false);
		addAttribute(element, "type", link.getType(), false);
		addAttribute(element, "hreflang", link.getHreflang(), false);
		addAttribute(element, "title", link.getTitle(), false);
		addAttribute(element, "length", link.getLength(), false);
	}
	
}
