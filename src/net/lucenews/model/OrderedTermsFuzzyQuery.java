package org.apache.lucene.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.*;

import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.*;

/**
 * A sibling of the FuzzyQuery class with the ability to give a score sorted
 * list of fuzzy term queries.
 */

public class OrderedTermsFuzzyQuery extends MultiTermQuery
{
    private Term  term;
    private Token token;
    
    protected OrderedTermsFuzzyQuery (TermQuery termQuery) {
        super(termQuery.getTerm());
        term = termQuery.getTerm();
        if( termQuery instanceof TokenTermQuery )
            token = ( (TokenTermQuery) termQuery ).getToken();
    }
    
    protected OrderedTermsFuzzyQuery (TokenTermQuery tokenTermQuery) {
        super(tokenTermQuery.getTerm());
        term  = tokenTermQuery.getTerm();
        token = tokenTermQuery.getToken();
    }
    
    protected OrderedTermsFuzzyQuery (Term term) {
        super(term);
        this.term = term;
    }
    
    protected FilteredTermEnum getEnum(IndexReader reader)
        throws IOException
    {
        return new FuzzyTermEnum(reader, getTerm());
    }
    
    public String toString(String field)
    {
        return super.toString(field) + '~';
    }
    
    /**
     * Computes an ordered list of alternative terms, ordered by edit distance.
     * @param  reader       the IndexReader
     * @param  useFreqOrder if <tt>true</tt>, return list will only contain
     *                      terms which will give more hits
     * @return List<TermQuery>
     */
    
    public List<TermQuery> bestOrderRewrite(IndexReader reader, boolean useFreqOrder)
        throws IOException
    {
        int originalDocFreq = useFreqOrder ? reader.docFreq(term) : 0;
        ArrayList<TermQuery> terms = new ArrayList<TermQuery>();
        FilteredTermEnum enumerator = getEnum( reader );
        try {
            do {
                Term t = enumerator.term();
                
                // Condition 1: While we're still on the same term text
                boolean condition1 = t != null && !t.text().equals( term.text() );
                
                // Condition 2:
                boolean condition2 = ( !useFreqOrder || ( reader.docFreq( t ) > originalDocFreq ) );
                
                if( condition1 && condition2 ) {
                    // found a match
                    TermQuery tq = null;
                    if( token == null )
                        tq = new TermQuery( t );
                    else
                        tq = new TokenTermQuery( t, token );
                    
                    // set the boost
                    // FilteredTermEnum.difference() was protected in revisions < 150572,
                    // forcing this class to be in same package if not used with
                    // Lucene revision >= 150572
                    tq.setBoost( getBoost() * enumerator.difference() );
                    
                    // add to query
                    terms.add( tq );
                }
            } while( enumerator.next() );
        } finally {
            enumerator.close();
        }
        
        //Collections.sort( terms, (Comparator<TermQuery>) new FuzzyTermScoreComparator( term ) );
        
        return terms;
    }
}
