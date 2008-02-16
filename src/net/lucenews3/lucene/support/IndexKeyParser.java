package net.lucenews3.lucene.support;

public interface IndexKeyParser<I> extends Parser<I, Object> {

	@Override
	public Object parse(I input);
	
}
