package net.lucenews3.opensearch;

import java.util.ArrayList;
import java.util.Collection;

public class QueryListImpl extends ArrayList<Query> implements QueryList {

	private static final long serialVersionUID = -6602379885358741859L;

	public QueryListImpl() {
		super();
	}

	public QueryListImpl(Collection<? extends Query> collection) {
		super(collection);
	}

	public QueryListImpl(int initialCapacity) {
		super(initialCapacity);
	}

}
