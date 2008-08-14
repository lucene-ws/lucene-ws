package net.lucenews3.queryParser;

import java.util.Vector;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;

public class SimpleQueryParserDelegate implements QueryParserDelegate {
	
	@Override
	public Query getBooleanQuery(QueryParserInternals parser, Vector<BooleanClause> clauses) throws ParseException {
		return parser.getBooleanQuery(clauses);
	}

	@Override
	public Query getBooleanQuery(QueryParserInternals parser, Vector<BooleanClause> clauses, boolean disableCoord) throws ParseException {
		return parser.getBooleanQuery(clauses, disableCoord);
	}

	@Override
	public Query getFieldQuery(QueryParserInternals parser, String field, String queryText) throws ParseException {
		return parser.getFieldQuery(field, queryText);
	}

	@Override
	public Query getFieldQuery(QueryParserInternals parser, String field, String queryText, int slop) throws ParseException {
		return parser.getFieldQuery(field, queryText, slop);
	}

	@Override
	public Query getFuzzyTerm(QueryParserInternals parser, String field, String termStr, float minSimilarity) throws ParseException {
		return parser.getFuzzyTerm(field, termStr, minSimilarity);
	}

	@Override
	public Query getPrefixQuery(QueryParserInternals parser, String field, String termStr) throws ParseException {
		return parser.getPrefixQuery(field, termStr);
	}

	@Override
	public Query getRangeQuery(QueryParserInternals parser, String field, String part1, String part2, boolean inclusive) throws ParseException {
		return parser.getRangeQuery(field, part1, part2, inclusive);
	}

	@Override
	public Query getWildcardQuery(QueryParserInternals parser, String field, String termStr) throws ParseException {
		return parser.getWildcardQuery(field, termStr);
	}

	@Override
	public Query parse(QueryParserInternals parser, String queryText) throws ParseException {
		return parser.parse(queryText);
	}

}
