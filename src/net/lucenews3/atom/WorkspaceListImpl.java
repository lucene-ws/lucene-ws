package net.lucenews3.atom;

import java.util.ArrayList;

public class WorkspaceListImpl extends ArrayList<Workspace> implements WorkspaceList {

	private static final long serialVersionUID = 9116098024688553869L;

	public WorkspaceListImpl() {
		super();
	}
	
	public WorkspaceListImpl(java.util.Collection<? extends Workspace> collection) {
		super(collection);
	}
	
	public WorkspaceListImpl(int initialCapacity) {
		super(initialCapacity);
	}
	
}
