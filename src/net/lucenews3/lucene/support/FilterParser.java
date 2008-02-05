package net.lucenews3.lucene.support;

import org.apache.lucene.search.Filter;

public interface FilterParser {

	public Filter parse(String string);
	
}
