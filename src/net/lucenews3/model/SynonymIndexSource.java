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

/**
 * Provides synonyms from an specially formatted index. Each document in the index
 * pertains to a word which has zero or more synonyms. For example, consider the
 * word "house". The word "house" has the following 10 synonyms (according to WordNet):
 * 
 * <ul>
 * 	<li>domiciliate</li>
 * 	<li>family</li>
 * 	<li>firm</li>
 * 	<li>home</li>
 * 	<li>household</li>
 * 	<li>mansion</li>
 * 	<li>menage</li>
 * 	<li>sign</li>
 * 	<li>theater</li>
 * 	<li>theatre</li>
 * </ul>
 * 
 * In the synonym index, the document for "house" would have one field named "word" 
 * with the value "house". It would have 10 fields with the name "synonym" (one per
 * synonym).
 * 
 * It is recommended that neither "word" fields nor "synonym" fields be tokenized.
 * Field names other than "word" and "synonym" may be used, but the index source
 * must be configured appropriately using {@link SynonymIndexSource#setWordFieldName(String)}
 * and {@link SynonymIndexSource#setSynonymFieldName(String)} appropriately.
 * 
 * A recommended source of synonyms is the WordNet synonym data set. A Lucene contribution
 * is capable of building such indexes.
 * 
 * @see WordnetSynonymIndexBuilder
 *
 */
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
	
	/**
	 * Collects synonyms associated with the given word from the
	 * current index. The synonym document is found by searching
	 * for documents containing a word field having the given 
	 * word value (for example, the field word:house). Although
	 * "word" is the default field name, it can be configured
	 * using {@link #setWordFieldName(String)}.
	 * 
	 * @param word
	 * @return
	 */
	@Override
	public Collection<String> getSynonyms(String word) {
		Collection<String> results;
		
		Term term = new Term(wordFieldName, word);
		TermQuery query = new TermQuery(term);
		Hits hits;
		
		try {
			hits = searcher.search(query);
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
		}
		
		results = getSynonyms(hits);
		
		return results;
	}
	
	/**
	 * Collects all synonyms contained in the documents of the given
	 * search hits.
	 * 
	 * @param hits
	 * @return
	 */
	public List<String> getSynonyms(Hits hits) {
		return getSynonyms(hits, hits.length());
	}
	
	/**
	 * Collects all synonyms contained in the first <code>length</code>
	 * documents of the given set of search hits.
	 * 
	 * @param hits
	 * @param length
	 * @return
	 */
	public List<String> getSynonyms(Hits hits, int length) {
		List<String> synonyms = new ArrayList<String>(length);
		
		for (int i = 0; i < length; i++) {
			final Document document;
			
			try {
				document = hits.doc(i);
			} catch (IOException e) {
				throw exceptionTranslator.translate(e);
			}
			
			synonyms.addAll(getSynonyms(document));
		}
		
		return synonyms;
	}
	
	/**
	 * Collects all synonyms contained in the given document. Synonyms
	 * are stored as the text values of those fields whose names are 
	 * equal to that returned by {@link #getSynonymFieldName()}.
	 * 
	 * @param document
	 * @return
	 * @see #setSynonymFieldName(String)
	 */
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
