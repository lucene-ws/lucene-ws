package net.lucenews3.model;

import java.util.List;

public interface FieldList extends List<Field> {
	
	public boolean add(org.apache.lucene.document.Fieldable fieldable);
	
	public FieldList byName(String name);
	
	public Field first();
	
	public Field only();
	
}
