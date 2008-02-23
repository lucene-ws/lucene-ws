package net.lucenews3.atom.dom4j;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import net.lucenews3.atom.Content;
import net.lucenews3.atom.InlineXhtmlContent;

public class ContentBuilder extends AbstractBuilder {

	public Element build(Content content) {
		final Element element = DocumentHelper.createElement("content");
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
