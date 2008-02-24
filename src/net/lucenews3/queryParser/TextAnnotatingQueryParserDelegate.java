package net.lucenews3.queryParser;

import java.util.Vector;

import net.lucenews3.model.Notes;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.Token;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;

/**
 * Annotates produced queries with their textual source. This allows for
 * regeneration of original, user-provided queries from Lucene's objects.
 *
 */
public class TextAnnotatingQueryParserDelegate extends QueryParserDelegateAdaptor {

	@Override
	public Query parse(QueryParserInternals parser, String queryText) throws ParseException {
		Token token = parser.getToken(0);
		if (token == null) {
			token = buildToken(queryText);
		} else if (token.image == null) {
			buildToken(queryText, token);
		}
		return annotate(token, super.parse(parser, queryText));
	}
	
	@Override
	public Query getFieldQuery(QueryParserInternals parser, String field, String queryText) throws ParseException {
		Token token = parser.getToken(0);
		return annotate(token, super.getFieldQuery(parser, field, queryText));
	}

	@Override
	public Query getBooleanQuery(QueryParserInternals parser,
			Vector<BooleanClause> clauses, boolean disableCoord)
			throws ParseException {
		Token token = parser.getToken(0);
		return annotate(token, target.getBooleanQuery(parser, clauses, disableCoord));
	}
	
	public Token buildToken(String text) {
		final Token token = new Token();
		buildToken(text, token);
		return token;
	}
	
	public void buildToken(String text, Token token) {
		token.beginLine = 1;
		token.beginColumn = 0;
		token.endLine = 1;
		token.endColumn = text.length();
		token.image = text;
	}
	
	/**
	 * Produces a query which has been annotated with the given string.
	 * @param query
	 * @param string
	 * @return
	 */
	public Query annotate(Token token, Query query) {
		Notes.put(query, "token", token);
		return query;
	}
	
}
