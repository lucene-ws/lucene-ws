package net.lucenews3.lucene.support;

public interface SearchRequestParser<I> {

	public SearchRequest parseSearchRequest(I input);
	
}
