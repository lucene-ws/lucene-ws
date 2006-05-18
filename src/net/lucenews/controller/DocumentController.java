package net.lucenews.controller;

import java.io.*;
import net.lucenews.*;
import net.lucenews.model.*;
import net.lucenews.model.exception.*;
import net.lucenews.view.*;
import net.lucenews.atom.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.apache.lucene.document.Field;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;



public class DocumentController extends Controller {
    
    
    /**
    * Deletes a document
    * 
    * @throws IndicesNotFoundException
    * @throws DocumentsNotFoundException
    * @throws IOException
    */
    
    public static void doDelete (LuceneContext c)
        throws
            IllegalActionException, IndicesNotFoundException, DocumentsNotFoundException,
            IOException, InsufficientDataException, TransformerException, ParserConfigurationException
    {
        c.log().debug("DocumentController.doDelete(LuceneContext)");
        
        LuceneWebService   service     = c.service();
        LuceneIndexManager manager     = service.getIndexManager();
        LuceneRequest      req         = c.req();
        LuceneResponse     res         = c.res();
        String[]           indexNames  = req.getIndexNames();
        LuceneIndex[]      indices     = manager.getIndices( indexNames );
        String[]           documentIDs = req.getDocumentIDs();
        
        
        // Buffers for header location construction
        StringBuffer indexNamesBuffer  = new StringBuffer();
        StringBuffer documentIDsBuffer = new StringBuffer();
        
        boolean deleted = false;
        
        // For each index...
        for( int i = 0; i < indices.length; i++ ) {
            LuceneIndex index = indices[ i ];
            
            if( i > 0 )
            indexNamesBuffer.append( "," );
            indexNamesBuffer.append( index.getName() );
            
            // For each document...
            for( int j = 0; j < documentIDs.length; j++ ) {
                String documentID = documentIDs[ j ];
                
                LuceneDocument document = index.removeDocument( documentID );
                
                deleted = true;
                
                if( i == 0 ) {
                    if( j > 0 )
                        documentIDsBuffer.append( "," );
                    documentIDsBuffer.append( index.getIdentifier( document ) );
                }
            }
        }
        
        String indexNamesString  = indexNamesBuffer.toString();
        String documentIDsString = documentIDsBuffer.toString();
        
        if( deleted )
            res.addHeader( "Location", service.getDocumentURL( req, indexNamesString, documentIDsString ) );
        else
            throw new InsufficientDataException( "No documents to be deleted" );
        
        XMLController.acknowledge( c );
    }
    
    
    
    /**
    * Gets a document
    * 
    * @throws IndicesNotFoundException
    * @throws DocumentsNotFoundException
    * @throws ParserConfigurationException
    * @throws TransformerException
    * @throws IOException
    */
    
    public static void doGet (LuceneContext c)
        throws
            IndicesNotFoundException, DocumentsNotFoundException, ParserConfigurationException,
            TransformerException, IOException, InsufficientDataException
    {
        c.log().debug("DocumentController.doGet(LuceneContext)");
        
        LuceneWebService   service     = c.service();
        LuceneIndexManager manager     = service.getIndexManager();
        LuceneRequest      req         = c.req();
        LuceneResponse     res         = c.res();
        
        
        
        Author firstAuthor = null;
        
        
        List<Entry> entries = new LinkedList<Entry>();
        
        LuceneIndex[] indices = manager.getIndices( req.getIndexNames() );
        String[] documentIDs = req.getDocumentIDs();
        
        for (int i = 0; i < documentIDs.length; i++) {
            String documentID = documentIDs[ i ];
            
            for( int j = 0; j < indices.length; j++ ) {
                LuceneIndex index = indices[ j ];
                
                LuceneDocument document = null;
                
                try {
                    document = index.getDocument( documentID );
                }
                    catch(DocumentNotFoundException dnfe) {
                }
                
                if( document != null ) {
                    if( entries.size() == 0 ) {
                        String name = index.getAuthor( document );
                        if( name == null )
                            name = service.getTitle();
                        firstAuthor = new Author( name );
                    }
                    
                    entries.add( asEntry( c, index, document ) );
                }
            }
        }
        
        if( entries.size() == 1 ) {
            entries.get( 0 ).addAuthor( firstAuthor );
        }
        
        //if( documents.length == 1 )
        //	AtomView.process( c, asEntry( c, documents[ 0 ] ) );
        //else
        //	AtomView.process( c, asFeed( c, documents ) );
        
        
        if( entries.size() == 0 )
            throw new DocumentsNotFoundException( documentIDs );
        
        if( entries.size() == 1 ) {
            AtomView.process( c, entries.get( 0 ) );
        }
        else {
            Feed feed = new Feed();
            
            feed.setTitle( "Documents" );
            feed.setUpdated( Calendar.getInstance() );
            feed.setID( req.getLocation() );
            feed.addLink( Link.Self( req.getLocation() ) );
            feed.addAuthor( new Author( service.getTitle() ) );
            
            Iterator<Entry> iterator = entries.iterator();
            while( iterator.hasNext() )
            feed.addEntry( iterator.next() );
            
            AtomView.process( c, feed );
        }
        
    }
    
    
    
    /**
    * Updates particular documents within the index
    * 
    * @throws InvalidIdentifierException
    * @throws IndicesNotFoundException
    * @throws SAXException
    * @throws TransformerException
    * @throws ParserConfigurationException
    * @throws DocumentNotFoundException
    * @throws IndexNotFoundException
    * @throws IOException
    * @throws AtomParseException
    */
    
    public static void doPut (LuceneContext c)
        throws
            IllegalActionException, InvalidIdentifierException, IndicesNotFoundException, SAXException,
            TransformerException, ParserConfigurationException, DocumentNotFoundException,
            IndexNotFoundException, IOException, InsufficientDataException, AtomParseException, LuceneParseException
    {
        c.log().debug("DocumentController.doPut(LuceneContext)");
        
        LuceneWebService   service = c.service();
        LuceneIndexManager manager = service.getIndexManager();
        LuceneRequest      req     = c.req();
        LuceneResponse     res     = c.res();
        
        LuceneIndex[]    indices   = manager.getIndices( req.getIndexNames() );
        LuceneDocument[] documents = req.getLuceneDocuments();
        
        // Buffers for header location construction
        StringBuffer indexNamesBuffer  = new StringBuffer();
        StringBuffer documentIDsBuffer = new StringBuffer();
        
        boolean updated = false;
        
        for( int i = 0; i < indices.length; i++ ) {
            LuceneIndex index = indices[ i ];
            
            if( i > 0 )
                indexNamesBuffer.append( "," );
            indexNamesBuffer.append( index.getName() );
            
            for( int j = 0; j < documents.length; j++ ) {
                LuceneDocument document = documents[ j ];
                
                index.updateDocument( document );
                
                updated = true;
                
                if( i == 0 ) {
                    if( j > 0 )
                        documentIDsBuffer.append( "," );
                    documentIDsBuffer.append( index.getIdentifier( document ) );
                }
            }
        }
        String indexNames  = indexNamesBuffer.toString();
        String documentIDs = documentIDsBuffer.toString();
        
        if( updated )
            res.addHeader( "Location", service.getDocumentURL( req, indexNames, documentIDs ) );
        else
            throw new InsufficientDataException( "No documents to be updated" );
        
        XMLController.acknowledge( c );
    }
    
    
    
    public static Entry asEntry (LuceneContext c, LuceneIndex index, LuceneDocument document)
        throws InsufficientDataException, ParserConfigurationException, IOException
    {
        c.log().debug("DocumentController.asEntry(LuceneContext,LuceneIndex,LuceneDocument)");
        
        return asEntry( c, index, document, null );
    }
    
    
    
    /**
    * Returns an Atom Entry reflecting the standard format chosen for documents.
    * 
    * @param c The context
    * @param index The index
    * @param document The document
    * @param score The score of the document (if it was a hit)
    * @return An Atom Entry
    * @throws ParserConfigurationException
    * @throws IOException
    */
    
    public static Entry asEntry (LuceneContext c, LuceneIndex index, LuceneDocument document, Float score)
        throws InsufficientDataException, ParserConfigurationException, IOException
    {
        c.log().debug("DocumentController.asEntry(LuceneContext,LuceneIndex,LuceneDocument,Float)");
        
        LuceneWebService service = c.service();
        LuceneRequest    req     = c.req();
        
        
        
        // Entry
        Entry entry = new Entry();
        
        
        // Content
        entry.setContent( asContent( c, index, document ) );
        
        
        if( index == null )
            return entry;
        
        
        // ID and Link may only be added if the document is identified
        if( index.isDocumentIdentified( document ) ) {
            // ID
            entry.setID( service.getDocumentURL( req, index, document ) );
            
            // Link
            entry.addLink( Link.Alternate( service.getDocumentURL( req, index, document ) ) );
        }
        
        // Title
        entry.setTitle( index.getTitle( document ) );
        
        // Updated
        try {
            entry.setUpdated( index.getUpdated( document ) );
        }
        catch(InsufficientDataException ide) {
        }
        
        // Author
        if( index.getAuthor( document ) != null ) {
            entry.addAuthor( new Author( index.getAuthor( document ) ) );
        }
        
        // Score
        if( score != null ) {
            entry.setPropertyNS( "http://a9.com/-/spec/opensearch/1.1/", "opensearch:relevance", String.valueOf( score ) );
        }
        
        // Summary
        if( index.getSummary( document ) != null ) {
            entry.setSummary( new Text( index.getSummary( document ) ) );
        }
        
        return entry;
    }
    
    
    
    /**
    * Returns a DOM Element (<div>...</div>) containing XOXO-formatted data
    * 
    * @param c The context
    * @param index The index
    * @param document The document
    * @return A DOM Element
    * @throws ParserConfigurationException
    * @throws IOException
    */
    
    public static Content asContent (LuceneContext c, LuceneIndex index, LuceneDocument document)
        throws ParserConfigurationException, IOException
    {
        c.log().debug("DocumentController.asContent(LuceneContext,LuceneIndex,LuceneDocument)");
        
        Document doc = XMLController.newDocument();
        
        Element div = doc.createElement( "div" );
        div.setAttribute( "xmlns", "http://www.w3.org/1999/xhtml" );
        
        div.appendChild( XOXOController.asElement( c, document, doc ) );
        
        return Content.xhtml( div );
    }
    
    
    
    /**
     * Retrieves the appropriate CSS class for the given field
     */
    
    public static String getCSSClass (LuceneContext c, Field field) {
        c.log().debug("DocumentController.getCSSClass(LuceneContext,Field)");
        
        StringBuffer _class = new StringBuffer();
        
        if( field.isStored() )
        _class.append( " stored" );
        
        if( field.isIndexed() )
        _class.append( " indexed" );
        
        if( field.isTokenized() )
        _class.append( " tokenized" );
        
        if( field.isTermVectorStored() )
        _class.append( " termvectorstored" );
        
        return String.valueOf( _class ).trim();
    }
    
    
    
    
    
    
    
    
    /**
     * Transforms an Atom feed into an array of Lucene documents!
     */
    
    public static LuceneDocument[] asLuceneDocuments (LuceneContext c, Feed feed)
        throws ParserConfigurationException, TransformerConfigurationException, TransformerException, LuceneParseException
    {
        c.log().debug("DocumentController.asLuceneDocuments(LuceneContext,Feed)");
        
        return asLuceneDocuments( c, feed.getEntries().toArray( new Entry[]{} ) );
    }
    
    
    /**
     * Transforms Atom entries into Lucene documents
     * 
     * @param entries Atom entries
     * @return Lucene documents
     */
    
    public static LuceneDocument[] asLuceneDocuments (LuceneContext c, Entry... entries)
        throws
            ParserConfigurationException, TransformerConfigurationException,
            TransformerException, LuceneParseException
    {
        c.log().debug("DocumentController.asLuceneDocuments(LuceneContext,Entry...)");
        
        List<LuceneDocument> documents = new LinkedList<LuceneDocument>();
        
        for (int i = 0; i < entries.length; i++) {
            documents.addAll( Arrays.asList( asLuceneDocuments( c, entries[ i ] ) ) );
        }
        
        return documents.toArray( new LuceneDocument[]{} );
    }
    
    
    /**
     * Transforms an Atom entry into Lucene documents.
     * Typically, this will only involve the output of one
     * Lucene document, however, the door may be open to multiple.
     * 
     * @param  entry
     * @return LuceneDocument[]
     */
    
    public static LuceneDocument[] asLuceneDocuments (LuceneContext c, Entry entry)
        throws ParserConfigurationException, TransformerConfigurationException, TransformerException, LuceneParseException
    {
        c.log().debug("DocumentController.asLuceneDocuments(LuceneContext,Entry)");
        
        return asLuceneDocuments( c, entry.getContent() );
    }
    
    
    
    /**
     * Transforms Atom content into Lucene documents.
     */
    
    public static LuceneDocument[] asLuceneDocuments (LuceneContext c, Content content)
        throws ParserConfigurationException, TransformerConfigurationException, TransformerException, LuceneParseException
    {
        c.log().debug("DocumentController.asLuceneDocuments(LuceneContext,Content)");
        
        if (content == null) {
            throw new LuceneParseException("Cannot parse Lucene document: NULL content in entry");
        }
        
        if (content instanceof TextContent) {
            return asLuceneDocuments( c, (TextContent) content );
        }
        
        if (content instanceof NodeContent) {
            return asLuceneDocuments( c, (NodeContent) content );
        }
        
        throw new LuceneParseException("Cannot parse Lucene document: Unknown content type in entry");
    }
    
    
    
    /**
     * Transforms Atom text content into Lucene documents
     */
    
    public static LuceneDocument[] asLuceneDocuments (LuceneContext c, TextContent content)
        throws ParserConfigurationException, TransformerConfigurationException, TransformerException, LuceneParseException
    {
        c.log().debug("DocumentController.asLuceneDocuments(LuceneContext,TextContent)");
        
        return asLuceneDocuments( c, content.asDocument() );
    }
    
    
    
    /**
     * Transforms Atom node content into Lucene documents
     */
    
    public static LuceneDocument[] asLuceneDocuments (LuceneContext c, NodeContent content)
        throws LuceneParseException
    {
        c.log().debug("DocumentController.asLuceneDocuments(LuceneContext,NodeContent)");
        
        return asLuceneDocuments( c, content.getNodes() );
    }
    
    
    
    public static LuceneDocument[] asLuceneDocuments (LuceneContext c, Document document)
        throws LuceneParseException
    {
        c.log().debug("DocumentController.asLuceneDocuments(LuceneContext,Document)");
        
        return asLuceneDocuments( c, document.getDocumentElement() );
    }
    
    public static LuceneDocument[] asLuceneDocuments (LuceneContext c, NodeList nodeList)
        throws LuceneParseException
    {
        c.log().debug("DocumentController.asLuceneDocuments(LuceneContext,NodeList)");
        
        Node[] nodes = new Node[ nodeList.getLength() ];
        for( int i = 0; i < nodes.length; i++ ) {
            nodes[ i ] = nodeList.item( i );
        }
        return asLuceneDocuments( c, nodes );
    }
    
    public static LuceneDocument[] asLuceneDocuments (LuceneContext c, Node[] nodes) throws LuceneParseException {
        c.log().debug("DocumentController.asLuceneDocuments(LuceneContext,Node[]): " + nodes.length + " nodes");
        c.log().debug("Nodes: " + ServletUtils.toString(nodes));
        
        List<LuceneDocument> documents = new LinkedList<LuceneDocument>();
        for( int i = 0; i < nodes.length; i++ ) {
            if (nodes[ i ].getNodeType() == Node.ELEMENT_NODE) {
                documents.addAll( Arrays.asList( asLuceneDocuments( c, (Element) nodes[ i ] ) ) );
            }
        }
        return documents.toArray( new LuceneDocument[]{} );
    }
    
    public static LuceneDocument[] asLuceneDocuments (LuceneContext c, Element element) throws LuceneParseException {
        c.log().debug("DocumentController.asLuceneDocuments(LuceneContext,Element)");
        
        List<LuceneDocument> documents = new LinkedList<LuceneDocument>();
        
        if (element.getTagName().equals( "div" )) {
            documents.addAll( Arrays.asList( asLuceneDocuments( c, (NodeList) element.getChildNodes() ) ) );
        }
        else {
            LuceneDocument document = new LuceneDocument();
            document.add( XOXOController.asFields( c, element ) );
            documents.add( document );
        }
        
        return documents.toArray( new LuceneDocument[]{} );
    }
    
    public static LuceneDocument asLuceneDocument (LuceneContext c, Element element) {
        c.log().debug("DocumentController.asLuceneDocument(LuceneContext,Element)");
        
        LuceneDocument document = new LuceneDocument();
        
        NodeList fields = element.getElementsByTagName( "field" );
        for (int i = 0; i < fields.getLength(); i++) {
            document.add( asField( c, (Element) fields.item( i ) ) );
        }
        
        return document;
    }
    
    
    public static Field asField (LuceneContext c, Element element) {
        c.log().debug("DocumentController.asField(LuceneContext,Element)");
        
        if (!element.getTagName().equals( "field" )) {
            throw new RuntimeException( "Must provide a <field> tag" );
        }
        
        String name  = element.getAttribute( "name" );
        String value = ServletUtils.toString( element.getChildNodes() );
        
        if (element.getAttribute( "type" ) != null) {
            String type = element.getAttribute( "type" ).trim().toLowerCase();
            
            if (type.equals( "keyword" )) {
                return new Field( name, value, Field.Store.YES, Field.Index.UN_TOKENIZED );
            }
            
            if (type.equals( "text" )) {
                return new Field( name, value, Field.Store.YES, Field.Index.TOKENIZED );
            }
            
            if (type.equals( "sort" )) {
                return new Field( name, value, Field.Store.NO, Field.Index.UN_TOKENIZED );
            }
            
            if (type.equals( "unindexed" )) {
                return new Field( name, value, Field.Store.YES, Field.Index.NO );
            }
            
            if (type.equals( "unstored" )) {
                return new Field( name, value, Field.Store.NO, Field.Index.TOKENIZED );
            }
                        
            return new Field( name, value, Field.Store.YES, Field.Index.TOKENIZED );
        }
        else {
            boolean index = ServletUtils.parseBoolean( element.getAttribute( "index" ) );
            boolean store = ServletUtils.parseBoolean( element.getAttribute( "store" ) );
            boolean token = ServletUtils.parseBoolean( element.getAttribute( "tokenized" ) );
            
            Field.Store stored = store ? Field.Store.YES : Field.Store.NO;
            
            Field.Index indexed = Field.Index.NO;
            if (index) {
                if (token) {
                    indexed = Field.Index.TOKENIZED;
                }
                else {
                    indexed = Field.Index.UN_TOKENIZED;
                }
            }
            
            return new Field( name, value, stored, indexed );
        }
    }
    
}
