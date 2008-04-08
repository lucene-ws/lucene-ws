package net.lucenews.model;

import java.io.*;
import java.util.*;
import org.apache.log4j.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.wordnet.*;

public class SynonymFilter extends TokenFilter {
    
    private Analyzer analyzer;
    private Searcher searcher;
    private float boost;
    private LinkedList<Token> tokens;
    
    public SynonymFilter (TokenStream tokenStream) {
        this( tokenStream, new KeywordAnalyzer() );
    }
    
    public SynonymFilter (TokenStream tokenStream, Analyzer analyzer) {
        this( tokenStream, analyzer, (Searcher) null );
    }
    
    public SynonymFilter (TokenStream tokenStream, Analyzer analyzer, Searcher searcher) {
        this( tokenStream, analyzer, searcher, 0.0f );
    }
    
    public SynonymFilter (TokenStream tokenStream, Analyzer analyzer, Searcher searcher, float boost) {
        super( tokenStream );
        setAnalyzer( analyzer );
        setBoost( boost );
        setSearcher( searcher );
        setTokenStream( tokenStream );
        tokens = new LinkedList<Token>();
    }
    
    
    
    public Analyzer getAnalyzer () {
        return analyzer;
    }
    
    public void setAnalyzer (Analyzer analyzer) {
        this.analyzer = analyzer;
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
    
    
    
    public TokenStream getTokenStream () {
        return input;
    }
    
    public void setTokenStream (TokenStream tokenStream) {
        this.input = tokenStream;
    }
    
    
    @Override
    public Token next () throws IOException {
        if (tokens.isEmpty()) {
            Token token = getTokenStream().next();
            
            try {
                // Expand the original
                Query query = SynExpand.expand(
                    token.termText(),
                    getSearcher(),
                    getAnalyzer(),
                    "synonym",
                    getBoost()
                );
                
                Logger.getRootLogger().debug("Query for \"" + token.termText() + "\" (" + query.getClass().getCanonicalName() + "): " + query);
                
                if (query instanceof BooleanQuery) {
                    BooleanClause[] clauses = ( (BooleanQuery) query ).getClauses();
                    for (int i = 0; i < clauses.length; i++) {
                        Query subQuery = clauses[ i ].getQuery();
                        
                        Logger.getRootLogger().debug("Sub-query (" + subQuery.getClass().getCanonicalName() + "): " + subQuery);
                        if (subQuery instanceof TermQuery) {
                            Term term = ( (TermQuery) subQuery ).getTerm();
                            Token subToken = new Token(
                                term.text(),
                                token.startOffset(),
                                token.endOffset(),
                                token.type()
                            );
                            Logger.getRootLogger().debug("Adding synonym: " + subToken);
                            tokens.add( subToken );
                        }
                    }
                }
            }
            catch (Exception exception) {
                tokens.add( token );
            }
        }
        
        if (tokens.isEmpty()) {
            return null;
        }
        else {
            return tokens.removeFirst();
        }
    }
    
}
