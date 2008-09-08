package net.lucenews3;

import java.util.AbstractList;

import org.apache.lucene.search.Hits;

public class DefaultResults extends AbstractList<Result> implements Results {

	private Hits hits;
	private Index index;
	private String searchTerms;

	public DefaultResults(Hits hits, Index index, String searchTerms) {
		this.hits = hits;
		this.index = index;
		this.searchTerms = searchTerms;
	}

	@Override
	public Result get(int index) {
		return new DefaultResult(hits, index, this.index);
	}

	@Override
	public int size() {
		return hits.length();
	}

	@Override
	public String getSearchTerms() {
		return searchTerms;
	}

}
