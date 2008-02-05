package net.lucenews3.lucene.support;

import org.apache.lucene.search.SortField;

public interface SortFieldParser {

	public SortField parse(String string);
	
}
