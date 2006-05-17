package net.lucenews.atom;

import javax.xml.transform.*;
import java.util.*;
import org.w3c.dom.*;
import net.lucenews.controller.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;

public class Entry extends Base {
    
    private Text m_summary;
    private Content m_content;
    
    
    private Properties properties;
    private Map<String,String> namespaces;
    
    
    public Entry () {
        properties = new Properties();
        namespaces = new HashMap<String,String>();
    }
    
    
    public Text getSummary () {
        return m_summary;
    }
    
    public void setSummary (String summary) {
        setSummary( new Text( summary ) );
    }
    
    public void setSummary (Text summary) {
        m_summary = summary;
    }
    
    public Content getContent () {
        return m_content;
    }
    
    public void setContent (Content content) {
        m_content = content;
    }
    
    
    public void setProperty (String name, String value) {
        properties.setProperty( name, value );
    }
    
    public void setPropertyNS (String namespaceURI, String qualifiedName, String value) {
        properties.setProperty( qualifiedName, value );
        namespaces.put( qualifiedName, namespaceURI );
    }
    
    
    public Document asDocument () throws TransformerException, ParserConfigurationException {
        Document document = XMLController.newDocument();
        
        document.appendChild( asElement( document ) );
        
        return document;
    }
    
    
    public static Entry parse (Document document)
        throws TransformerConfigurationException, TransformerException, AtomParseException
    {
        return parse( document.getDocumentElement() );
    }
    
    public static Entry parse (Element e)
        throws TransformerConfigurationException, TransformerException, AtomParseException
    {
        if (!e.getTagName().equals( "entry" )) {
            throw new AtomParseException("Invalid tag name for Atom entry: " + e.getTagName());
        }
        
        Entry entry = new Entry();
        
        NodeList nodes = e.getChildNodes();
        
        for( int i = 0; i < nodes.getLength(); i++ ) {
            if (nodes.item( i ).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            
            Element element = (Element) nodes.item( i );
            
            if (element.getTagName().equals( "id" )) {
                entry.setID( element.getFirstChild().getNodeValue() );
            }
            
            if (element.getTagName().equals( "title" )) {
                entry.setTitle( element.getFirstChild().getNodeValue() );
            }
            
            if (element.getTagName().equals( "updated" )) {
                entry.setUpdated( element.getFirstChild().getNodeValue() );
            }
            
            if (element.getTagName().equals( "content" )) {
                entry.setContent( Content.parse( element ) );
            }
        }
        
        return entry;
    }
    
    
    public Element asElement (Document document) throws TransformerException {
        Element entry = document.createElement( "entry" );
        
        
        
        entry.setAttribute( "xmlns", "http://www.w3.org/2005/Atom" );
        
        
        
        // <title>
        Element title = document.createElement( "title" );
        title.appendChild( document.createTextNode( String.valueOf( getTitle() ) ) );
        entry.appendChild( title );
        
        
        
        // <link>
        Iterator<Link> links = getLinks().iterator();
        while (links.hasNext()) {
            entry.appendChild( links.next().asElement( document ) );
        }
        
        
        
        // <updated>
        Element updated = document.createElement( "updated" );
        updated.appendChild( document.createTextNode( String.valueOf( getUpdated() ) ) );
        entry.appendChild( updated );
        
        
        
        // <author>
        Iterator<Author> authors = getAuthors().iterator();
        while (authors.hasNext()) {
            entry.appendChild( authors.next().asElement( document ) );
        }
        
        
        
        // <id>
        Element id = document.createElement( "id" );
        id.appendChild( document.createTextNode( String.valueOf( getID() ) ) );
        entry.appendChild( id );
        
        
        
        // <category>
        if (getCategory() != null) {
            entry.appendChild( getCategory().asElement( document ) );
        }
        
        
        
        // <contributor>
        Iterator<Contributor> contributors = getContributors().iterator();
        while (contributors.hasNext()) {
            entry.appendChild( contributors.next().asElement( document ) );
        }
        
        
        // <summary>
        if (getSummary() != null) {
            entry.appendChild( getSummary().asElement( document, "summary" ) );
        }
        
        
        // Additional properties
        Enumeration names = properties.propertyNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            
            Element property = null;
            if (namespaces.containsKey( name )) {
                property = document.createElementNS( namespaces.get( name ), name );
            }
            else {
                property = document.createElement( name );
            }
            
            property.appendChild( document.createTextNode( properties.getProperty( name ) ) );
            
            entry.appendChild( property );
        }
        
        
        // <content>
        if (getContent() != null) {
            entry.appendChild( getContent().asElement( document ) );
        }
        
        
        
        return entry;
    }
    
}
