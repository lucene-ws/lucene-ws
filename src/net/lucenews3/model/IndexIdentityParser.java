package net.lucenews3.model;

public interface IndexIdentityParser<I> extends Parser<I, IndexIdentity> {

	@Override
	public IndexIdentity parse(I input);
	
}
