package net.lucenews3.queryParser;

public class QueryParserDelegateAdaptor extends FilterQueryParserDelegate {
	
	public QueryParserDelegateAdaptor() {
		super(new SimpleQueryParserDelegate());
	}
	
	public QueryParserDelegateAdaptor(QueryParserDelegate target) {
		super(target);
	}

}
