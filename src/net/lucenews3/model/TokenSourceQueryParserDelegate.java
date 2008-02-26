package net.lucenews3.model;

import net.lucenews3.queryParser.QueryParserDelegateAdaptor;
import net.lucenews3.queryParser.QueryParserInternals;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.Token;
import org.apache.lucene.search.Query;

public class TokenSourceQueryParserDelegate extends QueryParserDelegateAdaptor {

	@Override
	public Query parse(QueryParserInternals parser, String queryText) throws ParseException {
		Token token = parser.getToken(0);
		if (token == null) {
			token = buildToken(queryText);
		} else if (token.image == null) {
			buildToken(queryText, token);
		}
		final Query target = super.parse(parser, queryText);
		
		return attachToken(target, token);
	}
	
	@Override
	public Query getFieldQuery(QueryParserInternals parser, String field, String queryText) throws ParseException {
		final Token token = parser.getToken(0);
		final Query target = super.getFieldQuery(parser, field, queryText);
		
		return attachToken(target, token);
	}
	
	public Query attachToken(final Query target, final Token token) {
		return new QueryTokenSource(target, token);
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
	
}
