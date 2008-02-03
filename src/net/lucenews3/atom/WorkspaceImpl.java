package net.lucenews3.atom;

public class WorkspaceImpl extends CommonImpl implements Workspace {

	private String title;
	private CollectionList collections;

	public WorkspaceImpl() {
		this(new CollectionListImpl());
	}
	
	public WorkspaceImpl(CollectionList collections) {
		this.collections = collections;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public CollectionList getCollections() {
		return collections;
	}

	public void setCollections(CollectionList collections) {
		this.collections = collections;
	}
	
}
