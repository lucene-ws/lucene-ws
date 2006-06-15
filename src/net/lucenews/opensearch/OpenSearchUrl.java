package net.lucenews.opensearch;

import java.util.*;
import org.w3c.dom.*;

public class OpenSearchUrl {
    
    private String template;
    private String type;
    private String method;
    private Map<String,String> params;
    
    
    public OpenSearchUrl () {
        params = new LinkedHashMap<String,String>();
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
    
    
    
    public Element asElement (Document document) throws Exception {
        Element element = document.createElement("Url");
        
        Iterator<Map.Entry<String,String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,String> entry = iterator.next();
            Element param = document.createElement("Param");
            
            if (entry.getKey() == null) {
                throw new Exception("Param name cannot be null");
            }
            param.setAttribute("name", entry.getKey());
            
            if (entry.getKey() == null) {
                throw new Exception("Param value cannot be null");
            }
            param.setAttribute("value", entry.getValue());
            
            element.appendChild( param );
        }
        
        return element;
    }
    
}
