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
        LuceneIndexManager manager = c.service().getIndexManager();
        LuceneRequest      req     = c.req();
        LuceneResponse     res     = c.res();
        LuceneIndex[]      indices = manager.getIndices( req.getIndexNames() );
        
        
        OpenSearchDescription description = new OpenSearchDescription();
        description.setShortName( ServletUtils.joined(ServletUtils.mapped("'[content]'", ServletUtils.objectsMapped("getTitle", indices))) );
        description.setDescription( "OpenSearch description for " + ServletUtils.joined(ServletUtils.mapped("'[content]'", ServletUtils.objectsMapped("getTitle", indices))));
        
        
        // Search URL
        StringBuffer searchURL = new StringBuffer();
        searchURL.append(c.service().getServiceURL( req ));
        for (int i = 0; i < indices.length; i++) {
            if (i > 0) {
                searchURL.append( "," );
            }
            searchURL.append( indices[i].getName() );
        }
        searchURL.append("/?query={searchTerms}&limit={count}&offset={startIndex}&page={startPage}&locale={language?}&sort={sort?}&analyzer={analyzer?}&operator={operator?}&maximum={maximum?}");
        
        
        
        // Atom
        OpenSearchUrl atomUrl = new OpenSearchUrl();
        atomUrl.setType("application/atom+xml");
        atomUrl.setTemplate(searchURL.toString() + "&format=atom");
        description.addUrl( atomUrl );
        
        
        // RSS
        OpenSearchUrl rssUrl = new OpenSearchUrl();
        rssUrl.setType("application/rss+xml");
        rssUrl.setTemplate(searchURL.toString() + "&format=rss");
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
