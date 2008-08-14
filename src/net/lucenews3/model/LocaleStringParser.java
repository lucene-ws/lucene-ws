package net.lucenews3.model;

import java.util.Locale;

public class LocaleStringParser implements LocaleParser<String> {

	@Override
	public Locale parse(String input) {
		String[] tokens = input.split("_");
		
		switch (tokens.length) {
		case 1:
			return new Locale(tokens[0]);
			
		case 2:
			return new Locale(tokens[0], tokens[1]);
			
		case 3:
			return new Locale(tokens[0], tokens[1], tokens[2]);
			
		default:
			throw new IllegalArgumentException();
		}
	}

}
