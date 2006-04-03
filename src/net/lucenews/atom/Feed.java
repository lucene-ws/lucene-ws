package net.lucenews.atom;

import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import javax.xml.transform.*;

public class Feed extends Base
{
	
	
	
	private List<Entry>       m_entries;
	//generator
	private String       m_icon;
	private String       m_logo;
	private String       m_rights;
	private Generator m_generator;
	private String       m_subtitle;
	
	
	public Feed ()
	{
		this( null, null, null );
	}
	
	public Feed (String id, String title, String updated)
	{
		m_entries = new LinkedList<Entry>();
		
		setID( id );
		setTitle( title );
		setUpdated( updated );
	}
	
	
	
	
	
	
	
	public List<Entry> getEntries ()
	{
		return m_entries;
	}
	
	public void addEntry (Entry entry)
	{
		m_entries.add( entry );
	}
	
	public Generator getGenerator ()
	{
		return m_generator;
	}
	
	public void setGenerator (Generator generator)
	{
		m_generator = generator;
	}
	
	
	
	public static Feed parse (Document document)
        throws TransformerConfigurationException, TransformerException, AtomParseException
	{
		Feed feed = new Feed();
		
		Element _feed = document.getDocumentElement();
		
		
		NodeList _nodes = _feed.getChildNodes();
		for( int i = 0; i < _nodes.getLength(); i++ )
		{
			if( _nodes.item( i ).getNodeType() != Node.ELEMENT_NODE )
				continue;
			
			Element _element = (Element) _nodes.item( i );
			
			if( _element.getTagName().equals( "id" ) )
				feed.setID( _element.getFirstChild().getNodeValue() );
			
			if( _element.getTagName().equals( "title" ) )
				feed.setTitle( _element.getFirstChild().getNodeValue() );
			
			if( _element.getTagName().equals( "updated" ) )
				feed.setUpdated( _element.getFirstChild().getNodeValue() );
			
			if( _element.getTagName().equals( "author" ) )
				feed.addAuthor( Author.parse( _element ) );
			
			if( _element.getTagName().equals( "entry" ) )
				feed.addEntry( Entry.parse( _element ) );
		}
		
		
		return feed;
	}
	
	
	
	public Document asDocument ()
		throws ParserConfigurationException, TransformerException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.newDocument();
		
		Element feed = document.createElement( "feed" );
		feed.setAttribute( "xmlns", "http://www.w3.org/2005/Atom" );
		
		document.appendChild( feed );
		
		addElements( document, feed );
		
		return document;
	}
	
	
	protected void addElements (Document document, Element feed)
		throws TransformerException
	{
		// <title>
		Element title = document.createElement( "title" );
		title.appendChild( document.createTextNode( String.valueOf( getTitle() ) ) );
		feed.appendChild( title );
		
		
		
		// <link>
		Iterator<Link> links = getLinks().iterator();
		while( links.hasNext() )
			feed.appendChild( links.next().asElement( document ) );
		
		
		
		// <updated>
		Element updated = document.createElement( "updated" );
		updated.appendChild( document.createTextNode( String.valueOf( getUpdated() ) ) );
		feed.appendChild( updated );
		
		
		
		// <author>
		Iterator<Author> authors = getAuthors().iterator();
		while( authors.hasNext() )
			feed.appendChild( authors.next().asElement( document ) );
		
		
		
		// <generator>
		if( getGenerator() != null )
			feed.appendChild( getGenerator().asElement( document ) );
		
		// <id>
		Element id = document.createElement( "id" );
		id.appendChild( document.createTextNode( String.valueOf( getID() ) ) );
		feed.appendChild( id );
		
		
		
		// <category>
		if( getCategory() != null )
			feed.appendChild( getCategory().asElement( document ) );
		
		
		
		// <contributor>
		Iterator<Contributor> contributors = getContributors().iterator();
		while( contributors.hasNext() )
			feed.appendChild( contributors.next().asElement( document ) );
		
		
		
		addEntries( feed, document );
	}
	
	protected void addEntries (Element feed, Document document)
		throws TransformerException
	{
		// <entry>
		Iterator<Entry> entries = getEntries().iterator();
		while( entries.hasNext() )
			feed.appendChild( asElement( entries.next(), document ) );
	}
	
	protected Element asElement (Entry entry, Document document)
		throws TransformerException
	{
		return entry.asElement( document );
	}
}
