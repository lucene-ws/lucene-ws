package net.lucenews3.atom;

import java.util.ArrayList;

public class CollectionListImpl extends ArrayList<Collection> implements CollectionList {

	private static final long serialVersionUID = -9114964816735274334L;

	public CollectionListImpl() {
		super();
	}
	
	public CollectionListImpl(java.util.Collection<? extends Collection> collection) {
		super(collection);
	}
	
	public CollectionListImpl(int initialCapacity) {
		super(initialCapacity);
	}
	
}
