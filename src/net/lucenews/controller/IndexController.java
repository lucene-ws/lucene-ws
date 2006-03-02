package net.lucenews.controller;

import java.util.*;
import java.io.*;
import net.lucenews.*;
import net.lucenews.model.*;
import net.lucenews.model.exception.*;
import net.lucenews.view.*;
import net.lucenews.atom.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;



public class IndexController extends Controller
{
	
	
	
	/**
	 * Deletes an index.
	 * 
	 * @param c The context
	 * @throws IndicesNotFoundException
	 * @throws InvalidActionException
	 * @throws IOException
	 */
	
	public static void doDelete (LuceneContext c)
		throws TransformerException, ParserConfigurationException, IndicesNotFoundException, IllegalActionException, IOException
	{
		LuceneWebService   service = c.service();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest      req     = c.req();
		LuceneResponse     res     = c.res();
		LuceneIndex[]      indices = manager.getIndices( req.getIndexNames() );
		
		
		StringBuffer indexNamesBuffer = new StringBuffer();
		
		boolean deleted = false;
		
		
		// Delete each index
		for( int i = 0; i < indices.length; i++ )
		{
			LuceneIndex index = indices[ i ];
			
			if( !index.delete() )
				throw new IOException( "Index '" + index.getName() + "' could not be deleted." );
			
			deleted = true;
			
			if( i > 0 )
				indexNamesBuffer.append( "," );
			indexNamesBuffer.append( index.getName() );
		}
		
		if( deleted )
			res.addHeader( "Location", service.getIndexURL( req, indexNamesBuffer.toString() ) );
		
		
		
		XMLController.acknowledge( c );
	}
	
	
	
	/**
	 * Displays basic information regarding the index.
	 * 
	 * @param c The context
	 * @throws ParserConfigurationException
	 * @throws InvalidIdentifierException
	 * @throws DocumentNotFoundException
	 * @throws IndicesNotFoundException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws IOException
	 * @throws DocumentAlreadyExistsException
	 */
	
	public static void doGet (LuceneContext c)
		throws
			ParserConfigurationException, InvalidIdentifierException, DocumentNotFoundException,
			IndicesNotFoundException, ParserConfigurationException, TransformerException,
			IOException, DocumentAlreadyExistsException, InsufficientDataException
	{
		LuceneWebService   service = c.service();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest      req     = c.req();
		LuceneResponse     res     = c.res();
		
		
		
		AtomView.process( c, asFeed( c ) );
	}
	
	
	
	/**
	 * Updates an index
	 * 
	 * @param c The context
	 * @throws IndicesNotFoundException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws LuceneException
	 */
	
	public static void doPut (LuceneContext c)
		throws IndicesNotFoundException, ParserConfigurationException, SAXException, IOException, LuceneException, TransformerException, ParserConfigurationException
	{
		LuceneWebService   service = c.service();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest      req     = c.req();
		LuceneResponse     res     = c.res();
		Entry[]            entries = req.getEntries();
		
		
		StringBuffer indexNamesBuffer = new StringBuffer();
		
		boolean updated = false;
		
		
		// For each index...
		for( int i = 0; i < entries.length; i++ )
		{
			Entry entry = entries[ i ];
			
			String name = entry.getTitle();
			if( !req.hasIndexName( name ) )
				throw new LuceneException( "Index '" + name + "' not mentioned in URL" );
			
			Properties properties = XOXOController.asProperties( entry );
			
			LuceneIndex index = manager.getIndex( name );
			
			index.setProperties( properties );
			
			updated = true;
			
			if( i > 0 )
				indexNamesBuffer.append( "," );
			indexNamesBuffer.append( index.getName() );
		}
		
		if( updated )
			res.addHeader( "Location", service.getIndexURL( req, indexNamesBuffer.toString() ) );
		
		XMLController.acknowledge( c );
	}
	
	
	
	/**
	 * Adds documents.
	 * 
	 * @param c The context
	 * @throws TransformerException
	 * @throws DocumentsAlreadyExistException
	 * @throws IndicesNotFoundException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws LuceneException
	 */
	
	public static void doPost (LuceneContext c)
		throws
			IllegalActionException, TransformerException, DocumentsAlreadyExistException, IndicesNotFoundException,
			ParserConfigurationException, IOException, SAXException, LuceneException
	{
		LuceneWebService   service   = c.service();
		LuceneIndexManager manager   = service.getIndexManager();
		LuceneRequest      req       = c.req();
		LuceneResponse     res       = c.res();
		LuceneIndex[]      indices   = manager.getIndices( req.getIndexNames() );
		LuceneDocument[]   documents = req.getLuceneDocuments();
		
		
		// Buffers for header location construction
		StringBuffer indexNamesBuffer  = new StringBuffer();
		StringBuffer documentIDsBuffer = new StringBuffer();
		
		
		// For each index...
		for( int i = 0; i < indices.length; i++ )
		{
			LuceneIndex index = indices[ i ];
			
			if( i > 0 )
				indexNamesBuffer.append( "," );
			indexNamesBuffer.append( index.getName() );
			
			// For each document...
			for( int j = 0; j < documents.length; j++ )
			{
				LuceneDocument document = documents[ j ];
				
				index.addDocument( document );
				
				if( i == 0 )
				{
					if( j > 0 )
						documentIDsBuffer.append( "," );
					documentIDsBuffer.append( index.getIdentifier( document ) );
				}
				
				res.setStatus( res.SC_CREATED );
			}
		}
		
		String indexNames  = indexNamesBuffer.toString();
		String documentIDs = documentIDsBuffer.toString();
		
		if( res.getStatus() == res.SC_CREATED )
			res.addHeader( "Location", service.getDocumentURL( req, indexNames, documentIDs ) );
		
		XMLController.acknowledge( c );
	}
	
	
	
	/**
	 * Transforms the currently selected index into an Atom feed.
	 * Feed includes the most recently modified documents.
	 * 
	 * 
	 * TO DO:     This thing only tolerates one index, due to
	 *            performance/complexity issues. If anyone can
	 *            come up with an algorithm to efficiently display the
	 *            most recently modified documents across multiple
	 *            indices (WHILE BEING ABLE TO PROPERLY ASSOCIATE
	 *            EACH DOCUMENT WITH ITS RESPECTIVE INDEX), please
	 *            feel free to improve this.
	 *
	 * @param c The context
	 * @return An Atom feed
	 * @throws IndexNotFoundException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws IOException
	 */
	
	public static Feed asFeed (LuceneContext c)
		throws IndexNotFoundException, ParserConfigurationException, TransformerException, IOException, InsufficientDataException
	{
		LuceneWebService   service = c.service();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest      req     = c.req();
		LuceneResponse     res     = c.res();
		
		
		
		// We're not accepting multiple indices for this page
		// as of the time being. It causes too many performance
		// headaches. The following line will throw an exception 
		// if many indices have been specified. Enjoy!
		
		LuceneIndex index = manager.getIndex( req.getIndexName() );
		
		
		
		/**
		 * Feed
		 */
		Feed feed = new Feed();
		
		
		
		// Title
		feed.setTitle( index.getTitle() );
		
		// ID
		feed.setID( service.getIndexURL( req, index ) );
		
		// Updated
		feed.setUpdated( Calendar.getInstance() );
		
		// Author
		if( index.hasAuthor() )
			feed.addAuthor( new Author( index.getAuthor() ) );
		else
			feed.addAuthor( new Author( service.getTitle() ) );
		
		// Link
		feed.addLink( new Link( req.getLocation(), "self", "application/atom+xml" ) );
		
		// This is critical
		if( !index.hasUpdatedField() )
			return feed;
		
		IndexReader reader = index.getIndexReader();
		
		
		Limiter limiter = req.getLimiter();
		limiter.setTotalEntries( reader.numDocs() );
		
		if( limiter.getFirst() == null || limiter.getLast() == null )
			return feed;
		
		
		
		int number = 1;
		int documentNumber = reader.maxDoc() - 1;
		
		// Skip over the front-most documents
		while( number < limiter.getFirst() )
		{
			if( !reader.isDeleted( documentNumber ) )
				number++;
			documentNumber--;
		}
		
		// Iterate over the documents in question
		while( number <= limiter.getLast() )
		{
			if( !reader.isDeleted( documentNumber ) )
			{
				LuceneDocument document = new LuceneDocument( reader.document( documentNumber ) );
				feed.addEntry( DocumentController.asEntry( c, index, document ) );
				number++;
			}
			documentNumber--;
		}
		
		/**
		int documentsRequired = limiter.getLast();
		
		LinkedList<Integer> documentIDs = new LinkedList<Integer>();
		
		String field = index.getUpdatedField();
		
		TermEnum _enum = reader.terms( new Term( field, "" ) );
		
		while( _enum.next() && field.equals( _enum.term().field() ) )
		{
			TermDocs docs = reader.termDocs( _enum.term() );
			
			while( docs.next() )
			{
				documentIDs.addFirst( new Integer( docs.doc() ) );
				while( documentIDs.size() > documentsRequired )
					documentIDs.removeLast();
			}
		}
		
		
		Iterator<Integer> ids = documentIDs.iterator();
		
		// Iterate over the unnecessary IDs
		int skipped = limiter.getSkipped();
		for( int i = 1; i <= skipped; i++ )
		{
			ids.next();
		}
		
		// Iterate over the necessary IDs
		for( int i = limiter.getFirst(); i <= limiter.getLast() && ids.hasNext(); i++ )
		{
			LuceneDocument document = new LuceneDocument( reader.document( ids.next() ) );
			feed.addEntry( DocumentController.asEntry( c, index, document ) );
		}
		*/
		
		
		index.putIndexReader( reader );
		
		
		
		if( limiter instanceof Pager )
		{
			Pager pager = (Pager) limiter;
			
			String qs = req.getQueryStringExcluding( "page" );
			
			if( pager.getFirstPage() != null )
				feed.addLink( new Link( req.getRequestURL() + LuceneRequest.getQueryStringWithParameter( qs, "page", pager.getFirstPage() ), "first", "application/atom+xml" ) );
			
			if( pager.getPreviousPage() != null )
				feed.addLink( new Link( req.getRequestURL() + LuceneRequest.getQueryStringWithParameter( qs, "page", pager.getPreviousPage() ), "previous", "application/atom+xml" ) );
			
			if( pager.getNextPage() != null )
				feed.addLink( new Link( req.getRequestURL() + LuceneRequest.getQueryStringWithParameter( qs, "page", pager.getNextPage() ), "next", "application/atom+xml" ) );
			
			if( pager.getLastPage() != null )
				feed.addLink( new Link( req.getRequestURL() + LuceneRequest.getQueryStringWithParameter( qs, "page", pager.getLastPage() ), "last", "application/atom+xml" ) );
		}
		
		
		
		return feed;
	}
	
	
	
	
}


