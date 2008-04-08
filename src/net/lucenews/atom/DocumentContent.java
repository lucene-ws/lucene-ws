package net.lucenews.atom;

import java.io.*;
import org.w3c.dom.*;

public class DocumentContent extends Content {
    
    protected byte[] bytes;
    
    public DocumentContent (byte[] bytes) {
        this.bytes = bytes;
    }
    
    public DocumentContent (byte[] bytes, String type) {
        this.bytes = bytes;
        setType( type );
    }
    
    
    
    public byte[] getBytes () {
        return bytes;
    }
    
    public InputStream getInputStream () {
        return new ByteArrayInputStream( bytes );
    }
    @Override
    public String toString () {
        return new String( bytes );
    }
    @Override
    public Element asElement (Document document) {
        Element element = super.asElement( document );
        element.appendChild( document.createTextNode( toString() ) );
        return element;
    }
    
}
