package net.lucenews3.model;

public class IndexIdentityImpl implements IndexIdentity {

	private final Object value;
	
	public IndexIdentityImpl(Object value) {
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof IndexIdentityImpl) {
			return equals((IndexIdentityImpl) other);
		} else {
			return value.equals(other);
		}
	}
	
	public boolean equals(IndexIdentityImpl other) {
		return value.equals(other.value);
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
