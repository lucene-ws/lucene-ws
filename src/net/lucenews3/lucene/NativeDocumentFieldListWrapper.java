package net.lucenews3.lucene;

import java.util.AbstractList;
import java.util.Map;

import org.apache.lucene.document.Field;

public class NativeDocumentFieldListWrapper extends AbstractList<Field> implements FieldList {
	
	private org.apache.lucene.document.Document nativeDocument;
	
	public NativeDocumentFieldListWrapper(org.apache.lucene.document.Document nativeDocument) {
		this.nativeDocument = nativeDocument;
	}

	@Override
	public boolean add(Field field) {
		nativeDocument.add(field);
		return true;
	}
	
	@Override
	public Field get(int index) {
		return (Field) nativeDocument.getFields().get(index);
	}

	@Override
	public int size() {
		return nativeDocument.getFields().size();
	}

	@Override
	public Map<String, FieldList> byName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FieldList byName(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
