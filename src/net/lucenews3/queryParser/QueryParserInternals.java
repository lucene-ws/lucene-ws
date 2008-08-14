package net.lucenews3.queryParser;

import java.util.Vector;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.Token;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;

public interface QueryParserInternals {

	public Query parse(String queryText) throws ParseException;

	public Query getFieldQuery(String field, String queryText) throws ParseException;

	public Query getFieldQuery(String field, String queryText, int slop) throws ParseException;

	public Query getRangeQuery(String field, String part1, String part2, boolean inclusive) throws ParseException;

	public Query getBooleanQuery(Vector<BooleanClause> clauses) throws ParseException;

	public Query getBooleanQuery(Vector<BooleanClause> clauses, boolean disableCoord) throws ParseException;

	public Query getWildcardQuery(String field, String termStr) throws ParseException;

	public Query getPrefixQuery(String field, String termStr) throws ParseException;

	public Query getFuzzyTerm(String field, String termStr, float minSimilarity) throws ParseException;
	
	public Token getToken(int index);

}
