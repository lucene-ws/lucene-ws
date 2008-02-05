package net.lucenews3.lucene;

import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Field;

public interface FieldList extends List<Field> {

	public Map<String, FieldList> byName();
	
	public FieldList byName(String name);
	
}
