package net.lucenews3;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.Hits;

public class DefaultResult implements Result {

	private Hits hits;
	private int n;

	public DefaultResult(Hits hits, int n) {
		this.hits = hits;
		this.n = n;
	}

	@Override
	public Document getDocument() {
		try {
			return hits.doc(n);
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

	@Override
	public double getRelevance() {
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

}
