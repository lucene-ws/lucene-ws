package net.lucenews3.lucene.support;

public interface IndexRangeParser<I> extends Parser<I, IndexRange> {

	@Override
	public IndexRange parse(I input);
	
}
