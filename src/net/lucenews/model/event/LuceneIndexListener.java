package net.lucenews.model.event;

public interface LuceneIndexListener {

	public void indexCreated(LuceneIndexEvent e);

	public void indexDeleted(LuceneIndexEvent e);

	public void indexModified(LuceneIndexEvent e);

}
