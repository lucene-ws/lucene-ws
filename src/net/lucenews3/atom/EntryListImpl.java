package net.lucenews3.atom;

import java.util.ArrayList;

public class EntryListImpl extends ArrayList<Entry> implements EntryList {

	private static final long serialVersionUID = 2574998696224569543L;

	public EntryListImpl() {
		super();
	}
	
	public EntryListImpl(java.util.Collection<? extends Entry> collection) {
		super(collection);
	}
	
	public EntryListImpl(int initialCapacity) {
		super(initialCapacity);
	}
	
}
