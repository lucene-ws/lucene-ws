package net.lucenews3.model;

import java.util.Locale;

public class LocaleStringParser implements LocaleParser<String> {

	@Override
	public Locale parse(String input) {
		final Locale result;
		
		final String[] tokens = input.split("_");
		
		switch (tokens.length) {
		case 1:
			result = new Locale(tokens[0]);
			break;
		case 2:
			result = new Locale(tokens[0], tokens[1]);
			break;
		case 3:
			result = new Locale(tokens[0], tokens[1], tokens[2]);
			break;
		default:
			throw new IllegalArgumentException();
		}
		
		return result;
	}

}
