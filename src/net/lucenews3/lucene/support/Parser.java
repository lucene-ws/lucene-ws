package net.lucenews3.lucene.support;

public interface Parser<I, O> {

	public O parse(I input);
	
}
