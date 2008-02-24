package net.lucenews3.model;

import java.util.Vector;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;

public interface QueryParserDelegate {

	public Query parse(QueryParser parser, String queryText)
			throws ParseException;

	public Query getFieldQuery(QueryParser parser, String field, String queryText)
			throws ParseException;

	public Query getFieldQuery(QueryParser parser, String field, String queryText, int slop)
			throws ParseException;

	public Query getRangeQuery(QueryParser parser, String field, String part1, String part2, boolean inclusive)
			throws ParseException;

	public Query getBooleanQuery(QueryParser parser, Vector<BooleanClause> clauses)
			throws ParseException;

	public Query getBooleanQuery(QueryParser parser, Vector<BooleanClause> clauses, boolean disableCoord)
			throws ParseException;

	public Query getWildcardQuery(QueryParser parser, String field, String termStr)
			throws ParseException;

	public Query getPrefixQuery(QueryParser parser, String field, String termStr)
			throws ParseException;

	public Query getFuzzyTerm(QueryParser parser, String field, String termStr, float minSimilarity)
			throws ParseException;

}
