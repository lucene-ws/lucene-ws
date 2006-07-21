package net.lucenews.model;

import java.io.*;
import java.util.*;
import org.apache.log4j.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.search.spell.*;
import org.apache.lucene.store.*;

public class LuceneSpellChecker extends SpellChecker {
    
    private int maximum_suggestions;
    private IndexReader reader;
    private boolean more_popular;
    
    
    public LuceneSpellChecker (Directory gramIndex) {
        super( gramIndex );
        setMaximumSuggestions( 0 );
    }
    
    
    
    /**
     * Typo in SpellChecker class (how ironic)
     */
    
    public void setAccuracy (float min) {
        setAccuraty( min );
    }
    
    
    
    public int getMaximumSuggestions () {
        return maximum_suggestions;
    }
    
    public void setMaximumSuggestions (int maximum_suggestions) {
        this.maximum_suggestions = maximum_suggestions;
    }
    
    
    
    public IndexReader getIndexReader () {
        return reader;
    }
    
    public void setIndexReader (IndexReader reader) {
        this.reader = reader;
    }
    
    
    
    public boolean getMorePopular () {
        return more_popular;
    }
    
    public void setMorePopular (boolean more_popular) {
        this.more_popular = more_popular;
    }
    
    
    
    
    
    public List<Query> suggestSimilar (Query query)
        throws IOException
    {
        return suggestSimilar( query, getMaximumSuggestions() );
    }
    
    public List<Query> suggestSimilar (Query query, int num_sug)
        throws IOException
    {
        return suggestSimilar( query, num_sug, getIndexReader() );
    }
    
    public List<Query> suggestSimilar (Query query, int num_sug, IndexReader ir)
        throws IOException
    {
        return suggestSimilar( query, num_sug, ir, getMorePopular() );
    }
    
    
    
    
    
    public List<Query> suggestSimilar (Query query, int num_sug, IndexReader ir, boolean morePopular)
        throws IOException
    {
        Logger.getLogger(this.getClass()).trace("suggestSimilar(Query,int,IndexReader,boolean);");
        
        List<Query> queries = new LinkedList<Query>();
        
        if (query instanceof BooleanQuery) {
            return suggestSimilar( (BooleanQuery) query, num_sug, ir, morePopular );
        }
        
        if (query instanceof TermQuery) {
            return suggestSimilar( (TermQuery) query, num_sug, ir, morePopular );
        }
        
        return queries;
    }
    
    
    
    public List<Query> suggestSimilar (BooleanQuery query, int num_sug, IndexReader ir, boolean morePopular)
        throws IOException
    {
        Logger.getLogger(this.getClass()).trace("suggestSimilar(BooleanQuery,int,IndexReader,boolean);");
        
        List<Query> queries = new LinkedList<Query>();
        
        BooleanClause[] clauses = query.getClauses();
        Query[][] clauseQuerySuggestions = new Query[ clauses.length ][];
        
        for (int i = 0; i < clauses.length; i++) {
            BooleanClause clause = clauses[ i ];
            clauseQuerySuggestions[ i ] = suggestSimilar( clause.getQuery(), num_sug, ir, morePopular ).toArray( new Query[]{} );
        }
        
        
        
        int[] indices = new int[ clauses.length ];
        
        while (indices[ 0 ] <= clauseQuerySuggestions[ 0 ].length) {
            
            BooleanQuery returnQuery = new BooleanQuery();
            
            boolean addThis = true;
            for (int i = 0; i < indices.length; i++) {
                int j = indices[ i ];
                if (clauseQuerySuggestions[ i ].length > 0 && clauseQuerySuggestions[ i ].length > j) {
                    Query clauseQuerySuggestion = clauseQuerySuggestions[ i ][ j ];
                    returnQuery.add( query, clauses[ i ].getOccur() );
                    addThis = true;
                }
                else {
                    addThis = false;
                    break;
                }
            }
            
            if (addThis) {
                queries.add( returnQuery );
            }
            
            // Increment
            int i = indices.length - 1;
            indices[ i ]++;
            while (i > 0 && indices[ i ] >= clauseQuerySuggestions[ i ].length) {
                indices[ i ] = 0;
                indices[ --i ]++;
            }
        }
        
        return queries;
    }
    
    
    public List<Query> suggestSimilar (TermQuery query, int num_sug, IndexReader ir, boolean morePopular)
        throws IOException
    {
        Logger.getLogger(this.getClass()).trace("suggestSimilar(TermQuery,int,IndexReader,boolean);");
        
        List<Query> queries = new LinkedList<Query>();
        
        Term term = query.getTerm();
        
        String field = term.field();
        String word  = term.text();
        
        String[] suggestions = suggestSimilar( word, num_sug, ir, field, morePopular );
        
        for (int i = 0; i < suggestions.length; i++) {
            queries.add( new TermQuery( new Term( field, suggestions[ i ] ) ) );
        }
        
        Logger.getLogger(this.getClass()).debug("Returning: " + queries);
        
        return queries;
    }
    
    
}
