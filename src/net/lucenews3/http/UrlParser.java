package net.lucenews3.http;

import net.lucenews3.model.Parser;

public interface UrlParser<I> extends Parser<I, Url> {

	@Override
	public Url parse(I input);
	
}
