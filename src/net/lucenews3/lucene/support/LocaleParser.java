package net.lucenews3.lucene.support;

import java.util.Locale;

public interface LocaleParser<I> {

	public Locale parseLocale(I input);
	
}
