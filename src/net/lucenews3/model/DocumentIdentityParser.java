package net.lucenews3.model;

public interface DocumentIdentityParser<I> extends Parser<I, DocumentIdentity> {

	@Override
	public DocumentIdentity parse(I input);
	
}
