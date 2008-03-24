package net.lucenews3.model;

import java.util.List;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.SpellChecker;

public class SpellCheckIndexBuilder extends AbstractIndexBuilder {

	private SpellChecker spellChecker;
	private Dictionary dictionary;
	private List<Dictionary> dictionaries;
	
	@Override
	public int buildIndex(IndexWriter writer) throws Exception {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int buildIndex() throws Exception {
		
		if (dictionary != null) {
			spellChecker.indexDictionary(dictionary);
		} else if (dictionaries != null) {
			for (Dictionary dictionary : dictionaries) {
				spellChecker.indexDictionary(dictionary);
			}
		} else {
			throw new Exception("No dictionaries to be added to spell check index");
		}
		
		return -1;
	}

	public SpellChecker getSpellChecker() {
		return spellChecker;
	}

	public void setSpellChecker(SpellChecker spellChecker) {
		this.spellChecker = spellChecker;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public List<Dictionary> getDictionaries() {
		return dictionaries;
	}

	public void setDictionaries(List<Dictionary> dictionaries) {
		this.dictionaries = dictionaries;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (dictionary != null && dictionaries != null) {
			throw new Exception("Both \"dictionary\" and \"dictionaries\" properties set");
		} else {
			super.afterPropertiesSet();
		}
	}
	
}
