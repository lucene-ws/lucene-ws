package net.lucenews3.model;

public interface SearchRequestParser<I> extends Parser<I, SearchRequest> {

	@Override
	public SearchRequest parse(I input);
	
}
