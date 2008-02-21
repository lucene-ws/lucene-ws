package net.lucenews3.model;

public interface DocumentIdentity extends Identity<Document> {

	public IndexIdentity getIndexIdentity();
	
	public void setIndexIdentity(IndexIdentity indexIdentity);
	
}
