package net.lucenews3.lucene.support;

import java.io.IOException;

import net.lucenews.http.ExceptionWrapper;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.Hits;

public class ResultImpl implements Result {

	private ExceptionWrapper exceptionWrapper;
	private Hits hits;
	private int number;
	
	public ResultImpl(Hits hits, int number) {
		this.hits = hits;
		this.number = number;
	}
	
	public Document getDocument() {
		try {
			return hits.doc(number);
		} catch (CorruptIndexException e) {
			throw exceptionWrapper.wrap(e);
		} catch (IOException e) {
			throw exceptionWrapper.wrap(e);
		}
	}

	public int getDocumentId() {
		try {
			return hits.id(number);
		} catch (IOException e) {
			throw exceptionWrapper.wrap(e);
		}
	}

	public int getNumber() {
		return number;
	}

	public float getScore() {
		try {
			return hits.score(number);
		} catch (IOException e) {
			throw exceptionWrapper.wrap(e);
		}
	}
	
}
