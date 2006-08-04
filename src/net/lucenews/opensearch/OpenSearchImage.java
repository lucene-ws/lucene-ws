package net.lucenews.opensearch;

import org.w3c.dom.*;

public class OpenSearchImage {
    
    private String  url;
    private Integer height;
    private Integer width;
    private String  type;
    
    
    
    public OpenSearchImage () {
    }
    
    public OpenSearchImage (String url) {
        setUrl( url );
    }
    
    
    
    public String getUrl () {
        return url;
    }
    
    public void setUrl (String url) {
        this.url = url;
    }
    
    
    
    public Integer getHeight () {
        return height;
    }
    
    public void setHeight (Integer height) {
        this.height = height;
    }
    
    
    
    public Integer getWidth () {
        return width;
    }
    
    public void setWidth (Integer width) {
        this.width = width;
    }
    
    
    
    public String getType () {
        return type;
    }
    
    public void setType (String type) {
        this.type = type;
    }
    
    
    
    
    public static OpenSearchImage asOpenSearchImage (Element element) {
        OpenSearchImage image = new OpenSearchImage();
        
        String url = element.getChildNodes().item( 0 ).getNodeValue();
        
        String width = element.getAttribute("width");
        if ( width != null ) {
            image.setWidth( Integer.valueOf( width ) );
        }
        
        String height = element.getAttribute("height");
        if ( height != null ) {
            image.setWidth( Integer.valueOf( height ) );
        }
        
        String type = element.getAttribute("type");
        if ( type != null ) {
            image.setType( type );
        }
        
        return image;
    }
    
    
    
    /**
     * Transforms the OpenSearch image into a DOM Element.
     */
    
    public Element asElement (Document document) throws OpenSearchException {
        return asElement( document, OpenSearch.getDefaultMode() );
    }
    
    public Element asElement (Document document, OpenSearch.Mode mode) throws OpenSearchException {
        Element element = document.createElement("Image");
        
        if (getUrl() != null) {
            element.appendChild( document.createTextNode( getUrl() ) );
        }
        
        if (getHeight() != null) {
            element.setAttribute( "height", getHeight().toString() );
        }
        
        if (getWidth() != null) {
            element.setAttribute( "width", getWidth().toString() );
        }
        
        if (getType() != null) {
            element.setAttribute( "type", getType().toString() );
        }
        
        return element;
    }
    
}
