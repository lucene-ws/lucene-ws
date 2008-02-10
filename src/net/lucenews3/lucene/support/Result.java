package net.lucenews3.lucene.support;

public interface Result {

	public int getNumber();
	
	public int getDocumentId();
	
	public Document getDocument();
	
	public float getScore();
	
}
