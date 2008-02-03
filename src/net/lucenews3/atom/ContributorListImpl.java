package net.lucenews3.atom;

import java.util.ArrayList;

public class ContributorListImpl extends ArrayList<Contributor> implements ContributorList {

	private static final long serialVersionUID = 1978621502470648767L;

	public ContributorListImpl() {
		super();
	}
	
	public ContributorListImpl(java.util.Collection<? extends Contributor> collection) {
		super(collection);
	}
	
	public ContributorListImpl(int initialCapacity) {
		super(initialCapacity);
	}
	
}
