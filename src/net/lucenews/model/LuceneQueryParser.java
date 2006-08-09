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
    private LuceneSynonymExpander synonymExpander;
    
    
    
    
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
    
    
    
    public LuceneSynonymExpander getSynonymExpander () {
        return synonymExpander;
    }
    
    public void setSynonymExpander (LuceneSynonymExpander synonymExpander) {
        this.synonymExpander = synonymExpander;
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
        
        try {
            LuceneSynonymExpander expander = getSynonymExpander();
            
            // construct a new expander if we have not already done so
            if ( expander == null ) {
                expander = new LuceneSynonymExpander();
                expander.setSearcher( getSynonymSearcher() );
                expander.setAnalyzer( getAnalyzer() );
                setSynonymExpander( expander );
            }
            
            query = expander.expand(query);
            
            if (query instanceof BooleanQuery) {
                BooleanQuery booleanQuery = (BooleanQuery) query;
                BooleanClause[] clauses = booleanQuery.getClauses();
                
                TokenBooleanQuery tokenBoolean = new TokenBooleanQuery( booleanQuery.isCoordDisabled(), getToken( 0 ) );
                for (int i = 0; i < clauses.length; i++) {
                    tokenBoolean.add( clauses[ i ] );
                }
                
                query = tokenBoolean;
            }
        }
        catch (Exception e) {
        }
        
        return query;
    }
    
    
    
    protected Query getRangeQuery (String field, String part1, String part2, boolean inclusive)
        throws ParseException
    {
        return new ConstantScoreRangeQuery( field, part1, part2, inclusive, inclusive );
    }
    
}
