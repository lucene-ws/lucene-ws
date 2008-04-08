package net.lucenews.atom;

import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class TextContent extends Content {
    
    private Text text;
    
    public TextContent (Text text) {
        this.text = text;
    }
    
    public TextContent (String text) {
        this( new Text( text, "text" ) );
    }
    
    public TextContent (String text, String type) {
        this( new Text( text, type ) );
    }
    @Override
    public String getType () {
        return text.getType();
    }
    
    public Text getText () {
        return text;
    }
    @Override
    public Element asElement (Document document) {
        Element element = super.asElement( document );
        element.appendChild( document.createTextNode( getText().toString() ) );
        return element;
    }
    
    public Node[] asNodes (Document document) {
        Text text = getText();
        
        Element parent = document.createElement( "content" );
        if( getType() != null )
            parent.setAttribute( "type", getType() );
        
        if( text == null || text.toString() == null )
            return new Node[]{ parent };
        
        parent.appendChild( document.createTextNode( text.toString() ) );
        
        return new Node[]{ parent };
    }
    
    public Document asDocument ()
        throws ParserConfigurationException, TransformerConfigurationException, TransformerException
    {
        if( !isType( "xhtml" ) )
            return null;
        
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        
        transformer.transform( new StreamSource( new StringReader( getText().toString() ) ), new DOMResult( document ) );
        
        return document;
    }
    
}
