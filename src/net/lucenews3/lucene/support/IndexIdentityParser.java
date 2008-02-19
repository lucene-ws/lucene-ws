package net.lucenews3.lucene.support;

public interface IndexIdentityParser<I> extends Parser<I, IndexIdentity> {

	@Override
	public IndexIdentity parse(I input);
	
}
