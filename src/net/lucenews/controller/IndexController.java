package net.lucenews.controller;

import java.util.*;
import java.io.*;
import net.lucenews.*;
import net.lucenews.model.*;
import net.lucenews.model.exception.*;
import net.lucenews.opensearch.*;
import net.lucenews.view.*;
import net.lucenews.atom.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.*;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.search.*;
import org.w3c.dom.*;
import org.xml.sax.*;



public class IndexController extends Controller {
    
    
    
    /**
     * Deletes an index.
     * 
     * @param c The context
     * @throws IndicesNotFoundException
     * @throws InvalidActionException
     * @throws IOException
     */
    
    public static void doDelete (LuceneContext c)
        throws
            TransformerException, ParserConfigurationException, IndicesNotFoundException,
            IllegalActionException, IOException, InsufficientDataException
    {
        Logger.getLogger(IndexController.class).trace("doDelete(LuceneContext)");
        
        LuceneWebService   service = c.service();
        LuceneIndexManager manager = service.getIndexManager();
        LuceneRequest      req     = c.req();
        LuceneResponse     res     = c.res();
        LuceneIndex[]      indices = manager.getIndices( req.getIndexNames() );
        
        
        StringBuffer indexNamesBuffer = new StringBuffer();
        
        boolean deleted = false;
        
        
        // Delete each index
        for (int i = 0; i < indices.length; i++) {
            LuceneIndex index = indices[ i ];
            
            if (!index.delete()) {
                throw new IOException( "Index '" + index.getName() + "' could not be deleted." );
            }
            
            deleted = true;
            
            if (i > 0) {
                indexNamesBuffer.append( "," );
            }
            indexNamesBuffer.append( index.getName() );
        }
        
        
        if (deleted) {
            res.addHeader( "Location", service.getIndexURL( req, indexNamesBuffer.toString() ) );
        }
        else {
            throw new InsufficientDataException( "No indices to be deleted" );
        }
        
        
        XMLController.acknowledge( c );
    }
    
    
    
    /**
     * Displays basic information regarding the index.
     * 
     * @param c The context
     * @throws ParserConfigurationException
     * @throws InvalidIdentifierException
     * @throws DocumentNotFoundException
     * @throws IndicesNotFoundException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IOException
     * @throws DocumentAlreadyExistsException
     */
    
    public static void doGet (LuceneContext c)
        throws
            ParserConfigurationException, InvalidIdentifierException, DocumentNotFoundException,
            IndicesNotFoundException, ParserConfigurationException, TransformerException,
            IOException, DocumentAlreadyExistsException, InsufficientDataException, ParseException,
            OpenSearchException
    {
        Logger.getLogger(IndexController.class).trace("doGet(LuceneContext)");
        
        LuceneWebService   service  = c.getService();
        LuceneIndexManager manager  = service.getIndexManager();
        LuceneRequest      request  = c.getRequest();
        LuceneResponse     response = c.getResponse();
        
        c.setSort( new Sort( new SortField( null, SortField.DOC, true ) ) );
        
        SearchController.doGet( c );
    }
    
    
    
    /**
     * Updates an index
     * 
     * @param c The context
     * @throws IndicesNotFoundException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws LuceneException
     * @throws AtomParseException
     */
    
    public static void doPut (LuceneContext c)
        throws
            IndicesNotFoundException, ParserConfigurationException, SAXException, IOException,
            LuceneException, TransformerException, ParserConfigurationException, AtomParseException
    {
        Logger.getLogger(IndexController.class).trace("doPut(LuceneContext)");
        
        LuceneWebService   service = c.service();
        LuceneIndexManager manager = service.getIndexManager();
        LuceneRequest      req     = c.req();
        LuceneResponse     res     = c.res();
        Entry[]            entries = req.getEntries();
        
        
        StringBuffer indexNamesBuffer = new StringBuffer();
        
        boolean updated = false;
        
        
        // For each index...
        for (int i = 0; i < entries.length; i++) {
            Entry entry = entries[ i ];
            
            String name = entry.getTitle();
            if (!req.hasIndexName( name )) {
                throw new LuceneException( "Index '" + name + "' not mentioned in URL" );
            }
            
            Properties properties = XOXOController.asProperties( c, entry );
            
            LuceneIndex index = manager.getIndex( name );
            
            index.setProperties( properties );
            
            updated = true;
            
            if (i > 0) {
                indexNamesBuffer.append( "," );
            }
            indexNamesBuffer.append( index.getName() );
        }
        
        if (updated) {
            res.addHeader( "Location", service.getIndexURL( req, indexNamesBuffer.toString() ) );
        }
        
        XMLController.acknowledge( c );
    }
    
    
    
    /**
     * Adds documents.
     * 
     * @param c The context
     * @throws TransformerException
     * @throws DocumentsAlreadyExistException
     * @throws IndicesNotFoundException
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws LuceneException
     * @throws AtomParseException
     */
    
    public static void doPost (LuceneContext c)
        throws
            IllegalActionException, TransformerException, DocumentsAlreadyExistException, IndicesNotFoundException,
            ParserConfigurationException, IOException, SAXException, LuceneException, AtomParseException
    {
        Logger.getLogger(IndexController.class).trace("doPost(LuceneContext)");
        
        LuceneWebService   service   = c.service();
        LuceneIndexManager manager   = service.getIndexManager();
        LuceneRequest      req       = c.req();
        LuceneResponse     res       = c.res();
        LuceneIndex[]      indices   = manager.getIndices( req.getIndexNames() );
        LuceneDocument[]   documents = req.getLuceneDocuments();
        
        List<LuceneDocument> addedDocuments = new LinkedList<LuceneDocument>();
        
        // Buffers for header location construction
        StringBuffer indexNamesBuffer  = new StringBuffer();
        StringBuffer documentIDsBuffer = new StringBuffer();
        
        
        // For each index...
        for (int i = 0; i < indices.length; i++) {
            LuceneIndex index = indices[ i ];
            
            if (i > 0) {
                indexNamesBuffer.append( "," );
            }
            indexNamesBuffer.append( index.getName() );
            
            // For each document...
            for (int j = 0; j < documents.length; j++) {
                LuceneDocument document = documents[ j ];
                
                index.addDocument( document );
                
                if (i == 0) {
                    if (j > 0) {
                        documentIDsBuffer.append( "," );
                    }
                    documentIDsBuffer.append( index.getIdentifier( document ) );
                }
                
                res.setStatus( res.SC_CREATED );
            }
        }
        
        String indexNames  = indexNamesBuffer.toString();
        String documentIDs = documentIDsBuffer.toString();
        
        if (res.getStatus() == res.SC_CREATED) {
            res.addHeader( "Location", service.getDocumentURL( req, indexNames, documentIDs ) );
            res.addHeader( "Content-Location", service.getDocumentURL( req, indexNames, documentIDs ) );
        }
        else {
            throw new InsufficientDataException( "No documents to be added" );
        }
        
        
        String optimize = req.getCleanParameter("optimize");
        if (optimize == null || ServletUtils.parseBoolean(optimize)) {
            IndexController.doOptimize( c );
        }
        
        
        // TODO: A representation of the added entries must be returned as per the APP
        if (addedDocuments.size() == 1) {
            LuceneDocument document = addedDocuments.iterator().next();
            
            Entry entry = DocumentController.asEntry( c, document );
            entry.addLink( Link.Edit( service.getDocumentURL( req, document ) ) );
            AtomView.process( c, entry );
        }
        else {
            Feed feed = new Feed();
            
            Iterator<LuceneDocument> iterator = addedDocuments.iterator();
            while (iterator.hasNext()) {
                LuceneDocument document = iterator.next();
                Entry entry = DocumentController.asEntry( c, document );
                entry.addLink( Link.Edit( service.getDocumentURL( req, document ) ) );
                feed.addEntry( entry );
            }
            
            AtomView.process( c, feed );
        }
    }
    
    
    
    /**
     * Transforms the currently selected index into an Atom feed.
     * Feed includes the most recently modified documents.
     * 
     * @param c The context
     * @return An Atom feed
     * @throws IndexNotFoundException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IOException
     */
    
    public static Feed asFeed (LuceneContext c)
        throws IndicesNotFoundException, IndexNotFoundException, ParserConfigurationException, TransformerException, IOException, InsufficientDataException
    {
        Logger.getLogger(IndexController.class).trace("asFeed(LuceneContext)");
        
        LuceneWebService   service  = c.getService();
        LuceneIndexManager manager  = service.getIndexManager();
        LuceneRequest      request  = c.getRequest();
        LuceneResponse     response = c.getResponse();
        
        
        
        LuceneIndex[] indices = manager.getIndices( request.getIndexNames() );
        
        
        
        /**
         * Feed
         */
        Feed feed = new Feed();
        
        
        
        // Title
        StringBuffer title = new StringBuffer();
        if (indices.length == 1) {
            title.append( indices[ 0 ].getTitle() );
        }
        else {
            title.append( ServletUtils.joined( ServletUtils.mapped( "[content]", ServletUtils.objectsMapped( "getTitle", indices ) ) ) );
        }
        feed.setTitle( title.toString() );
        
        // ID
        feed.setID( service.getIndicesURL( request, indices ) );
        
        // Updated
        feed.setUpdated( Calendar.getInstance() );
        
        // Author
        boolean authorFound = false;
        for (int i = 0; i < indices.length; i++) {
            if (indices[ i ].hasAuthor()) {
                feed.addAuthor( new Author( indices[ i ].getAuthor() ) );
                authorFound = true;
            }
        }
        
        if (!authorFound) {
            feed.addAuthor( new Author( service.getTitle() ) );
        }
        
        // Link
        feed.addLink( new Link( request.getLocation(), "self", "application/atom+xml" ) );
        
        // This is critical
        //if( !index.hasUpdatedField() )
        //	return feed;
        
        //IndexReader reader = index.getIndexReader();
        
        
        Limiter limiter = request.getLimiter();
        //limiter.setTotalEntries( reader.numDocs() );
        
        //if( limiter.getFirst() == null || limiter.getLast() == null )
        //	return feed;
        
        String sortField = null;
        for (int i = 0; i < indices.length; i++) {
            LuceneIndex index = indices[ i ];
            if (index.hasUpdatedField()) {
                if (sortField == null) {
                    sortField = index.getUpdatedField();
                }
                else if (!sortField.equals( index.getUpdatedField() )) {
                    sortField = null;
                }
            }
            else {
                sortField = null;
                break;
            }
        }
        
        
        LuceneMultiSearcher   searcher  = null;
        IndexSearcher[] searchers = new IndexSearcher[ indices.length ];
        
        for (int i = 0; i < indices.length; i++) {
            searchers[ i ] = indices[ i ].getIndexSearcher();
        }
        
        searcher = new LuceneMultiSearcher( searchers, SearchController.getSearcherIndexField() );
        
        Query query = new MatchAllDocsQuery();
        
        Sort sort = null;
        if (sortField == null) {
            sort = new Sort( new SortField( null, SortField.DOC, true ) );
        }
        else {
            sort = new Sort( new SortField( sortField, SortField.STRING, true ) );
        }
        
        Hits hits = searcher.search( query, sort );
        limiter.setTotalEntries( hits.length() );
        HitsIterator iterator = new HitsIterator( hits, limiter );
        
        while (iterator.hasNext()) {
            LuceneDocument document = iterator.next();
            feed.addEntry( DocumentController.asEntry( c, indices[ SearchController.extractSearcherIndex( document ) ], document ) );
        }
        
        if (limiter instanceof Pager) {
            Pager pager = (Pager) limiter;
            
            String qs = request.getQueryStringExcluding( "page" );
            
            if (pager.getFirstPage() != null) {
                feed.addLink( new Link( request.getRequestURL() + LuceneRequest.getQueryStringWithParameter( qs, "page", pager.getFirstPage() ), "first", "application/atom+xml" ) );
            }
            
            if (pager.getPreviousPage() != null) {
                feed.addLink( new Link( request.getRequestURL() + LuceneRequest.getQueryStringWithParameter( qs, "page", pager.getPreviousPage() ), "previous", "application/atom+xml" ) );
            }
            
            if (pager.getNextPage() != null) {
                feed.addLink( new Link( request.getRequestURL() + LuceneRequest.getQueryStringWithParameter( qs, "page", pager.getNextPage() ), "next", "application/atom+xml" ) );
            }
            
            if (pager.getLastPage() != null) {
                feed.addLink( new Link( request.getRequestURL() + LuceneRequest.getQueryStringWithParameter( qs, "page", pager.getLastPage() ), "last", "application/atom+xml" ) );
            }
        }
        
        
        
        return feed;
    }
    
    
    
    public static void doOptimize (LuceneContext c)
        throws ParserConfigurationException, TransformerException, IndicesNotFoundException, IOException
    {
        Logger.getLogger(IndexController.class).trace("doOptimize(LuceneContext)");
        
        LuceneWebService   service = c.service();
        LuceneIndexManager manager = service.getIndexManager();
        LuceneRequest      req     = c.req();
        LuceneResponse     res     = c.res();
        LuceneIndex[]      indices = manager.getIndices( req.getIndexNames() );
        
        for (int i = 0; i < indices.length; i++) {
            LuceneIndex index = indices[ i ];
            IndexWriter writer = index.getIndexWriter();
            writer.optimize();
            index.putIndexWriter( writer );
        }
        
        XMLController.acknowledge( c );
    }
    
    
    
}
