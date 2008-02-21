package net.lucenews3.model;

import java.util.Locale;

public interface LocaleParser<I> extends Parser<I, Locale> {

	@Override
	public Locale parse(I input);
	
}
