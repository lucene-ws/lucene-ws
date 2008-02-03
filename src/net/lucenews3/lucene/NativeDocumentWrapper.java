package net.lucenews3.lucene;

public class NativeDocumentWrapper implements Document {

	private org.apache.lucene.document.Document nativeDocument;
	
	public FieldList getFields() {
		// TODO Auto-generated method stub
		return null;
	}

	public FieldList getFields(String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeFields(String fieldName) {
		// TODO Auto-generated method stub
		
	}

	public void setBoost(float boost) {
		// TODO Auto-generated method stub
		
	}

	public org.apache.lucene.document.Document asNative() {
		return nativeDocument;
	}

}
