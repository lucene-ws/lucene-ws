package net.lucenews3.lucene.support;

public interface Document {

	public FieldList getFields();
	
	public void setFields(FieldList fields);
	
	public float getBoost();
	
	public void setBoost(float boost);
	
}
