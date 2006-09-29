package net.lucenews.opensearch;

import org.w3c.dom.*;

public class OpenSearchParameter {
    
    private String name;
    private String value;
    private Integer minimum;
    private Boolean minimumUnbounded;
    private Integer maximum;
    private Boolean maximumUnbounded;
    
    
    public OpenSearchParameter () {
    }
    
    public OpenSearchParameter (String name, String value) {
        setName( name );
        setValue( value );
    }
    
    
    public String getName () {
        return name;
    }
    
    public void setName (String name) {
        this.name = name;
    }
    
    
    public String getValue () {
        return value;
    }
    
    public void setValue (String value) {
        this.value = value;
    }
    
    
    public Integer getMinimum () {
        return minimum;
    }
    
    public void setMinimum (Integer minimum) {
        this.minimum = minimum;
    }
    
    public void setMinimum (String minimum) {
        if ( minimum.equals("*") ) {
            minimumUnbounded = true;
            setMinimum( (Integer) null );
        }
        else {
            minimumUnbounded = false;
            setMinimum( Integer.valueOf( minimum ) );
        }
    }
    
    
    public Integer getMaximum () {
        return maximum;
    }
    
    public void setMaximum (Integer maximum) {
        this.maximum = maximum;
    }
    
    public void setMaximum (String maximum) {
        if ( maximum.equals("*") ) {
            maximumUnbounded = true;
            setMaximum( (Integer) null );
        }
        else {
            maximumUnbounded = false;
            setMaximum( Integer.valueOf( maximum ) );
        }
    }
    
    public static OpenSearchParameter asOpenSearchParameter (Element element) {
        OpenSearchParameter parameter = new OpenSearchParameter();
        
        parameter.setName( element.getAttribute("name") );
        parameter.setValue( element.getAttribute("value") );
        parameter.setMinimum( element.getAttribute("minimum") );
        parameter.setMaximum( element.getAttribute("maximum") );
        
        return parameter;
    }
    
    public Element asElement (Document document, OpenSearch.Format format, OpenSearch.Mode mode) throws OpenSearchException {
        Element parameter = document.createElementNS( "http://a9.com/-/spec/opensearch/extensions/parameters/1.0/", "parameters:Parameter" );
        
        if ( getName() == null ) {
            if ( mode == OpenSearch.STRICT ) {
                throw new OpenSearchException("Parameter name cannot be null");
            }
            else {
                return parameter;
            }
        }
        
        // set the name
        parameter.setAttribute( "name", getName() );
        
        if ( getValue() == null ) {
            if ( mode == OpenSearch.STRICT ) {
                throw new OpenSearchException("Parameter value cannot be null");
            }
            else {
                parameter.setAttribute( "value", "" );
            }
        }
        else {
            parameter.setAttribute( "value", getValue() );
        }
        
        return parameter;
    }
    
    public boolean equals (Object other) {
        if ( other == null ) {
            return false;
        }
        
        if ( other == this ) {
            return true;
        }
        
        if ( !( other instanceof OpenSearchParameter ) ) {
            return false;
        }
        
        OpenSearchParameter parameter = (OpenSearchParameter) other;
        
        if ( parameter.getName() == null ) {
            return false;
        }
        
        if ( getName() == null ) {
            return false;
        }
        
        if ( getName().equals( parameter.getName() ) ) {
            return true;
        }
        
        return false;
    }
}
