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
    
    private OpenSearchQuery      openSearchQuery;
    private OpenSearch.Format    openSearchFormat;
    private Analyzer             analyzer;
    private QueryParser.Operator defaultOperator;
    private Locale               locale;
    private Filter               filter;
    private Boolean              expanding;
    private Boolean              spellChecking;
    private Boolean              suggesting;
    
    
    
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
    
    public Map<String,Object> stash () {
        return getStash();
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
    
    public LuceneRequest request () {
        return getRequest();
    }
    
    public LuceneRequest req () {
        return getRequest();
    }
    
    
    
    public LuceneResponse getResponse () {
        return response;
    }
    
    public void setResponse (LuceneResponse response) {
        this.response = response;
    }
    
    public LuceneResponse response () {
        return getResponse();
    }
    
    public LuceneResponse res () {
        return getResponse();
    }
    
    
    
    public LuceneWebService service () {
        return getService();
    }
    
    public LuceneWebService getService () {
        return service;
    }
    
    public void setService (LuceneWebService service) {
        this.service = service;
    }
    
    
    
    public OpenSearchQuery getOpenSearchQuery () {
        return openSearchQuery;
    }
    
    public void setOpenSearchQuery (OpenSearchQuery openSearchQuery) {
        this.openSearchQuery = openSearchQuery;
    }
    
    
    
    public Analyzer getAnalyzer () {
        return analyzer;
    }
    
    public void setAnalyzer (Analyzer analyzer) {
        this.analyzer = analyzer;
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
    
    
    
    public Boolean isExpanding () {
        return expanding;
    }
    
    public void isExpanding (Boolean expanding) {
        this.expanding = expanding;
    }
    
    
    
    public Boolean isSpellChecking () {
        return spellChecking;
    }
    
    public void isSpellChecking (Boolean spellChecking) {
        this.spellChecking = spellChecking;
    }
    
    
    
    public Boolean isSuggesting () {
        return suggesting;
    }
    
    public void isSuggesting (Boolean suggesting) {
        this.suggesting = suggesting;
    }
    
    
    
    
    public OpenSearch.Format getOpenSearchFormat () {
        return openSearchFormat;
    }
    
    public void setOpenSearchFormat (OpenSearch.Format openSearchFormat) {
        this.openSearchFormat = openSearchFormat;
    }
    
}
