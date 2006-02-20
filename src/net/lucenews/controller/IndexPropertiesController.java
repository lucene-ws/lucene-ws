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



public class IndexPropertiesController extends Controller
{
	
	
	
	/**
	 * Gets an index's properties
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
		LuceneIndex[]      indices      = manager.getIndices( req.getIndexNames() );
		
		
		
		/**
		 * Transform the indices into entries
		 */
		if( indices.length == 1 )
		{
			LuceneIndex index = indices[ 0 ];
			Entry entry = asEntry( c, index, true );
			
			Calendar lastModified = index.getPropertiesLastModified();
			String   httpDate     = ServletUtils.asHTTPDate( lastModified );
			
			
			if( httpDate != null )
			{
				res.addHeader( "Etag", httpDate );
				res.addHeader( "Last-Modified", httpDate );
			}
			
			
			if( req.shouldHandle( lastModified, httpDate ) )
				AtomView.process( c, entry );
			else
				res.setStatus( res.SC_NOT_MODIFIED );
		}
		else
		{
			Feed feed = new Feed();
			
			feed.setTitle( "Properties" );
			feed.setUpdated( Calendar.getInstance() );
			feed.setID( req.getRequestURL().toString() );
			feed.addAuthor( new Author( service.getTitle() ) );
			feed.addLink( Link.Self( req.getLocation() ) );
			
			for( int i = 0; i < indices.length; i++ )
				feed.addEntry( asEntry( c, indices[ i ], false ) );
			
			AtomView.process( c, feed );
		}
	}
	
	
	
	/**
	 * Updates an index's properties
	 * 
	 * @param c The context
	 * @throws SAXException
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws IndexNotFoundException
	 * @throws LuceneException
	 */
	
	public static void doPut (LuceneContext c)
		throws SAXException, TransformerException, ParserConfigurationException, IOException, IndexNotFoundException, LuceneException
	{
		LuceneWebService   service = c.service();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest      req     = c.req();
		LuceneResponse     res     = c.res();
		
		
		
		
		// For each index...
		Entry[] entries = req.getEntries();
		for( int i = 0; i < entries.length; i++ )
		{
			Entry entry = entries[ i ];
			
			String name = entry.getTitle();
			
			if( !req.hasIndexName( name ) )
				continue;
			
			LuceneIndex index = manager.getIndex( name );
			
			Properties properties = XOXOController.asProperties( entry );
			
			index.setProperties( properties );
			
			res.addHeader( "Location", service.getIndexPropertiesURL( req, index ) );
		}
		
		XMLController.acknowledge( c );
	}
	
	
	
	/**
	 * Adds to an index's properties
	 * 
	 * @param c The context
	 * @throws SAXException
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws IndexNotFoundException
	 * @throws LuceneException
	 */
	
	public static void doPost (LuceneContext c)
		throws SAXException, TransformerException, ParserConfigurationException, IOException, IndexNotFoundException, LuceneException
	{
		LuceneWebService   service = c.service();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest      req     = c.req();
		LuceneResponse     res     = c.res();
		
		
		
		
		// For each index...
		Entry[] entries = req.getEntries();
		for( int i = 0; i < entries.length; i++ )
		{
			Entry entry = entries[ i ];
			
			String name = entry.getTitle();
			
			if( !req.hasIndexName( name ) )
				continue;
			
			LuceneIndex index = manager.getIndex( name );
			
			Properties properties = XOXOController.asProperties( entry );
			
			index.addProperties( properties );
			
			res.addHeader( "Location", service.getIndexPropertiesURL( req, index ) );
		}
		
		XMLController.acknowledge( c );
	}
	
	
	
	
	/**
	 * Transforms an index's properties into an Atom entry.
	 * 
	 * @param c The context
	 * @param index The index
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	
	public static Entry asEntry (LuceneContext c, LuceneIndex index, boolean authorRequired)
		throws ParserConfigurationException, IOException
	{
		LuceneWebService   service = c.service();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest      req     = c.req();
		LuceneResponse     res     = c.res();
		
		
		
		Entry entry = new Entry();
		
		entry.setTitle( "Properties of '" + index.getTitle() + "'" );
		entry.setID( c.service().getIndexURL( req, index ) + "index.properties" );
		entry.setUpdated( new Date( index.getPropertiesFile().lastModified() ) );
		entry.setContent( XOXOController.asContent( index.getProperties() ) );
		if( index.hasAuthor() )
			entry.addAuthor( new Author( index.getAuthor() ) );
		else if( authorRequired )
			entry.addAuthor( new Author( service.getTitle() ) );
		
		return entry;
	}
	
	
	
	
}
