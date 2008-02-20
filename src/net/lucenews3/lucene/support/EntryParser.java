package net.lucenews3.lucene.support;

import net.lucenews3.atom.Entry;

public interface EntryParser<I> extends Parser<I, Entry> {

	@Override
	public Entry parse(I input);
	
}
