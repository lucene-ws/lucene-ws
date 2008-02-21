package net.lucenews3.controller;

import net.lucenews3.model.Index;

public interface IndexAccess {

	public Index get(String indexName);
	
	public void remove(String indexName);
	
}
