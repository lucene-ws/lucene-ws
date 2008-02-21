package net.lucenews3.http;

import java.util.Collection;

public interface CompositeIndex extends Index {

	public Collection<Index> getIndexes();
	
}
