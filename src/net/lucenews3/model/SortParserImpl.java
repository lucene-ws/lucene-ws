package net.lucenews3.model;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

public class SortParserImpl implements SortParser<String> {

	private SortFieldParser<String> sortFieldParser;
	private String sortFieldDelimiter;
	
	public SortParserImpl() {
		this(new SortFieldParserImpl());
	}
	
	public SortParserImpl(SortFieldParser<String> sortFieldParser) {
		this.sortFieldParser = sortFieldParser;
		this.sortFieldDelimiter = ",";
	}
	
	public SortFieldParser<String> getSortFieldParser() {
		return sortFieldParser;
	}

	public void setSortFieldParser(SortFieldParser<String> sortFieldParser) {
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
	@Override
	public Sort parse(String string) {
		String[] sortFieldStrings = string.split(sortFieldDelimiter);
		SortField[] sortFields = new SortField[sortFieldStrings.length];
		for (int i = 0; i < sortFields.length; i++) {
			String sortFieldString = sortFieldStrings[i];
			SortField sortField = sortFieldParser.parse(sortFieldString);
			sortFields[i] = sortField;
		}
		return new Sort(sortFields);
	}

}
