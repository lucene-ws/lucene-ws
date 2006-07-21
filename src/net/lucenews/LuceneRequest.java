package net.lucenews;

import net.lucenews.atom.*;
import java.util.*;
import net.lucenews.controller.*;
import net.lucenews.model.*;
import net.lucenews.model.exception.*;
import java.nio.charset.*;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import org.apache.log4j.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class LuceneRequest extends HttpServletRequestWrapper {
    
    private HttpURI uri;
    
    public static final int DELETE  = 1;
    public static final int GET     = 2;
    public static final int HEAD    = 3;
    public static final int OPTIONS = 4;
    public static final int POST    = 5;
    public static final int PUT     = 6;
    public static final int TRACE   = 7;
    
    
    
    private LuceneContext context;
    
    private boolean  m_analyzerCached;
    private Analyzer m_analyzer;
    
    private boolean  m_domDocumentCached;
    private Document m_domDocument;
    
    private boolean  m_defaultFieldCached;
    private String   m_defaultField;
    
    private boolean  m_defaultOperatorCached;
    private QueryParser.Operator  m_defaultOperator;
    
    private boolean  m_entriesPerPageCached;
    private Integer  m_entriesPerPage;
    
    private boolean  m_filterCached;
    private Filter   m_filter;
    
    private boolean  m_limiterCached = false;
    private Limiter  m_limiter;
    
    private boolean  m_localeCached;
    private Locale   m_locale;
    
    private boolean  m_pagerCached;
    private Pager    m_pager;
    
    private boolean  m_pageCached;
    private Integer  m_page;
    
    private boolean  m_queryCached;
    private Query    m_query;
    
    private boolean  m_sortCached;
    private Sort     m_sort;
    
    
    
    
    
    
    
    protected LuceneRequest (HttpServletRequest request) {
        super( request );
    }
    
    
    
    public static LuceneRequest newInstance (HttpServletRequest request) {
        return new LuceneRequest( request );
    }
    
    
    
    public LuceneContext getContext () {
        return context;
    }
    
    public void setContext (LuceneContext context) {
        this.context = context;
    }
    
    
    
    
    
    
    
    
    
    
    public HttpURI getUri () {
        if (uri == null) {
            uri = new HttpURI( this );
        }
        return uri;
    }
    
    public HttpURI uri () {
        return getUri();
    }
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Method type
     *
     * ========================================
     */
    
    
    
    /**
     * Gets the values associated with a certain header.
     * 
     * @param name the name of desired header
     * @return     an array of values
     */
    
    public String[] getHeaderValues (String name) {
        Enumeration enumeration = getHeaders( name );
        
        List<String> valuesList = new LinkedList<String>();
        while (enumeration.hasMoreElements()) {
            Object value = enumeration.nextElement();
            if (value instanceof String) {
                valuesList.add( (String) value );
            }
        }
        
        return valuesList.toArray( new String[]{} );
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Method type
     *
     * ========================================
     */
    
    
    
    /**
     * Determines which method was used to make
     * this request. Returned value is an integer
     * reflecting one of the class's predefined
     * constants (i.e. - POST, GET, etc...).
     *
     * @return An integer reflecting the method
     */
    
    public Integer getMethodType () {
        String method = getMethod();
        
        if (method.equals( "DELETE" )) {
            return DELETE;
        }
        
        if (method.equals( "GET" )) {
            return GET;
        }
        
        if (method.equals( "HEAD" )) {
            return HEAD;
        }
        
        if (method.equals( "OPTIONS" )) {
            return OPTIONS;
        }
        
        if (method.equals( "POST" )) {
            return POST;
        }
        
        if (method.equals( "PUT" )) {
            return PUT;
        }
        
        if (method.equals( "TRACE" )) {
            return TRACE;
        }
        
        return null;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Deprecated methods
     *
     * ========================================
     */
    
    
    
    /**
     * getRealPath
     */
    
    @Deprecated
    public String getRealPath (String path) {
        return super.getRealPath( path );
    }
    
    
    
    /**
     * isRequestedSessionIdFromUrl
     */
    
    @Deprecated
    public boolean isRequestedSessionIdFromUrl () {
        return isRequestedSessionIdFromURL();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Limiting and paging
     *
     * ========================================
     */
    
    
    
    /**
     * Retrieves the limiter
     */
    
    public Limiter getLimiter () {
        if (m_limiterCached) {
            return m_limiter;
        }
        
        m_limiter       = new Pager( getPage(), getEntriesPerPage() );
        m_limiterCached = true;
        
        return m_limiter;
    }
    
    
    
    /**
     * Retrieves the number of entries per page
     */
    
    public Integer getEntriesPerPage () {
        if (m_entriesPerPageCached) {
            return m_entriesPerPage;
        }
        return getIntegerParameter( LuceneKeys.ENTRIES_PER_PAGE );
    }
    
    
    
    /**
     * Sets the number of entries per page
     */
    
    public void setEntriesPerPage (Integer entriesPerPage) {
        getPager().setEntriesPerPage( entriesPerPage );
        m_entriesPerPage       = entriesPerPage;
        m_entriesPerPageCached = entriesPerPage != null;
    }
    
    
    
    /**
     * Gets the page
     */
    
    public Integer getPage () {
        if (m_pageCached) {
            return m_page;
        }
        return getIntegerParameter( LuceneKeys.PAGE );
    }
    
    
    
    /**
     * Sets the page
     */
    
    public void setPage (Integer page) {
        getPager().setCurrentPage( page );
        m_page       = page;
        m_pageCached = page != null;
    }
    
    
    
    /**
     * Gets the pager
     */
    
    public Pager getPager () {
        if (m_pagerCached) {
            return m_pager;
        }
        
        m_pager = new Pager( getPage(), getEntriesPerPage() );
        m_pagerCached = true;
        return m_pager;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Index names
     *
     * ========================================
     */
    
    
    
    /**
     * Gets the index name
     */
    
    public String getIndexName () throws MultipleValueException {
        String[] names = getIndexNames();
        if (names.length == 0) {
            return null;
        }
        if (names.length > 1) {
            throw new MultipleValueException( "Multiple values specified for index name" );
        }
        return names[ 0 ];
    }
    
    
    
    /**
     * Gets the index names
     */
    
    public String[] getIndexNames () {
        List<String> names = new ArrayList<String>();
        
        
        String[] path = getRequestURI().split( "/" );
        
        if (path.length > 2) {
            String partsString = path[2];
            
            if (partsString != null) {
                String[] parts = partsString.split(",");
                for (int i = 0; i < parts.length; i++) {
                    names.add( parts[i] );
                }
            }
        }
        
        
        return names.toArray( new String[]{} );
    }
    
    
    
    public boolean hasIndexName () {
        return getIndexNames().length == 1;
    }
    
    
    
    /**
     * Checks to see if there is an index name
     */
    
    public boolean hasIndexName (String name) {
        String[] names = getIndexNames();
        for (int i = 0; i < names.length; i++) {
            if (names[ i ].equals( name )) {
                return true;
            }
        }
        return false;
    }
    
    
    
    /**
     * Checks to see if there are index names
     */
    
    public boolean hasIndexNames () {
        return getIndexNames().length > 0;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Document IDs
     *
     * ========================================
     */
    
    
    
    /**
     * Gets the document ID
     */
    
    public String getDocumentID () throws MultipleValueException {
        String[] ids = getDocumentIDs();
        if (ids.length == 0) {
            return null;
        }
        if (ids.length > 1) {
            throw new MultipleValueException( "Multiple values specified for document ID" );
        }
        return ids[ 0 ];
    }
    
    
    
    /**
     * Gets the document IDs
     */
    
    public String[] getDocumentIDs () {
        List<String> ids = new ArrayList<String>();
        
        String[] path = getRequestURI().split("/");
        
        if (path.length > 3) {
            String str = path[3];
            
            String[] parts = str.split(",");
            for (int i = 0; i < parts.length; i++) {
                ids.add( parts[i] );
            }
        }
        
        return ids.toArray( new String[]{} );
    }
    
    
    
    public boolean hasDocumentID () {
        return getDocumentIDs().length == 1;
    }
    
    
    
    /**
     * Checks to see if it has document IDs
     */
    
    public boolean hasDocumentIDs () {
        return getDocumentIDs().length > 0;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Documents
     *
     * ========================================
     */
    
    
    
    /**
     * Gets the documents
     */
    
    public LuceneDocument[] getLuceneDocuments ()
        throws
            TransformerConfigurationException, TransformerException, ParserConfigurationException,
            AtomParseException, SAXException, IOException, LuceneParseException
    {
        Logger.getLogger(this.getClass()).trace("getLuceneDocuments()");
        
        Entry[] entries = getEntries();
        
        List<LuceneDocument> documents = new LinkedList<LuceneDocument>();
        
        for (int i = 0; i < entries.length; i++) {
            documents.addAll( Arrays.asList( DocumentController.asLuceneDocuments( getContext(), entries[ i ] ) ) );
        }
        
        return documents.toArray( new LuceneDocument[]{} );
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * DOM document
     *
     * ========================================
     */
    
    
    
    /**
     * Gets the internal DOM document
     */
    
    public Document getDOMDocument ()
        throws ParserConfigurationException, IOException, SAXException
    {
        Logger.getLogger(this.getClass()).trace("getDOMDocument()");
        
        if (m_domDocumentCached) {
            return m_domDocument;
        }
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        m_domDocument       = builder.parse( getInputStream() );
        m_domDocumentCached = true;
        return m_domDocument;
    }
    
    
    
    /**
     * Gets the Atom feed
     */
    
    public Feed getFeed ()
        throws
            TransformerConfigurationException, TransformerException,
            ParserConfigurationException, AtomParseException, IOException, SAXException
    {
        Logger.getLogger(this.getClass()).trace("getFeed()");
        
        return Feed.parse( getDOMDocument() );
    }
    
    
    
    /**
     * Gets the Atom entry
     */
    
    public Entry getEntry ()
        throws
            TransformerConfigurationException, TransformerException,
            ParserConfigurationException, AtomParseException, IOException, SAXException
    {
        Logger.getLogger(this.getClass()).trace("getEntry()");
        
        return Entry.parse( getDOMDocument() );
    }
    
    
    
    /**
     * Gets the Atom entries
     */
    
    public Entry[] getEntries ()
        throws
            TransformerConfigurationException, TransformerException,
            ParserConfigurationException, AtomParseException, SAXException, IOException
    {
        Logger.getLogger(this.getClass()).trace("getEntries()");
        
        List<Entry> entries = new LinkedList<Entry>();
        
        Feed feed = getFeed();
        if (feed != null) {
            entries.addAll( feed.getEntries() );
        }
        
        Entry entry = getEntry();
        if (entry != null) {
            entries.add( entry );
        }
        
        return entries.toArray( new Entry[]{} );
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Parameters
     *
     * ========================================
     */
    
    
    
    /**
     * Cleans a string
     */
    
    protected String clean (String string) {
        if (string == null || string.trim().length() == 0) {
            return null;
        }
        return string.trim();
    }
    
    
    
    /**
     * Gets a cleaned parameter
     */
    
    public String getParameterName (int key) {
        switch (key) {
            
            case LuceneKeys.ANALYZER:
                return "analyzer";
                
            case LuceneKeys.DEFAULT_FIELD:
                return "default";
                
            case LuceneKeys.DEFAULT_OPERATOR:
                return "operator";
                
            case LuceneKeys.ENTRIES_PER_PAGE:
                return "entriesperpage";
                
            case LuceneKeys.FILTER:
                return "filter";
                
            case LuceneKeys.LOCALE:
                return "locale";
                
            case LuceneKeys.PAGE:
                return "page";
                
            case LuceneKeys.SEARCH_STRING:
                return "query";
                
            case LuceneKeys.SORT:
                return "sort";
                
            default:
                return null;
                
        }
    }
    
    
    
    /**
     * Gets a cleaned parameter
     */
    
    public String getCleanParameter (int key) {
        return ServletUtils.clean( getParameter( key ) );
    }
    
    public String getCleanParameter (String name) {
        return ServletUtils.clean( getParameter( name ) );
    }
    
    
    
    /**
     * Gets an integer parameter
     */
    
    public Integer getIntegerParameter (String name) {
        try {
            return Integer.valueOf( getParameter( name ) );
        }
        catch (NullPointerException npe) {
            return null;
        }
        catch (NumberFormatException nfe) {
            return null;
        }
    }
    
    public Integer getIntegerParameter (int key) {
        try {
            return Integer.valueOf( getParameter( key ) );
        }
        catch (NullPointerException npe) {
            return null;
        }
        catch (NumberFormatException nfe) {
            return null;
        }
    }
    
    
    public Boolean getBooleanParameter (String name) {
        String value = getCleanParameter(name);
        if (value == null) {
            return null;
        }
        return ServletUtils.parseBoolean(value);
    }
    
    
    /**
     * Gets a parameter
     */
    
    public String getParameter (int key) {
        return getParameter( getParameterName( key ) );
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Filter
     *
     * ========================================
     */
    
    
    
    /**
     * Gets the filter
     */
    
    public Filter getFilter (QueryParser parser) throws ParseException {
        Logger.getLogger(this.getClass()).trace("getFilter()");
        
        if (m_filterCached) {
            return m_filter;
        }
        
        m_filter = LuceneUtils.parseFilter( getCleanParameter( LuceneKeys.FILTER ), parser );
        m_filterCached = true;
        return m_filter;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Default field
     *
     * ========================================
     */
    
    
    
    /**
     * Gets the default field
     */
    
    public String getDefaultField () {
        Logger.getLogger(this.getClass()).trace("getDefaultField()");
        
        if (m_defaultFieldCached) {
            return m_defaultField;
        }
        
        m_defaultField = clean( getParameter( LuceneKeys.DEFAULT_FIELD ) );
        
        m_defaultFieldCached = true;
        return m_defaultField;
    }
    
    
    
    /**
     * Sets the default field
     */
    
    public void setDefaultField (String defaultField) {
        Logger.getLogger(this.getClass()).trace("setDefaultField(String)");
        
        m_defaultField       = defaultField;
        m_defaultFieldCached = defaultField != null;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Search string (query)
     *
     * ========================================
     */
    
    
    
    /**
     * Gets the search string as specified by the user
     */
    
    public String getSearchString () {
        Logger.getLogger(this.getClass()).trace("getSearchString()");
        
        return clean( getParameter( LuceneKeys.SEARCH_STRING ) );
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Analyzer
     *
     * ========================================
     */
    
    
    
    /**
     * Gets the <tt>Analyzer</tt>
     */
    
    public Analyzer getAnalyzer () {
        Logger.getLogger(this.getClass()).trace("getAnalyzer()");
        
        if (m_analyzerCached) {
            return m_analyzer;
        }
        
        m_analyzer = LuceneUtils.parseAnalyzer( clean( getParameter( LuceneKeys.ANALYZER ) ) );
        
        m_analyzerCached = true;
        return m_analyzer;
    }
    
    
    /**
     * Sets the <tt>Analyzer</tt>
     */
    
    public void setAnalyzer (Analyzer analyzer) {
        Logger.getLogger(this.getClass()).trace("setAnalyzer(Analyzer)");
        
        m_analyzer       = analyzer;
        m_analyzerCached = analyzer != null;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Default operator
     *
     * ========================================
     */
    
    
    
    /**
     * Gets the default operator
     */
    
    public QueryParser.Operator getDefaultOperator () {
        Logger.getLogger(this.getClass()).trace("getDefaultOperator()");
        
        if (m_defaultOperatorCached) {
            return m_defaultOperator;
        }
        
        m_defaultOperator = LuceneUtils.parseOperator( clean( getParameter( LuceneKeys.DEFAULT_OPERATOR ) ) );
        
        m_defaultOperatorCached = true;
        return m_defaultOperator;
    }
    
    
    
    /**
     * Sets the default operator
     */
    public void setDefaultOperator (QueryParser.Operator defaultOperator) {
        Logger.getLogger(this.getClass()).trace("setDefaultOperator(QueryParser.Operator)");
        
        m_defaultOperator       = defaultOperator;
        m_defaultOperatorCached = defaultOperator != null;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Locale
     *
     * ========================================
     */
    
    
    
    /**
     * Gets the locale
     */
    
    public Locale getLocale () {
        Logger.getLogger(this.getClass()).trace("getLocale()");
        
        if (m_localeCached) {
            return m_locale;
        }
        
        m_locale = ServletUtils.parseLocale( clean( getParameter( LuceneKeys.LOCALE ) ) );
        
        m_localeCached = true;
        return m_locale;
    }
    
    
    
    /**
     * Sets the locale
     */
    
    public void setLocale (Locale locale) {
        Logger.getLogger(this.getClass()).trace("setLocale()");
        
        m_locale       = locale;
        m_localeCached = locale != null;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Query
     *
     * ========================================
     */
    
    
    
    /**
     * Gets the query
     */
    
    public Query getQuery () throws InsufficientDataException {
        Logger.getLogger(this.getClass()).trace("getQuery()");
        
        if (m_queryCached) {
            return m_query;
        }
        
        // Perhaps try to form a query
        
        m_queryCached = true;
        return m_query;
    }
    
    
    
    /**
     * Sets the query
     */
    
    public void setQuery (Query query) {
        Logger.getLogger(this.getClass()).trace("setQuery(Query)");
        
        m_query       = query;
        m_queryCached = query != null;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Sort
     *
     * ========================================
     */
    
    
    
    /**
     * Gets the <tt>Sort</tt>
     */
    
    public Sort getSort () throws InsufficientDataException {
        Logger.getLogger(this.getClass()).trace("getSort()");
        
        if (m_sortCached) {
            return m_sort;
        }
        
        m_sort = LuceneUtils.parseSort( clean( getParameter( LuceneKeys.SORT ) ) );
        
        m_sortCached = true;
        return m_sort;
    }
    
    
    
    /**
     * Sets the <tt>Sort</tt>
     */
    
    public void setSort (Sort sort) {
        Logger.getLogger(this.getClass()).trace("setSort(Sort)");
        
        m_sort       = sort;
        m_sortCached = sort != null;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * URLs
     *
     * ========================================
     */
    
    
    
    /**
     * Gets the servlet URL
     */
    
    public String getServletURL () {
        Logger.getLogger(this.getClass()).trace("getServletURL()");
        
        String requestURL  = String.valueOf( getRequestURL() );
        String contextPath = getContextPath();
        
        if (requestURL.indexOf( contextPath ) > -1) {
            int substringLength = requestURL.indexOf( contextPath ) + contextPath.length();
            return requestURL.substring( 0, substringLength ) + "/";
        }
        
        return null;
    }
    
    
    
    /**
     * Gets the location
     */
    
    public String getLocation () {
        Logger.getLogger(this.getClass()).trace("getLocation()");
        
        return getRequestURL() + ( ( getQueryString() != null ) ? "?" + getQueryString() : "" );
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public String getQueryStringExcluding (String... fields) {
        Logger.getLogger(this.getClass()).trace("getQueryStringExcluding(String...)");
        
        StringBuffer buffer = new StringBuffer();
        
        Enumeration names = getParameterNames();
        //buffer.append( getRequestURL() );
        
        boolean first = true;
        while( names.hasMoreElements() ) {
            String name = (String) names.nextElement();
            
            boolean exclude = false;
            
            for (int i = 0; i < fields.length; i++) {
                if (name.equals( fields[ i ] )) {
                    exclude = true;
                }
            }
            
            if (exclude) {
                continue;
            }
            
            String[] values = getParameterValues( name );
            for (int i = 0; i < values.length; i++) {
                if (first) {
                    buffer.append( "?" );
                    first = false;
                }
                else {
                    buffer.append( "&" );
                }
                buffer.append( name + "=" + values[i] );
            }
            
        }
        
        //buffer.append( first ? "?" : "&" );
        
        return buffer.toString();
    }
    
    
    public static String getQueryStringWithParameter (String queryString, String name, Object value) {
        if (value == null) {
            return queryString;
        }
        
        StringBuffer buffer = new StringBuffer( queryString );
        
        if (queryString == null || !queryString.contains( "?" )) {
            buffer.append( "?" );
        }
        else {
            buffer.append( "&" );
        }
        
        buffer.append( name + "=" + value );
        
        return buffer.toString();
    }
    
    public String getQueryStringWithParameter (String name, String value) {
        Logger.getLogger(this.getClass()).trace("getQueryStringWithParameter(String,String)");
        
        return getQueryStringWithParameter( getQueryString(), name, value );
    }
    
    
    
    
    public boolean noneMatch (String... strings) {
        Logger.getLogger(this.getClass()).trace("noneMatch(String...)");
        
        String[] clauses = getIfNoneMatchClauses();
        
        for (int i = 0; i < strings.length; i++) {
            for( int j = 0; j < clauses.length; j++ ) {
                if( strings[ i ] != null && clauses[ j ] != null ) {
                    if( strings[ i ].equals( clauses[ j ] ) ) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    
    public String[] getIfNoneMatchClauses () {
        Logger.getLogger(this.getClass()).trace("getIfNoneMatchClauses()");
        
        String clause = getHeader( "If-None-Match" );
        
        if (clause == null) {
            return new String[]{};
        }
        
        boolean insideQuotes = false;
        
        List<String> clauses = new LinkedList<String>();
        
        StringBuffer buffer = new StringBuffer();
        
        for (int i = 0; i < clause.length(); i++) {
            char c = clause.charAt( i );
            
            if (c == '"') {
                if (insideQuotes) {
                    insideQuotes = false;
                    clauses.add( buffer.toString() );
                }
                else {
                    insideQuotes = true;
                    buffer = new StringBuffer();
                }
            }
            else {
                if (insideQuotes) {
                    buffer.append( c );
                }
            }
        }
        
        return clauses.toArray( new String[]{} );
    }
    
    
    
    public boolean shouldHandle (Calendar lastModified, String... etags) {
        Logger.getLogger(this.getClass()).trace("shouldHandle(Calendar,String...)");
        
        if (lastModified == null) {
            return true;
        }
        
        
        
        /**
         * Determine if none match...
         */
        
        boolean _noneMatch = noneMatch( etags );
        
        
        
        /**
         * Determine if modified since...
         */
        
        boolean _modifiedSince = true;
        
        if (lastModified != null && getHeader( "If-Modified-Since" ) != null) {
            try {
                Calendar _ifModifiedSince = ServletUtils.asCalendarFromHTTPDate( getHeader( "If-Modified-Since" ) );
                
                if (_ifModifiedSince.getTime().compareTo( lastModified.getTime() ) == 0) {
                    _modifiedSince = false;
                }
                else if (_ifModifiedSince.compareTo( lastModified ) > 0) {
                    _modifiedSince = false;
                }
            }
            catch (NullPointerException npe) {
            }
        }
        
        
        
        
        /**
         * Final decision
         */
        
        if (getHeader( "If-None-Match" ) == null) {
            return _modifiedSince;
        }
        else {
            if (!_noneMatch) {
                return false;
            }
            
            return _modifiedSince;
        }
    }
    
    
    
    
    
    
    public String getUrlWith (String name, Object... values) {
        StringBuffer buffer = getRequestURL();
        
        boolean foundName = false;
        boolean first     = true;
        
        Enumeration names = getParameterNames();
        while (names.hasMoreElements()) {
            Object element = names.nextElement();
            if (element instanceof String) {
                String   _name   = (String) element;
                Object[] _values = _name.equals(name) ? values : getParameterValues(_name);
                
                if (_name.equals(name)) {
                    foundName = true;
                }
                
                for (int i = 0; i < _values.length; i++) {
                    if (first) {
                        buffer.append("?");
                        buffer.append(_name + "=" + _values[i].toString());
                        first = false;
                    }
                    else {
                        buffer.append("&");
                        buffer.append(_name + "=" + _values[i].toString());
                    }
                }
            }
        }
        
        if (!foundName) {
            for (int i = 0; i < values.length; i++) {
                if (first) {
                    buffer.append("?");
                    buffer.append(name + "=" + values[i].toString());
                    first = false;
                }
                else {
                    buffer.append("&");
                    buffer.append(name + "=" + values[i].toString());
                }
            }
        }
        
        return buffer.toString();
    }
    
    
    
    
    
    public String getInputEncoding () {
        String inputEncoding = getCleanParameter("inputEncoding");
        if (inputEncoding != null) {
            return inputEncoding;
        }
        else {
            return "UTF-8";
        }
    }
    
    public String getOutputEncoding () {
        String outputEncoding = getCleanParameter("outputEncoding");
        if (outputEncoding != null) {
            return outputEncoding;
        }
        else {
            return "UTF-8";
        }
    }
    
    
    public Charset getInputCharset ()
        throws IllegalCharsetNameException, UnsupportedCharsetException
    {
        return Charset.forName( getInputEncoding() );
    }
    
    public Charset getOutputCharset ()
        throws IllegalCharsetNameException, UnsupportedCharsetException
    {
        return Charset.forName( getOutputEncoding() );
    }
    
}
