package net.lucenews.model;

import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.search.*;

public class TokenBooleanQuery extends BooleanQuery {
    
    private org.apache.lucene.queryParser.Token token;
    
    
    public TokenBooleanQuery () {
        super();
    }
    
    public TokenBooleanQuery (boolean disableCoord) {
        super( disableCoord );
    }
    
    public TokenBooleanQuery (org.apache.lucene.queryParser.Token token) {
        super();
        this.token = token;
    }
    
    public TokenBooleanQuery (boolean disableCoord, org.apache.lucene.queryParser.Token token) {
        super( disableCoord );
        this.token = token;
    }
    
    public org.apache.lucene.queryParser.Token getToken () {
        return token;
    }
    
}
