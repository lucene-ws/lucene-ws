package net.lucenews.model.exception;

public class DocumentNotFoundException extends DocumentsNotFoundException {
    
    public DocumentNotFoundException (String id) {
        super( id );
    }
    
    public String getDocumentID () {
        String[] i = getDocumentIDs();
        if ( i.length > 0 ) {
            return i[ 0 ];
        }
        return null;
    }
    
}
