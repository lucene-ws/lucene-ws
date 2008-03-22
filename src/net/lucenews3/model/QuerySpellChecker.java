package net.lucenews3.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.lucenews3.ExceptionTranslator;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.spell.SpellChecker;

/**
 * Attempts to determine suggested corrections for queries.
 *
 */
public class QuerySpellChecker {

	private SpellChecker spellChecker;
	private ExceptionTranslator exceptionTranslator;
	private Cloner cloner;
	
	public QuerySpellChecker() {
		this.cloner = new ClonerImpl();
	}
	
	public SpellChecker getSpellChecker() {
		return spellChecker;
	}

	public void setSpellChecker(SpellChecker spellChecker) {
		this.spellChecker = spellChecker;
	}
	
	public ExceptionTranslator getExceptionTranslator() {
		return exceptionTranslator;
	}

	public void setExceptionTranslator(ExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}

	public Cloner getCloner() {
		return cloner;
	}

	public void setCloner(Cloner cloner) {
		this.cloner = cloner;
	}

	public List<Query> suggest(Query query) {
		List<Query> results = new ArrayList<Query>();
		results.addAll(suggestSimilar(query));
		return results;
	}
	
	public List<Query> suggestSimilar(Query query) {
		List<Query> results = new ArrayList<Query>();
		suggestSimilar(query, results);
		return results;
	}
	
	public void suggestSimilar(Query query, Collection<Query> suggestedQueries) {
		if (query instanceof BooleanQuery) {
			suggestSimilar((BooleanQuery) query, suggestedQueries);
		} else if (query instanceof TermQuery) {
			suggestSimilar((TermQuery) query, suggestedQueries);
		}
	}
	
	public List<Query> suggestSimilar(BooleanQuery query) {
		final List<Query> results = new ArrayList<Query>();
		suggestSimilar(query, results);
		return results;
	}
	
	/**
	 * Suggests all permutations of the given boolean query. This is 
	 * accomplished by determining the suggested queries of each clause's
	 * sub-query.
	 * @param query
	 * @param suggestedQueries
	 */
	@SuppressWarnings("unchecked")
	public void suggestSimilar(BooleanQuery query, Collection<Query> suggestedQueries) {
		final BooleanClause[] clauses = query.getClauses();
		final List<Query>[] suggestedClauseQueries = new List[clauses.length];
		
		for (int i = 0; i < clauses.length; i++) {
			suggestedClauseQueries[i] = suggestSimilar(clauses[i].getQuery());
		}
		
		Collection<List<Query>> permutations = new PermutationCollection<Query>(suggestedClauseQueries);
		for (List<Query> permutation : permutations) {
			BooleanQuery suggestedQuery = new BooleanQuery();
			for (Query suggestedSubquery : permutation) {
				suggestedQuery.add(suggestedSubquery, BooleanClause.Occur.SHOULD);
			}
			suggestedQueries.add(suggestedQuery);
		}
	}
	
	public List<Query> suggestSimilar(TermQuery query) {
		final List<Query> results = new ArrayList<Query>();
		suggestSimilar(query, results);
		return results;
	}
	
	public void suggestSimilar(TermQuery query, Collection<Query> suggestedQueries) {
		Term term = query.getTerm();
		String word = term.text();
		
		String[] similarWords;
		//if (true) throw new RuntimeException("Words similar to \"" + word + "\"");
		try {
			similarWords = spellChecker.suggestSimilar(word, 5);
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
		}
		
		for (String similarWord : similarWords) {
			TermQuery similarQuery = new TermQuery(new Term(term.field(), similarWord));
			suggestedQueries.add(similarQuery);
		}
	}
	
}
