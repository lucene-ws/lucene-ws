package net.lucenews3.lucene.support;

public interface ClassParser<T, I> extends Parser<I, Class<T>> {

	@Override
	public Class<T> parse(I input);
	
}
