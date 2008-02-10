package net.lucenews3.opensearch;

import java.util.ArrayList;
import java.util.Collection;

public class LinkListImpl extends ArrayList<Link> implements LinkList {

	private static final long serialVersionUID = -3982664390165729259L;

	public LinkListImpl() {
		super();
	}

	public LinkListImpl(Collection<? extends Link> collection) {
		super(collection);
	}

	public LinkListImpl(int initialCapacity) {
		super(initialCapacity);
	}
	
}
