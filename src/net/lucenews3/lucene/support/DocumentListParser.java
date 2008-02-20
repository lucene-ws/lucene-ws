package net.lucenews3.lucene.support;

import java.util.List;

public interface DocumentListParser<I> extends Parser<I, List<Document>> {

	@Override
	public List<Document> parse(I input);
	
}
