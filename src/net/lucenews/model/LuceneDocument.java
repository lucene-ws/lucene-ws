package net.lucenews.model;

/**
*
* Basically a clone of the Document class found within the 
* Lucene packages. We couldn't just extend it because Lucene's
* author made its class final. This means we can't take advantage
* of any inheritance.
*
*/

import java.io.*;
import net.lucenews.*;
import net.lucenews.model.event.*;
import net.lucenews.model.exception.*;
import net.lucenews.opensearch.*;

import org.apache.lucene.index.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.*;
import java.util.*;
import org.w3c.dom.*;


public class LuceneDocument {
    
    private Analyzer    analyzer;
    private Document    document;
    private LuceneIndex index;
    private Integer     number;
    private Hits        similarDocumentHits;
    
    
    
    
    public LuceneDocument () {
        setAnalyzer( null );
        setDocument( new Document() );
    }
    
    public LuceneDocument (Document document) {
        setAnalyzer( null );
        setDocument( document );
    }
    
    public LuceneDocument (Analyzer analyzer) {
        setAnalyzer( analyzer );
        setDocument( new Document() );
    }
    
    public LuceneDocument (Document document, Analyzer analyzer) {
        setAnalyzer( analyzer );
        setDocument( document );
    }
    
    
    
    
    public Analyzer getAnalyzer () {
        return analyzer;
    }
    
    public void setAnalyzer (Analyzer analyzer) {
        this.analyzer = analyzer;
    }
    
    
    
    public Document getDocument () {
        return document;
    }
    
    public void setDocument (Document document) {
        this.document = document;
    }
    
    
    
    
    public LuceneIndex getIndex () {
        return index;
    }
    
    public void setIndex (LuceneIndex index) {
        this.index = index;
    }
    
    public void update ()
        throws
        IllegalActionException, InvalidIdentifierException,
        DocumentNotFoundException, InsufficientDataException, IOException
    {
        getIndex().updateDocument(this);
    }
    
    public String getIdentifier () throws InsufficientDataException, IOException {
        return getIndex().getIdentifier( this );
    }
    
    public String getTitle () throws IOException {
        return getIndex().getTitle( this );
    }
    
    public Calendar getLastModified () throws java.text.ParseException, InsufficientDataException, IOException {
        return getIndex().getLastModified( this );
    }
    
    public String getAuthor () throws IOException {
        return getIndex().getAuthor( this );
    }
    
    public OpenSearchText getRights () throws IOException {
        return getIndex().getRights( this );
    }
    
    
    public Integer getNumber () {
        return number;
    }
    
    public void setNumber (Integer number) {
        this.number = number;
    }
    
    
    public Hits getSimilarDocumentHits () {
        return similarDocumentHits;
    }
    
    public void setSimilarDocumentHits (Hits similarDocumentHits) {
        this.similarDocumentHits = similarDocumentHits;
    }
    
    
    
    
    
    public void setBoost (float boost) {
        document.setBoost( boost );
    }
    
    public float getBoost () {
        return document.getBoost();
    }
    
    
    
    
    public void add (Field... fields) {
        for (int i = 0; i < fields.length; i++) {
            document.add( fields[ i ] );
        }
    }
    
    
    
    
    public void removeField (String name) {
        document.removeField( name );
    }
    
    
    
    
    public void removeFields (String name) {
        document.removeFields( name );
    }
    
    
    
    
    public Field getField (String name) {
        return document.getField( name );
    }
    
    
    
    
    public String get (String name) {
        return document.get( name );
    }
    
    
    
    public List<Field> getFields () {
        List<Field> fields  = new ArrayList<Field>();
        
        /**
        
            NOTE: The following lines will only work
            when compiling against Lucene-Core 2.0.1.
            Until that is officially launched, we must
            deal with 2.0.0 API.
        
        List<?>     objects = document.getFields();
        Iterator<?> iterator = objects.iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof Field) {
                fields.add( (Field) object );
            }
        }
        */
        
        Enumeration<?> enumeration = document.fields();
        while (enumeration.hasMoreElements()) {
            Object object = enumeration.nextElement();
            if (object instanceof Field) {
                fields.add( (Field) object );
            }
        }
        
        return fields;
    }
    
    
    
    public Field[] getFields (String name) {
        return document.getFields( name );
    }
    
    
    
    
    public String[] getValues (String name) {
        return document.getValues( name );
    }
    
    
    
    public String toString () {
        return document.toString();
    }
    
    
    
}
