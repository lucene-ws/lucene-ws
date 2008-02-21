package net.lucenews3.model;

public class DocumentIdentityImpl implements DocumentIdentity {

	private final Object value;
	private IndexIdentity indexIdentity;
	
	public DocumentIdentityImpl(Object value) {
		this.value = value;
	}
	
	public DocumentIdentityImpl(IndexIdentity indexIdentity, Object value) {
		this.indexIdentity = indexIdentity;
		this.value = value;
	}

	public IndexIdentity getIndexIdentity() {
		return indexIdentity;
	}

	public void setIndexIdentity(IndexIdentity indexIdentity) {
		this.indexIdentity = indexIdentity;
	}

	public Object getValue() {
		return value;
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
}
