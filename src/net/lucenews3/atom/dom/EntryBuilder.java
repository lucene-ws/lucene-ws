package net.lucenews3.atom.dom;

import net.lucenews3.atom.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EntryBuilder extends AbstractBuilder {

	public Element build(Entry entry, Document document) {
		final Element element = document.createElement("entry");
		build(entry, element);
		return element;
	}
	
	public void build(Entry entry, Element element) {
		appendPropertyElement(element, "title", entry.getTitle());
	}
	
}
