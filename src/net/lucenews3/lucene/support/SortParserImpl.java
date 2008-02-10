package net.lucenews3.lucene.support;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

public class SortParserImpl implements SortParser {

	private SortFieldParser sortFieldParser;
	private String sortFieldDelimiter;
	
	public SortParserImpl() {
		this(new SortFieldParserImpl());
	}
	
	public SortParserImpl(SortFieldParser sortFieldParser) {
		this.sortFieldParser = sortFieldParser;
		this.sortFieldDelimiter = ",";
	}
	
	public SortFieldParser getSortFieldParser() {
		return sortFieldParser;
	}

	public void setSortFieldParser(SortFieldParser sortFieldParser) {
		this.sortFieldParser = sortFieldParser;
	}

	public String getSortFieldDelimiter() {
		return sortFieldDelimiter;
	}

	public void setSortFieldDelimiter(String sortFieldDelimiter) {
		this.sortFieldDelimiter = sortFieldDelimiter;
	}

	/**
	 * Splits the given string by the sort field delimiter. Each token
	 * produced is parse via the sort field parser. The final sort is 
	 * produced via the parsed sort fields.
	 */
	public Sort parseSort(String string) {
		final String[] sortFieldStrings = string.split(sortFieldDelimiter);
		final SortField[] sortFields = new SortField[sortFieldStrings.length];
		for (int i = 0; i < sortFields.length; i++) {
			final String sortFieldString = sortFieldStrings[i];
			final SortField sortField = sortFieldParser.parseSortField(sortFieldString);
			sortFields[i] = sortField;
		}
		return new Sort(sortFields);
	}

}
