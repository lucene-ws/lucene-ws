package net.lucenews3.lucene.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;

import org.apache.lucene.search.SortComparatorSource;

public class SortComparatorSourceParserImpl implements SortComparatorSourceParser<String> {

	private ClassParser<? extends SortComparatorSource, String> classParser;
	private Pattern objectStringPattern;
	private ExceptionTranslator exceptionTranslator;
	
	public SortComparatorSourceParserImpl() {
		this.objectStringPattern = Pattern.compile("(\\w+)(\\@([0-9a-fA-F]+)?)");
		this.exceptionTranslator = new ExceptionTranslatorImpl();
		this.classParser = new ClassParserImpl<SortComparatorSource>(SortComparatorSource.class);
	}
	
	@Override
	public SortComparatorSource parse(String string) {
		SortComparatorSource result;
		
		Matcher matcher = objectStringPattern.matcher(string);
		if (matcher.matches()) {
			String className = matcher.group(1);
			try {
				result = classParser.parse(className).newInstance();
			} catch (InstantiationException e) {
				throw exceptionTranslator.translate(e);
			} catch (IllegalAccessException e) {
				throw exceptionTranslator.translate(e);
			}
		} else {
			throw new RuntimeException("Cannot parse \"" + string + "\" into a SortComparatorSource");
		}
		
		return result;
	}
	
}
