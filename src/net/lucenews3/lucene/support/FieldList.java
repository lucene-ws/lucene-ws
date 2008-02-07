package net.lucenews3.lucene.support;

import java.util.List;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;

public interface FieldList extends List<Field> {

	public boolean add(Fieldable fieldable);
	
}
