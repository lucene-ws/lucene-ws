package net.lucenews.model;

import java.io.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.search.*;
import org.apache.lucene.wordnet.*;

public class SynonymAnalyzer extends Analyzer {
    
    private float boost;
    private Analyzer  analyzer;
    private SynExpand expander;
    private Searcher searcher;
    
    public SynonymAnalyzer (Analyzer analyzer, SynExpand expander) {
        this( analyzer, expander, (Searcher) null );
    }
    
    public SynonymAnalyzer (Analyzer analyzer, SynExpand expander, Searcher searcher) {
        this( analyzer, expander, searcher, 0.0f );
    }
    
    public SynonymAnalyzer (Analyzer analyzer, SynExpand expander, Searcher searcher, float boost) {
        setAnalyzer( analyzer );
        setBoost( boost );
        setExpander( expander );
        setSearcher( searcher );
    }
    
    
    
    public Analyzer getAnalyzer () {
        return analyzer;
    }
    
    public void setAnalyzer (Analyzer analyzer) {
        this.analyzer = analyzer;
    }
    
    
    
    public SynExpand getExpander () {
        return expander;
    }
    
    public void setExpander (SynExpand expander) {
        this.expander = expander;
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
            getExpander(),
            getAnalyzer(),
            getSearcher(),
            getBoost()
        );
    }
    
}
