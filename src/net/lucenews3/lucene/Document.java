package net.lucenews3.lucene;

public interface Document extends NativeImplementationProvider<org.apache.lucene.document.Document> {

	public FieldList getFields();
	
	public FieldList getFields(String fieldName);
	
	public void setBoost(float boost);
	
}
