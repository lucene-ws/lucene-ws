package net.lucenews.http;

import java.util.Deque;

import org.apache.lucene.search.Searcher;

public class SearcherPool {

	private Deque<Searcher> searchers;
	
	/**
	 * Retrieves a searcher from the pool.
	 * @return
	 */
	public Searcher getSearcher() {
		Searcher result;
		
		synchronized (searchers) {
			result = searchers.removeFirst();
		}
		
		return result;
	}
	
	/**
	 * Adds a searcher to the pool.
	 * @param searcher
	 */
	public void addSearcher(Searcher searcher) {
		synchronized (searcher) {
			searchers.addLast(searcher);
		}
	}
	
}
