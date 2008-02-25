package net.lucenews3.atom.dom4j;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

import net.lucenews3.atom.Link;

public class LinkBuilder extends AbstractBuilder {

	private Namespace atomNamespace;
	
	public LinkBuilder() {
		this.atomNamespace = Namespace.get("atom", "http://www.w3.org/2005/Atom");
	}
	
	public Element build(Link link) {
		final Element element = DocumentHelper.createElement(QName.get("link", atomNamespace));
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
