/*
 * The Lucene Web Service
 * 
 * @author Adam Paynter
 */

package net.lucenews;

import java.util.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import net.lucenews.atom.*;
import net.lucenews.controller.*;
import net.lucenews.model.*;
import net.lucenews.model.exception.*;
import net.lucenews.opensearch.*;
import net.lucenews.view.*;

import org.apache.log4j.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.*;
import org.w3c.dom.*;
import org.xml.sax.*;




public class LuceneWebService extends HttpServlet {
    
    
    
    public static final String VERSION = "0.75";
    
    
    
    
    /**
     * The current version of the web service
     */
    
    public String getVersion () {
        return VERSION;
    }
    
    
    
    
    
    
    /**
     * Stores the properties
     */
    private boolean usePropertiesFile;
    private File propertiesFile;
    private long propertiesFileLastModified;
    private Date propertiesLastModified;
    
    private LuceneIndexManager manager;
    private Properties         properties;
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Initialization
     *
     * ========================================
     */
    
    
    
    /**
     * Initializes the service.
     */
    
    public void init () {
        manager = new LuceneIndexManager( this );
        
        
        OpenSearch.setDefaultFormat( OpenSearch.ATOM );
        OpenSearch.setDefaultMode( OpenSearch.PASSIVE );
        
        
        /**
         * Properties
         */
        
        try {
            setProperties( getServletConfig() );
        }
        catch(IOException ioe) {
        }
        
        // attempt to resolve a file containing the properties
        try {
            String propertiesFile = null;
            if ( propertiesFile == null ) { propertiesFile = getProperty("properties.file"); }
            if ( propertiesFile == null ) { propertiesFile = getProperty("properties-file"); }
            
            if ( propertiesFile != null ) {
                try {
                    setProperties( new File( getProperty( "properties-file" ) ) );
                }
                catch(NullPointerException npe) {
                }
            }
        }
        catch(IOException ioe) {
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Index manager
     *
     * ========================================
     */
    
    
    
    public LuceneIndexManager getIndexManager () {
        return manager;
    }
    
    public void setIndexManager (LuceneIndexManager manager) {
        this.manager = manager;
    }	
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Request servicing
     *
     * ========================================
     */
    
    
    
    // inherit javadoc
    public void service (HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        LuceneRequest  req = LuceneRequest.newInstance( request );
        LuceneResponse res = LuceneResponse.newInstance( response );
        LuceneContext  c   = new LuceneContext( req, res, this );
        req.setContext( c );
        
        Logger.getLogger(this.getClass()).info("request:  " + req.getMethod() + " " + req.getLocation() + " " + req.getProtocol());
        
        /*
        Logger.getLogger(this.getClass()).debug("getContextPath(): " + request.getContextPath());
        Logger.getLogger(this.getClass()).debug("getPathInfo(): " + request.getPathInfo());
        Logger.getLogger(this.getClass()).debug("getPathTranslated(): " + request.getPathTranslated());
        Logger.getLogger(this.getClass()).debug("getQueryString(): " + request.getQueryString());
        Logger.getLogger(this.getClass()).debug("getRequestURI(): " + request.getRequestURI());
        Logger.getLogger(this.getClass()).debug("getRequestURL(): " + request.getRequestURL());
        Logger.getLogger(this.getClass()).debug("getServletPath(): " + request.getServletPath());
        */
        
        res.setContentType("application/atom+xml; charset=utf-8");
        
        try {
            switch ( req.getMethodType() ) {
                
                case LuceneRequest.DELETE:
                    doDelete( c );
                    break;
                    
                case LuceneRequest.GET:
                    doGet( c );
                    break;
                    
                case LuceneRequest.HEAD:
                    doHead( c );
                    break;
                    
                case LuceneRequest.OPTIONS:
                    doOptions( c );
                    break;
                    
                case LuceneRequest.POST:
                    doPost( c );
                    break;
                    
                case LuceneRequest.PUT:
                    doPut( c );
                    break;
                    
                case LuceneRequest.TRACE:
                    doTrace( c );
                    break;
                    
            }
        }
        catch (ServletException se) {
            throw se;
        }
        
        // ParseException: 400 Bad Request
        catch (ParseException pe) {
            res.setStatus( res.SC_BAD_REQUEST );
            ExceptionController.process( c, pe );
        }
        
        // AtomParseException: 400 Bad Request
        catch (AtomParseException ape) {
            res.setStatus( res.SC_BAD_REQUEST );
            ExceptionController.process( c, ape );
        }
        
        // SAXException: 400 Bad Request
        catch (SAXException saxe) {
            res.setStatus( res.SC_BAD_REQUEST );
            ExceptionController.process( c, saxe );
        }
        
        // LuceneException: Possibly 500 Internal Server Error
        catch (LuceneException le) {
            if (le.hasStatus()) {
                res.setStatus( le.getStatus() );
            }
            else {
                res.setStatus( res.SC_INTERNAL_SERVER_ERROR );
            }
            ExceptionController.process( c, le );
        }
        
        // Exception: 500 Internal Server Error
        catch (Exception e) {
            res.setStatus( res.SC_INTERNAL_SERVER_ERROR );
            ExceptionController.process( c, e );
        }
        
        Logger.getLogger( this.getClass() ).info("response: " + res.getStatus());
    }	
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Request handlers
     *
     * ========================================
     */
    
    
    
    /**
     * Handles DELETE requests
     * 
     * @param c The <tt>LuceneContext</tt> of the request
     * @throws TransformerException
     * @throws ParserConfigurationException
     * @throws IndicesNotFoundException
     * @throws IllegalActionException
     * @throws DocumentsNotFoundException
     * @throws IOException
     */
    
    public void doDelete (LuceneContext c)
        throws
            TransformerException, ParserConfigurationException, IndicesNotFoundException,
            IllegalActionException, DocumentsNotFoundException, IOException, InsufficientDataException
    {
        Logger.getLogger( this.getClass() ).trace( "doDelete" );
        LuceneRequest req = c.getRequest();
        
        if (req.hasIndexNames()) {
            if (req.hasDocumentIDs()) {
                DocumentController.doDelete( c );
            }
            else {
                IndexController.doDelete( c );
            }
        }
    }
    
    
    
    /**
     * Handles GET requests
     * 
     * @param c The <tt>LuceneContext</tt> of the request
     * @throws ParserConfigurationException
     * @throws IndicesNotFoundException
     * @throws TransformerException
     * @throws IOException
     * @throws ParseException
     * @throws LuceneException
     */
    
    public void doGet (LuceneContext c)
        throws
            ParserConfigurationException, IndicesNotFoundException,
            TransformerException, IOException, ParseException, LuceneException,
            OpenSearchException
    {
        Logger.getLogger( this.getClass() ).trace( "doGet" );
        LuceneRequest request = c.getRequest();
        
        
        Logger.getLogger(this.getClass()).debug("request has " + request.getIndexNames().length + " index names");
        
        if ( request.getIndexNames().length == 1 ) {
            Logger.getLogger(this.getClass()).debug("request has 1 index name");
            if (request.getIndexName().equals( "service.properties" )) {
                ServicePropertiesController.doGet( c );
                return;
            }
        }
        
        ServletUtils.prepareContext( c );
        
        if ( request.getDocumentIDs().length == 1 ) {
            
            // OpenSearch Description
            if ( request.getDocumentID().equals("opensearchdescription.xml") || request.getDocumentID().equals("description.xml") ) {
                OpenSearchController.doGet( c );
                return;
            }
            
            // Index properties
            if ( request.getDocumentID().equals("index.properties") ) {
                IndexPropertiesController.doGet( c );
                return;
            }
            
            // Tag cloud
            if ( request.getDocumentID().equals("tagcloud") ) {
                IndexController.doTagCloud( c );
                return;
            }
            
            /**
            // JavaScript suggestions
            if ( request.getDocumentID().equals("suggest") ) {
                SearchController.doSuggest( c );
                return;
            }
            */
            
        }
        
        
        
        if ( request.hasIndexNames() ) {
            if ( request.hasDocumentIDs() ) {
                DocumentController.doGet( c );
            }
            else if ( c.getOpenSearchQuery() != null && c.getOpenSearchQuery().getSearchTerms() != null ) {
                SearchController.doGet( c );
            }
            else {
                IndexController.doGet( c );
            }
        }
        else {
            ServiceController.doGet( c );
        }
    }
    
    
    
    /**
     * Handles HEAD requests
     * 
     * @param c The <tt>LuceneContext</tt> of the request
     * @throws ServletException
     * @throws IOException
     */
    
    public void doHead (LuceneContext c)
        throws
            ParserConfigurationException, IndicesNotFoundException,
            TransformerException, IOException, ParseException, LuceneException,
            OpenSearchException
    {
        Logger.getLogger( this.getClass() ).trace( "doHead" );
        doGet( c );
    }
    
    
    
    /**
     * Handles OPTIONS requests
     * 
     * @param c The <tt>LuceneContext</tt> of the request
     * @throws ServletException
     * @throws IOException
     */
    
    public void doOptions (LuceneContext c) throws ServletException, IOException {
        Logger.getLogger( this.getClass() ).trace( "doOptions" );
        super.doOptions( c.getRequest(), c.getResponse() );
    }
    
    
    
    /**
     * Handles POST requests
     * 
     * @param c The <tt>LuceneContext</tt> of the request
     * @throws DocumentsAlreadyExistException
     * @throws ParserConfigurationException
     * @throws IndicesAlreadyExistException
     * @throws TransformerException
     * @throws IndicesNotFoundException
     * @throws SAXException
     * @throws IOException
     * @throws LuceneException
     * @throws AtomParseException
     */
    
    public void doPost (LuceneContext c)
        throws
            IllegalActionException, DocumentsAlreadyExistException, ParserConfigurationException,
            IndicesAlreadyExistException, TransformerException, AtomParseException,
            IndicesNotFoundException, SAXException, IOException, LuceneException
    {
        Logger.getLogger( this.getClass() ).trace( "doPost" );
        LuceneRequest req = c.getRequest();
        
        if (req.hasIndexName() && !req.hasDocumentIDs()) {
            if (req.getIndexName().equals( "service.properties" )) {
                ServicePropertiesController.doPost( c );
                return;
            }
        }
        
        if (req.hasDocumentID()) {
            if (req.getDocumentID().equals( "index.properties" )) {
                IndexPropertiesController.doPost( c );
                return;
            }
        }
        
        if (req.hasIndexNames()) {
            IndexController.doPost( c );
        }
        else {
            ServiceController.doPost( c );
        }
    }
    
    
    
    /**
     * Handles PUT requests
     * 
     * @param c The <tt>LuceneContext</tt> of the request
     * @throws InvalidIdentifierException
     * @throws IndicesNotFoundException
     * @throws SAXException
     * @throws TransformerException
     * @throws ParserConfigurationException
     * @throws DocumentNotFoundException
     * @throws IndexNotFoundException
     * @throws IOException
     * @throws LuceneException
     * @throws AtomParseException
     */
    
    public void doPut (LuceneContext c)
        throws
            IllegalActionException, InvalidIdentifierException, IndicesNotFoundException, SAXException,
            TransformerException, ParserConfigurationException, DocumentNotFoundException,
            IndexNotFoundException, IOException, LuceneException, AtomParseException
    {
        Logger.getLogger( this.getClass() ).trace( "doPut" );
        LuceneRequest req = c.getRequest();
        
        if (req.getDocumentIDs().length == 0) {
            if (req.getIndexNames().length == 1 && req.getIndexName().equals( "service.properties" )) {
                ServicePropertiesController.doPut( c );
                return;
            }
            if (req.getIndexNames().length > 0 && req.getParameter( "optimize" ) != null) {
                IndexController.doOptimize( c );
                return;
            }
        }
        
        if (req.getDocumentIDs().length == 1) {
            if (req.getDocumentID().equals( "index.properties" )) {
                IndexPropertiesController.doPut( c );
                return;
            }
        }
        
        if (req.hasIndexNames() && req.hasDocumentIDs()) {
            DocumentController.doPut( c );
        }
    }
    
    
    
    /**
     * Handles TRACE requests
     * 
     * @param c The <tt>LuceneContext</tt> of the request
     * @throws ServletException
     * @throws IOException
     */
    
    public void doTrace (LuceneContext c) throws ServletException, IOException {
        Logger.getLogger( this.getClass() ).trace( "doTrace" );
        super.doTrace( c.getRequest(), c.getResponse() );
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * Service properties
     *
     * ========================================
     */
    
    
    
    /**
     * Lucene web service properties
     * 
     * @throws IOException
     */
    
    public Properties getProperties () throws IOException {
        if (usePropertiesFile && propertiesFileLastModified < propertiesFile.lastModified()) {
            setProperties( propertiesFile );
        }
        return properties;
    }
    
    
    
    /**
     * Stores the current properties to the designated
     * properties file.
     * 
     * @throws IOException
     */
    
    public void storeProperties () throws IOException {
        storeProperties( null );
    }
    
    
    
    /**
     * Stored the current properties to the designated
     * properties file using the provided comment.
     * 
     * @param comment A comment to include in the file
     * @throws IOException
     */
    
    public void storeProperties (String comment) throws IOException {
        Properties properties = getProperties();
        
        FileOutputStream stream = new FileOutputStream( propertiesFile );
        properties.store( stream, comment );
        stream.close();
    }
    
    
    
    /**
     * Adds to the Lucene web service properties.
     * 
     * @param properties the properties to be added
     * @throws IOException
     */
    
    public void addProperties (Properties properties) throws IOException {
        Enumeration<?> names = properties.propertyNames();
        while (names.hasMoreElements()) {
            Object object = names.nextElement();
            if ( object instanceof String ) {
                String name = (String) object;
                setProperty( name, properties.getProperty( name ) );
            }
        }
        
        propertiesChanged();
    }
    
    
    
    /**
     * Sets the Lucene web service properties to the 
     * given set of properties.
     * 
     * @param properties A set of properties
     * @throws IOException
     */
    
    public void setProperties (Properties properties) throws IOException {
        this.properties = properties;
        
        usePropertiesFile = false;
        
        propertiesChanged();
    }
    
    
    
    /**
     * Sets the Lucene web service properties to the
     * given set of properties.
     * 
     * @param config A ServletConfig object
     * @throws IOException if index manager could not refresh
     */
    
    public void setProperties (ServletConfig config) throws IOException {
        properties = new Properties();
        
        Enumeration names = config.getInitParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            properties.setProperty( name, config.getInitParameter( name ) );
        }
        
        usePropertiesFile = false;
        
        propertiesChanged();
    }
    
    
    
    /**
     * Sets the current properties as the contents
     * of the provided file.
     * 
     * @param file The file to load from
     * @throws IOException
     */
    
    public void setProperties (File file) throws IOException {
        propertiesFile = file;
        propertiesFileLastModified = file.lastModified();
        properties = new Properties();
        
        FileInputStream stream = new FileInputStream( file );
        properties.load( stream );
        stream.close();
        
        usePropertiesFile = true;
        
        propertiesChanged();
    }
    
    
    
    /**
     * Loads properties from provided file (assumed
     * to be stored in XML format.
     * 
     * @param file The file to load from
     * @throws IOException
     */
    
    public void setPropertiesFromXML (File file) throws IOException {
        properties = new Properties();
        
        FileInputStream stream = new FileInputStream( file );
        properties.loadFromXML( stream );
        stream.close();
        
        usePropertiesFile = false;
        
        propertiesChanged();
    }
    
    
    
    /**
     * Invoked whenever the properties have been updated.
     * 
     * @throws IOException
     */
    
    public void propertiesChanged () throws IOException {
        propertiesLastModified = new Date();
        getIndexManager().refresh();
        
        if (getProperty("log4j.configuration") != null) {
            PropertyConfigurator.configureAndWatch( getProperty("log4j.configuration") );
        }
        
        /**
        String writeLockTimeout = getProperty("indexwriter.writelocktimeout");
        if ( writeLockTimeout != null ) {
            IndexWriter.setDefaultWriteLockTimeout( Long.valueOf( writeLockTimeout ) );
        }
        */
    }
    
    
    
    /**
     * Retrieves a specific property.
     * 
     * @throws IOException
     */
    
    public String getProperty (String name) throws IOException {
        return getProperties().getProperty( name );
    }
    
    public String getCleanProperty (String name) throws IOException {
        return ServletUtils.clean( getProperty( name ) );
    }
    
    public Boolean getBooleanProperty (String name) throws IOException {
        String value = ServletUtils.clean( getProperty( name ) );
        if (value == null) {
            return null;
        }
        return ServletUtils.parseBoolean( value );
    }
    
    
    
    /**
     * Retrieves a specific property, defaulting to the second
     * parameter if such a property does not exist.
     * 
     * @param name The name of desired property
     * @param defaultValue The value returned if the appropriate property does not exist
     * @throws IOException
     */
    
    public String getProperty (String name, String defaultValue) throws IOException {
        return getProperties().getProperty( name, defaultValue );
    }
    
    
    
    /**
     * Sets a certain property.
     * 
     * @param name The property name
     * @param value The property value
     * @throws IOException
     */
    
    public void setProperty (String name, String value) throws IOException {
        getProperties().setProperty( name, value );
        
        if (usePropertiesFile) {
            storeProperties();
        }
        
        propertiesChanged();
    }
    
    
    
    public Calendar getPropertiesLastModified () {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( propertiesLastModified );
        return calendar;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * ========================================
     *
     * URL construction
     *
     * ========================================
     */
    
    
    
    /**
     * Service URL
     */
    
    public static String getServiceURL (LuceneRequest request) {
        return request.getServletURL();
    }
    
    
    
    /**
     * Service properties URL
     */
    
    public static String getServicePropertiesURL (LuceneRequest request) {
        return getServiceURL( request ) + "service.properties";
    }
    
    
    
    /**
     * Index URL
     */
    
    public static String getIndexURL (LuceneRequest request, String indexName) {
        return getServiceURL( request ) + indexName + "/";
    }
    
    public static String getIndexURL (LuceneRequest request, LuceneIndex index) {
        return getIndexURL( request, index.getName() );
    }
    
    public static String getIndicesURL (LuceneRequest request, LuceneIndex[] indices) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < indices.length; i++) {
            if (i > 0) {
                buffer.append(",");
            }
            buffer.append( indices[ i ].getName() );
        }
        return getIndexURL( request, buffer.toString() );
    }
    
    
    
    /**
     * Index properties URL
     */
    
    public static String getIndexPropertiesURL (LuceneRequest request, String indexName) {
        return getIndexURL( request, indexName ) + "index.properties";
    }
    
    public static String getIndexPropertiesURL (LuceneRequest request, LuceneIndex index) {
        return getIndexPropertiesURL( request, index.getName() );
    }
    
    
    
    /**
     * Document URL
     */
    
    
    public static String getDocumentURL (LuceneRequest request, String indexName, String documentID)
        throws InsufficientDataException, IOException
    {
        return getIndexURL( request, indexName ) + documentID + "/";
    }
    
    public static String getDocumentURL (LuceneRequest request, LuceneDocument document)
        throws InsufficientDataException, IOException
    {
        return getDocumentURL( request, document.getIndex(), document );
    }
    
    public static String getDocumentURL (LuceneRequest request, LuceneIndex index, LuceneDocument document)
        throws InsufficientDataException, IOException
    {
        return getDocumentURL( request, index.getName(), index.getIdentifier( document ) );
    }
    
    
    
    /**
     * OpenSearch description URL
     */
    
    public static String getOpenSearchDescriptionURL (LuceneRequest request, String... indices)
    {
        String s = getServiceURL( request );
        return s.substring( 0, s.length() - 1 ) + getOpenSearchDescriptionPathInfo( indices );
    }
    
    
    
    /**
     * OpenSearch description path info
     */
    
    public static String getOpenSearchDescriptionPathInfo (String... indices) {
        StringBuffer url = new StringBuffer();
        url.append( "/" );
        
        for (int i = 0; i < indices.length; i++) {
            if ( i > 0 ) {
                url.append( "," );
            }
            url.append( indices[i] );
        }
        
        url.append( "/description.xml" );
        
        return String.valueOf( url );
    }
    
    
    
    
    /**
     * Determines the last time any of this service's indices have 
     * been updated.
     * 
     * @throws IndicesNotFoundException
     * @throws IOException
     */
    
    public Calendar getUpdated () throws IndicesNotFoundException, IOException {
        Calendar updated = null;
        
        LuceneIndex[] indices = getIndexManager().getIndices();
        for ( int i = 0; i < indices.length; i++ ) {
            LuceneIndex index = indices[ i ];
            if ( updated == null || updated.compareTo( index.getLastModified() ) < 0 ) {
                updated = index.getLastModified();
            }
        }
        
        if ( updated == null ) {
            return Calendar.getInstance();
        }
        
        return updated;
    }
    
    
    
    /**
     * Determines the title of this service as specified by the 
     * service.title service property. Defaults to "Lucene Web Service 
     * (vX.X.X)".
     */
    public String getTitle () throws IOException {
        String defaultTitle = "Lucene Web Service (v"+getVersion()+")";
        
        String title = null;
        if ( title == null ) { title = getProperty("service.title"); }
        if ( title == null ) { title = getProperty("title");         }
        if ( title == null ) { title = defaultTitle;                 }
        
        return title;
    }
    
    
    
    /**
     * Determines the service-wide default field to be used as a last 
     * resort when searching.
     * 
     * @throws IOException
     */
    
    public String[] getDefaultFields () throws IOException {
        String defaultFields = null;
        
        if ( defaultFields == null ) { defaultFields = getProperty("service.field.<default>"); }
        if ( defaultFields == null ) { defaultFields = getProperty("service.field.default");   }
        if ( defaultFields == null ) { defaultFields = getProperty("service.defaultfield");    }
        
        return ServletUtils.split( defaultFields );
    }
    
    
    
    /**
     * Determines the service-wide default operator to be used as a 
     * last resort when searching.
     * 
     * @throws IOException
     */
    
    public QueryParser.Operator getDefaultOperator ()
        throws IOException
    {
        String defaultOperator = null;
        
        if ( defaultOperator == null ) { defaultOperator = getProperty("service.operator.<default>"); }
        if ( defaultOperator == null ) { defaultOperator = getProperty("service.operator.default");   }
        if ( defaultOperator == null ) { defaultOperator = getProperty("service.defaultoperator");    }
        if ( defaultOperator == null ) { defaultOperator = getProperty("service.operator");           }
        
        return LuceneUtils.parseOperator( defaultOperator );
    }
    
    
    
    /**
     * Determines whether or not the service is in a state of debugging.
     * 
     * @throws IOException
     */
    
    public boolean isDebugging () throws IOException {
        String debugging = getProperty("service.debugging");
        return ServletUtils.parseBoolean( debugging );
    }
    
    
    
    
    
    
    
    
    /**
     * Retrieves an OpenSearch image corresponding to this service.
     */
    
    public OpenSearchImage getImage () throws NumberFormatException, IOException {
        OpenSearchImage image = new OpenSearchImage();
        
        String url = null;
        if ( url == null ) { url = getCleanProperty("service.image.url"); }
        if ( url == null ) { url = getCleanProperty("service.image");     }
        
        if ( url == null ) {
            image.setUrl("http://www.lucene-ws.net/images/magnifying_glass.png");
            return image;
        }
        
        image = new OpenSearchImage();
        
        image.setUrl( url );
        
        String height = getCleanProperty("service.image.height");
        if ( height != null ) {
            image.setHeight( Integer.valueOf( height ) );
        }
        
        String width = getCleanProperty("service.image.width");
        if ( width != null ) {
            image.setWidth( Integer.valueOf( width ) );
        }
        
        String type = getCleanProperty("service.image.type");
        if ( type != null ) {
            image.setType( type );
        }
        
        return image;
    }
    
    
}
