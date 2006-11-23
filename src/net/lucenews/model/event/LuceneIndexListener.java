package net.lucenews.model.event;

import net.lucenews.model.*;

public interface LuceneIndexListener {
    
    public void indexCreated (LuceneIndexEvent e);
    public void indexDeleted (LuceneIndexEvent e);
    public void indexModified (LuceneIndexEvent e);
    
}
