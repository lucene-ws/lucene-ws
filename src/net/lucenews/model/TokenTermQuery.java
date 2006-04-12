package org.apache.lucene.search;

import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.search.*;



public class TokenTermQuery extends TermQuery {
    
    private org.apache.lucene.queryParser.Token token;
    
    public TokenTermQuery (Term term) {
        super( term );
    }
    
    public TokenTermQuery (Term term, org.apache.lucene.queryParser.Token token) {
        super( term );
        this.token = token;
    }
    
    public org.apache.lucene.queryParser.Token getToken () {
        return token;
    }
    
}
