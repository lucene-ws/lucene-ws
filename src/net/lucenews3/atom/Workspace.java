package net.lucenews3.atom;

public interface Workspace {

	public String getTitle();
	
	public void setTitle(String title);
	
	public CollectionList getCollections();
	
	public void setCollections(CollectionList collections);
	
}
