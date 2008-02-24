package net.lucenews3.model;

import net.lucenews3.queryParser.QueryParserDelegateAdaptor;
import net.lucenews3.queryParser.QueryParserInternals;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;

public class MultiFieldQueryParserDelegate extends QueryParserDelegateAdaptor {

	private String defaultField;
	
	public boolean isDefaultField(String field) {
		return field.equals(defaultField);
	}
	
	@Override
	public Query getFieldQuery(QueryParserInternals parser, String field, String queryText)
			throws ParseException {
		return super.getFieldQuery(parser, field, queryText);
	}
	
}
