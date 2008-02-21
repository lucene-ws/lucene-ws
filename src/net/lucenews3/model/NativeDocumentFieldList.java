package net.lucenews3.model;

import java.util.AbstractList;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;

public class NativeDocumentFieldList extends AbstractList<Field> implements FieldList {

	private org.apache.lucene.document.Document nativeDocument;
	
	public NativeDocumentFieldList(org.apache.lucene.document.Document nativeDocument) {
		this.nativeDocument = nativeDocument;
	}
	
	@Override
	public boolean add(Field field) {
		nativeDocument.add(field);
		return true;
	}

	@Override
	public boolean add(Fieldable fieldable) {
		nativeDocument.add(fieldable);
		return true;
	}
	
	@Override
	public Field get(int index) {
		return (Field) nativeDocument.getFields().get(index);
	}

	@Override
	public Field remove(int index) {
		return null;
	}
	
	@Override
	public int size() {
		return nativeDocument.getFields().size();
	}

	public FieldList byName(String name) {
		FieldList results = new FieldListImpl();
		for (Field field : this) {
			if (field.name().equals(name)) {
				results.add(field);
			}
		}
		return results;
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
