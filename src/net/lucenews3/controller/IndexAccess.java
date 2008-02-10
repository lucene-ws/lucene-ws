package net.lucenews3.controller;

import net.lucenews3.lucene.support.Index;

public interface IndexAccess {

	public Index get(String indexName);
	
	public void remove(String indexName);
	
}
