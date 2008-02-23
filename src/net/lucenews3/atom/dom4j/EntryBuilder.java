package net.lucenews3.atom.dom4j;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import net.lucenews3.atom.Entry;
import net.lucenews3.atom.Link;
import net.lucenews3.atom.LinkList;

public class EntryBuilder extends AbstractBuilder {

	private ContentBuilder contentBuilder;
	private LinkBuilder linkBuilder;
	
	public EntryBuilder() {
		this.contentBuilder = new ContentBuilder();
		this.linkBuilder = new LinkBuilder();
	}
	
	public Element build(Entry entry) {
		final Element element = DocumentHelper.createElement("entry");
		build(entry, element);
		return element;
	}
	
	public void build(Entry entry, Element element) {
		final LinkList links = entry.getLinks();
		addProperty(element, "title", entry.getTitle());
		for (Link link : links) {
			element.add(linkBuilder.build(link));
		}
		if (entry.getContent() != null) {
			element.add(contentBuilder.build(entry.getContent()));
		}
	}
	
}
