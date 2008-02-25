package net.lucenews3.model;

import java.io.File;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.wordnet.Syns2Index;

public class WordnetSynonymIndexBuilder extends AbstractIndexBuilder {

	private ExceptionTranslator exceptionTranslator;
	
	public static void main(String... arguments) throws Throwable {
		Syns2Index.main(new String[]{ "data/wordnet-prolog/wn_s.pl", "indexes/synonyms" });
	}
	
	public WordnetSynonymIndexBuilder() {
		this(new ExceptionTranslatorImpl());
	}
	
	public WordnetSynonymIndexBuilder(ExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}
	
	@Override
	public int buildIndex(IndexWriter writer) throws Exception {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int buildIndex() throws Exception {
		String[] arguments = new String[2];
		arguments[0] = new File(new File(new File("data"), "wordnet-prolog"), "wn_s.pl").toString();
		arguments[1] = getDirectoryPath().toString();
		
		try {
			Syns2Index.main(arguments);
		} catch (Throwable e) {
			throw exceptionTranslator.translate(e);
		}
		
		return -1;
	}

}
