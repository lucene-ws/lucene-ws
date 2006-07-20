package net.lucenews.opensearch;

import java.util.*;
import org.w3c.dom.*;

public class OpenSearchUrl {
    
    private String template;
    private String type;
    private String method;
    private Map<String,String> params;
    private Map<String,String> namespaces;
    
    
    public OpenSearchUrl () {
        params     = new LinkedHashMap<String,String>();
        namespaces = new LinkedHashMap<String,String>();
    }
    
    
    
    public String getTemplate () {
        return template;
    }
    
    public void setTemplate (String template) {
        this.template = template;
    }
    
    
    
    public String getType () {
        return type;
    }
    
    public void setType (String type) {
        this.type = type;
    }
    
    
    
    public String getMethod () {
        return method;
    }
    
    public void setMethod (String method) {
        this.method = method;
    }
    
    
    
    public void addParam (String name, String value) {
        params.put( name, value );
    }
    
    public String removeParam (String name) {
        return params.remove( name );
    }
    
    public Map<String,String> getParams () {
        return params;
    }
    
    
    
    public void setNamespace (String namespace, String uri) {
        namespaces.put( namespace, uri );
    }
    
    
    
    public Element asElement (Document document, OpenSearch.Format format) throws OpenSearchException {
        return asElement(document, format, OpenSearch.STRICT);
    }
    
    public Element asElement (Document document, OpenSearch.Format format, OpenSearch.Mode mode) throws OpenSearchException {
        Element element = document.createElement("Url");
        
        // type
        if (getType() != null) {
            element.setAttribute("type", getType());
        }
        
        // method
        if (getMethod() != null) {
            element.setAttribute("method", getMethod());
        }
        
        // template
        if (getTemplate() != null) {
            element.setAttribute("template", getTemplate());
        }
        
        // namespaces
        Iterator<Map.Entry<String,String>> namespaceIterator = namespaces.entrySet().iterator();
        while (namespaceIterator.hasNext()) {
            Map.Entry<String,String> namespace = namespaceIterator.next();
            element.setAttribute( "xmlns:"+namespace.getKey(), namespace.getValue() );
        }
        
        Iterator<Map.Entry<String,String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,String> entry = iterator.next();
            Element param = document.createElement("Param");
            
            if (entry.getKey() == null) {
                if (mode == OpenSearch.STRICT) {
                    throw new OpenSearchException("Param name cannot be null");
                }
                else {
                    continue;
                }
            }
            param.setAttribute("name", entry.getKey());
            
            if (entry.getKey() == null) {
                if (mode == OpenSearch.STRICT) {
                    throw new OpenSearchException("Param value cannot be null");
                }
                else {
                    param.setAttribute("value", "");
                }
            }
            else {
                param.setAttribute("value", entry.getValue());
            }
            
            element.appendChild( param );
        }
        
        return element;
    }
    
}
