package net.lucenews3.lucene.support;

public interface DocumentIdentity extends Identity<Document> {

	public IndexIdentity getIndexIdentity();
	
	public void setIndexIdentity(IndexIdentity indexIdentity);
	
}
