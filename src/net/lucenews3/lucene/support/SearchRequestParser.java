package net.lucenews3.lucene.support;

public interface SearchRequestParser<I> extends Parser<I, SearchRequest> {

	@Override
	public SearchRequest parse(I input);
	
}
