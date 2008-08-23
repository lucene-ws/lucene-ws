package net.lucenews3;

import java.util.AbstractList;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.search.Hits;

public class DefaultResults extends AbstractList<Result> implements Results {

	private Hits hits;

	public DefaultResults(Hits hits) {
		this.hits = hits;
	}

	@Override
	public Result get(int index) {
		//return null;
		final Document document = new Document();
		document.add(new Field("id", "5", Store.YES, Index.TOKENIZED));
		return new Result() {
			@Override
			public Document getDocument() {
				return document;
			}

			@Override
			public double getRelevance() {
				// TODO Auto-generated method stub
				return 0.8;
			}

			@Override
			public String getTitle() {
				// TODO Auto-generated method stub
				return "Cool";
			}
			
		};
	}

	@Override
	public int size() {
		//return hits.length();
		return 1;
	}

}
