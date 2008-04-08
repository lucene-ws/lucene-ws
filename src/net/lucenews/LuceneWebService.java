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
import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.*;
import org.w3c.dom.*;
import org.xml.sax.*;




public class LuceneWebService extends HttpServlet {
    
    
    
    public static final String VERSION = "1.0_03";
    
    
    
    
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
    @Override
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
                    setProperties( new File( propertiesFile ) );
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
        
        c.getLogger().info("request:  " + req.getMethod() + " " + req.getLocation() + " " + req.getProtocol());
        
        res.setContentType("application/atom+xml; charset=utf-8");
        
        try {
            switch ( req.getMethodType() ) {
                
                case DELETE:
                    doDelete( c );
                    break;
                    
                case GET:
                    doGet( c );
                    break;
                    
                case HEAD:
                    doHead( c );
                    break;
                    
                case OPTIONS:
                    doOptions( c );
                    break;
                    
                case POST:
                    doPost( c );
                    break;
                    
                case PUT:
                    doPut( c );
                    break;
                    
                case TRACE:
                    doTrace( c );
                    break;
                    
            }req=null;
        }
        catch (ServletException se) {
            throw se;
        }
        
        // ParseException: 400 Bad Request
        catch (ParseException pe) {
            res.setStatus( LuceneResponse.SC_BAD_REQUEST );
            ExceptionController.process( c, pe );
        }
        
        // AtomParseException: 400 Bad Request
        catch (AtomParseException ape) {
            res.setStatus( LuceneResponse.SC_BAD_REQUEST );
            ExceptionController.process( c, ape );
        }
        
        // SAXException: 400 Bad Request
        catch (SAXException saxe) {
            res.setStatus( LuceneResponse.SC_BAD_REQUEST );
            ExceptionController.process( c, saxe );
        }
        
        // LuceneException: Possibly 500 Internal Server Error
        catch (LuceneException le) {
            if (le.hasStatus()) {
                res.setStatus( le.getStatus() );
            }
            else {
                res.setStatus( LuceneResponse.SC_INTERNAL_SERVER_ERROR );
            }
            ExceptionController.process( c, le );
        }
        
        // Exception: 500 Internal Server Error
        catch (Exception e) {
            res.setStatus( LuceneResponse.SC_INTERNAL_SERVER_ERROR );
            ExceptionController.process( c, e );
        }
        
        c.getLogger().info("response: " + res.getStatus());
        res=null;c=null;
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
        c.getLogger().trace( "doDelete" );
        LuceneRequest request = c.getRequest();
        
        if (request.hasIndexNames()) {
            if (request.hasDocumentIDs()) {
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
            OpenSearchException, Exception
    {
        c.getLogger().trace( "doGet" );
        LuceneRequest request = c.getRequest();
             
        c.getLogger().debug("request has " + request.getIndexNames().length + " index names");
        
        if ( request.getIndexNames().length == 1 ) {
            if (request.getIndexName().equals( "service.properties" )) {
                ServicePropertiesController.doGet( c );
                c=null;
                return;
            }
        }
        
        ServletUtils.prepareContext( c );
        
        if ( request.getDocumentIDs().length == 1 ) {
            
            // OpenSearch Description
            if ( request.getDocumentID().equals("opensearchdescription.xml") || request.getDocumentID().equals("description.xml") ) {
                request=null;
                OpenSearchController.doGet( c );
                c=null;
                return;
            }
            
            // facets
            if ( request.getDocumentID().equals("facets") ) {
                request=null;
                FacetController.doGet( c );
                c=null;
                return;
            }
            
            // Index properties
            if ( request.getDocumentID().equals("index.properties") ) {
                request=null;
                IndexPropertiesController.doGet( c );
                c=null;
                return;
            }
            
            // Tag cloud
            if ( request.getDocumentID().equals("tagcloud") ) {
                request=null;
                IndexController.doTagCloud( c );
                c=null;
                return;
            }
            
            
        }
        
        
        
        if ( request.hasIndexNames() ) {
            if ( request.hasDocumentIDs() ) {
                request=null;
                DocumentController.doGet( c );
                c=null;
            }
            else if ( c.getOpenSearchQuery() != null && c.getOpenSearchQuery().getSearchTerms() != null ) {
                request=null;
                SearchController.doGet( c );
                c=null;
            }
            else {
                request=null;
                IndexController.doGet( c );
                c=null;
            }
        }
        else {
            request=null;
            ServiceController.doGet( c );
            c=null;
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
            OpenSearchException, Exception
    {
        c.getLogger().trace( "doHead" );
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
        c.getLogger().trace( "doOptions" );
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
        c.getLogger().trace( "doPost" );
        LuceneRequest request = c.getRequest();
        
        if (request.hasIndexName() && !request.hasDocumentIDs()) {
            if (request.getIndexName().equals( "service.properties" )) {
                ServicePropertiesController.doPost( c );
                return;
            }
        }
        
        if (request.hasDocumentID()) {
            if (request.getDocumentID().equals( "index.properties" )) {
                IndexPropertiesController.doPost( c );
                return;
            }
        }
        
        if (request.hasIndexNames()) {
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
        c.getLogger().trace( "doPut" );
        LuceneRequest request = c.getRequest();
        
        if (request.getDocumentIDs().length == 0) {
            if (request.getIndexNames().length == 1 && request.getIndexName().equals( "service.properties" )) {
                ServicePropertiesController.doPut( c );
                return;
            }
            if (request.getIndexNames().length > 0 && request.getParameter( "optimize" ) != null) {
                IndexController.doOptimize( c );
                return;
            }
        }
        
        if (request.getDocumentIDs().length == 1) {
            if (request.getDocumentID().equals( "index.properties" )) {
                IndexPropertiesController.doPut( c );
                return;
            }
        }
        
        if (request.hasIndexNames() && request.hasDocumentIDs()) {
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
        c.getLogger().trace( "doTrace" );
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
        String name;
        while (names.hasMoreElements()) {
            name = (String) names.nextElement();
            properties.setProperty( name, config.getInitParameter( name ) );
            name=null;
        }
        names=null;
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
    
    @Deprecated
    public static String getServiceURL (LuceneRequest request) {
        return request.getServletURL();
    }
    
    public static HttpURI getServiceURI (LuceneRequest request) {
        return new HttpURI( request.getServletURL() );
    }
    
    
    
    /**
     * Service properties URL
     */
    
    @Deprecated
    public static String getServicePropertiesURL (LuceneRequest request) {
        return getServiceURL( request ) + "service.properties";
    }
    
    public static HttpURI getServicePropertiesURI (LuceneRequest request) {
        return getServiceURI( request ).withPath( "service.properties" );
    }
    
    
    
    /**
     * Index URL
     */
     
    @Deprecated
    public static String getIndexURL (LuceneRequest request, String indexName) {
        return getServiceURL( request ) + indexName;
    }
    
    public static HttpURI getIndexURI (LuceneRequest request, String indexName) {
        return getServiceURI( request ).withPath( indexName );
    }
    
    @Deprecated
    public static String getIndexURL (LuceneRequest request, LuceneIndex index) {
        return getIndexURL( request, index.getName() );
    }
    
    public static HttpURI getIndexURI (LuceneRequest request, LuceneIndex index) {
        return getIndexURI( request, index.getName() );
    }
    
    @Deprecated
    public static String getIndicesURL (LuceneRequest request, LuceneIndex... indices) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < indices.length; i++) {
            if (i > 0) {
                buffer.append(",");
            }
            buffer.append( indices[ i ].getName() );
        }
        return getIndexURL( request, buffer.toString() );
    }
    
    public static HttpURI getIndicesURI (LuceneRequest request, String... indices) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < indices.length; i++) {
            if (i > 0) {
                buffer.append(",");
            }
            buffer.append( indices[ i ] );
        }
        return getIndexURI( request, buffer.toString() );
    }
    
    public static HttpURI getIndicesURI (LuceneRequest request, LuceneIndex... indices) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < indices.length; i++) {
            if (i > 0) {
                buffer.append(",");
            }
            buffer.append( indices[ i ].getName() );
        }
        return getIndexURI( request, buffer.toString() );
    }
    
    
    
    /**
     * Index properties URL
     */
    
    @Deprecated
    public static String getIndexPropertiesURL (LuceneRequest request, String indexName) {
        return getIndexURL( request, indexName ) + "/index.properties";
    }
    
    public static HttpURI getIndexPropertiesURI (LuceneRequest request, String indexName) {
        return getIndexURI( request, indexName ).withPath( "index.properties" );
    }
    
    @Deprecated
    public static String getIndexPropertiesURL (LuceneRequest request, LuceneIndex index) {
        return getIndexPropertiesURL( request, index.getName() );
    }
    
    public static HttpURI getIndexPropertiesURI (LuceneRequest request, LuceneIndex index) {
        return getIndexPropertiesURI( request, index.getName() );
    }
    
    
    
    /**
     * Document URL
     */
    
    
    @Deprecated
    public static String getDocumentURL (LuceneRequest request, String indexName, String documentID)
        throws InsufficientDataException, IOException
    {
        return getIndexURL( request, indexName ) + "/" + documentID;
    }
    
    public static HttpURI getDocumentURI (LuceneRequest request, String indexName, String documentID)
        throws InsufficientDataException, IOException
    {
        return getIndexURI( request, indexName ).withPath( documentID );
    }
    
    @Deprecated
    public static String getDocumentURL (LuceneRequest request, LuceneDocument document)
        throws InsufficientDataException, IOException
    {
        return getDocumentURL( request, document.getIndex(), document );
    }
    
    public static HttpURI getDocumentURI (LuceneRequest request, LuceneDocument document)
        throws InsufficientDataException, IOException
    {
        return getDocumentURI( request, document.getIndex(), document );
    }
    
    @Deprecated
    public static String getDocumentURL (LuceneRequest request, LuceneIndex index, LuceneDocument document)
        throws InsufficientDataException, IOException
    {
        return getDocumentURL( request, index.getName(), index.getIdentifier( document ) );
    }
    
    public static HttpURI getDocumentURI (LuceneRequest request, LuceneIndex index, LuceneDocument document)
        throws InsufficientDataException, IOException
    {
        return getDocumentURI( request, index.getName(), index.getIdentifier( document ) );
    }
    
    
    
    /**
     * OpenSearch description URL
     */
    
    @Deprecated
    public static String getOpenSearchDescriptionURL (LuceneRequest request, String... indices)
    {
        String s = getServiceURL( request );
        return s.substring( 0, s.length() - 1 ) + getOpenSearchDescriptionPathInfo( indices );
    }

    public static HttpURI getOpenSearchDescriptionURI (LuceneRequest request, String... indices)
    {
        return getIndicesURI( request, indices ).withPath( "description.xml" );
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
        
        if ( defaultOperator == null ) { defaultOperator = getProperty("service.operator");           }
        
        return LuceneUtils.parseOperator( defaultOperator );
    }
    
    
    
    /**
     * Determines whether or not the service is in a state of debugging.
     * 
     * @throws IOException
     */
    
    public boolean isDebugging () throws IOException {
        String debug = null;
        if ( debug == null ) debug = getProperty("service.debugging");
        return ServletUtils.parseBoolean( debug );
    }
    
    
    
    
    
    
    
    
    /**
     * Retrieves an OpenSearch image corresponding to this service.
     */
    
    public OpenSearchImage getImage () throws NumberFormatException, IOException {
        OpenSearchImage image = new OpenSearchImage();
        
        String url = null;
        if ( url == null ) { url = getCleanProperty("service.image.url"); }
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
