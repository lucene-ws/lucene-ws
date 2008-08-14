package net.lucenews3.queryParser;

import java.util.Vector;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;

public interface QueryParserDelegate {

	public Query parse(QueryParserInternals parser, String queryText) throws ParseException;

	public Query getFieldQuery(QueryParserInternals parser, String field, String queryText) throws ParseException;

	public Query getFieldQuery(QueryParserInternals parser, String field, String queryText, int slop) throws ParseException;

	public Query getRangeQuery(QueryParserInternals parser, String field, String part1, String part2, boolean inclusive) throws ParseException;

	public Query getBooleanQuery(QueryParserInternals parser, Vector<BooleanClause> clauses) throws ParseException;

	public Query getBooleanQuery(QueryParserInternals parser, Vector<BooleanClause> clauses, boolean disableCoord) throws ParseException;

	public Query getWildcardQuery(QueryParserInternals parser, String field, String termStr) throws ParseException;

	public Query getPrefixQuery(QueryParserInternals parser, String field, String termStr) throws ParseException;

	public Query getFuzzyTerm(QueryParserInternals parser, String field, String termStr, float minSimilarity) throws ParseException;

}
