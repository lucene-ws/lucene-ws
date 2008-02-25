package net.lucenews3.atom.dom4j;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

import net.lucenews3.atom.Entry;
import net.lucenews3.atom.Link;
import net.lucenews3.atom.LinkList;

public class EntryBuilder extends AbstractBuilder {

	private ContentBuilder contentBuilder;
	private LinkBuilder linkBuilder;
	private Namespace atomNamespace;
	
	public EntryBuilder() {
		this.contentBuilder = new ContentBuilder();
		this.linkBuilder = new LinkBuilder();
		this.atomNamespace = Namespace.get("atom", "http://www.w3.org/2005/Atom");
	}
	
	public Element build(Entry entry) {
		final Element element = DocumentHelper.createElement(QName.get("entry", atomNamespace));
		build(entry, element);
		return element;
	}
	
	public void build(Entry entry, Element element) {
		final LinkList links = entry.getLinks();
		addProperty(element, QName.get("title", atomNamespace), entry.getTitle());
		for (Link link : links) {
			element.add(linkBuilder.build(link));
		}
		if (entry.getContent() != null) {
			element.add(contentBuilder.build(entry.getContent()));
		}
	}
	
}
