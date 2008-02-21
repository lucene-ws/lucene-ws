package net.lucenews3.atom.dom;

import net.lucenews3.atom.Entry;
import net.lucenews3.atom.Feed;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FeedBuilder extends AbstractBuilder {

	private EntryBuilder entryBuilder;
	
	public FeedBuilder() {
		this.entryBuilder = new EntryBuilder();
	}
	
	public Element build(Feed feed, Document document) {
		final Element element = document.createElement("feed");
		build(feed, element);
		document.appendChild(element);
		return element;
	}
	
	public void build(Feed feed, Element element) {
		appendPropertyElement(element, "title", feed.getTitle());
		appendPropertyElement(element, "updated", feed.getUpdated());
		for (Entry entry : feed.getEntries()) {
			Element entryElement = entryBuilder.build(entry, element.getOwnerDocument());
			element.appendChild(entryElement);
		}
	}
	
}
