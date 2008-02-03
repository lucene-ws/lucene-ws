package net.lucenews3.atom;

import java.util.ArrayList;

public class CategoryListImpl extends ArrayList<Category> implements CategoryList {

	private static final long serialVersionUID = -5610847061637456797L;
	
	public CategoryListImpl() {
		super();
	}
	
	public CategoryListImpl(java.util.Collection<? extends Category> collection) {
		super(collection);
	}
	
	public CategoryListImpl(int initialCapacity) {
		super(initialCapacity);
	}

}
