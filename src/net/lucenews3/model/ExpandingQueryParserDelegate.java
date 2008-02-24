package net.lucenews3.model;

import net.lucenews.model.QueryExpander;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;

/**
 * Expands queries to include synonyms.
 *
 */
public class ExpandingQueryParserDelegate extends QueryParserDelegateAdaptor {
	
	private QueryExpander queryExpander;
	
	public ExpandingQueryParserDelegate() {
		
	}
	
	public ExpandingQueryParserDelegate(QueryExpander queryExpander) {
		this.queryExpander = queryExpander;
	}

	public QueryExpander getQueryExpander() {
		return queryExpander;
	}

	public void setQueryExpander(QueryExpander queryExpander) {
		this.queryExpander = queryExpander;
	}

	@Override
	public Query parse(QueryParserInternals parser, String queryText) throws ParseException {
		return expand(parser.parse(queryText));
	}
	
	public Query expand(Query query) {
		return queryExpander.expand(query);
	}
	
}
