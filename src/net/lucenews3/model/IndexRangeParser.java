package net.lucenews3.model;

public interface IndexRangeParser<I> extends Parser<I, IndexRange> {

	@Override
	public IndexRange parse(I input);
	
}
