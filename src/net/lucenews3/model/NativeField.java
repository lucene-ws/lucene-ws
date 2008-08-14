package net.lucenews3.model;

import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;

public class NativeField implements Field {

	private static final long serialVersionUID = -6420255173370031881L;

	private final org.apache.lucene.document.Field target;

	public NativeField(org.apache.lucene.document.Field target) {
		this.target = target;
	}
	
	public byte[] binaryValue() {
		return target.binaryValue();
	}

	public boolean equals(Object obj) {
		return target.equals(obj);
	}

	public float getBoost() {
		return target.getBoost();
	}

	public boolean getOmitNorms() {
		return target.getOmitNorms();
	}

	public int hashCode() {
		return target.hashCode();
	}

	public final boolean isBinary() {
		return target.isBinary();
	}

	public final boolean isCompressed() {
		return target.isCompressed();
	}

	public final boolean isIndexed() {
		return target.isIndexed();
	}

	public boolean isLazy() {
		return target.isLazy();
	}

	public final boolean isStored() {
		return target.isStored();
	}

	public boolean isStoreOffsetWithTermVector() {
		return target.isStoreOffsetWithTermVector();
	}

	public boolean isStorePositionWithTermVector() {
		return target.isStorePositionWithTermVector();
	}

	public final boolean isTermVectorStored() {
		return target.isTermVectorStored();
	}

	public final boolean isTokenized() {
		return target.isTokenized();
	}

	public String name() {
		return target.name();
	}

	public Reader readerValue() {
		return target.readerValue();
	}

	public void setBoost(float boost) {
		target.setBoost(boost);
	}

	public void setOmitNorms(boolean omitNorms) {
		target.setOmitNorms(omitNorms);
	}

	public void setValue(byte[] value) {
		target.setValue(value);
	}

	public void setValue(Reader value) {
		target.setValue(value);
	}

	public void setValue(String value) {
		target.setValue(value);
	}

	public void setValue(TokenStream value) {
		target.setValue(value);
	}

	public String stringValue() {
		return target.stringValue();
	}

	public TokenStream tokenStreamValue() {
		return target.tokenStreamValue();
	}

	public final String toString() {
		return target.toString();
	}

	@Override
	public org.apache.lucene.document.Field asNative() {
		return target;
	}

}
