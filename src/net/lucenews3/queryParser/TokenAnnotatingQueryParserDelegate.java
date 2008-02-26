package net.lucenews3.queryParser;

import java.util.Vector;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.Token;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;

/**
 * Annotates produced queries with their textual source. This allows for
 * regeneration of original, user-provided queries from Lucene's objects.
 *
 */
public class TokenAnnotatingQueryParserDelegate extends QueryParserDelegateAdaptor {

	private TokenSourceQueryAnnotator annotator;
	
	public TokenAnnotatingQueryParserDelegate() {
		this.annotator = new TokenSourceQueryAnnotator();
	}
	
	@Override
	public Query parse(QueryParserInternals parser, String queryText) throws ParseException {
		Token token = parser.getToken(0);
		if (token == null) {
			token = buildToken(queryText);
		} else if (token.image == null || token.image.trim().length() == 0) {
			buildToken(queryText, token);
		}
		return annotate(super.parse(parser, queryText), token);
	}
	
	@Override
	public Query getFieldQuery(QueryParserInternals parser, String field, String queryText) throws ParseException {
		Token token = parser.getToken(0);
		return annotate(super.getFieldQuery(parser, field, queryText), token);
	}

	@Override
	public Query getBooleanQuery(QueryParserInternals parser,
			Vector<BooleanClause> clauses, boolean disableCoord)
			throws ParseException {
		Token token = parser.getToken(0);
		return annotate(target.getBooleanQuery(parser, clauses, disableCoord), token);
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
	public Query annotate(final Query query, final Token token) {
		return annotator.annotate(query, token);
	}
	
}
