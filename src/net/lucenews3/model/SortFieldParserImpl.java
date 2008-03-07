package net.lucenews3.model;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.search.SortComparatorSource;
import org.apache.lucene.search.SortField;

public class SortFieldParserImpl implements SortFieldParser<String> {

	private transient LocaleParser<String> localeParser;
	private transient SortComparatorSourceParser<String> sortComparatorSourceParser;
	private transient final Pattern pattern;
	private transient final int scoreGroup;
	private transient final int docGroup;
	private transient final int customFieldGroup;
	private transient final int customFactoryGroup;
	private transient final int fieldNameGroup;
	private transient final int localeGroup;
	private transient final int reverseGroup;
	
	public SortFieldParserImpl() {
		this.pattern = Pattern.compile("((<score>)|(<doc>)|(<custom:\"(\\w+)\":\\s*(\\w+)>)|(\"(\\w+)\"))(\\((\\w+)\\))?(!)?");
		this.scoreGroup = 2;
		this.docGroup = 3;
		this.customFieldGroup = 5;
		this.customFactoryGroup = 6;
		this.fieldNameGroup = 8;
		this.localeGroup = 10;
		this.reverseGroup = 11;
	}
	
	public LocaleParser<String> getLocaleParser() {
		return localeParser;
	}

	public void setLocaleParser(LocaleParser<String> localeParser) {
		this.localeParser = localeParser;
	}

	public SortComparatorSourceParser<String> getSortComparatorSourceParser() {
		return sortComparatorSourceParser;
	}

	public void setSortComparatorSourceParser(
			SortComparatorSourceParser<String> sortComparatorSourceParser) {
		this.sortComparatorSourceParser = sortComparatorSourceParser;
	}

	@Override
	public SortField parse(String string) {
		SortField result;
		
		Matcher matcher = pattern.matcher(string);
		if (matcher.matches()) {
			String score = matcher.group(scoreGroup);
			String doc = matcher.group(docGroup);
			String customField = matcher.group(customFieldGroup);
			String customFactory = matcher.group(customFactoryGroup);
			String fieldName = matcher.group(fieldNameGroup);
			String localeString = matcher.group(localeGroup);
			String reverse = matcher.group(reverseGroup);
			
			boolean isScore = (score != null);
			boolean isDoc = (doc != null);
			boolean isCustom = (customField != null && customFactory != null);
			boolean isField = (fieldName != null);
			boolean isReversed = (reverse != null);
			
			// TODO: Sort out locale
			Locale locale;
			if (localeString == null) {
				locale = null;
			} else {
				locale = localeParser.parse(localeString);
			}
			
			if (isScore) {
				result = new SortField(null, SortField.SCORE, isReversed);
			} else if (isDoc) {
				result = new SortField(null, SortField.DOC, isReversed);
			} else if (isCustom) {
				SortComparatorSource sortComparatorSource = sortComparatorSourceParser.parse(customFactory);
				result = new SortField(customField, sortComparatorSource, isReversed);
			} else if (isField) {
				result = new SortField(fieldName, locale, isReversed);
			} else {
				throw new RuntimeException("Cannot parse sort field from '" + string + "'");
			}
		} else {
			throw new RuntimeException("Cannot parse sort field from '" + string + "'");
		}
		
		return result;
	}

}
