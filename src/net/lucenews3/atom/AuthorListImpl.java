package net.lucenews3.atom;

import java.util.ArrayList;

public class AuthorListImpl extends ArrayList<Author> implements AuthorList {

	private static final long serialVersionUID = -1804386212106359185L;
	
	public AuthorListImpl() {
		super();
	}
	
	public AuthorListImpl(java.util.Collection<? extends Author> collection) {
		super(collection);
	}
	
	public AuthorListImpl(int initialCapacity) {
		super(initialCapacity);
	}

}
