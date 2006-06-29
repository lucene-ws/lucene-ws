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
    
    private Map<String,Object> m_stash;
    private LuceneRequest      m_request;
    private LuceneResponse     m_response;
    private LuceneWebService   m_service;
    
    private OpenSearchQuery requestQuery;
    private OpenSearch.Format openSearchFormat;
    private Analyzer analyzer;
    private QueryParser.Operator defaultOperator;
    private Locale locale;
    private Filter filter;
    private Boolean expanding;
    private Boolean spellChecking;
    private Boolean suggesting;
    
    
    
    public LuceneContext (HttpServletRequest request, HttpServletResponse response) {
        m_request  = LuceneRequest.newInstance( request );
        m_response = LuceneResponse.newInstance( response );
    }
    
    
    
    public LuceneContext (LuceneRequest request, LuceneResponse response) {
        m_stash    = new HashMap<String,Object>();
        m_request  = request;
        m_response = response;
    }
    
    public LuceneContext (LuceneRequest request, LuceneResponse response, LuceneWebService service) {
        m_stash    = new HashMap<String,Object>();
        m_request  = request;
        m_response = response;
        m_service  = service;
    }
    
    
    public Map<String,Object> stash () {
        return m_stash;
    }
    
    public LuceneRequest request () {
        return m_request;
    }
    
    public LuceneRequest req () {
        return request();
    }
    
    public LuceneResponse response () {
        return m_response;
    }
    
    public LuceneResponse res () {
        return response();
    }
    
    public LuceneWebService service () {
        return getService();
    }
    
    public LuceneWebService getService () {
        return m_service;
    }
    
    public void setService (LuceneWebService service) {
        m_service = service;
    }
    
    
    
    public OpenSearchQuery getRequestQuery () {
        return requestQuery;
    }
    
    public void setRequestQuery (OpenSearchQuery requestQuery) {
        this.requestQuery = requestQuery;
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
