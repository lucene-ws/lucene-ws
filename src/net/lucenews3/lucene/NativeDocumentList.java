package net.lucenews3.lucene;

import java.io.IOException;
import java.util.AbstractList;

import net.lucenews3.ExceptionTranslator;

import org.apache.lucene.index.CorruptIndexException;

public class NativeDocumentList extends AbstractList<org.apache.lucene.document.Document> implements DocumentList {

	private org.apache.lucene.index.IndexReader indexReader;
	private org.apache.lucene.index.IndexWriter indexWriter;
	private ExceptionTranslator exceptionTranslator;
	
	@Override
	public boolean add(org.apache.lucene.document.Document document) {
		try {
			indexWriter.addDocument(document);
		} catch (CorruptIndexException e) {
			throw exceptionTranslator.translate(e);
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
		}
		return false;
	}
	
	public org.apache.lucene.document.Document get(int index) {
		try {
			return indexReader.document(index);
		} catch (CorruptIndexException e) {
			throw exceptionTranslator.translate(e);
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
		}
	}
	
	public int size() {
		return indexReader.maxDoc();
	}
	
}
