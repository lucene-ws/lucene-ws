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
import org.apache.lucene.queryParser.*;
import org.w3c.dom.*;
import org.xml.sax.*;




public class LuceneWebService extends HttpServlet {
    
    /**
     * The current version of the web service
     */
    
    public String getVersion () {
        return "0.0.6";
    }
    
    
    
    
    
    
    protected Logger logger;
    
    
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
        
        
        // Attempt to load an external file, if at all possible
        try {
            setProperties( new File( getProperty( "properties-file" ) ) );
        }
        catch(NullPointerException npe) {
        }
        catch(IOException ioe) {
        }
    }
    
    
    /**
     * Logging
     */
    
    public Logger getLogger () {
        if (logger == null) {
            logger = Logger.getRootLogger();
        }
        return logger;
    }
    
    public void setLogger (Logger logger) {
        this.logger = logger;
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
        c.log( getLogger() );
        Logger.getLogger(this.getClass()).info("request:  " + req.getMethod() + " " + req.getLocation());
        
        res.setContentType( "application/atom+xml; charset=utf-8" );
        
        applyDefaults( req );
        
        try {
            switch (req.getMethodType()) {
                
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
        catch (ParseException pe) {
            res.setStatus( res.SC_BAD_REQUEST );
            ExceptionController.process( c, pe );
        }
        catch (AtomParseException ape) {
            res.setStatus( res.SC_BAD_REQUEST );
            ExceptionController.process( c, ape );
        }
        catch (SAXException saxe) {
            res.setStatus( res.SC_BAD_REQUEST );
            ExceptionController.process( c, saxe );
        }
        catch (LuceneException le) {
            if (le.hasStatus()) {
                res.setStatus( le.getStatus() );
            }
            else {
                res.setStatus( res.SC_INTERNAL_SERVER_ERROR );
            }
            ExceptionController.process( c, le );
        }
        catch (Exception e) {
            res.setStatus( res.SC_INTERNAL_SERVER_ERROR );
            ExceptionController.process( c, e );
        }
        
        Logger.getLogger(this.getClass()).info("response: " + res.getStatus());
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
        LuceneRequest req = c.req();
        
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
        LuceneRequest req = c.req();
        
        
        if (req.getIndexNames().length == 1) {
            if (req.getIndexName().equals( "service.properties" )) {
                ServicePropertiesController.doGet( c );
                return;
            }
        }
        
        if (req.getDocumentIDs().length == 1) {
            if (req.getDocumentID().equals( "opensearchdescription.xml" )) {
                OpenSearchController.doGet( c );
                return;
            }
            
            if (req.getDocumentID().equals( "index.properties" )) {
                IndexPropertiesController.doGet( c );
                return;
            }
        }
        
        
        
        if (req.hasIndexNames()) {
            if (req.hasDocumentIDs()) {
                DocumentController.doGet( c );
            }
            else if (req.getParameter( LuceneKeys.SEARCH_STRING ) != null) {
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
        super.doOptions( c.req(), c.res() );
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
        LuceneRequest req = c.req();
        
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
        LuceneRequest req = c.req();
        
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
        super.doTrace( c.req(), c.res() );
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
            Object _name = names.nextElement();
            if (_name instanceof String) {
                String name = (String) _name;
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
            setLogger( Logger.getRootLogger() );
        }
    }
    
    
    
    /**
     * Retrieves a specific property.
     * 
     * @throws IOException
     */
    
    public String getProperty (String name) throws IOException {
        return getProperties().getProperty( name );
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
            if (i > 0) {
                url.append( "," );
            }
            url.append( indices[i] );
        }
        
        url.append( "/opensearchdescription.xml" );
        
        return String.valueOf( url );
    }
    
    public Calendar getLastModified () {
        Calendar lastModified = null;
        
        /**Iterator<LuceneIndex> indices = getIndexManager().getIndices().iterator();
        while( indices.hasNext() )
        {
        LuceneIndex index = indices.next();
        
        if( lastModified == null || lastModified.compareTo( index.getLastModified() ) < 0 )
        lastModified = index.getLastModified();
        }*/
        
        if (lastModified == null)
            return Calendar.getInstance();
        
        return lastModified;
    }
    
    public String getTitle () throws IOException {
        return getProperty( "service.title", getProperty( "title", "Lucene Web Service (v" + getVersion() + ")" ) );
    }
    
    public String getDefaultField () throws IOException {
        return getProperty( "service.defaultfield" );
    }
    
    public QueryParser.Operator getDefaultOperator () throws IOException {
        return LuceneUtils.parseOperator( getProperty( "service.defaultoperator" ) );
    }
    
    public boolean isDebugging () throws IOException
    {
        return ServletUtils.parseBoolean( getProperty( "service.debugging" ) );
    }
    
    
    
    
    
    
    
    public void applyDefaults (LuceneRequest req) throws IOException {
        if (req.getAnalyzer() == null) {
            req.setAnalyzer( new StandardAnalyzer() );
        }
        //	req.setAnalyzer( getLuceneConfig().getAnalyzer() );
        
        
        //if( req.getDefaultField() == null )
        //	req.setDefaultField( getProperty( "service.defaultfield", "all" ) );
        
        if (req.getPage() == null) {
            req.setPage( 1 );
        }
        //if( getLuceneConfig().getDefaultPage() == null )
        //	req.setPage( 1 );
        //else
        //	req.setPage( getLuceneConfig().getDefaultPage() );
        
        if (req.getEntriesPerPage() == null) {
            req.setEntriesPerPage( 10 );
        }
        //req.setEntriesPerPage( getLuceneConfig().getEntriesPerPage() );
    }
    
    
    
    
    
    
}
