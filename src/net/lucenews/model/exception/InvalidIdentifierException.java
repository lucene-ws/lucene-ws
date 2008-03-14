package net.lucenews.model.exception;

import javax.servlet.http.*;

public class InvalidIdentifierException extends LuceneException {
    
    private String identifier;
    
    public InvalidIdentifierException (String identifier) {
        this.identifier = identifier;
    }
    @Override
    public String getMessage () {
        return "Identifier '" + identifier + "' is invalid.";
    }
    @Override
    public int getStatus () {
        return HttpServletResponse.SC_BAD_REQUEST;
    }
    
}
