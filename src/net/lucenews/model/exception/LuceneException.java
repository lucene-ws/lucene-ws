package net.lucenews.model.exception;

import javax.servlet.ServletException;

public class LuceneException extends Exception {
    
    private int status;
    
    public LuceneException () {
        super();
    }
    
    public LuceneException (String message) {
        super( message );
        setStatus( -1 );
    }
    
    public LuceneException (String message, int status) {
        super( message );
        setStatus( status );
    }
    
    public boolean hasStatus () {
        return getStatus() > 0;
    }
    
    public int getStatus () {
        return status;
    }
    
    public void setStatus (int status) {
        this.status = status;
    }
    
}
