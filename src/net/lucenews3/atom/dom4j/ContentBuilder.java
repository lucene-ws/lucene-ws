package net.lucenews3.atom.dom4j;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;

import net.lucenews3.atom.Content;
import net.lucenews3.atom.InlineXhtmlContent;

public class ContentBuilder extends AbstractBuilder {
	
	private Namespace atomNamespace;

	public ContentBuilder() {
		this.atomNamespace = Namespace.get("atom", "http://www.w3.org/2005/Atom");
	}

	public Element build(Content content) {
		final Element element = DocumentHelper.createElement(QName.get("content", atomNamespace));
		build(content, element);
		return element;
	}
	
	public void build(Content content, Element element) {
		addAttribute(element, "type", content.getType(), false);
		if (content instanceof InlineXhtmlContent) {
			final Element div = DocumentHelper.createElement("div");
			div.addAttribute("xmlns", "http://www.w3.org/1999/xhtml");
			build((InlineXhtmlContent) content, div);
			element.add(div);
		}
	}
	
	public void build(InlineXhtmlContent content, Element element) {
		for (Node contentNode : content.getContentNodes()) {
			element.add(contentNode);
		}
	}
	
}
