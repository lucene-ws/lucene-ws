package net.lucenews3.lucene;

public class LazyDocumentImpl extends LazyImpl<Document> implements LazyDocument {
	
	public LazyDocumentImpl() {
		super();
	}
	
	public LazyDocumentImpl(Provider<Document> provider) {
		super(provider);
	}
	
	@Override
	public FieldList getFields() {
		return getTarget().getFields();
	}

	@Override
	public FieldList getFields(String fieldName) {
		return getTarget().getFields(fieldName);
	}

	@Override
	public void setBoost(float boost) {
		getTarget().setBoost(boost);
	}

	@Override
	public org.apache.lucene.document.Document asNative() {
		return getTarget().asNative();
	}

}
