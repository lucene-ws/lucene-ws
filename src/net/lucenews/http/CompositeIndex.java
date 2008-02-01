package net.lucenews.http;

import java.util.Collection;

public interface CompositeIndex extends Index {

	public Collection<Index> getIndexes();
	
}
