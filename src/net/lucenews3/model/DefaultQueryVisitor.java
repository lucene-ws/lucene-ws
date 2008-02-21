package net.lucenews3.model;

import java.util.List;

import net.lucenews3.ExceptionTranslator;

import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.ConstantScoreRangeQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.function.CustomScoreQuery;
import org.apache.lucene.search.function.ValueSourceQuery;
import org.apache.lucene.search.spans.SpanQuery;

public class DefaultQueryVisitor extends ReflectiveQueryVisitor {
	
	public DefaultQueryVisitor() {
		super();
	}
	
	public DefaultQueryVisitor(ExceptionTranslator exceptionTranslator) {
		super(exceptionTranslator);
	}
	
	/**
	 * Visits the queries of the given query's boolean clauses.
	 * @param query
	 * @return
	 */
	public Object visit(BooleanQuery query) {
		final List<?> objects = query.clauses();
		for (final Object object : objects) {
			final BooleanClause clause = (BooleanClause) object;
			visit(clause.getQuery());
		}
		return null;
	}
	
	public Object visit(ConstantScoreQuery query) {
		return null;
	}
	
	public Object visit(ConstantScoreRangeQuery query) {
		return null;
	}
	
	public Object visit(CustomScoreQuery query) {
		return null;
	}
	
	public Object visit(DisjunctionMaxQuery query) {
		return null;
	}
	
	/**
	 * Visits the query which is causing the filter.
	 * @param query
	 * @return
	 */
	public Object visit(FilteredQuery query) {
		visit(query.getQuery());
		return null;
	}
	
	public Object visit(MatchAllDocsQuery query) {
		return null;
	}
	
	public Object visit(MultiPhraseQuery query) {
		return null;
	}
	
	public Object visit(MultiTermQuery query) {
		return null;
	}
	
	public Object visit(PhraseQuery query) {
		return null;
	}
	
	public Object visit(PrefixQuery query) {
		return null;
	}
	
	public Object visit(RangeQuery query) {
		return null;
	}
	
	public Object visit(SpanQuery query) {
		return null;
	}
	
	public Object visit(TermQuery query) {
		return null;
	}
	
	public Object visit(ValueSourceQuery query) {
		return null;
	}
	
}
