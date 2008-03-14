package net.lucenews.model.exception;

public class IndexAlreadyExistsException extends IndicesAlreadyExistException {
    
    private String indexName;
    
    public IndexAlreadyExistsException (String indexName) {
        super( new String[]{ indexName } );
        this.indexName = indexName;
    }
    @Override
    public String[] getIndexNames () {
        return new String[]{ getIndexName() };
    }
    @Override
    public String getIndexName () {
        return this.indexName;
    }
    @Override
    public int size () {
        return 1;
    }
    @Override
    public String getMessage () {
        return "Index '" + getIndexName() + "' already exists";
    }
    
}
