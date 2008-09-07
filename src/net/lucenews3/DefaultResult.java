package net.lucenews3;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.Hits;

public class DefaultResult implements Result {

	private Hits hits;
	private int n;
	private Index index;
	private Document document;
	private DocumentMetaData documentMetaData;

	public DefaultResult(Hits hits, int n, Index index) {
		this.hits = hits;
		this.n = n;
		this.index = index;
	}

	@Override
	public int getNumber() {
		return n;
	}

	@Override
	public Document getDocument() {
		if (document == null) {
			try {
				document = hits.doc(n);
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		return document;
	}

	@Override
	public double getScore() {
		try {
			return hits.score(n);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocumentMetaData getDocumentMetaData() {
		if (documentMetaData == null) {
			documentMetaData = new DefaultDocumentMetaData(index, getDocument());
		}
		
		return documentMetaData;
	}

}
