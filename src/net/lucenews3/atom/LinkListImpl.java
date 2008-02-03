package net.lucenews3.atom;

import java.util.ArrayList;

public class LinkListImpl extends ArrayList<Link> implements LinkList {

	private static final long serialVersionUID = -2083216800725093960L;

	public LinkListImpl() {
		super();
	}
	
	public LinkListImpl(java.util.Collection<? extends Link> collection) {
		super(collection);
	}
	
	public LinkListImpl(int initialCapacity) {
		super(initialCapacity);
	}
	
}
