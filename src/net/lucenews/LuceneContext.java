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
    private Map<Locale,Map<Object,String>> localizations;
    
    private Searcher searcher;
    private IndexSearcher indexSearcher;
    private MultiSearcher multiSearcher;
    
    private OpenSearchQuery       openSearchQuery;
    private OpenSearchResponse    openSearchResponse;
    private OpenSearch.Format     openSearchFormat;
    private Analyzer              analyzer;
    private Analyzer              filterAnalyzer;
    private String                defaultField;
    private String[]              defaultFields;
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
    private String startPageParameter;
    
    
    
    public LuceneContext (HttpServletRequest request, HttpServletResponse response) {
        setRequest( LuceneRequest.newInstance( request ) );
        setResponse( LuceneResponse.newInstance( response ) );
        localizations = new HashMap<Locale,Map<Object,String>>();
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
    
    
    
    /**
     * Gets a Logger object suitable for whatever class has called this 
     * accessor.
     */
    
    public Logger getLogger () {
        Thread thread = Thread.currentThread();
        StackTraceElement[] elements = thread.getStackTrace();
        StackTraceElement element = elements[ 3 ];
        
        Class loggerClass = this.getClass();
        try {
            loggerClass = Class.forName( element.getClassName() );
        }
        catch (ClassNotFoundException cnfe) {
        }
        
        return Logger.getLogger( loggerClass );
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
    
    
    
    public Analyzer getFilterAnalyzer () {
        return filterAnalyzer;
    }
    
    public void setFilterAnalyzer (Analyzer filterAnalyzer) {
        this.filterAnalyzer = filterAnalyzer;
    }
    
    
    
    @Deprecated
    public String getDefaultField () {
        return defaultField;
    }
    
    @Deprecated
    public void setDefaultField (String defaultField) {
        this.defaultField = defaultField;
    }
    
    
    
    public String[] getDefaultFields () {
        return defaultFields;
    }
    
    public void setDefaultFields (String... defaultFields) {
        this.defaultFields = defaultFields;
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
    
    
    
    /**
     * Localize a particular key into a particular language
     */
    
    public Map<Locale,Map<Object,String>> getLocalizations () {
        return localizations;
    }
    
    public void setLocalizations (Map<Locale,Map<Object,String>> localizations) {
        this.localizations = localizations;
    }
    
    public Map<Object,String> getLocalizations (String locale) {
        return getLocalizations( new Locale( locale ) );
    }
    
    public Map<Object,String> getLocalizations (Locale locale) {
        return getLocalizations().get( locale );
    }
    
    public void setLocalizations (String locale, Map<Object,String> localizations) {
        setLocalizations( new Locale( locale ), localizations );
    }
    
    public void setLocalizations (Locale locale, Map<Object,String> localizations) {
        getLocalizations().put( locale, localizations );
    }
    
    public String getLocalization (String locale, Object key) {
        return getLocalization( new Locale( locale ), key );
    }
    
    public String getLocalization (Locale locale, Object key) {
        return getLocalizations( locale ).get( key );
    }
    
    public void setLocalization (String locale, Object key, String localization) {
        setLocalization( new Locale( locale ), key, localization );
    }
    
    public void setLocalization (Locale locale, Object key, String localization) {
        if ( getLocalizations( locale ) == null ) {
            setLocalizations( locale, new HashMap<Object,String>() );
        }
        Map<Object,String> mapping = getLocalizations( locale );
        mapping.put( key, localization );
    }
    
    public boolean hasLocalizations (Locale locale) {
        return getLocalizations( locale ) != null;
    }
    
    public String localize (Object key, String... parameters) {
        return localize( getLocale() );
    }
    
    public String localize (Locale locale, Object key, String... parameters) {
        if ( key == null ) {
            return null;
        }
        
        if ( locale == null || !hasLocalizations( locale ) ) {
            return key.toString();
        }
        
        String localization = getLocalization( locale, key );
        
        if ( localization == null ) {
            return key.toString();
        }
        
        return localization;
    }
    
    
    
    public String getStartPageParameter () {
        if ( startPageParameter == null ) {
            return "startPage";
        }
        else {
            return startPageParameter;
        }
    }
    
    public void setStartPageParameter (String startPageParameter) {
        this.startPageParameter = startPageParameter;
    }
    
}
