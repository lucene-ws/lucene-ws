package net.lucenews3.lucene.support;

public interface IndexMetaDataParser<I> extends Parser<I, IndexMetaData> {

	@Override
	public IndexMetaData parse(I input);
	
}
