package net.lucenews.controller;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import net.lucenews.*;
import net.lucenews.atom.*;
import net.lucenews.controller.*;
import net.lucenews.model.*;
import net.lucenews.model.exception.*;
import net.lucenews.view.*;
import org.w3c.dom.*;
import org.xml.sax.*;



public class ServicePropertiesController extends Controller {
    
    
    
    /**
     * Gets the current service properties
     * 
     * @param c The context
     * @throws IndicesNotFoundException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IOException
     */
    
    public static void doGet (LuceneContext c)
        throws
            IndicesNotFoundException, ParserConfigurationException,
            TransformerException, IOException
    {
        LuceneWebService   service      = c.getService();
        LuceneIndexManager manager      = service.getIndexManager();
        LuceneRequest      request      = c.getRequest();
        LuceneResponse     response     = c.getResponse();
        Calendar           lastModified = service.getPropertiesLastModified();
        String             httpDate     = ServletUtils.asHTTPDate( lastModified );
        
        
        
        
        if (httpDate != null) {
            response.addHeader( "Etag", httpDate );
            response.addHeader( "Last-Modified", httpDate );
        }
        
        
        
        
        if (!request.shouldHandle( lastModified, httpDate )) {
            response.setStatus( response.SC_NOT_MODIFIED );
            return;
        }
        
        
        
        
        Entry entry = new Entry();
        
        entry.setTitle( service.getTitle() );
        entry.setUpdated( service.getPropertiesLastModified() );
        entry.setID( service.getServicePropertiesURI( request ).toString() );
        entry.setContent( XOXOController.asContent( c, service.getProperties() ) );
        entry.addAuthor( new Author( service.getTitle() ) );
        
        AtomView.process( c, entry );
    }
    
    
    
    
    
    /**
     * Updates the current service settings. Service properties
     * will only be updated if the properties.readonly property has a false
     * value. If it is null, it's value will be assumed to be true.
     * 
     * @param c the Context
     * @throws IllegalActionException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IOException
     * @throws AtomParseException
     */
    
    public static void doPut (LuceneContext c)
        throws
            IllegalActionException, LuceneException, SAXException, ParserConfigurationException,
            TransformerException, IOException, AtomParseException
    {
        LuceneWebService   service  = c.getService();
        LuceneIndexManager manager  = service.getIndexManager();
        LuceneRequest      request  = c.getRequest();
        LuceneResponse     response = c.getResponse();
        
        
        
        
        boolean readOnly = true;
        
        String _readOnly = service.getProperty( "properties.readonly" );
        if (_readOnly != null) {
            readOnly = ServletUtils.parseBoolean( _readOnly );
        }
        
        if (readOnly) {
            throw new IllegalActionException( "Service properties cannot be updated" );
        }
        
        Entry[] entries = request.getEntries();
        
        if (entries.length == 0) {
            throw new InsufficientDataException( "No set of properties submitted" );
        }
        
        if (entries.length > 1) {
            throw new MultipleValueException( "Many sets of properties submitted" );
        }
        
        Entry entry = entries[ 0 ];
        
        Properties properties = XOXOController.asProperties( c, entry );
        
        service.setProperties( properties );
        
        response.addHeader( "Location", service.getServicePropertiesURI( request ).toString() );
        
        XMLController.acknowledge( c );
    }
    
    
    
    
    
    /**
     * Adds to the current service settings. Service properties
     * will only be updated if the properties.readonly property has a false
     * value. If it is null, it's value will be assumed to be true.
     * 
     * @param c the Context
     * @throws IllegalActionException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IOException
     * @throws AtomParseException
     */
    
    public static void doPost (LuceneContext c)
        throws
            IllegalActionException, LuceneException, SAXException, ParserConfigurationException,
            TransformerException, IOException, AtomParseException
    {
        LuceneWebService   service  = c.getService();
        LuceneIndexManager manager  = service.getIndexManager();
        LuceneRequest      request  = c.getRequest();
        LuceneResponse     response = c.getResponse();
        
        
        
        
        boolean readOnly = true;
        
        String _readOnly = service.getProperty( "properties.readonly" );
        if (_readOnly != null) {
            readOnly = ServletUtils.parseBoolean( _readOnly );
        }
        
        if (readOnly) {
            throw new IllegalActionException( "Service properties cannot be added to" );
        }
        
        Entry[] entries = request.getEntries();
        
        if (entries.length == 0) {
            throw new InsufficientDataException( "No set of properties submitted" );
        }
        
        if (entries.length > 1) {
            throw new MultipleValueException( "Many sets of properties submitted" );
        }
        
        Entry entry = entries[ 0 ];
        
        Properties properties = XOXOController.asProperties( c, entry );
        
        service.addProperties( properties );
        
        response.addHeader( "Location", service.getServicePropertiesURI( request ).toString() );
        
        XMLController.acknowledge( c );
    }
    
    
    
}
