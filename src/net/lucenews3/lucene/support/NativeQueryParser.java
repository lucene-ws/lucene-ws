package net.lucenews3.lucene.support;

import net.lucenews.http.ExceptionWrapper;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;

public class NativeQueryParser implements QueryParser<String> {

	private org.apache.lucene.queryParser.QueryParser queryParser;
	private ExceptionWrapper exceptionWrapper;
	
	public NativeQueryParser(org.apache.lucene.queryParser.QueryParser queryParser) {
		this.queryParser = queryParser;
	}
	
	@Override
	public Query parse(String string) {
		try {
			return queryParser.parse(string);
		} catch (ParseException e) {
			throw exceptionWrapper.wrap(e);
		}
	}

}
