package net.lucenews.opensearch;

import org.w3c.dom.*;

public class OpenSearchPerson {
    
    private String name;
    private String uri;
    private String email;
    private String role;
    
    
    
    public OpenSearchPerson () {
    }
    
    
    
    public String getName () {
        return name;
    }
    
    public void setName (String name) {
        this.name = name;
    }
    
    
    
    public String getUri () {
        return uri;
    }
    
    public void setUri (String uri) {
        this.uri = uri;
    }
    
    
    
    public String getEmail () {
        return email;
    }
    
    public void setEmail (String email) {
        this.email = email;
    }
    
    
    
    public String getRole () {
        return role;
    }
    
    public void setRole (String role) {
        this.role = role;
    }
    
    
    
    public Element asElement (Document document, OpenSearch.Format format) throws OpenSearchException {
        return asElement(document, format, OpenSearch.STRICT);
    }
    
    
    public Element asElement (Document document, OpenSearch.Format format, OpenSearch.Mode mode) throws OpenSearchException {
        
        // Atom
        if (format == OpenSearch.ATOM) {
            Element element = null;
            if (getRole() == null) {
                element = document.createElement("person");
            }
            else {
                element = document.createElement(getRole());
            }
            
            // name
            if (getName() != null) {
                element.appendChild( asElement( document, "name", getName() ) );
            }
            
            // uri
            if (getUri() != null) {
                element.appendChild( asElement( document, "uri", getUri() ) );
            }
            
            // email
            if (getEmail() != null) {
                element.appendChild( asElement( document, "email", getEmail() ) );
            }
            
            return element;
        }
        
        
        // RSS
        if (format == OpenSearch.RSS) {
            Element element = null;
            if (getRole() == null) {
                element = document.createElement("person");
            }
            else {
                element = document.createElement(getRole());
            }
            
            StringBuffer buffer = new StringBuffer();
            
            if (getEmail() != null) {
                buffer.append( getEmail() );
            }
            
            if (getName() != null) {
                buffer.append( " (" + getName() + ")" );
            }
            
            element.appendChild( document.createTextNode( buffer.toString() ) );
            
            return element;
        }
        
        
        throw new OpenSearchException("Unknown format");
    }
    
    
    protected Element asElement (Document document, String name, Object value) throws OpenSearchException {
        Element element = document.createElement(name);
        element.appendChild( document.createTextNode(value.toString()) );
        return element;
    }
    
    protected Element asElementNS (Document document, String namespaceURI, String qualifiedName, Object value) throws OpenSearchException {
        Element element = document.createElementNS(namespaceURI, qualifiedName);
        element.appendChild( document.createTextNode(value.toString()) );
        return element;
    }
    
}
