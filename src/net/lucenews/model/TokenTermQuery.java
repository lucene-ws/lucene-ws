package net.lucenews.model;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;

public class TokenTermQuery extends TermQuery {

	private static final long serialVersionUID = 7616743947708467541L;

	private org.apache.lucene.queryParser.Token token;

	public TokenTermQuery(Term term) {
		super(term);
	}

	public TokenTermQuery(Term term, org.apache.lucene.queryParser.Token token) {
		super(term);
		this.token = token;
	}

	public org.apache.lucene.queryParser.Token getToken() {
		return token;
	}

}
