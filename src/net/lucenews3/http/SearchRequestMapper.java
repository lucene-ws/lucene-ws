package net.lucenews3.http;

public class SearchRequestMapper {

	public SearchRequest mapSearchRequest(HttpRequest http) {
		SearchRequest search = null;
		mapSearchRequest(search, http);
		return search;
	}
	
	/**
	 * Transforms an HTTP request into a search request.
	 * @param search
	 * @param http
	 */
	public void mapSearchRequest(SearchRequest search, HttpRequest http) {
		
	}
	
}
