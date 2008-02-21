package net.lucenews3.model;

import java.text.DateFormat;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

public class ClassParserImplTest {

	@Test
	public void testLocaleClass() {
		ClassParser<Locale, String> parser = new ClassParserImpl<Locale>(Locale.class);
		Class<Locale> type = parser.parse("java.util.Locale");
		Assert.assertEquals(Locale.class, type);
	}
	
	@Test
	public void testValidStrictClass() {
		ClassParser<DateFormat, String> parser = new ClassParserImpl<DateFormat>(DateFormat.class);
		Class<DateFormat> type = parser.parse("java.text.SimpleDateFormat");
		Assert.assertTrue(DateFormat.class.isAssignableFrom(type));
	}
	
	@Test(expected=RuntimeException.class)
	public void testInvalidStrictClass() {
		ClassParser<DateFormat, String> parser = new ClassParserImpl<DateFormat>(DateFormat.class);
		parser.parse("java.lang.Object");
	}
	
}
