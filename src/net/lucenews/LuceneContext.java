package net.lucenews;

import java.util.*;
import javax.servlet.http.*;
import net.lucenews.model.*;
import net.lucenews.opensearch.*;
import org.apache.log4j.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.search.*;

public class LuceneContext {
    
    private Map<String,Object> stash;
    private LuceneRequest      request;
    private LuceneResponse     response;
    private LuceneWebService   service;
    
    private Searcher searcher;
    private IndexSearcher indexSearcher;
    private MultiSearcher multiSearcher;
    
    private OpenSearchQuery       openSearchQuery;
    private OpenSearchResponse    openSearchResponse;
    private OpenSearch.Format     openSearchFormat;
    private Analyzer              analyzer;
    private String                defaultField;
    private QueryParser.Operator  defaultOperator;
    private Locale                locale;
    private Filter                filter;
    private QueryParser           queryParser;
    private Boolean               optimizing;
    private Sort                  sort;
    private String                title;
    private Boolean               suggestSimilar;
    private Boolean               suggestSpelling;
    private LuceneSpellChecker    spellChecker;
    private Boolean               suggestSynonyms;
    private LuceneSynonymExpander synonymExpander;
    private QueryReconstructor    queryReconstructor;
    
    
    
    public LuceneContext (HttpServletRequest request, HttpServletResponse response) {
        setRequest( LuceneRequest.newInstance( request ) );
        setResponse( LuceneResponse.newInstance( response ) );
    }
    
    
    
    public LuceneContext (LuceneRequest request, LuceneResponse response) {
        setStash( new HashMap<String,Object>() );
        setRequest( request );
        setResponse( response );
    }
    
    public LuceneContext (LuceneRequest request, LuceneResponse response, LuceneWebService service) {
        setStash( new HashMap<String,Object>() );
        setRequest( request );
        setResponse( response );
        setService( service );
    }
    
    
    
    public Map<String,Object> getStash () {
        return stash;
    }
    
    public void setStash (Map<String,Object> stash) {
        this.stash = stash;
    }
    
    
    
    public LuceneRequest getRequest () {
        return request;
    }
    
    public void setRequest (LuceneRequest request) {
        this.request = request;
    }
    
    
    
    
    public LuceneResponse getResponse () {
        return response;
    }
    
    public void setResponse (LuceneResponse response) {
        this.response = response;
    }
    
    
    
    
    public LuceneWebService getService () {
        return service;
    }
    
    public void setService (LuceneWebService service) {
        this.service = service;
    }
    
    
    
    public Searcher getSearcher () {
        return searcher;
    }
    
    public void setSearcher (Searcher searcher) {
        this.searcher = searcher;
    }
    
    public IndexSearcher getIndexSearcher () {
        return indexSearcher;
    }
    
    public void setIndexSearcher (IndexSearcher indexSearcher) {
        this.indexSearcher = indexSearcher;
    }
    
    public MultiSearcher getMultiSearcher () {
        return multiSearcher;
    }
    
    public void setMultiSearcher (MultiSearcher multiSearcher) {
        this.multiSearcher = multiSearcher;
    }
    
    
    
    
    
    public OpenSearchQuery getOpenSearchQuery () {
        return openSearchQuery;
    }
    
    public void setOpenSearchQuery (OpenSearchQuery openSearchQuery) {
        this.openSearchQuery = openSearchQuery;
    }
    
    
    
    public OpenSearchResponse getOpenSearchResponse () {
        return openSearchResponse;
    }
    
    public void setOpenSearchResponse (OpenSearchResponse openSearchResponse) {
        this.openSearchResponse = openSearchResponse;
    }
    
    
    
    public Analyzer getAnalyzer () {
        return analyzer;
    }
    
    public void setAnalyzer (Analyzer analyzer) {
        this.analyzer = analyzer;
    }
    
    
    
    public String getDefaultField () {
        return defaultField;
    }
    
    public void setDefaultField (String defaultField) {
        this.defaultField = defaultField;
    }
    
    
    
    public QueryParser.Operator getDefaultOperator () {
        return defaultOperator;
    }
    
    public void setDefaultOperator (QueryParser.Operator defaultOperator) {
        this.defaultOperator = defaultOperator;
    }
    
    
    
    public Locale getLocale () {
        return locale;
    }
    
    public void setLocale (Locale locale) {
        this.locale = locale;
    }
    
    
    
    public Filter getFilter () {
        return filter;
    }
    
    public void setFilter (Filter filter) {
        this.filter = filter;
    }
    
    
    
    public Boolean suggestSynonyms () {
        return suggestSynonyms;
    }
    
    public Boolean suggestSynonyms (Boolean suggestSynonyms) {
        this.suggestSynonyms = suggestSynonyms;
        return suggestSynonyms();
    }
    
    
    public LuceneSynonymExpander getSynonymExpander () {
        return synonymExpander;
    }
    
    public void setSynonymExpander (LuceneSynonymExpander synonymExpander) {
        this.synonymExpander = synonymExpander;
    }
    
    
    
    
    public QueryParser getQueryParser () {
        return queryParser;
    }
    
    public void setQueryParser (QueryParser queryParser) {
        this.queryParser = queryParser;
    }
    
    
    
    public Sort getSort () {
        return sort;
    }
    
    public void setSort (Sort sort) {
        this.sort = sort;
    }
    
    
    
    public String getTitle () {
        return title;
    }
    
    public void setTitle (String title) {
        this.title = title;
    }
    
    
    
    public Boolean isOptimizing () {
        return optimizing;
    }
    
    public void isOptimizing (Boolean optimizing) {
        this.optimizing = optimizing;
    }
    
    
    
    public Boolean suggestSpelling () {
        return suggestSpelling;
    }
    
    public Boolean suggestSpelling (Boolean suggestSpelling) {
        this.suggestSpelling = suggestSpelling;
        return suggestSpelling();
    }
    
    public LuceneSpellChecker getSpellChecker () {
        return spellChecker;
    }
    
    public void setSpellChecker (LuceneSpellChecker spellChecker) {
        this.spellChecker = spellChecker;
    }
    
    
    
    public Boolean suggestSimilar () {
        return suggestSimilar;
    }
    
    public Boolean suggestSimilar (Boolean suggestSimilar) {
        this.suggestSimilar = suggestSimilar;
        return suggestSimilar();
    }
    
    
    
    public QueryReconstructor getQueryReconstructor () {
        return queryReconstructor;
    }
    
    public void setQueryReconstructor (QueryReconstructor queryReconstructor) {
        this.queryReconstructor = queryReconstructor;
    }
    
    
    
    public OpenSearch.Format getOpenSearchFormat () {
        return openSearchFormat;
    }
    
    public void setOpenSearchFormat (OpenSearch.Format openSearchFormat) {
        this.openSearchFormat = openSearchFormat;
    }
    
}
