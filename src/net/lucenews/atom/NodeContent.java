package net.lucenews.atom;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import org.w3c.dom.*;

public class NodeContent extends Content {
    
    private Node[] nodes;
    
    public NodeContent (Node node) {
        this.nodes = new Node[]{ node };
    }
    
    public NodeContent (Node node, String type) {
        this.nodes = new Node[]{ node };
        setType( type );
    }
    
    public NodeContent (NodeList nodes) {
        this.nodes = new Node[ nodes.getLength() ];
        for( int i = 0; i < nodes.getLength(); i++ )
            this.nodes[ i ] = nodes.item( i );
    }
    
    public NodeContent (NodeList nodes, String type) {
        this.nodes = new Node[ nodes.getLength() ];
        for( int i = 0; i < nodes.getLength(); i++ )
            this.nodes[ i ] = nodes.item( i );
        setType( type );
    }
    
    public NodeContent (Node[] nodes) {
        this.nodes = nodes;
    }
    
    public NodeContent (Node[] nodes, String type) {
        this.nodes = nodes;
        setType( type );
    }
    
    public Node[] getNodes () {
        return nodes;
    }
    
    public Node getNode () {
        return nodes[ 0 ];
    }
    
    public Element asElement (Document document) {
        Element element = super.asElement( document );
        
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            for( int i = 0; i < nodes.length; i++ )
                transformer.transform( new DOMSource(nodes[i]), new DOMResult(element) );
        }
        catch(Exception e) {
        }
        
        return element;
    }
    
    public Node[] asNodes (Document document) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Element parent = document.createElement( "content" );
            if( getType() != null )
                parent.setAttribute( "type", getType() );
            for( int i = 0; i < nodes.length; i++ )
                transformer.transform( new DOMSource(nodes[i]), new DOMResult(parent) );
            NodeList nodeList = parent.getChildNodes();
            Node[] _nodes = new Node[ nodeList.getLength() ];
            for( int i = 0; i < _nodes.length; i++ )
                _nodes[ i ] = nodeList.item( i );
            return _nodes;
        }
        catch(Exception e) {
            return new Node[]{};
        }
    }
    
}
