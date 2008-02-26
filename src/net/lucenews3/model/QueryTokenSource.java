package net.lucenews3.model;

import org.apache.lucene.queryParser.Token;
import org.apache.lucene.search.Query;

public class QueryTokenSource extends FilterQueryImpl implements TokenSource {

	private static final long serialVersionUID = -1022496241778242436L;
	
	private Token token;
	
	public QueryTokenSource(Query target) {
		super(target);
	}
	
	public QueryTokenSource(Query target, Token token) {
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
