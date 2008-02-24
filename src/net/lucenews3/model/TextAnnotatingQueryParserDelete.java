package net.lucenews3.model;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;

/**
 * Annotates produced queries with their textual source. This allows for
 * regeneration of original, user-provided queries from Lucene's objects.
 *
 */
public class TextAnnotatingQueryParserDelete extends QueryParserDelegateAdaptor {

	@Override
	public Query parse(QueryParser parser, String queryText) throws ParseException {
		return annotate(parser.parse(queryText), queryText);
	}
	
	/**
	 * Produces a query which has been annotated with the given string.
	 * @param query
	 * @param string
	 * @return
	 */
	public Query annotate(Query query, String queryText) {
		return query;
	}
	
}
