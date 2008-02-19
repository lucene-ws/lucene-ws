package net.lucenews3.lucene.support;

import net.lucenews3.ExceptionTranslator;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;

public class NativeQueryParser implements QueryParser<String> {

	private org.apache.lucene.queryParser.QueryParser queryParser;
	private ExceptionTranslator exceptionTranslator;
	
	public NativeQueryParser(org.apache.lucene.queryParser.QueryParser queryParser) {
		this.queryParser = queryParser;
	}
	
	@Override
	public Query parse(String string) {
		try {
			return queryParser.parse(string);
		} catch (ParseException e) {
			throw exceptionTranslator.translate(e);
		}
	}

}
