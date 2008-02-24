package net.lucenews.model;

import java.io.IOException;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.wordnet.SynExpand;

/**
 * Expands queries via the synonyms contribution package (org.apache.lucene.wordnet).
 *
 */
public class SynonymQueryExpander implements QueryExpander {

	private ExceptionTranslator exceptionTranslator;
	
	public SynonymQueryExpander() {
		this(new ExceptionTranslatorImpl());
	}
	
	public SynonymQueryExpander(ExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}
	
	public ExceptionTranslator getExceptionTranslator() {
		return exceptionTranslator;
	}

	public void setExceptionTranslator(ExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}

	@Override
	public Query expand(Query query) {
		Query result;
		
		if (query instanceof TermQuery) {
			result = expand((TermQuery) query);
		} else if (query instanceof BooleanQuery) {
			result = expand((BooleanQuery) query);
		} else {
			result = query;
		}
		
		return result;
	}
	
	public Query expand(BooleanQuery query) {
		Query result = query;
		
		for (BooleanClause clause : query.getClauses()) {
			clause.setQuery(expand(clause.getQuery()));
		}
		
		return result;
	}
	
	public Query expand(TermQuery query) {
		final Term term = query.getTerm();
		final Searcher searcher = null;
		final Analyzer analyzer = null;
		final String field = null;
		final float boost = 0.0f;
		
		Query expandedQuery;
		try {
			expandedQuery = SynExpand.expand(term.field(), searcher, analyzer, field, boost);
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
		}
		
		return expandedQuery;
	}

}
