package net.lucenews3.lucene.support;

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

}
