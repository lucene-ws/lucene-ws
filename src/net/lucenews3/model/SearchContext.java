package net.lucenews3.model;

import net.lucenews3.http.Url;

public interface SearchContext {

	public IndexIdentity getIndexIdentity();
	
	public void setIndexIdentity(IndexIdentity indexIdentity);
	
	public Index getIndex();
	
	public void setIndex(Index index);
	
	public SearchRequest getSearchRequest();
	
	public void setSearchRequest(SearchRequest searchRequest);
	
	public ResultList getResults();
	
	public void setResults(ResultList results);
	
	public IndexRange getIndexRange();
	
	public void setIndexRange(IndexRange indexRange);
	
	public Url getUrl();
	
	public void setUrl(Url url);
	
	public Url getBaseUrl();
	
	public void setBaseUrl(Url url);
	
}
