package net.lucenews.model.exception;

public class DocumentAlreadyExistsException extends DocumentsAlreadyExistException {
    
    public DocumentAlreadyExistsException (String identifier) {
        super( identifier );
    }
    
}
