package net.lucenews3.model;

import java.io.IOException;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.Hits;

public class ResultImpl implements Result {

	private ExceptionTranslator exceptionTranslator;
	private Hits hits;
	private int number;
	
	public ResultImpl(Hits hits, int number) {
		this(hits, number, new ExceptionTranslatorImpl());
	}
	
	public ResultImpl(Hits hits, int number, ExceptionTranslator exceptionTranslator) {
		this.hits = hits;
		this.number = number;
		this.exceptionTranslator = exceptionTranslator;
	}
	
	public Document getDocument() {
		try {
			return new NativeDocumentDocument(hits.doc(number));
		} catch (CorruptIndexException e) {
			throw exceptionTranslator.translate(e);
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
		}
	}

	public int getDocumentId() {
		try {
			return hits.id(number);
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
		}
	}

	public int getNumber() {
		return number;
	}

	public float getScore() {
		try {
			return hits.score(number);
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
		}
	}
	
}
