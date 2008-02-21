package net.lucenews3.model;

import net.lucenews3.ExceptionTranslator;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;

/**
 * Parses strings into queries using a native Lucene QueryParser.
 * 
 */
public class NativeQueryParser implements QueryParser<String> {

	private org.apache.lucene.queryParser.QueryParser queryParser;
	private ExceptionTranslator exceptionTranslator;

	public NativeQueryParser() {
		
	}
	
	public NativeQueryParser(org.apache.lucene.queryParser.QueryParser queryParser) {
		this.queryParser = queryParser;
	}

	public org.apache.lucene.queryParser.QueryParser getQueryParser() {
		return queryParser;
	}

	public void setQueryParser(org.apache.lucene.queryParser.QueryParser queryParser) {
		this.queryParser = queryParser;
	}

	public ExceptionTranslator getExceptionTranslator() {
		return exceptionTranslator;
	}

	public void setExceptionTranslator(ExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
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
