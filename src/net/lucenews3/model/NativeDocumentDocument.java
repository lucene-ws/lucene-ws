package net.lucenews3.model;

public class NativeDocumentDocument implements Document {

	private org.apache.lucene.document.Document nativeDocument;
	private FieldList fields;
	
	public NativeDocumentDocument() {
		this(new org.apache.lucene.document.Document());
	}
	
	public NativeDocumentDocument(org.apache.lucene.document.Document nativeDocument) {
		this.nativeDocument = nativeDocument;
		this.fields = new NativeDocumentFieldList(this.nativeDocument);
	}

	public FieldList getFields() {
		return fields;
	}

	public void setFields(FieldList fields) {
		this.fields = fields;
	}
	
	public float getBoost() {
		return nativeDocument.getBoost();
	}

	public void setBoost(float boost) {
		nativeDocument.setBoost(boost);
	}

	public org.apache.lucene.document.Document asNative() {
		return nativeDocument;
	}

}
