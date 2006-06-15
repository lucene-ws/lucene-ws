package net.lucenews.opensearch;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import org.w3c.dom.*;

public class OpenSearchText {
    
    // Various content containers
    private String content_string;
    private Node[] content_nodes;
    
    private String type;
    private String role;
    
    
    public OpenSearchText () {
        content_nodes = new Node[]{};
    }
    
    public OpenSearchText (String content) {
        this();
        setContent( content );
    }
    
    public OpenSearchText (Node content) {
        this();
        setContent( content );
    }
    
    public OpenSearchText (NodeList content) {
        this();
        setContent( content );
    }
    
    
    
    public void setContent (String content) {
        content_string = content;
        setType("text");
    }
    
    public void setContent (Node... contents) {
        content_nodes = contents;
        
        if (contents.length == 1) {
            Node content = contents[ 0 ];
            
            if (content.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) content;
                
                String namespaceURI = element.getAttribute("xmlns");
                if (namespaceURI != null && namespaceURI.equals("http://www.w3.org/1999/xhtml")) {
                    setType("xhtml");
                }
                else {
                    setType("xml");
                }
            }
        }
    }
    
    public void setContent (NodeList content) {
        Node[] nodes = new Node[ content.getLength() ];
        for (int i = 0; i < content.getLength(); i++) {
            nodes[i] = content.item(i);
        }
        setContent( nodes );
    }
    
    
    
    public String getType () {
        return type;
    }
    
    public void setType (String type) {
        this.type = type;
    }
    
    
    
    
    public String getRole () {
        return role;
    }
    
    public void setRole (String role) {
        this.role = role;
    }
    
    
    
    public Element asElement (Document document, OpenSearch.Format format)
        throws OpenSearchException
    {
        return asElement(document, format, OpenSearch.STRICT);
    }
    
    public Element asElement (Document document, OpenSearch.Format format, OpenSearch.Mode mode)
        throws OpenSearchException
    {
        
        /**
         * Atom
         */
        
        if (format == OpenSearch.ATOM) {
            Element element = null;
            
            if (getRole() != null) {
                element = document.createElement( getRole() );
            }
            else {
                element = document.createElement("content");
            }
            
            
            // type
            if (getType() != null) {
                element.setAttribute("type", getType());
            }
            
            
            // content
            if (getType().equals("xhtml") || getType().equals("xml")) {
                try {
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    for (int i = 0; i < content_nodes.length; i++) {
                        transformer.transform( new DOMSource(content_nodes[i]), new DOMResult(element) );
                    }
                }
                catch (TransformerConfigurationException tce) {
                    throw new OpenSearchException(tce.getMessage());
                }
                catch (TransformerException te) {
                    throw new OpenSearchException(te.getMessage());
                }
            }
            
            
            return element;
        }
        
        
        /**
         * RSS
         */
        
        if (format == OpenSearch.RSS) {
            Element element = null;
            
            if (getRole() != null) {
                element = document.createElement( getRole() );
            }
            else {
                element = document.createElement("description");
            }
            
            element.appendChild( document.createTextNode( String.valueOf(content_nodes) ) );
            
            return element;
        }
        
        
        throw new OpenSearchException("Unknown format");
    }
    
}
