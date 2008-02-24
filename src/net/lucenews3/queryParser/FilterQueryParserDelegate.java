package net.lucenews3.queryParser;

import java.util.Vector;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;

public class FilterQueryParserDelegate implements QueryParserDelegate {

	protected QueryParserDelegate target;
	
	public FilterQueryParserDelegate() {
		
	}
	
	public FilterQueryParserDelegate(QueryParserDelegate target) {
		this.target = target;
	}
	
	public QueryParserDelegate getTarget() {
		return target;
	}

	public void setTarget(QueryParserDelegate target) {
		this.target = target;
	}

	@Override
	public Query getBooleanQuery(QueryParserInternals parser,
			Vector<BooleanClause> clauses) throws ParseException {
		return target.getBooleanQuery(parser, clauses);
	}

	@Override
	public Query getBooleanQuery(QueryParserInternals parser,
			Vector<BooleanClause> clauses, boolean disableCoord)
			throws ParseException {
		return target.getBooleanQuery(parser, clauses, disableCoord);
	}

	@Override
	public Query getFieldQuery(QueryParserInternals parser, String field,
			String queryText) throws ParseException {
		return target.getFieldQuery(parser, field, queryText);
	}

	@Override
	public Query getFieldQuery(QueryParserInternals parser, String field,
			String queryText, int slop) throws ParseException {
		return target.getFieldQuery(parser, field, queryText, slop);
	}

	@Override
	public Query getFuzzyTerm(QueryParserInternals parser, String field, String termStr,
			float minSimilarity) throws ParseException {
		return target.getFuzzyTerm(parser, field, termStr, minSimilarity);
	}

	@Override
	public Query getPrefixQuery(QueryParserInternals parser, String field, String termStr)
			throws ParseException {
		return target.getPrefixQuery(parser, field, termStr);
	}

	@Override
	public Query getRangeQuery(QueryParserInternals parser, String field, String part1,
			String part2, boolean inclusive) throws ParseException {
		return target.getRangeQuery(parser, field, part1, part2, inclusive);
	}

	@Override
	public Query getWildcardQuery(QueryParserInternals parser, String field,
			String termStr) throws ParseException {
		return target.getWildcardQuery(parser, field, termStr);
	}

	@Override
	public Query parse(QueryParserInternals parser, String queryText)
			throws ParseException {
		return target.parse(parser, queryText);
	}


}
