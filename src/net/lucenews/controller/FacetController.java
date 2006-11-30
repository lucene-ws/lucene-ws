package net.lucenews.controller;

import java.util.*;
import net.lucenews.*;
import net.lucenews.atom.*;
import net.lucenews.model.*;
import net.lucenews.model.exception.*;
import net.lucenews.view.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.w3c.dom.*;

public class FacetController extends Controller {
    
    public static void doGet (LuceneContext c) throws Exception {
        LuceneWebService   service  = c.getService();
        LuceneIndexManager manager  = service.getIndexManager();
        LuceneRequest      request  = c.getRequest();
        LuceneResponse     response = c.getResponse();
        LuceneIndex[]      indices  = manager.getIndices( request.getIndexNames() );
        String[]           facets   = request.getFacets();
        
        // Atom feed
        Feed feed = new Feed();
        
        // DOM Document
        org.w3c.dom.Document document = XMLController.newDocument();
        
        // load the readers
        IndexReader[] readers = new IndexReader[ indices.length ];
        for (int i = 0; i < indices.length; i++) {
            readers[ i ] = indices[ i ].getIndexReader();
        }
        MultiReader reader = new MultiReader( readers );
        
        // build the query
        Query query = null;
        if ( c.getOpenSearchQuery() != null && c.getOpenSearchQuery().getSearchTerms() != null ) {
            query = c.getQueryParser().parse( c.getOpenSearchQuery().getSearchTerms() );
        }
        else {
            query = new MatchAllDocsQuery();
        }
        
        // build the filter
        Filter filter = new CachingWrapperFilter( new QueryFilter( query ) );
        if ( c.getFilter() != null ) {
            filter = new CachingWrapperFilter( c.getFilter() );
        }
        BitSet bits = filter.bits( reader );
        
        // build an entry for each facet
        for (String facet : facets) {
            Entry entry = new Entry();
            
            entry.setTitle( facet );
            
            Element div = document.createElement("div");
            div.setAttribute( "xmlns", XMLController.getXHTMLNamespace() );
            
            Element dl = document.createElement("dl");
            
            TermEnum termEnumeration = reader.terms( new Term( facet, "" ) );
            while ( termEnumeration.next() && termEnumeration.term().field().equals( facet ) ) {
                TermDocs termDocuments = reader.termDocs( termEnumeration.term() );
                String name = termEnumeration.term().text();
                
                int count = 0;
                while ( termDocuments.next() ) {
                    if ( bits.get( termDocuments.doc() ) ) {
                        count++;
                    }
                }
                
                // only mention facets with more than one hit
                if ( count > 0 ) {
                    Element dt = document.createElement("dt");
                    dt.appendChild( document.createTextNode( name ) );
                    dl.appendChild( dt );
                    
                    Element dd = document.createElement("dd");
                    dd.appendChild( document.createTextNode( String.valueOf( count ) ) );
                    dl.appendChild( dd );
                }
            }
            
            div.appendChild( dl );
            
            entry.setContent( Content.xhtml( div ) );
            
            feed.addEntry( entry );
        }
        
        // put back the readers
        for (int i = 0; i < indices.length; i++) {
            indices[ i ].putIndexReader( readers[ i ] );
        }
        
        AtomView.process( c, feed );
    }
    
}
