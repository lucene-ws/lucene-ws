package net.lucenews3.queryParser;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;

public class LoggingQueryParserDelegate extends QueryParserDelegateAdaptor {

	private Logger logger;
	private int depth;
	
	public LoggingQueryParserDelegate() {
		this.logger = Logger.getLogger(getClass());
		this.depth = 0;
	}
	
	public LoggingQueryParserDelegate(Logger logger) {
		this.logger = logger;
		this.depth = 0;
	}
	
	protected void beforeCall(Object... parameters) {
		depth++;
		Thread thread = Thread.currentThread();
		StackTraceElement[] elements = thread.getStackTrace();
		
		StringBuffer text = new StringBuffer();
		
		for (int i = 0; i < (depth - 1); i++) {
			text.append("  ");
		}
		
		text.append(elements[2].getMethodName());
		text.append("(");
		for (int i = 1; i < parameters.length; i++) {
			if (i > 1) {
				text.append(", ");
			}
			text.append(parameters[i]);
		}
		text.append(")");
		
		logger.info(text);
		System.out.println(text);
	}
	
	protected void afterCall() {
		depth--;
	}

	@Override
	public Query getBooleanQuery(QueryParserInternals parser,
			Vector<BooleanClause> clauses) throws ParseException {
		Query result;
		beforeCall(parser, clauses);
		result = super.getBooleanQuery(parser, clauses);
		afterCall();
		return result;
	}

	@Override
	public Query getBooleanQuery(QueryParserInternals parser,
			Vector<BooleanClause> clauses, boolean disableCoord)
			throws ParseException {
		Query result;
		beforeCall(parser, clauses, disableCoord);
		result = super.getBooleanQuery(parser, clauses, disableCoord);
		afterCall();
		return result;
	}

	@Override
	public Query getFieldQuery(QueryParserInternals parser, String field,
			String queryText) throws ParseException {
		Query result;
		beforeCall(parser, field, queryText);
		result = super.getFieldQuery(parser, field, queryText);
		afterCall();
		return result;
	}

	@Override
	public Query getFieldQuery(QueryParserInternals parser, String field,
			String queryText, int slop) throws ParseException {
		Query result;
		beforeCall(parser, field, queryText, slop);
		result = super.getFieldQuery(parser, field, queryText, slop);
		afterCall();
		return result;
	}

	@Override
	public Query getFuzzyTerm(QueryParserInternals parser, String field, String termStr,
			float minSimilarity) throws ParseException {
		Query result;
		beforeCall(parser, field, termStr, minSimilarity);
		result = super.getFuzzyTerm(parser, field, termStr, minSimilarity);
		afterCall();
		return result;
	}

	@Override
	public Query getPrefixQuery(QueryParserInternals parser, String field, String termStr)
			throws ParseException {
		Query result;
		beforeCall(parser, field, termStr);
		result = super.getPrefixQuery(parser, field, termStr);
		afterCall();
		return result;
	}

	@Override
	public Query getRangeQuery(QueryParserInternals parser, String field, String part1,
			String part2, boolean inclusive) throws ParseException {
		Query result;
		beforeCall(parser, field, part1, part2, inclusive);
		result = super.getRangeQuery(parser, field, part1, part2, inclusive);
		afterCall();
		return result;
	}

	@Override
	public Query getWildcardQuery(QueryParserInternals parser, String field,
			String termStr) throws ParseException {
		Query result;
		beforeCall(parser, field, termStr);
		result = super.getWildcardQuery(parser, field, termStr);
		afterCall();
		return result;
	}

	@Override
	public Query parse(QueryParserInternals parser, String queryText)
			throws ParseException {
		Query result;
		beforeCall(parser, queryText);
		result = super.parse(parser, queryText);
		afterCall();
		return result;
	}
	
}
