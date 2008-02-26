package net.lucenews3.model;

import org.apache.lucene.queryParser.Token;
import org.apache.lucene.search.TermQuery;

public class TermQueryTokenSource extends FilterTermQueryImpl implements TokenSource {

	private static final long serialVersionUID = 6464666487258309377L;

	private Token token;
	
	public TermQueryTokenSource(TermQuery termQuery) {
		super(termQuery);
	}
	
	public TermQueryTokenSource(TermQuery termQuery, Token token) {
		super(termQuery);
		this.token = token;
	}
	
	@Override
	public Token getToken() {
		return token;
	}

	@Override
	public void setToken(Token token) {
		this.token = token;
	}

}
