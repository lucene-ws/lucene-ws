package net.lucenews3.model;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LocaleStringParserTest {

	private LocaleStringParser parser;
	
	@Before
	public void setup() {
		this.parser = new LocaleStringParser();
	}
	
	protected void test(String... tokens) {
		Locale expectedLocale;
		
		switch (tokens.length) {
		case 1:
			expectedLocale = new Locale(tokens[0]);
			break;
		case 2:
			expectedLocale = new Locale(tokens[0], tokens[1]);
			break;
		case 3:
			expectedLocale = new Locale(tokens[0], tokens[1], tokens[2]);
			break;
		default:
			throw new IllegalArgumentException();
		}
		
		test(expectedLocale);
	}
	
	protected void test(Locale expectedLocale) {
		String localeString = expectedLocale.toString();
		
		Locale actualLocale = parser.parse(localeString);
		
		Assert.assertEquals(expectedLocale, actualLocale);
	}
	
	@Test
	public void testEnUS() {
		test("en", "US");
	}
	
	@Test
	public void testUS() {
		test(Locale.US);
	}
	
}
