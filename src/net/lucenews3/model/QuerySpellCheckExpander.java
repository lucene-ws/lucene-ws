package net.lucenews3.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.spell.SpellChecker;

public class QuerySpellCheckExpander {

	private SpellChecker spellChecker;
	private MethodInvoker methodInvoker;
	private int suggestionCount;
	
	public QuerySpellCheckExpander() {
		this.methodInvoker = new MethodInvokerImpl();
		this.suggestionCount = 5;
	}
	
	@SuppressWarnings("unchecked")
	public List<Query> getSuggestions(Query query) {
		return (List<Query>) methodInvoker.invoke(this, "getQuerySuggestions", query);
	}
	
	public List<Query> getQuerySuggestions(Query query) {
		return new ArrayList<Query>();
	}
	
	public List<Query> getQuerySuggestions(BooleanQuery query) {
		return new ArrayList<Query>();
	}
	
	public List<Query> getQuerySuggestions(TermQuery query) throws IOException {
		final List<Query> results = new ArrayList<Query>();
		
		final Term term = query.getTerm();
		final String word = term.text();
		final String[] suggestions = spellChecker.suggestSimilar(word, suggestionCount);
		
		for (String suggestion : suggestions) {
			TermQuery suggestionQuery = new TermQuery(new Term(term.field(), suggestion));
			results.add(suggestionQuery);
		}
		
		return results;
	}

	public SpellChecker getSpellChecker() {
		return spellChecker;
	}

	public void setSpellChecker(SpellChecker spellChecker) {
		this.spellChecker = spellChecker;
	}

	public MethodInvoker getMethodInvoker() {
		return methodInvoker;
	}

	public void setMethodInvoker(MethodInvoker methodInvoker) {
		this.methodInvoker = methodInvoker;
	}

	public int getSuggestionCount() {
		return suggestionCount;
	}

	public void setSuggestionCount(int suggestionCount) {
		this.suggestionCount = suggestionCount;
	}
	
}
