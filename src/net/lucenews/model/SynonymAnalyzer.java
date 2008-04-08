package net.lucenews.model;

import java.io.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.search.*;
import org.apache.lucene.wordnet.*;

public class SynonymAnalyzer extends Analyzer {
    
    private float boost;
    private Analyzer  analyzer;
    private Searcher searcher;
    
    public SynonymAnalyzer (Analyzer analyzer) {
        this( analyzer, (Searcher) null );
    }
    
    public SynonymAnalyzer (Analyzer analyzer, Searcher searcher) {
        this( analyzer, searcher, 0.0f );
    }
    
    public SynonymAnalyzer (Analyzer analyzer, Searcher searcher, float boost) {
        setAnalyzer( analyzer );
        setBoost( boost );
        setSearcher( searcher );
    }
    
    
    
    public Analyzer getAnalyzer () {
        return analyzer;
    }
    
    public void setAnalyzer (Analyzer analyzer) {
        this.analyzer = analyzer;
    }
    
    
    
    public Searcher getSearcher () {
        return searcher;
    }
    
    public void setSearcher (Searcher searcher) {
        this.searcher = searcher;
    }
    
    
    
    public float getBoost () {
        return boost;
    }
    
    public void setBoost (float boost) {
        this.boost = boost;
    }
    
    
    
    public int getPositionIncrementGap (String fieldName) {
        return getAnalyzer().getPositionIncrementGap( fieldName );
    }
    
    
    
    public TokenStream tokenStream (String fieldName, Reader reader) {
        return new SynonymFilter(
            getAnalyzer().tokenStream( fieldName, reader ),
            getAnalyzer(),
            getSearcher(),
            getBoost()
        );
    }
    
}
