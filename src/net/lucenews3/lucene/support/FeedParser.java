package net.lucenews3.lucene.support;

import net.lucenews3.atom.Feed;

public interface FeedParser<I> extends Parser<I, Feed> {

	@Override
	public Feed parse(I input);
	
}
