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



public class ServicePropertiesController extends Controller
{
	
	
	
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
		throws IndicesNotFoundException, ParserConfigurationException, TransformerException, IOException
	{
		LuceneWebService   service      = c.service();
		LuceneIndexManager manager      = service.getIndexManager();
		LuceneRequest      req          = c.req();
		LuceneResponse     res          = c.res();
		Calendar           lastModified = service.getPropertiesLastModified();
		String             httpDate     = ServletUtils.asHTTPDate( lastModified );
		
		
		
		
		if( httpDate != null )
		{
			res.addHeader( "Etag", httpDate );
			res.addHeader( "Last-Modified", httpDate );
		}
		
		
		
		
		if( !req.shouldHandle( lastModified, httpDate ) )
		{
			res.setStatus( res.SC_NOT_MODIFIED );
			return;
		}
		
		
		
		
		Entry entry = new Entry();
		
		entry.setTitle( service.getTitle() );
		entry.setUpdated( service.getPropertiesLastModified() );
		entry.setID( service.getServicePropertiesURL( req ) );
		entry.setContent( XOXOController.asContent( service.getProperties() ) );
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
		LuceneWebService   service = c.service();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest      req     = c.req();
		LuceneResponse     res     = c.res();
		
		
		
		
		boolean readOnly = true;
		
		String _readOnly = service.getProperty( "properties.readonly" );
		if( _readOnly != null )
			readOnly = ServletUtils.parseBoolean( _readOnly );
		
		if( readOnly )
			throw new IllegalActionException( "Service properties cannot be updated" );
		
		Entry[] entries = req.getEntries();
		
		if( entries.length == 0 )
			throw new InsufficientDataException( "No set of properties submitted" );
		
		if( entries.length > 1 )
			throw new MultipleValueException( "Many sets of properties submitted" );
		
		Entry entry = entries[ 0 ];
		
		Properties properties = XOXOController.asProperties( entry );
		
		service.setProperties( properties );
		
		res.addHeader( "Location", service.getServicePropertiesURL( req ) );
		
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
		LuceneWebService   service = c.service();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest      req     = c.req();
		LuceneResponse     res     = c.res();
		
		
		
		
		boolean readOnly = true;
		
		String _readOnly = service.getProperty( "properties.readonly" );
		if( _readOnly != null )
			readOnly = ServletUtils.parseBoolean( _readOnly );
		
		if( readOnly )
			throw new IllegalActionException( "Service properties cannot be added to" );
		
		Entry[] entries = req.getEntries();
		
		if( entries.length == 0 )
			throw new InsufficientDataException( "No set of properties submitted" );
		
		if( entries.length > 1 )
			throw new MultipleValueException( "Many sets of properties submitted" );
		
		Entry entry = entries[ 0 ];
		
		Properties properties = XOXOController.asProperties( entry );
		
		service.addProperties( properties );
		
		res.addHeader( "Location", service.getServicePropertiesURL( req ) );
		
		XMLController.acknowledge( c );
	}
	
	
	
}
