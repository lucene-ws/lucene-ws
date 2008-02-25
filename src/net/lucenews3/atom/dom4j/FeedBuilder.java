package net.lucenews3.atom.dom4j;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

import net.lucenews3.atom.Entry;
import net.lucenews3.atom.Feed;
import net.lucenews3.atom.Link;

public class FeedBuilder extends AbstractBuilder {

	private LinkBuilder linkBuilder;
	private EntryBuilder entryBuilder;
	private Namespace atomNamespace;
	
	public FeedBuilder() {
		this.linkBuilder = new LinkBuilder();
		this.entryBuilder = new EntryBuilder();
		this.atomNamespace = Namespace.get("atom", "http://www.w3.org/2005/Atom");
	}
	
	public Element build(Feed feed) {
		final Element element = DocumentHelper.createElement(QName.get("feed", atomNamespace));
		build(feed, element);
		return element;
	}
	
	public void build(Feed feed, Element element) {
		addProperty(element, QName.get("title", atomNamespace), feed.getTitle());
		addProperty(element, QName.get("updated", atomNamespace), feed.getUpdated());
		addProperty(element, QName.get("logo", atomNamespace), feed.getLogo(), false);
		for (Link link : feed.getLinks()) {
			element.add(linkBuilder.build(link));
		}
		for (Entry entry : feed.getEntries()) {
			Element entryElement = entryBuilder.build(entry);
			element.add(entryElement);
		}
	}
	
}
