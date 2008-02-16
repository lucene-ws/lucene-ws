package net.lucenews3.lucene.support;

import java.util.Locale;

public interface LocaleParser<I> extends Parser<I, Locale> {

	public Locale parse(I input);
	
}
