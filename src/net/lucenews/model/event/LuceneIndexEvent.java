package net.lucenews.model.event;

import net.lucenews.model.*;

public class LuceneIndexEvent {
    
    private LuceneIndex index;
    
    public LuceneIndexEvent (LuceneIndex index) {
        this.index = index;
    }
    
    public LuceneIndex getIndex () {
        return index;
    }
}
