package net.lucenews.atom;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.apache.commons.codec.binary.*;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.*;

public abstract class Content {
    
    private String type;
    
    
    /**
     * Default constructors
     */
    
    
    
    /**
     * Text
     */
    
    public static Content newInstance (String value) {
        return new TextContent( value, "text" );
    }
    
    public static TextContent text (String text) {
        return new TextContent( text, "text" );
    }
    
    public static TextContent html (String html) {
        return new TextContent( html, "html" );
    }
    
    public static NodeContent xhtml (Document document) {
        return new NodeContent( document, "xhtml" );
    }
    
    public static NodeContent xhtml (Element element) {
        return new NodeContent( element, "xhtml" );
    }
    
    public static NodeContent xhtml (NodeList nodes) {
        return new NodeContent( nodes, "xhtml" );
    }
    
    
    
    /**
     * DOM
     */
    
    public static Content newInstance (NodeList value) {
        return new NodeContent( value, "text/xml" );
    }
    
    public static Content newInstance (Node value) {
        return new NodeContent( value, "text/xml" );
    }

    public static NodeContent xml (NodeList nodes) {
        return new NodeContent( nodes, "xml" );
    }
    
    public static NodeContent xml (Document document) {
        return new NodeContent( document, "xml" );
    }
    
    public static NodeContent xml (Element element) {
        return new NodeContent( element, "xml" );
    }
    
    
    
    /**
     * URI
     */
    
    public static URIContent uri (URI uri) {
        return new URIContent( uri, "src" );
    }
    
    public static URIContent uri (String uri) {
        return new URIContent( uri, "src" );
    }

    
    
    
    
    
    /**
     * Type accessors
     */
    
    public boolean hasType () {
        return getType() != null && getType().length() > 0;
    }
    
    public boolean isType (String type) {
        return hasType() && getType().toLowerCase().trim().equals( type );
    }
    
    public String getType () {
        return type;
    }
    
    public void setType (String type) {
        this.type = type;
    }
    
    
    
    
    
    
    
    
    
    
    /**
     * The skeleton method for sub classes to take advantage of.
     */
    
    public Element asElement (Document document)
    {
        Element content = document.createElement( "content" );
        
        if( getType() != null )
            content.setAttribute( "type", getType() );
        
        return content;
    }
    
    
    
    /**
     * Transforms a list of nodes into a String
     */
    
    protected static String toString (NodeList nodes)
        throws TransformerConfigurationException, TransformerException
    {
        StringBuffer buffer = new StringBuffer();
        
        for( int i = 0; i < nodes.getLength(); i++ )
            buffer.append( toString( nodes.item( i ) ) );
        
        return buffer.toString();
    }
    
    protected static String toString (Node node) {
        switch( node.getNodeType() ) {
            case Node.ELEMENT_NODE:
                return toString( (Element) node );
            case Node.TEXT_NODE:
                return toString( (org.w3c.dom.Text) node );
            default:
                return "";
        }
    }
    
    protected static String toString (Element element) {
        StringBuffer buffer = new StringBuffer();
        buffer.append( "<" );
        buffer.append( element.getTagName() );
        
        NamedNodeMap attributes = element.getAttributes();
        for( int i = 0; i < attributes.getLength(); i++ ) {
            Attr attribute = (Attr) attributes.item( i );
            buffer.append( " " );
            buffer.append( attribute.getName() );
            buffer.append( "=\"" );
            buffer.append( attribute.getValue() );
            buffer.append( "\"" );
        }
        
        if( element.getChildNodes().getLength() > 0 ) {
            buffer.append( ">" );
            try {
                buffer.append( toString( element.getChildNodes() ) );
            }
            catch(Exception e) {
            }
            buffer.append( "</" );
            buffer.append( element.getTagName() );
            buffer.append( ">" );
        }
        else {
            buffer.append( "/>" );
        }
        
        return buffer.toString();
    }
    
    protected static String toString (org.w3c.dom.Text text) {
        return text.getNodeValue();
    }
    
    
    
    protected static Node[] toNodes (NodeList nodeList) {
        Node[] nodes = new Node[ nodeList.getLength() ];
        for( int i = 0; i < nodeList.getLength(); i++ )
            nodes[ i ] = nodeList.item( i );
        return nodes;
    }
    
    
    
    protected static Node[] childNodes (Element element) {
        String mode = element.getAttribute( "mode" );
        
        if( mode != null && mode.equals( "base64" ) ) {
            try {
                String content = childNodesToString( element );
                Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                Transformer t = TransformerFactory.newInstance().newTransformer();
                
                t.transform( new StreamSource( new StringReader( "<wrapper>" + content + "</wrapper>" ) ), new DOMResult( d ) );
                
                NodeList childNodes = d.getDocumentElement().getChildNodes();
                return toNodes( childNodes );
            }
            catch(Exception e) {
                return new Node[]{};
            }
        }
        else {
            return toNodes( element.getChildNodes() );
        }
    }
    
    
    
    /**
     * Extracts the string representation of the child nodes.
     * Adapts to base64 encoded data whenever present.
     */
    
    protected static String childNodesToString (Element element)
        throws TransformerConfigurationException, TransformerException, AtomParseException
    {
        NodeList childNodes = element.getChildNodes();
        
        String mode = element.getAttribute( "mode" );
        if( mode != null && mode.equals( "base64" ) )
            return childNodesToStringBase64( element );
        
        return toString( childNodes );
    }
    
    protected static String childNodesToStringBase64 (Element element)
        throws TransformerConfigurationException, TransformerException, AtomParseException
    {
        try {
            return new String( new Base64().decode( toString( element.getChildNodes() ).getBytes() ), "UTF-8" );
        }
        catch(UnsupportedEncodingException uee) {
            throw new AtomParseException( uee.getMessage() );
        }
    }
    
    
    
    
    protected static String clean (String value) {
        if( value == null )
            return null;
        return value.toLowerCase().trim();
    }
    
    
    
    /**
     * Parses a DOM element for Atom content. Follows the specifications outlined
     * at http://www.atomenabled.org/developers/syndication/
     * Feel free to follow along at home...
     */
    
    public static Content parse (Element element)
        throws TransformerConfigurationException, TransformerException, AtomParseException
    {
        System.err.println( "Parsing element: " + element );
        try {
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(element),new StreamResult(System.err));
        }
        catch(Exception eeEE){}
        
        if( !element.getTagName().equals( "content" ) )
            throw new AtomParseException( "Invalid content tag name: \"" + element.getTagName() + "\"" );
        
        String type = clean( element.getAttribute( "type" ) );
        String src  = clean( element.getAttribute( "src" ) );
        
        
        
        /**
         * "In the most common case, the type attribute is either text, html, xhtml,
         *  in which case the content element is defined identically to other text
         *  constructs..."
         */
        
        if( type != null ) {
            
            if( type.equals( "text" ) || type.equals( "html" ) )
                return new TextContent( childNodesToString( element ), type );
            
            if( type.equals( "xhtml" ) )
                return new NodeContent( childNodes( element ), "xhtml" );
            
        }
        
        
        
        
        /**
         * "Otherwise, if the src attribute is present, it represents the URI
         *  of where the content can be found. The type attribute, if present,
         *  is the media type of the content."
         */
        
        if( src != null )
            if( !src.equals("") )
                return new URIContent( src, type );
        
        
        
        
        /**
         * "Otherwise, if the type attribute ends in +xml or /xml, then an xml
         *  document of this type is contained inline."
         */
        
        if( type != null )
            if( type.endsWith("+xml") || type.endsWith("/xml") )
                return new net.lucenews.atom.NodeContent( element.getChildNodes() );
        
        
        
        
        /**
         * "Otherwise, if the type attribute starts with text, then an escaped
         *  document of this type is contained inline."
         */
        
        if( type != null )
            if( type.startsWith("text") )
                return new TextContent( childNodesToString( element ) );
        
        
        
        
        /**
         * "Otherwise, a base64 encoded document of the indicated media type
         *  is contained inline."
         */
        
        return new DocumentContent( childNodesToStringBase64( element ).getBytes() );
    }
    
}
