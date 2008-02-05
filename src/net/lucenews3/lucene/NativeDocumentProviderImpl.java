package net.lucenews3.lucene;

import java.io.IOException;

import net.lucenews.http.ExceptionWrapper;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;

public class NativeDocumentProviderImpl implements Provider<org.apache.lucene.document.Document> {

	private org.apache.lucene.index.IndexReader indexReader;
	private int docNum;
	private ExceptionWrapper exceptionWrapper;
	
	public NativeDocumentProviderImpl(org.apache.lucene.index.IndexReader indexReader, int docNum) {
		this.indexReader = indexReader;
		this.docNum = docNum;
	}
	
	@Override
	public Document provide() {
		try {
			return indexReader.document(docNum);
		} catch (CorruptIndexException e) {
			throw exceptionWrapper.wrap(e);
		} catch (IOException e) {
			throw exceptionWrapper.wrap(e);
		}
	}

}
