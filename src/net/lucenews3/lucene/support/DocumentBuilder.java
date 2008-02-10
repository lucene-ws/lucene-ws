package net.lucenews3.lucene.support;

import net.lucenews3.atom.Entry;

public interface DocumentBuilder {

	public Document buildDocument(Entry entry);
	
	public void buildDocument(Document document, Entry entry);
	
}
