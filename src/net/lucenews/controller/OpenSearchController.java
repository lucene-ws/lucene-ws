package net.lucenews.controller;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import net.lucenews.*;
import net.lucenews.model.*;
import net.lucenews.model.exception.*;
import net.lucenews.opensearch.*;
import net.lucenews.view.*;
import org.w3c.dom.*;


public class OpenSearchController extends Controller {
    
    
    /**
     * Gets the OpenSearch description document for the particular index.
     * 
     * @param c The context
     * @throws IndicesNotFoundException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IOException
     */
    
    public static void doGet (LuceneContext c)
        throws
            IndicesNotFoundException, ParserConfigurationException, TransformerException,
            IOException, OpenSearchException
    {
        LuceneWebService   service = c.service();
        LuceneIndexManager manager = service.getIndexManager();
        LuceneRequest      req     = c.req();
        LuceneRequest      request = c.req();
        LuceneResponse     res     = c.res();
        LuceneIndex[]      indices = manager.getIndices( req.getIndexNames() );
        
        
        OpenSearchDescription description = new OpenSearchDescription();
        description.setShortName( ServletUtils.joined(ServletUtils.mapped("'[content]'", ServletUtils.objectsMapped("getTitle", indices))) );
        description.setDescription( "OpenSearch description for " + ServletUtils.joined(ServletUtils.mapped("'[content]'", ServletUtils.objectsMapped("getTitle", indices))));
        
        
        // Template
        HttpURI template = new HttpURI( service.getServiceURL( request ) );
        template.addPath( ServletUtils.join( ",", (Object[]) indices ) );
        template.setParameter( "searchTerms",     "{searchTerms}" );
        template.setParameter( "count",           "{count?}" );
        template.setParameter( "startIndex",      "{startIndex?}" );
        template.setParameter( "startPage",       "{startPage?}" );
        template.setParameter( "language",        "{language?}" );
        template.setParameter( "outputEncoding",  "{outputEncoding?}" );
        template.setParameter( "inputEncoding",   "{inputEncoding?}" );
        template.setParameter( "totalResults",    "{totalResults?}" );
        template.setParameter( "analyzer",        "{lucene:analyzer?}" );
        template.setParameter( "defaultField",    "{lucene:defaultField?}" );
        template.setParameter( "defaultOperator", "{lucene:defaultOperator?}" );
        template.setParameter( "filter",          "{lucene:filter?}" );
        template.setParameter( "locale",          "{lucene:locale?}" );
        template.setParameter( "sort",            "{lucene:sort?}" );
        
        
        // Atom
        OpenSearchUrl atomUrl = new OpenSearchUrl();
        atomUrl.setType("application/atom+xml");
        atomUrl.setTemplate( template.with( "format", "atom" ).toString() );
        atomUrl.setNamespace( "lucene", "http://www.lucene-ws.net/" );
        description.addUrl( atomUrl );
        
        
        // RSS
        OpenSearchUrl rssUrl = new OpenSearchUrl();
        rssUrl.setType("application/rss+xml");
        rssUrl.setTemplate( template.with( "format", "rss" ).toString() );
        rssUrl.setNamespace( "lucene", "http://www.lucene-ws.net/" );
        description.addUrl( rssUrl );
        
        
        
        // OutputEncoding / InputEncoding
        Iterator<Map.Entry<String,Charset>> charsetIterator = Charset.availableCharsets().entrySet().iterator();
        
        while (charsetIterator.hasNext()) {
            Map.Entry<String,Charset> entry = charsetIterator.next();
            
            String  name    = entry.getKey();
            Charset charset = entry.getValue();
            
            // OutputEncoding
            if (charset.canEncode()) {
                description.addOutputEncoding( name );
            }
            
            // InputEncoding
            try {
                charset.newDecoder();
                description.addInputEncoding( name );
            }
            catch (UnsupportedOperationException inputUoe) {
            }
        }
        
        
        OpenSearchView.process( c, description );
    }
    
}
