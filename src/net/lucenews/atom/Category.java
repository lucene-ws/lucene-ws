package net.lucenews.atom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Category {
    
    
    private String text, scheme, label;
    
    
    
    public Category (String text) {
        this(text, null, null);
    }
    
    public Category (String text, String scheme, String label) {
        setText( text );
        setScheme( scheme );
        setLabel( label );
    }
    
    
    public String getText () {
        return text;
    }
    
    public void setText (String text) {
        this.text = text;
    }
    
    
    public String getScheme () {
        return scheme;
    }
    
    public void setScheme (String scheme) {
        this.scheme = scheme;
    }
    
    
    public String getLabel () {
        return label;
    }
    
    public void setLabel (String label) {
        this.label = label;
    }
    
    
    
    public Element asElement (Document document) {
        Element category = document.createElement("category");
        
        category.appendChild( document.createTextNode( String.valueOf( getText() ) ) );
        
        if (getScheme() != null) {
            category.setAttribute("scheme", getScheme());
        }
        
        if (getLabel() != null) {
            category.setAttribute("label", getLabel());
        }
        
        return category;
    }
    
}
