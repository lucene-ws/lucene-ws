package net.lucenews.controller;

import java.io.*;
import net.lucenews.*;
import net.lucenews.model.*;
import net.lucenews.model.exception.*;
import net.lucenews.view.*;
import net.lucenews.atom.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.TransformerException;
import org.apache.lucene.document.Field;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;



public class DocumentController extends Controller
{
	
	
	/**
	 * Deletes a document
	 * 
	 * @throws IndicesNotFoundException
	 * @throws DocumentsNotFoundException
	 * @throws IOException
	 */
	
	public static void doDelete (LuceneContext c)
		throws IllegalActionException, IndicesNotFoundException, DocumentsNotFoundException, IOException, InsufficientDataException, TransformerException, ParserConfigurationException
	{
		LuceneWebService   service     = c.service();
		LuceneIndexManager manager     = service.getIndexManager();
		LuceneRequest      req         = c.req();
		LuceneResponse     res         = c.res();
		String[]           indexNames  = req.getIndexNames();
		LuceneIndex[]      indices     = manager.getIndices( indexNames );
		String[]           documentIDs = req.getDocumentIDs();
		
		
		// Buffers for header location construction
		StringBuffer indexNamesBuffer  = new StringBuffer();
		StringBuffer documentIDsBuffer = new StringBuffer();
		
		boolean deleted = false;
		
		// For each index...
		for( int i = 0; i < indices.length; i++ )
		{
			LuceneIndex index = indices[ i ];
			
			if( i > 0 )
				indexNamesBuffer.append( "," );
			indexNamesBuffer.append( index.getName() );
			
			// For each document...
			for( int j = 0; j < documentIDs.length; j++ )
			{
				String documentID = documentIDs[ j ];
				
				LuceneDocument document = index.removeDocument( documentID );
				
				deleted = true;
				
				if( i == 0 )
				{
					if( j > 0 )
						documentIDsBuffer.append( "," );
					documentIDsBuffer.append( index.getIdentifier( document ) );
				}
			}
		}
		
		String indexNamesString  = indexNamesBuffer.toString();
		String documentIDsString = documentIDsBuffer.toString();
		
		if( deleted )
			res.addHeader( "Location", service.getDocumentURL( req, indexNamesString, documentIDsString ) );
		else
			throw new InsufficientDataException( "No documents to be deleted" );
		
		XMLController.acknowledge( c );
	}
	
	
	
	/**
	 * Gets a document
	 * 
	 * @throws IndicesNotFoundException
	 * @throws DocumentsNotFoundException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws IOException
	 */
	
	public static void doGet (LuceneContext c)
		throws
			IndicesNotFoundException, DocumentsNotFoundException, ParserConfigurationException,
			TransformerException, IOException, InsufficientDataException
	{
		LuceneWebService   service     = c.service();
		LuceneIndexManager manager     = service.getIndexManager();
		LuceneRequest      req         = c.req();
		LuceneResponse     res         = c.res();
		
		
		
		Author firstAuthor = null;
		
		
		List<Entry> entries = new LinkedList<Entry>();
		
		LuceneIndex[] indices = manager.getIndices( req.getIndexNames() );
		String[] documentIDs = req.getDocumentIDs();
		
		for( int i = 0; i < documentIDs.length; i++ )
		{
			String documentID = documentIDs[ i ];
			
			for( int j = 0; j < indices.length; j++ )
			{
				LuceneIndex index = indices[ j ];
				
				LuceneDocument document = null;
				
				try
				{
					document = index.getDocument( documentID );
				}
				catch(DocumentNotFoundException dnfe)
				{
				}
				
				if( document != null )
				{
					if( entries.size() == 0 )
					{
						String name = index.getAuthor( document );
						if( name == null )
							name = service.getTitle();
						firstAuthor = new Author( name );
					}
					
					entries.add( asEntry( c, index, document ) );
				}
			}
		}
		
		if( entries.size() == 1 )
		{
			entries.get( 0 ).addAuthor( firstAuthor );
		}
		
		//if( documents.length == 1 )
		//	AtomView.process( c, asEntry( c, documents[ 0 ] ) );
		//else
		//	AtomView.process( c, asFeed( c, documents ) );
		
		
		if( entries.size() == 0 )
			throw new DocumentsNotFoundException( documentIDs );
		
		if( entries.size() == 1 )
		{
			AtomView.process( c, entries.get( 0 ) );
		}
		else
		{
			Feed feed = new Feed();
			
			feed.setTitle( "Documents" );
			feed.setUpdated( Calendar.getInstance() );
			feed.setID( req.getLocation() );
			feed.addLink( Link.Self( req.getLocation() ) );
			feed.addAuthor( new Author( service.getTitle() ) );
			
			Iterator<Entry> iterator = entries.iterator();
			while( iterator.hasNext() )
				feed.addEntry( iterator.next() );
			
			AtomView.process( c, feed );
		}
		
	}
	
	
	
	/**
	 * Updates particular documents within the index
	 * 
	 * @throws InvalidIdentifierException
	 * @throws IndicesNotFoundException
	 * @throws SAXException
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws DocumentNotFoundException
	 * @throws IndexNotFoundException
	 * @throws IOException
	 */
	 
	public static void doPut (LuceneContext c)
		throws
			IllegalActionException, InvalidIdentifierException, IndicesNotFoundException, SAXException,
			TransformerException, ParserConfigurationException, DocumentNotFoundException,
			IndexNotFoundException, IOException, InsufficientDataException
	{
		LuceneWebService   service = c.service();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest      req     = c.req();
		LuceneResponse     res     = c.res();
		
		LuceneIndex[]    indices   = manager.getIndices( req.getIndexNames() );
		LuceneDocument[] documents = req.getLuceneDocuments();
		
		// Buffers for header location construction
		StringBuffer indexNamesBuffer  = new StringBuffer();
		StringBuffer documentIDsBuffer = new StringBuffer();
		
		boolean updated = false;
		
		for( int i = 0; i < indices.length; i++ )
		{
			LuceneIndex index = indices[ i ];
			
			if( i > 0 )
				indexNamesBuffer.append( "," );
			indexNamesBuffer.append( index.getName() );
			
			for( int j = 0; j < documents.length; j++ )
			{
				LuceneDocument document = documents[ j ];
				
				index.updateDocument( document );
				
				updated = true;
				
				if( i == 0 )
				{
					if( j > 0 )
						documentIDsBuffer.append( "," );
					documentIDsBuffer.append( index.getIdentifier( document ) );
				}
			}
		}
		String indexNames  = indexNamesBuffer.toString();
		String documentIDs = documentIDsBuffer.toString();
		
		if( updated )
			res.addHeader( "Location", service.getDocumentURL( req, indexNames, documentIDs ) );
		else
			throw new InsufficientDataException( "No documents to be updated" );
		
		XMLController.acknowledge( c );
	}
	
	
	
	public static Entry asEntry (LuceneContext c, LuceneIndex index, LuceneDocument document)
		throws InsufficientDataException, ParserConfigurationException, IOException
	{
		return asEntry( c, index, document, null );
	}
	
	
	
	/**
	 * Returns an Atom Entry reflecting the standard format chosen for documents.
	 * 
	 * @param c The context
	 * @param index The index
	 * @param document The document
	 * @param score The score of the document (if it was a hit)
	 * @return An Atom Entry
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	
	public static Entry asEntry (LuceneContext c, LuceneIndex index, LuceneDocument document, Float score)
		throws InsufficientDataException, ParserConfigurationException, IOException
	{
		LuceneWebService service = c.service();
		LuceneRequest    req     = c.req();
		
		
		
		// Entry
		Entry entry = new Entry();
		
		
		// Content
		entry.setContent( asContent( c, index, document ) );
		
		
		if( index == null )
			return entry;
		
		
		// ID and Link may only be added if the document is identified
		if( index.isDocumentIdentified( document ) )
		{
			// ID
			entry.setID( service.getDocumentURL( req, index, document ) );
		
			// Link
			entry.addLink( Link.Alternate( service.getDocumentURL( req, index, document ) ) );
		}
		
		// Title
		entry.setTitle( index.getTitle( document ) );
		
		// Updated
		try
		{
			entry.setUpdated( index.getUpdated( document ) );
		}
		catch(InsufficientDataException ide)
		{
		}
		
		// Author
		if( index.getAuthor( document ) != null )
			entry.addAuthor( new Author( index.getAuthor( document ) ) );
		
		// Score
		if( score != null )
			entry.setPropertyNS( "http://a9.com/-/spec/opensearch/1.1/", "opensearch:relevance", String.valueOf( score ) );
		
		// Summary
		if( index.getSummary( document ) != null )
			entry.setSummary( new Text( index.getSummary( document ) ) );
		
		return entry;
	}
	
	
	
	/**
	 * Returns a DOM Element (<div>...</div>) containing XOXO-formatted data
	 * 
	 * @param c The context
	 * @param index The index
	 * @param document The document
	 * @return A DOM Element
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	 
	public static Content asContent (LuceneContext c, LuceneIndex index, LuceneDocument document)
		throws ParserConfigurationException, IOException
	{
		Document doc = XMLController.newDocument();
		
		Element div = doc.createElement( "div" );
		div.setAttribute( "xmlns", "http://www.w3.org/1999/xhtml" );
		
		div.appendChild( XOXOController.asElement( document, doc ) );
		
		/**Element dl = doc.createElement( "dl" );
		dl.setAttribute( "class", "xoxo" );
		
		Enumeration<Field> fields = document.fields();
		while( fields.hasMoreElements() )
		{
			Field field = fields.nextElement();
			
			Element dt = doc.createElement( "dt" );
			dt.setAttribute( "class", getCSSClass( field ) );
			dt.appendChild( doc.createTextNode( String.valueOf( field.name() ) ) );
			dl.appendChild( dt );
		
			Element dd = doc.createElement( "dd" );
			dd.appendChild( doc.createTextNode( String.valueOf( field.stringValue() ) ) );
			dl.appendChild( dd );
		}
		
		div.appendChild( dl );*/
		
		return Content.xhtml( div );
	}
	
	
	
	/**
	 * Retrieves the appropriate CSS class for the given field
	 */
	
	public static String getCSSClass (Field field)
	{
		StringBuffer _class = new StringBuffer();
		
		if( field.isStored() )
			_class.append( " stored" );
		
		if( field.isIndexed() )
			_class.append( " indexed" );
		
		if( field.isTokenized() )
			_class.append( " tokenized" );
		
		if( field.isTermVectorStored() )
			_class.append( " termvectorstored" );
		
		return String.valueOf( _class ).trim();
	}
	
	
	
	
	
	
	
	
	/**
	 * Transforms an Atom feed into an array of Lucene documents!
	 */
	
	public static LuceneDocument[] asLuceneDocuments (Feed feed)
	{
		return asLuceneDocuments( feed.getEntries().toArray( new Entry[]{} ) );
	}
	
	
	/**
	 * Transforms Atom entries into Lucene documents
	 * 
	 * @param entries Atom entries
	 * @return Lucene documents
	 */
	
	public static LuceneDocument[] asLuceneDocuments (Entry... entries)
	{
		List<LuceneDocument> documents = new LinkedList<LuceneDocument>();
		
		for( int i = 0; i < entries.length; i++ )
			documents.addAll( Arrays.asList( asLuceneDocuments( entries[ i ] ) ) );
		
		return documents.toArray( new LuceneDocument[]{} );
	}
	
	public static LuceneDocument[] asLuceneDocuments (Entry entry)
	{
		List<LuceneDocument> documents = new LinkedList<LuceneDocument>();
		
		if( entry.getContent() != null )
		{
			if( entry.getContent().getData() != null )
			{
				Object content = entry.getContent().getData();
							
				if( content instanceof Element )
				{
					documents.addAll( Arrays.asList( asLuceneDocuments( (Element) content ) ) );
				}
				else if( content instanceof Document )
				{
					documents.addAll( Arrays.asList( asLuceneDocuments( (Document) content ) ) );
				}
							
				else if( content instanceof NodeList )
				{
					documents.addAll( Arrays.asList( asLuceneDocuments( (NodeList) content ) ) );
				}
			}
		}
		
		return documents.toArray( new LuceneDocument[]{} );
	}
	
	public static LuceneDocument[] asLuceneDocuments (Document document)
	{
		return asLuceneDocuments( document.getDocumentElement() );
	}
	
	public static LuceneDocument[] asLuceneDocuments (NodeList nodes)
	{
		List<LuceneDocument> documents = new LinkedList<LuceneDocument>();
		for( int i = 0; i < nodes.getLength(); i++ )
		{
			if( nodes.item( i ).getNodeType() == Node.ELEMENT_NODE )
			documents.addAll( Arrays.asList( asLuceneDocuments( (Element) nodes.item( i ) ) ) );
		}
		return documents.toArray( new LuceneDocument[]{} );
	}
	
	public static LuceneDocument[] asLuceneDocuments (Element element)
	{
		List<LuceneDocument> documents = new LinkedList<LuceneDocument>();
		
		if( element.getTagName().equals( "div" ) )
		{
			documents.addAll( Arrays.asList( asLuceneDocuments( (NodeList) element.getChildNodes() ) ) );
		}
		else
		{
			try
			{
				LuceneDocument document = new LuceneDocument();
				document.add( XOXOController.asFields( element ) );
				documents.add( document );
			}
			catch(LuceneException le)
			{
			}
			/**
			if( element.getTagName().equals( "documents" ) )
			{
				documents.addAll( Arrays.asList( asLuceneDocuments( (NodeList) element.getChildNodes() ) ) );
			}
			else if( element.getTagName().equals( "document" ) )
			{
				documents.add( asLuceneDocument( element ) );
			}
			*/
		}
		
		return documents.toArray( new LuceneDocument[]{} );
	}
	
	public static LuceneDocument asLuceneDocument (Element element)
	{
		LuceneDocument document = new LuceneDocument();
		
		/**if( element.getAttribute( "id" ) != null )
		 try
		 {
		 //document.setDocumentID( element.getAttribute( "id" ) );
		 }
		 catch(IOException ioe)
		 {
		 }*/
		
		NodeList fields = element.getElementsByTagName( "field" );
		for( int i = 0; i < fields.getLength(); i++ )
			document.add( asField( (Element) fields.item( i ) ) );
		
		return document;
	}
	
	
	public static Field asField (Element element)
	{
		if( !element.getTagName().equals( "field" ) )
			throw new RuntimeException( "Must provide a <field> tag" );
		
		String name  = element.getAttribute( "name" );
		String value = ServletUtils.toString( element.getChildNodes() );
		
		if( element.getAttribute( "type" ) != null )
		{
			String type = element.getAttribute( "type" ).trim().toLowerCase();
			
			if( type.equals( "keyword" ) )
				return Field.Keyword( name, value );
			
			if( type.equals( "text" ) )
				return Field.Text( name, value );
			
			if( type.equals( "sort" ) )
				return new Field( name, value, true, true, false );
			
			if( type.equals( "unindexed" ) )
				return Field.UnIndexed( name, value );
			
			if( type.equals( "unstored" ) )
				return Field.UnStored( name, value );
			
			return Field.Text( name, value );
		}
		else
		{
			boolean index = ServletUtils.parseBoolean( element.getAttribute( "index" ) );
			boolean store = ServletUtils.parseBoolean( element.getAttribute( "store" ) );
			boolean token = ServletUtils.parseBoolean( element.getAttribute( "tokenized" ) );
			
			return new Field( name, value, index, store, token );
		}
	}
	
}
