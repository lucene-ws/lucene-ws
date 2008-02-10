package net.lucenews3.opensearch;

import java.util.ArrayList;
import java.util.Collection;

public class ResultListImpl extends ArrayList<Result> implements ResultList {

	private static final long serialVersionUID = 7202017793346994172L;

	public ResultListImpl() {
		super();
	}

	public ResultListImpl(Collection<? extends Result> collection) {
		super(collection);
	}

	public ResultListImpl(int initialCapacity) {
		super(initialCapacity);
	}

}
