package net.lucenews3.model;

import java.io.IOException;

import net.lucenews3.ExceptionTranslator;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.spell.SpellChecker;

public class QueryExpansionVisitor extends DefaultQueryVisitor {

	private SpellChecker spellChecker;
	private ExceptionTranslator exceptionTranslator;
	
	@Override
	public Query visit(TermQuery query) {
		final Term term = query.getTerm();
		String word = term.text();
		String[] suggestedWords;
		try {
			suggestedWords = spellChecker.suggestSimilar(word, 5);
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
		}
		return null;
	}

	public SpellChecker getSpellChecker() {
		return spellChecker;
	}

	public void setSpellChecker(SpellChecker spellChecker) {
		this.spellChecker = spellChecker;
	}
	
}
