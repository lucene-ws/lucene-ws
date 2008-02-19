package net.lucenews3.lucene.support;

public interface DocumentIdentityParser<I> extends Parser<I, DocumentIdentity> {

	@Override
	public DocumentIdentity parse(I input);
	
}
