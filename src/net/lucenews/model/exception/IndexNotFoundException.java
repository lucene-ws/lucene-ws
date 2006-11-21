package net.lucenews.model.exception;

public class IndexNotFoundException extends IndicesNotFoundException {
    private String indexName;
    
    public IndexNotFoundException (String indexName) {
        super( new String[]{ indexName } );
        this.indexName = indexName;
    }
    
    public int size () {
        return 1;
    }
    
    public String[] getIndexNames () {
        return new String[]{ getIndexName() };
    }
    
    public String getIndexName () {
        return indexName;
    }
    
    public String getMessage () {
        return "Index '" + getIndexName() + "' was not found";
    }
    
}
