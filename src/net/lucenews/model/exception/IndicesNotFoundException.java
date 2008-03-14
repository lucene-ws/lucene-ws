package net.lucenews.model.exception;

import java.util.List;
import javax.servlet.http.*;


public class IndicesNotFoundException extends LuceneException {
    private String[] indexNames;
    
    public IndicesNotFoundException (String[] indexNames) {
        this.indexNames = indexNames;
    }
    
    public IndicesNotFoundException (List<String> indexNames) {
        this( indexNames.toArray( new String[ 0 ] ) );
    }
    
    public int size () {
        return indexNames.length;
    }
    
    public String[] getIndexNames () {
        return indexNames;
    }
    @Override
    public String getMessage () {
        String[] names = getIndexNames();
        
        StringBuffer buffer = new StringBuffer();
        
        switch (names.length) {
            case 1:
                buffer.append( "Index '" + names[0] + " was not found." );
                break;
                
            default:
                buffer.append( "Indices" );
                
                for (int i = 0; i < names.length; i++) {
                    if (i == 0) {
                        buffer.append( " " );
                    }
                    if (i > 0) {
                        if (i == names.length - 1) {
                            buffer.append( " and " );
                        }
                        else {
                            buffer.append( ", " );
                        }
                    }
                    
                    buffer.append( "'" + names[i] + "'" );
                }
                
                buffer.append( " were not found." );
                break;
        }
        
        return String.valueOf( buffer );
    }
    @Override
    public int getStatus () {
        return HttpServletResponse.SC_NOT_FOUND;
    }
    
}
