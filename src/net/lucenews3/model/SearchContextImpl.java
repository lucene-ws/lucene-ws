package net.lucenews3.model;

import net.lucenews3.http.Url;

public class SearchContextImpl implements SearchContext {

	private IndexIdentity indexIdentity;
	private Index index;
	private SearchRequest searchRequest;
	private ResultList results;
	private IndexRange indexRange;
	private Url url;
	private Url baseUrl;

	public IndexIdentity getIndexIdentity() {
		return indexIdentity;
	}

	public void setIndexIdentity(IndexIdentity indexIdentity) {
		this.indexIdentity = indexIdentity;
	}

	public Index getIndex() {
		return index;
	}

	public void setIndex(Index index) {
		this.index = index;
	}

	public SearchRequest getSearchRequest() {
		return searchRequest;
	}

	public void setSearchRequest(SearchRequest searchRequest) {
		this.searchRequest = searchRequest;
	}

	public ResultList getResults() {
		return results;
	}

	public void setResults(ResultList results) {
		this.results = results;
	}

	public IndexRange getIndexRange() {
		return indexRange;
	}

	public void setIndexRange(IndexRange indexRange) {
		this.indexRange = indexRange;
	}

	public Url getUrl() {
		return url;
	}

	public void setUrl(Url url) {
		this.url = url;
	}

	public Url getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(Url baseUrl) {
		this.baseUrl = baseUrl;
	}
	
}
