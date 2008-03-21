package net.lucenews3.model;

import java.io.IOException;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.spell.SpellChecker;

public class QueryExpander {

	private MethodInvoker methodInvoker;
	private MethodResolver methodResolver;
	private SpellChecker spellChecker;
	
	public Query expand(Query query) {
		return (Query) methodInvoker.invoke(this, "expandQuery", query);
	}
	
	public Query expandQuery(Query query) {
		return query;
	}
	
	public Query expandQuery(BooleanQuery query) {
		BooleanClause[] clauses = query.getClauses();
		return query;
	}
	
	public Query expandQuery(TermQuery query) throws IOException {
		Term term = query.getTerm();
		
		String[] suggestedWords = spellChecker.suggestSimilar(term.text(), 5);
		
		return query;
	}
	
}
