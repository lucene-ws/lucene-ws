package net.lucenews3.model;

public interface IndexMetaDataParser<I> extends Parser<I, IndexMetaData> {

	@Override
	public IndexMetaData parse(I input);
	
}
