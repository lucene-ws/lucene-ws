package net.lucenews.atom;

import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import javax.xml.transform.*;
import net.lucenews.*;

public class Feed extends Base {
    
    private List<Entry> entries;
    private String      icon;
    private String      logo;
    private String      rights;
    private Generator   generator;
    private String      subtitle;
    
    
    public Feed () {
        this( null, null, null );
    }
    
    public Feed (String id, String title, String updated) {
        setID( id );
        setTitle( title );
        setUpdated( updated );
        setEntries( new LinkedList<Entry>() );
    }
    
    
    
    
    
    
    
    public List<Entry> getEntries () {
        return entries;
    }
    
    public void setEntries (List<Entry> entries) {
        this.entries = entries;
    }
    
    public void addEntry (Entry entry) {
        entries.add( entry );
    }
    
    public Generator getGenerator () {
        return generator;
    }
    
    public void setGenerator (Generator generator) {
        this.generator = generator;
    }
    
    
    
    public static Feed parse (Document document)
        throws
            TransformerConfigurationException, TransformerException,
            ParserConfigurationException, AtomParseException
    {
        Feed feed = new Feed();
        
        Element _feed = document.getDocumentElement();
        
        NodeList _nodes = _feed.getChildNodes();
        for (int i = 0; i < _nodes.getLength(); i++) {
            if ( _nodes.item( i ).getNodeType() != Node.ELEMENT_NODE ) {
                continue;
            }
            
            Element _element = (Element) _nodes.item( i );
            
            if ( _element.getTagName().equals( "id" ) ) {
                feed.setID( _element.getFirstChild().getNodeValue() );
            }
            
            if ( _element.getTagName().equals( "title" ) ) {
                feed.setTitle( _element.getFirstChild().getNodeValue() );
            }
            
            if ( _element.getTagName().equals( "updated" ) ) {
                feed.setUpdated( _element.getFirstChild().getNodeValue() );
            }
            
            if ( _element.getTagName().equals( "author" ) ) {
                feed.addAuthor( Author.parse( _element ) );
            }
            
            if ( _element.getTagName().equals( "entry" ) ) {
                feed.addEntry( Entry.parse( _element ) );
            }
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
        if ( getTitle() != null ) {
            Element title = document.createElement("title");
            title.appendChild( document.createTextNode( getTitle() ) );
            feed.appendChild( title );
        }
        
        // <link>
        Iterator<Link> links = getLinks().iterator();
        while ( links.hasNext() ) {
            feed.appendChild( links.next().asElement( document ) );
        }
        
        // <updated>
        if ( getUpdated() != null ) {
            Element updated = document.createElement("updated");
            updated.appendChild( document.createTextNode( getUpdated() ) );
            feed.appendChild( updated );
        }
        
        // <author>
        Iterator<Author> authors = getAuthors().iterator();
        while ( authors.hasNext() ) {
            feed.appendChild( authors.next().asElement( document ) );
        }
        
        // <generator>
        if ( getGenerator() != null ) {
            feed.appendChild( getGenerator().asElement( document ) );
        }
        
        // <id>
        if ( getID() != null ) {
            Element id = document.createElement("id");
            id.appendChild( document.createTextNode( getID() ) );
            feed.appendChild( id );
        }
        
        // <category>
        if ( getCategory() != null ) {
            feed.appendChild( getCategory().asElement( document ) );
        }
        
        // <contributor>
        Iterator<Contributor> contributors = getContributors().iterator();
        while ( contributors.hasNext() ) {
            feed.appendChild( contributors.next().asElement( document ) );
        }
        
        addEntries( feed, document );
    }
    
    protected void addEntries (Element feed, Document document)
        throws TransformerException
    {
        // <entry>
        Iterator<Entry> entries = getEntries().iterator();
        while ( entries.hasNext() ) {
            feed.appendChild( asElement( entries.next(), document ) );
        }
    }
    
    protected Element asElement (Entry entry, Document document)
        throws TransformerException
    {
        return entry.asElement( document );
    }
}
