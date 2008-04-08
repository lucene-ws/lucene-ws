package net.lucenews.model.exception;

import javax.servlet.http.*;

public class InsufficientDataException extends LuceneException {
    
    public InsufficientDataException (String message) {
        super( message );
    }
    
    @Override
    public int getStatus () {
        return HttpServletResponse.SC_BAD_REQUEST;
    }
    
}
