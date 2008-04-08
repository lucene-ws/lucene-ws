package net.lucenews.model.exception;

import javax.servlet.http.*;

public class LuceneParseException extends LuceneException {
    
    public LuceneParseException (String message) {
        super( message );
    }
    @Override
    public int getStatus () {
        return HttpServletResponse.SC_BAD_REQUEST;
    }
    
}
