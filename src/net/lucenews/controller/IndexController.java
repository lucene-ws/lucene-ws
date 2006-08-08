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
        
        LuceneWebService   service  = c.getService();
        LuceneIndexManager manager  = service.getIndexManager();
        LuceneRequest      request  = c.getRequest();
        LuceneResponse     response = c.getResponse();
        LuceneIndex[]      indices  = manager.getIndices( request.getIndexNames() );
        
        
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
            response.addHeader( "Location", service.getIndexURL( request, indexNamesBuffer.toString() ) );
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
        LuceneIndex[]      indices  = manager.getIndices( request.getIndexNames() );
        
        if ( c.getSort() == null ) {
            c.setSort( new Sort( new SortField( null, SortField.DOC, true ) ) );
        }
        
        String[] titles = new String[ indices.length ];
        for ( int i = 0; i < indices.length; i++ ) {
            LuceneIndex index = indices[ i ];
            
            String title = null;
            if ( title == null ) { title = index.getTitle(); }
            if ( title == null ) { title = index.getName();  }
            titles[ i ] = title;
        }
        c.setTitle( ServletUtils.joined( titles ) );
        
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
        
        LuceneWebService   service  = c.getService();
        LuceneIndexManager manager  = service.getIndexManager();
        LuceneRequest      request  = c.getRequest();
        LuceneResponse     response = c.getResponse();
        Entry[]            entries  = request.getEntries();
        
        
        StringBuffer indexNamesBuffer = new StringBuffer();
        
        boolean updated = false;
        
        
        // For each index...
        for (int i = 0; i < entries.length; i++) {
            Entry entry = entries[ i ];
            
            String name = entry.getTitle();
            if (!request.hasIndexName( name )) {
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
            response.addHeader( "Location", service.getIndexURL( request, indexNamesBuffer.toString() ) );
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
        
        LuceneWebService   service   = c.getService();
        LuceneIndexManager manager   = service.getIndexManager();
        LuceneRequest      request   = c.getRequest();
        LuceneResponse     response  = c.getResponse();
        LuceneIndex[]      indices   = manager.getIndices( request.getIndexNames() );
        LuceneDocument[]   documents = request.getLuceneDocuments();
        
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
                
                response.setStatus( response.SC_CREATED );
            }
        }
        
        String indexNames  = indexNamesBuffer.toString();
        String documentIDs = documentIDsBuffer.toString();
        
        if (response.getStatus() == response.SC_CREATED) {
            response.addHeader( "Location", service.getDocumentURL( request, indexNames, documentIDs ) );
            response.addHeader( "Content-Location", service.getDocumentURL( request, indexNames, documentIDs ) );
        }
        else {
            throw new InsufficientDataException( "No documents to be added" );
        }
        
        
        if ( c.isOptimizing() == null || c.isOptimizing() ) {
            IndexController.doOptimize( c );
        }
        
        
        // TODO: A representation of the added entries must be returned as per the APP
        if (addedDocuments.size() == 1) {
            LuceneDocument document = addedDocuments.iterator().next();
            
            Entry entry = DocumentController.asEntry( c, document );
            entry.addLink( Link.Edit( service.getDocumentURL( request, document ) ) );
            AtomView.process( c, entry );
        }
        else {
            Feed feed = new Feed();
            
            Iterator<LuceneDocument> iterator = addedDocuments.iterator();
            while (iterator.hasNext()) {
                LuceneDocument document = iterator.next();
                Entry entry = DocumentController.asEntry( c, document );
                entry.addLink( Link.Edit( service.getDocumentURL( request, document ) ) );
                feed.addEntry( entry );
            }
            
            AtomView.process( c, feed );
        }
    }
    
    
    
    /**
     * Optimizes the current indices.
     * 
     * @param c
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IndicesNotFoundException
     * @throws IOException
     */
    
    public static void doOptimize (LuceneContext c)
        throws ParserConfigurationException, TransformerException, IndicesNotFoundException, IOException
    {
        Logger.getLogger(IndexController.class).trace("doOptimize(LuceneContext)");
        
        LuceneWebService   service  = c.getService();
        LuceneIndexManager manager  = service.getIndexManager();
        LuceneRequest      request  = c.getRequest();
        LuceneResponse     response = c.getResponse();
        LuceneIndex[]      indices  = manager.getIndices( request.getIndexNames() );
        
        for (int i = 0; i < indices.length; i++) {
            LuceneIndex index = indices[ i ];
            IndexWriter writer = index.getIndexWriter();
            writer.optimize();
            index.putIndexWriter( writer );
        }
        
        XMLController.acknowledge( c );
    }
    
    
    
    public static void doTagCloud (LuceneContext c)
        throws ParserConfigurationException, TransformerException, IndicesNotFoundException, IOException
    {
        Logger.getLogger(IndexController.class).trace("doTagCloud(LuceneContext)");
        
        LuceneWebService   service  = c.getService();
        LuceneIndexManager manager  = service.getIndexManager();
        LuceneRequest      request  = c.getRequest();
        LuceneResponse     response = c.getResponse();
        LuceneIndex[]      indices  = manager.getIndices( request.getIndexNames() );
        
        
        IndexReader[] readers = new IndexReader[ indices.length ];
        
        for (int i = 0; i < indices.length; i++) {
            readers[ i ] = indices[ i ].getIndexReader();
        }
        
        MultiReader reader = new MultiReader( readers );
        
        String[] fields = request.getParameterValues("field");
        
        if ( fields == null || fields.length == 0 ) {
            String field = c.getDefaultField();
            if ( field != null ) {
                fields = new String[]{ field };
            }
        }
        
        // cloud
        Map<String,Integer> cloud = new LinkedHashMap<String,Integer>();
        
        Integer minimum = null;
        Integer maximum = null;
        
        for (int i = 0; i < fields.length; i++) {
            String field = fields[ i ];
            
            TermEnum terms = reader.terms( new Term( field, "" ) );
            while ( terms.next() && terms.term().field().equals( field ) ) {
                Term   term = terms.term();
                String text = term.text();
                
                int count = 0;
                TermPositions positions = reader.termPositions( term );
                while ( positions.next() ) {
                    count++;
                }
                
                if ( minimum == null || count < minimum ) {
                    minimum = count;
                }
                
                if ( maximum == null || count > maximum ) {
                    maximum = count;
                }
                
                if ( cloud.get( text ) == null ) {
                    cloud.put( text, count );
                }
                else {
                    cloud.put( text, cloud.get( text ) + count );
                }
            }
        }
        
        Document document = XMLController.newDocument();
        
        Element htmlElement = document.createElement("html");
        document.appendChild(htmlElement);
        
        Element bodyElement = document.createElement("body");
        htmlElement.appendChild(bodyElement);
        
        Element cloudElement = document.createElement("div");
        bodyElement.appendChild(cloudElement);
        
        int minimum_font_size = 10;
        int maximum_font_size = 100;
        
        Iterator<Map.Entry<String,Integer>> iterator = cloud.entrySet().iterator();
        while ( iterator.hasNext() ) {
            Map.Entry<String,Integer> entry = iterator.next();
            String  text  = entry.getKey();
            Integer count = entry.getValue();
            if ( count == null ) {
                count = 0;
            }
            
            double size = (double)( count - minimum ) / (double)( maximum - minimum );
            size = minimum_font_size + size * ( maximum_font_size - minimum_font_size );
            size = (int) size;
            
            float percentage = ( (float) count * 100 ) / (float) maximum;
            Element element = document.createElement("span");
            element.appendChild( document.createTextNode( text ) );
            element.setAttribute("style","font-size: "+size+"pt;");
            
            cloudElement.appendChild( element );
        }
        
        for (int i = 0; i < indices.length; i++) {
            indices[ i ].putIndexReader( readers[ i ] );
        }
        
        response.setContentType("text/html; charset=UTF-8");
        XMLView.process( c, document );
    }
}
