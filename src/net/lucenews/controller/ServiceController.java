package net.lucenews.controller;

import java.io.*;
import java.util.*;
import net.lucenews.*;
import net.lucenews.model.*;
import net.lucenews.model.exception.*;
import net.lucenews.view.*;
import net.lucenews.atom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.apache.log4j.*;
import org.xml.sax.*;
import org.w3c.dom.*;



public class ServiceController extends Controller {
    
    
    
    /**
     * Displays the service in the form of an Atom Introspection Document.
     * 
     * @param c The context
     * @throws LuceneException
     * @throws IndicesNotFoundException
     * @throws TransformerException
     * @throws ParserConfigurationException
     * @throws IOException
     */
    
    public static void doGet (LuceneContext c)
        throws
            LuceneException, IndicesNotFoundException, TransformerException,
            ParserConfigurationException, IOException
    {
        Logger.getLogger(ServiceController.class).trace("doGet(LuceneContext)");
        
        LuceneWebService   service  = c.getService();
        LuceneIndexManager manager  = service.getIndexManager();
        LuceneRequest      request  = c.getRequest();
        LuceneResponse     response = c.getResponse();
        
        AtomView.process( c, asIntrospectionDocument( c, service, request ) );
    }
    
    
    
    
    
    /**
     * Posts a new index (or indices) to the web service.
     * 
     * @param c The context
     * @throws IndicesAlreadyExistException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws AtomParseException
     * @throws LuceneException
     */
    
    public static void doPost (LuceneContext c)
        throws
            IllegalActionException, IndicesAlreadyExistException, TransformerException,
            ParserConfigurationException, SAXException, IOException, InsufficientDataException,
            LuceneException, AtomParseException
    {
        Logger.getLogger(ServiceController.class).trace("doPost(LuceneContext)");
        
        LuceneWebService   service  = c.getService();
        LuceneIndexManager manager  = service.getIndexManager();
        LuceneRequest      request  = c.getRequest();
        LuceneResponse     response = c.getResponse();
        
        
        StringBuffer indexNamesBuffer = new StringBuffer();
        
        boolean created = false;
        
        
        Entry[] entries = getEntries( c, request );
        
        for (int i = 0; i < entries.length; i++) {
            Entry entry = entries[ i ];
            
            // Index name
            String name = entry.getTitle();
            
            Properties properties = XOXOController.asProperties( c, entry );
            
            File parentDirectory = manager.getCreatedIndicesDirectory();
            File directory = new File( parentDirectory, name );
            
            LuceneIndex index = null;
            
            // We don't want any exceptions thrown to give away
            // where in the file system the index was being
            // placed at.
            try {
                index = LuceneIndex.create( directory );
            }
            catch (IndexAlreadyExistsException iaee) {
                throw new IndexAlreadyExistsException( name );
            }
            
            if (properties != null) {
                index.setProperties( properties );
            }
            
            created = true;
            
            if (i > 0) {
                indexNamesBuffer.append( "," );
            }
            indexNamesBuffer.append( index.getName() );
            
            response.setStatus( response.SC_CREATED );
        }
        
        if (created) {
            response.addHeader( "Location", service.getIndexURI( request, indexNamesBuffer.toString() ).toString() );
        }
        else {
            throw new InsufficientDataException( "No indices to be added" );
        }
        
        
        
        XMLController.acknowledge( c );
    }
    
    
    
    
    public static Entry[] getEntries (LuceneContext c, LuceneRequest request)
        throws
            TransformerConfigurationException, TransformerException,
            ParserConfigurationException, SAXException, IOException, AtomParseException
    {
        List<Entry> entries = new LinkedList<Entry>();
        
        Feed  feed  = request.getFeed();
        Entry entry = request.getEntry();
        
        if (feed != null) {
            entries.addAll( feed.getEntries() );
        }
        
        if (entry != null) {
            entries.add( entry );
        }
        
        return entries.toArray( new Entry[]{} );
    }
    
    
    
    
    
    /**
     * Converts a Lucene web service into an Atom introspection document.
     * 
     * @param service The web service to be converted
     * @param request a Lucene request to govern URL generation
     * @return An Atom introspection document
     * @throws IndicesNotFoundException
     * @throws IOException
     */
    
    public static IntrospectionDocument asIntrospectionDocument (LuceneContext c, LuceneWebService service, LuceneRequest request)
        throws IndicesNotFoundException, IOException
    {
        IntrospectionDocument d = new IntrospectionDocument();
        
        Workspace w = new Workspace( service.getTitle() );
        
        List<LuceneIndex> indicesList = new ArrayList<LuceneIndex>( Arrays.asList( service.getIndexManager().getIndices() ) );
        Collections.sort( indicesList, new IndexComparator() );
        LuceneIndex[] indices = indicesList.toArray( new LuceneIndex[]{} );
        
        for (LuceneIndex index : indices) {
            // TODO: remove the adding of the empty path to make this work with Perl client!
            String href = service.getIndexURI( request, index ).withPath("").toString();
            
            String title = index.getTitle();
            if (title == null) {
                title = index.getName();
            }
            
            String accept = "application/atom+xml;type=entry";
            
            AtomCollection coll = new AtomCollection( title, href, accept );
            
            w.addCollection( coll );
        }
        
        d.addWorkspace( w );
        
        return d;
    }
    
    
    
    
    /**
     * Transforms a Lucene web service into an Atom feed.
     * 
     * @return An Atom feed
     * @throws IndicesNotFoundException
     * @throws IOException
     */
    
    public static Feed asFeed (LuceneContext c, LuceneWebService service, LuceneRequest request)
        throws IndicesNotFoundException, IOException
    {
        Feed feed = new Feed();
        
        feed.setTitle( "Lucene Web Service" );
        //feed.setUpdated( service.getLastModified() );
        feed.setID( service.getServiceURI( request ).toString() );
        
        //Iterator<LuceneIndex> indices = service.getIndexManager().getIndices().iterator();
        //while( indices.hasNext() )
        //  feed.addEntry( asEntry( service, indices.next() ) );
        
        List<LuceneIndex> list = Arrays.asList( service.getIndexManager().getIndices() );
        
        Collections.sort( list, new IndexComparator() );
        
        LuceneIndex[] indices = list.toArray( new LuceneIndex[]{} );
        
        for (LuceneIndex index : indices) {
            feed.addEntry( asEntry( c, service, request, index ) );
        }
        
        
        feed.addLink( Link.Alternate( service.getServiceURI( request ).toString() ) );
        
        return feed;
    }
    
    
    
    /**
     * Transforms a Lucene web service into an Atom Entry.
     * 
     * @param service The web service
     * @param request The request
     * @param index The index
     * @return An Atom entry
     * @throws IOException
     */
    
    public static Entry asEntry (LuceneContext c, LuceneWebService service, LuceneRequest request, LuceneIndex index)
        throws IOException
    {
        Entry entry = new Entry();
        
        entry.setTitle( index.getTitle() );
        entry.setID( service.getIndexURI( request, index ).toString() );
        
        
        String summary = null;
        
        try {
            summary = index.getSummary();
        }
        catch (FileNotFoundException fnfe) {
            summary = null;
        }
        
        if (summary == null) {
            int count = index.getDocumentCount();
            
            switch (count) {
                case 0:
                    summary = "Index '" + index.getName() + "' contains no documents.";
                    break;
                    
                case 1:
                    summary = "Index '" + index.getName() + "' contains 1 document.";
                    break;
                    
                default:
                    summary = "Index '" + index.getName() + "' contains " + index.getDocumentCount() + " documents.";
            }
        }
        
        entry.addLink( Link.Alternate( service.getIndexURI( request, index ).toString() ) );
        entry.setSummary( new net.lucenews.atom.Text( summary ) );
        
        return entry;
    }
    
    
    
}

class IndexComparator implements Comparator<LuceneIndex> {
    
    public IndexComparator () {
    }
    
    public int compare (LuceneIndex i1, LuceneIndex i2) {
        try {
            String title1 = i1.getTitle();
            String title2 = i2.getTitle();
            
            String comparable1 = title1 == null ? i1.getName() : title1;
            String comparable2 = title2 == null ? i2.getName() : title2;
            
            if (comparable1 == null && comparable1 == null) {
                return 0;
            }
            
            if (comparable1 != null && comparable2 != null) {
                return comparable1.toLowerCase().compareTo( comparable2.toLowerCase() );
            }
            
            if (comparable1 != null) {
                return 1;
            }
            
            if (comparable2 != null) {
                return -1;
            }
            
            return 0;
        }
        catch (IOException ioe) {
            return 0;
        }
    }
}
