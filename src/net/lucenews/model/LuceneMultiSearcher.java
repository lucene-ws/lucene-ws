package net.lucenews.model;

import java.io.*;
import org.apache.lucene.document.*;
import org.apache.lucene.search.*;



public class LuceneMultiSearcher extends MultiSearcher
{
    private String subSearcherField;
    
    public LuceneMultiSearcher (Searchable[] searchables, String subSearcherField)
        throws IOException
    {
        super( searchables );
        this.subSearcherField = subSearcherField;
    }
    
    public Document doc (int i)
        throws IOException
    {
        Document document = super.doc( i );
        
        // Ensure fields are not already existing
        document.removeFields( subSearcherField );
        
        // Add appropriate field
        document.add(
            new Field(
                subSearcherField,
                String.valueOf( subSearcher( i ) ),
                Field.Store.YES,
                Field.Index.NO
            )
        );
        
        return document;
    }
    
    @Deprecated
    public Explanation explain (Query query, int doc) throws IOException {
        return super.explain( query, doc );
    }
    
    @Deprecated
    public void search (Query query, Filter filter, HitCollector results) throws IOException {
        super.search( query, filter, results );
    }
    
    @Deprecated
    public TopDocs search (Query query, Filter filter, int n) throws IOException {
        return super.search( query, filter, n );
    }
    
    @Deprecated
    public TopFieldDocs search (Query query, Filter filter, int n, Sort sort) throws IOException {
        return super.search( query, filter, n, sort );
    }
    
}
