package net.lucenews3.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.wordnet.Syns2Index;

public class SynonymIndexSource implements SynonymSource {

	private Searcher searcher;
	private String wordFieldName;
	private String synonymFieldName;
	private ExceptionTranslator exceptionTranslator;
	
	public SynonymIndexSource() {
		this.wordFieldName = Syns2Index.F_WORD;
		this.synonymFieldName = Syns2Index.F_SYN;
		this.exceptionTranslator = new ExceptionTranslatorImpl();
	}
	
	@Override
	public Collection<String> getSynonyms(String word) {
		Collection<String> results;
		
		final Term term = new Term(wordFieldName, word);
		final TermQuery query = new TermQuery(term);
		final Hits hits;
		
		try {
			hits = searcher.search(query);
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
		}
		
		results = getSynonyms(hits);
		
		return results;
	}
	
	public List<String> getSynonyms(Hits hits) {
		return getSynonyms(hits, hits.length());
	}
	
	public List<String> getSynonyms(Hits hits, int length) {
		final List<String> results = new ArrayList<String>(length);
		
		for (int i = 0; i < length; i++) {
			final Document document;
			
			try {
				document = hits.doc(i);
			} catch (IOException e) {
				throw exceptionTranslator.translate(e);
			}
			
			results.addAll(getSynonyms(document));
		}
		
		return results;
	}
	
	public List<String> getSynonyms(Document document) {
		return Arrays.asList(document.getValues(synonymFieldName));
	}

	public Searcher getSearcher() {
		return searcher;
	}

	public void setSearcher(Searcher searcher) {
		this.searcher = searcher;
	}

	public String getWordFieldName() {
		return wordFieldName;
	}

	public void setWordFieldName(String wordFieldName) {
		this.wordFieldName = wordFieldName;
	}

	public String getSynonymFieldName() {
		return synonymFieldName;
	}

	public void setSynonymFieldName(String synonymFieldName) {
		this.synonymFieldName = synonymFieldName;
	}

	public ExceptionTranslator getExceptionTranslator() {
		return exceptionTranslator;
	}

	public void setExceptionTranslator(ExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}

}
