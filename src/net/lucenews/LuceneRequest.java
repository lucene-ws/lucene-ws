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
    
    
    private Document domDocument;
    
    
    
    
    
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
        
        if (domDocument == null) {
            domDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( getInputStream() );
        }
        return domDocument;
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
    
    
    
    public String getCleanParameter (String name) {
        return ServletUtils.clean( getParameter( name ) );
    }
    
    
    
    /**
     * Gets an integer parameter
     */
    
    public Integer getIntegerParameter (String name) {
        String value = getCleanParameter( name );
        
        if ( value == null ) {
            return null;
        }
        
        return Integer.valueOf( value );
    }
    
    
    public Boolean getBooleanParameter (String name) {
        String value = getCleanParameter(name);
        
        if ( value == null ) {
            return null;
        }
        
        return ServletUtils.parseBoolean(value);
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
