package net.lucenews.model;

import org.apache.log4j.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.search.spell.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.wordnet.*;

public class LuceneQueryParser extends QueryParser {
    
    
    private Searcher synonym_searcher;
    private SpellChecker spellchecker;
    private int maximum_corrections;
    private float boost;
    
    
    
    
    public LuceneQueryParser (String f, Analyzer a) {
        super( f, a );
        setBoost( 1.0f );
        setMaximumCorrections( 0 );
    }
    
    public LuceneQueryParser (CharStream stream) {
        super( stream );
        setBoost( 1.0f );
        setMaximumCorrections( 0 );
    }
    
    public LuceneQueryParser (QueryParserTokenManager tm) {
        super( tm );
        setBoost( 1.0f );
        setMaximumCorrections( 0 );
    }
    
    
    
    public float getBoost () {
        return boost;
    }
    
    public void setBoost (float boost) {
        this.boost = boost;
    }
    
    
    
    public Searcher getSynonymSearcher () {
        return synonym_searcher;
    }
    
    public void setSynonymSearcher (Searcher synonym_searcher) {
        this.synonym_searcher = synonym_searcher;
    }
    
    
    
    public SpellChecker getSpellChecker () {
        return spellchecker;
    }
    
    public void setSpellChecker (SpellChecker spellchecker) {
        this.spellchecker = spellchecker;
    }
    
    public int getMaximumCorrections () {
        return maximum_corrections;
    }
    
    public void setMaximumCorrections (int maximum_corrections) {
        this.maximum_corrections = maximum_corrections;
    }
    
    
    
    protected Query getFieldQuery (String field, String queryText) throws ParseException {
        Query query = super.getFieldQuery( field, queryText );
        
        if (query instanceof TermQuery) {
            TermQuery termQuery = (TermQuery) query;
            query = new TokenTermQuery( termQuery.getTerm(), getToken(0) );
        }
        
        Logger.getLogger(this.getClass()).debug("Class of query: " + query.getClass());
        
        try {
            Logger.getLogger(this.getClass()).debug("getFieldQuery(\"" + field + "\", \"" + queryText + "\");");
            
            LuceneQueryExpander expander = new LuceneQueryExpander();
            expander.setSearcher(getSynonymSearcher());
            expander.setAnalyzer(getAnalyzer());
            query = expander.expand(query);
            
            if (query instanceof BooleanQuery) {
                BooleanQuery booleanQuery = (BooleanQuery) query;
                BooleanClause[] clauses = booleanQuery.getClauses();
                
                Logger.getLogger(this.getClass()).debug("We have a boolean query");
                
                TokenBooleanQuery tokenBoolean = new TokenBooleanQuery( booleanQuery.isCoordDisabled(), getToken( 0 ) );
                for (int i = 0; i < clauses.length; i++) {
                    tokenBoolean.add( clauses[ i ] );
                }
                
                Logger.getLogger(this.getClass()).debug("Returning TokenBooleanQuery: " + tokenBoolean);
                
                query = tokenBoolean;
            }
        }
        catch (Exception exception) {
            Logger.getLogger(this.getClass()).error("An error occured while trying to expand", exception);
        }
        
        return query;
    }
    
    
    
    protected Query getRangeQuery (String field, String part1, String part2, boolean inclusive)
    throws ParseException
    {
        return new ConstantScoreRangeQuery( field, part1, part2, inclusive, inclusive );
    }
    
}
