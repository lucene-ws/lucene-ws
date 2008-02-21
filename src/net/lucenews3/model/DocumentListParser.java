package net.lucenews3.model;

import java.util.List;

public interface DocumentListParser<I> extends Parser<I, List<Document>> {

	@Override
	public List<Document> parse(I input);
	
}
