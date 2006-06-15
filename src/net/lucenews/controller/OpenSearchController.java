package net.lucenews.controller;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import net.lucenews.*;
import net.lucenews.model.*;
import net.lucenews.model.exception.*;
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
        throws IndicesNotFoundException, ParserConfigurationException, TransformerException, IOException
    {
        LuceneIndexManager manager = c.service().getIndexManager();
        LuceneRequest      req     = c.req();
        LuceneResponse     res     = c.res();
        
        LuceneIndex[] indices = manager.getIndices( req.getIndexNames() );
        
        
        Document document = XMLController.newDocument();
        
        Element desc = document.createElement( "OpenSearchDescription" );
        desc.setAttribute("xmlns", "http://a9.com/-/spec/opensearch/1.1/");
        
        
        /**
         * description
         */
        
        StringBuffer _description = new StringBuffer();
        _description.append( "OpenSearch description for" );
        for (int i = 0; i < indices.length; i++) {
            if (i > 0) {
                if (i == ( indices.length - 1 )) {
                    _description.append( " and" );
                }
                else {
                    _description.append( "," );
                }
            }
            _description.append( " '" + indices[ i ].getTitle() + "'" );
        }
        
        String _shortName = "";
        if (indices.length == 1) {
            _shortName = indices[ 0 ].getTitle();
        }
        else {
            _shortName = ServletUtils.joined( ServletUtils.mapped( "'[content]'", ServletUtils.objectsMapped( "getTitle", indices ) ) );
        }
        
        
        // short name
        Element shortName = document.createElement("ShortName");
        shortName.appendChild( document.createTextNode( _shortName ) );
        desc.appendChild( shortName );
        
        
        // description
        Element description = document.createElement( "Description" );
        description.appendChild( document.createTextNode( String.valueOf( _description ) ) );
        desc.appendChild( description );
        
        
        /**
         * Search URL
         */
        
        StringBuffer searchURL = new StringBuffer();
        searchURL.append(c.service().getServiceURL( req ));
        for (int i = 0; i < indices.length; i++) {
            if (i > 0) {
                searchURL.append( "," );
            }
            searchURL.append( indices[i].getName() );
        }
        searchURL.append("/?query={searchTerms}&limit={count}&offset={startIndex}&page={startPage}&locale={language?}&sort={sort?}&analyzer={analyzer?}&operator={operator?}&maximum={maximum?}");
        
        
        
        /**
         * Atom URL
         */
        
        Element atomUrl = document.createElement("Url");
        atomUrl.setAttribute("type", "application/atom+xml");
        atomUrl.setAttribute("template", searchURL.toString() + "&format=atom");
        desc.appendChild(atomUrl);
        
        
        /**
         * RSS URL
         */
        
        Element rssUrl = document.createElement("Url");
        rssUrl.setAttribute("type", "application/rss+xml");
        rssUrl.setAttribute("template", searchURL.toString() + "&format=rss");
        desc.appendChild(rssUrl);
        
        
        document.appendChild(desc);
        
        res.setContentType("application/opensearchdescription+xml;charset=utf-8");
        XMLView.process( c, document );
    }
    
}
