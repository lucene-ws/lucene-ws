package net.lucenews.model;

import org.apache.log4j.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.wordnet.*;

public class LuceneQueryParser extends QueryParser {
    
    
    private Searcher searcher;
    private float boost;
    
    
    
    
    public LuceneQueryParser (String f, Analyzer a) {
        super( f, a );
        boost = 1.0f;
    }
    
    public LuceneQueryParser (CharStream stream) {
        super( stream );
        boost = 1.0f;
    }
    
    public LuceneQueryParser (QueryParserTokenManager tm) {
        super( tm );
        boost = 1.0f;
    }
    
    
    
    public float getBoost () {
        return boost;
    }
    
    public void setBoost (float boost) {
        this.boost = boost;
    }
    
    
    
    public Searcher getSearcher () {
        return searcher;
    }
    
    public void setSearcher (Searcher searcher) {
        this.searcher = searcher;
    }
    
    
    
    protected Query getFieldQuery (String field, String queryText) throws ParseException {
        try {
            return SynExpand.expand( queryText, getSearcher(), getAnalyzer(), field, getBoost() );
        }
        catch (Exception exception) {
            Query query = super.getFieldQuery( field, queryText );
            
            if (query instanceof TermQuery) {
                TermQuery termQuery = (TermQuery) query;
                return new TokenTermQuery( termQuery.getTerm(), getToken( 0 ) );
            }
            
            return query;
        }
    }
    
    protected Query getRangeQuery (String field, String part1, String part2, boolean inclusive)
    throws ParseException
    {
        return new ConstantScoreRangeQuery( field, part1, part2, inclusive, inclusive );
    }
    
}
