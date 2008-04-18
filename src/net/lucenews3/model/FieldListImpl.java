package net.lucenews3.model;

import java.util.ArrayList;
import java.util.Collection;

public class FieldListImpl extends ArrayList<Field> implements FieldList {

	private static final long serialVersionUID = -945660472095730009L;

	public FieldListImpl() {
		super();
	}

	public FieldListImpl(Collection<? extends Field> collection) {
		super(collection);
	}

	public FieldListImpl(int initialCapacity) {
		super(initialCapacity);
	}

	public boolean add(org.apache.lucene.document.Fieldable fieldable) {
		org.apache.lucene.document.Field field = new org.apache.lucene.document.Field(fieldable.name(), fieldable.stringValue(), fieldable.isStored() ? org.apache.lucene.document.Field.Store.YES : org.apache.lucene.document.Field.Store.NO, fieldable.isTokenized() ? org.apache.lucene.document.Field.Index.TOKENIZED : org.apache.lucene.document.Field.Index.UN_TOKENIZED);
		return add(field);
	}

	public FieldList byName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public Field first() {
		return get(0);
	}

	public Field only() {
		final int size = size();
		switch (size) {
		case 0:
			throw new RuntimeException();
		case 1:
			return get(0);
		default:
			throw new RuntimeException();
		}
	}

}
