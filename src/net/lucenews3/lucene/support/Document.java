package net.lucenews3.lucene.support;

import net.lucenews3.lucene.NativeImplementationProvider;

public interface Document extends NativeImplementationProvider<org.apache.lucene.document.Document> {

	public FieldList getFields();
	
	public void setFields(FieldList fields);
	
	public float getBoost();
	
	public void setBoost(float boost);
	
}
