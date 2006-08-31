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
    private String[] fields;
    private BooleanClause.Occur occur;
    
    
    
    
    public LuceneQueryParser (String field, Analyzer analyzer) {
        super( field, analyzer );
        setBoost( 1.0f );
        setMaximumCorrections( 0 );
    }
    
    public LuceneQueryParser (String[] fields, Analyzer analyzer) {
        this( fields[ 0 ], analyzer );
        setFields( fields );
    }
    
    public LuceneQueryParser (CharStream stream) {
        super( stream );
        setBoost( 1.0f );
        setMaximumCorrections( 0 );
    }
    
    public LuceneQueryParser (QueryParserTokenManager tokenManager) {
        super( tokenManager );
        setBoost( 1.0f );
        setMaximumCorrections( 0 );
    }
    
    
    
    public String[] getFields () {
        return fields;
    }
    
    public void setFields (String... fields) {
        this.fields = fields;
    }
    
    public String getField (int index) {
        String[] fields = getFields();
        return fields[ index ];
    }
    
    
    public BooleanClause.Occur getDefaultOccur () {
        if ( occur == null ) {
            occur = BooleanClause.Occur.SHOULD;
        }
        return occur;
    }
    
    public void setDefaultOccur (BooleanClause.Occur occur) {
        this.occur = occur;
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
        if ( field.equals( getField() ) ) {
            BooleanQuery booleanQuery = new BooleanQuery();
            for ( int i = 0; i < getFields().length; i++ ) {
                String subfield = getField( i );
                Query query = getFieldQuery( getFieldName( subfield ), queryText );
                if ( hasFieldBoost( subfield ) ) {
                    query.setBoost( getFieldBoost( subfield ) );
                }
                booleanQuery.add( query, getDefaultOccur() );
            }
            return booleanQuery;
        }
        else {
            Query query = super.getFieldQuery( field, queryText );
            
            Logger.getLogger(this.getClass()).debug("Getting FieldQuery for \"" + field + "\", \"" + queryText + "\"");
            
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
    }
    
    protected Query getFieldQuery (String field, String queryText, int slop) throws ParseException {
        if ( field.equals( getField() ) ) {
            BooleanQuery booleanQuery = new BooleanQuery();
            for ( int i = 0; i < getFields().length; i++ ) {
                String subfield = getField( i );
                Query query = getFieldQuery( getFieldName( subfield ), queryText, slop );
                if ( hasFieldBoost( subfield ) ) {
                    query.setBoost( getFieldBoost( subfield ) );
                }
                booleanQuery.add( query, getDefaultOccur() );
            }
            return booleanQuery;
        }
        else {
            return super.getFieldQuery( field, queryText, slop );
        }
    }
    
    protected Query getFuzzyQuery (String field, String termStr, float minSimilarity) throws ParseException {
        if ( field.equals( getField() ) ) {
            BooleanQuery booleanQuery = new BooleanQuery();
            for ( int i = 0; i < getFields().length; i++ ) {
                String subfield = getField( i );
                Query query = getFuzzyQuery( getFieldName( subfield ), termStr, minSimilarity );
                if ( hasFieldBoost( subfield ) ) {
                    query.setBoost( getFieldBoost( subfield ) );
                }
                booleanQuery.add( query, getDefaultOccur() );
            }
            return booleanQuery;
        }
        else {
            return super.getFuzzyQuery( field, termStr, minSimilarity );
        }
    }
    
    protected Query getPrefixQuery (String field, String termStr) throws ParseException {
        if ( field.equals( getField() ) ) {
            BooleanQuery booleanQuery = new BooleanQuery();
            for ( int i = 0; i < getFields().length; i++ ) {
                String subfield = getField( i );
                Query query = getPrefixQuery( getFieldName( subfield ), termStr );
                if ( hasFieldBoost( subfield ) ) {
                    query.setBoost( getFieldBoost( subfield ) );
                }
                booleanQuery.add( query, getDefaultOccur() );
            }
            return booleanQuery;
        }
        else {
            return super.getPrefixQuery( field, termStr );
        }
    }
    
    protected Query getWildcardQuery (String field, String termStr) throws ParseException {
        if ( field.equals( getField() ) ) {
            BooleanQuery booleanQuery = new BooleanQuery();
            for ( int i = 0; i < getFields().length; i++ ) {
                String subfield = getField( i );
                Query query = getWildcardQuery( getFieldName( subfield ), termStr );
                if ( hasFieldBoost( subfield ) ) {
                    query.setBoost( getFieldBoost( subfield ) );
                }
                booleanQuery.add( query, getDefaultOccur() );
            }
            return booleanQuery;
        }
        else {
            return super.getWildcardQuery( field, termStr );
        }
    }
    
    
    
    protected Query getRangeQuery (String field, String part1, String part2, boolean inclusive)
        throws ParseException
    {
        if ( fields.equals( getField() ) ) {
            BooleanQuery booleanQuery = new BooleanQuery();
            for ( int i = 0; i < getFields().length; i++ ) {
                String subfield = getField( i );
                Query query = getRangeQuery( getFieldName( subfield ), part1, part2, inclusive );
                if ( hasFieldBoost( subfield ) ) {
                    query.setBoost( getFieldBoost( subfield ) );
                }
                booleanQuery.add( query, getDefaultOccur() );
            }
            return booleanQuery;
        }
        else {
            return new ConstantScoreRangeQuery( field, part1, part2, inclusive, inclusive );
        }
    }
    
    
    
    protected static String getFieldName (String field) {
        if ( field == null || field.trim().length() == 0 ) {
            return null;
        }
        
        if ( field.indexOf("^") >= 0 ) {
            String[] tokens = field.split("\\^");
            return tokens[ 0 ];
        }
        else {
            return field;
        }
    }
    
    protected static boolean hasFieldBoost (String field) {
        return getFieldBoost( field ) != null;
    }
    
    protected static Float getFieldBoost (String field) {
        if ( field == null || field.trim().length() == 0 ) {
            return null;
        }
        
        if ( field.indexOf("^") >= 0 ) {
            String[] tokens = field.split("\\^");
            String boost = tokens[ 1 ].trim();
            Logger.getLogger(LuceneQueryParser.class).debug("TOKEN FOUND!");
            return Float.valueOf( boost );
        }
        else {
            Logger.getLogger(LuceneQueryParser.class).debug("'"+field+"' contains no caret");
            return null;
        }
    }
    
}
