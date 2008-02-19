package net.lucenews3.lucene;

import java.io.IOException;

import net.lucenews3.ExceptionTranslator;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;

public class NativeDocumentProviderImpl implements Provider<org.apache.lucene.document.Document> {

	private org.apache.lucene.index.IndexReader indexReader;
	private int docNum;
	private ExceptionTranslator exceptionTranslator;
	
	public NativeDocumentProviderImpl(org.apache.lucene.index.IndexReader indexReader, int docNum) {
		this.indexReader = indexReader;
		this.docNum = docNum;
	}
	
	@Override
	public Document provide() {
		try {
			return indexReader.document(docNum);
		} catch (CorruptIndexException e) {
			throw exceptionTranslator.translate(e);
		} catch (IOException e) {
			throw exceptionTranslator.translate(e);
		}
	}

}
