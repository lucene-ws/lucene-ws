package net.lucenews3.lucene.support;

import org.apache.lucene.document.Document;

public interface Result {

	public int getNumber();
	
	public int getDocumentId();
	
	public Document getDocument();
	
	public float getScore();
	
}
