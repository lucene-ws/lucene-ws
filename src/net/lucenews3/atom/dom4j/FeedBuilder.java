package net.lucenews3.atom.dom4j;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import net.lucenews3.atom.Entry;
import net.lucenews3.atom.Feed;
import net.lucenews3.atom.Link;

public class FeedBuilder extends AbstractBuilder {

	private LinkBuilder linkBuilder;
	private EntryBuilder entryBuilder;
	
	public FeedBuilder() {
		this.linkBuilder = new LinkBuilder();
		this.entryBuilder = new EntryBuilder();
	}
	
	public Element build(Feed feed) {
		final Element element = DocumentHelper.createElement("feed");
		build(feed, element);
		return element;
	}
	
	public void build(Feed feed, Element element) {
		addProperty(element, "title", feed.getTitle());
		addProperty(element, "updated", feed.getUpdated());
		addProperty(element, "logo", feed.getLogo(), false);
		for (Link link : feed.getLinks()) {
			element.add(linkBuilder.build(link));
		}
		for (Entry entry : feed.getEntries()) {
			Element entryElement = entryBuilder.build(entry);
			element.add(entryElement);
		}
	}
	
}
