package net.lucenews3.model;

public interface Result {

	public int getNumber();
	
	public int getDocumentId();
	
	public Document getDocument();
	
	public float getScore();
	
}
