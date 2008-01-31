package net.lucenews.model;

import org.apache.lucene.search.BooleanQuery;

public class TokenBooleanQuery extends BooleanQuery {

	private static final long serialVersionUID = 8962379917036321834L;

	private org.apache.lucene.queryParser.Token token;

	public TokenBooleanQuery() {
		super();
	}

	public TokenBooleanQuery(boolean disableCoord) {
		super(disableCoord);
	}

	public TokenBooleanQuery(org.apache.lucene.queryParser.Token token) {
		super();
		this.token = token;
	}

	public TokenBooleanQuery(boolean disableCoord,
			org.apache.lucene.queryParser.Token token) {
		super(disableCoord);
		this.token = token;
	}

	public org.apache.lucene.queryParser.Token getToken() {
		return token;
	}

}
