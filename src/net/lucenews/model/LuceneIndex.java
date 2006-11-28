package net.lucenews.model;

import java.io.*;
import java.text.*;
import java.util.*;
import net.lucenews.*;
import net.lucenews.model.event.*;
import net.lucenews.model.exception.*;
import net.lucenews.opensearch.*;
import org.apache.log4j.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;



/**
 * LuceneIndex is the class used to encapsulate the concept
 * of a Lucene index stored on a file system.
 * Various functionalities include:
 * <ul>
 * <li>Ability to add an index</li>
 * <li>Ability to remove an index</li>
 * </ul>
 * 
 * @author  Adam Paynter
 * @version 0.2.0.01
 */

public class LuceneIndex {
    private static Map<File,LuceneIndex> managedIndices = new HashMap<File,LuceneIndex>();
    
    private Map<String,LuceneDocument> managedDocuments;
    
    private File directory;
    
    private IndexReader reader;
    private boolean     readerCheckedOut;
    
    private IndexWriter writer;
    private boolean     writerCheckedOut;
    
    private IndexSearcher searcher;
    
    private Properties properties;
    private long propertiesLastModified;
    
    private boolean refreshing;
    private long lastKnownVersion;
    
    private static List<LuceneIndexListener> listeners = new LinkedList<LuceneIndexListener>();
    
    
    
    
    
    
    
    /**
     * Adds a Lucene index listener to the global list
     * 
     * @param listener The listener to be added
     */
    
    public static void addIndexListener (LuceneIndexListener listener) {
        listeners.add( listener );
    }
    
    
    
    
    
    
    
    /**
     * Constructs a new index. The goal of this class is to guarantee
     * only one instance of the class exists for each, individual
     * directory on the file system. This is to help maintain consistency.
     * Therefore, this constructor is protected and will only be called
     * by the static {@link #retrieve retrieve} or {@link #create create}
     * methods whenever an instance reflecting a desired directory has not
     * already been constructed and stored in the <code>managedIndices</code>
     * map.
     * 
     * @param directory The directory in which the index resides
     * @throws IOException
     */
    
    protected LuceneIndex (File directory) throws IOException {
        this.directory = directory;
        managedDocuments = new HashMap<String,LuceneDocument>();
    }
    
    
    
    
    
    
    
    
    /**
     * Creates a Lucene index using the default analyzer. This involves
     * creating a directory if it does not already exist and placing
     * the initial segment files in it. If Lucene has detected the presence
     * of an index already residing within the specified directory,
     * an {@link net.lucenews.model.exception.IndexAlreadyExistsException}
     * exception will be thrown. A file called "index.properties" will be
     * written to the directory, storing some initial properties required
     * to effectively manage the index. Such properties include "index.analyzer",
     * which stores the class name of the index's default analyzer.
     * The static {@link net.lucenews.model.event.LuceneIndexListener} objects
     * are then attached to the object. All listeners are then informed of the
     * creation.
     * 
     * @param  directory The directory where the index is to be created
     * @return           A <code>LuceneIndex</code> representing the created index
     * @throws           IndexAlreadyExistsException if an index already resides in the directory
     */
    
    public static LuceneIndex create (File directory)
        throws IndexAlreadyExistsException, IOException
    {
        return create( directory, new StandardAnalyzer() );
    }
    
    
    
    
    
    
    
    
    /**
     * Creates a Lucene index using the provided analyzer. This involves
     * creating a directory if it does not already exist and placing
     * the initial segment files in it. If Lucene has detected the presence
     * of an index already residing within the specified directory,
     * an {@link net.lucenews.model.exception.IndexAlreadyExistsException}
     * exception will be thrown. A file called "index.properties" will be
     * written to the directory, storing some initial properties required
     * to effectively manage the index. Such properties include "index.analyzer",
     * which stores the class name of the index's default analyzer.
     * The static {@link net.lucenews.model.event.LuceneIndexListener} objects
     * are then attached to the object. All listeners are then informed of the
     * creation.
     * 
     * @param directory the directory where the index is to be created
     * @param analyzer  the analyzer associated with this index
     * @return          a <code>LuceneIndex</code> representing the created index
     * @throws          IndexAlreadyExistsException if an index already resides in the directory
     */
    
    public static LuceneIndex create (File directory, Analyzer analyzer)
        throws IndexAlreadyExistsException, IOException
    {
        if (IndexReader.indexExists( directory )) {
            throw new IndexAlreadyExistsException(String.valueOf( directory ));
        }
        
        if (!managedIndices.containsKey(directory)) {
            LuceneIndex index = new LuceneIndex( directory );
            
            // Actually write the initial index
            IndexWriter writer = new IndexWriter( directory, analyzer, true );
            writer.close();
            
            if (analyzer != null) {
                index.getProperties().getProperty( "index.analyzer", analyzer.getClass().getCanonicalName() );
                index.storeProperties();
            }
            
            managedIndices.put( directory, index );
        }
        
        LuceneIndex index = managedIndices.get( directory );
        
        LuceneIndexEvent event = new LuceneIndexEvent( index );
        Iterator<LuceneIndexListener> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().indexCreated( event );
        }
        
        return index;
    }
    
    
    
    
    
    
    
    /**
     * Retrieves a <code>LuceneIndex</code> corresponding to the
     * given directory. If Lucene does not detect an index residing
     * within the directory, an {@link net.lucenews.model.exception.IndexNotFoundException}
     * exception will be thrown. Otherwise, method will attempt to
     * retrieve an instance out of the static <code>managedIndices</code>
     * map, using the directory as the key. If an entry corresponding
     * to the directory is not found within the map, the protected
     * {@link #LuceneIndex(File)} constructor is called, and subsequently
     * stored in the aforementioned map for later use.
     * 
     * @param  directory the directory where the desired index resides
     * @return           a <code>LuceneIndex</code> corresponding to the given directory
     * @throws           IndexNotFoundException if no index resides in the given directory
     */
    
    public static LuceneIndex retrieve (File directory)
        throws IndexNotFoundException, IOException
    {
        if (!IndexReader.indexExists( directory )) {
            throw new IndexNotFoundException(String.valueOf( directory ));
        }
        
        if (!managedIndices.containsKey( directory )) {
            managedIndices.put( directory, new LuceneIndex( directory ) );
        }
        
        return managedIndices.get( directory );
    }
    
    
    
    
    
    
    
    /**
     * Deletes the index residing within the given directory on the
     * file system.
     * 
     * @param directory the directory within which the index to be deleted resides
     * @return          true if all files were successfully deleted, false otherwise
     * @see             #delete()
     */
    
    public static boolean delete (File directory)
        throws IndexNotFoundException, IllegalActionException, IOException
    {
        return retrieve( directory ).delete();
    }
    
    
    
    
    
    
    
    /**
     * Deletes the index from its directory on the file system.
     * Resources are first closed before attempting to procede 
     * with deletion. Files to be deleted include those specified
     * by the {@link org.apache.lucene.store.FSDirectory#list}
     * method of the {@link org.apache.lucene.store.FSDirectory}
     * class. All attached <code>LuceneIndexListener</code> objects
     * are informed of the deletion.
     * 
     * @return true if all files were deleted, false otherwise
     * @throws IOException if files could not be deleted.
     */
    
    public boolean delete ()
        throws IllegalActionException, IOException
    {
        if (isReadOnly()) {
            throw new IllegalActionException( "Index is read-only." );
        }
        
        close();
        
        IndexReader reader = getIndexReader();
        
        boolean wasSuccess = true;
        
        if (reader.directory() instanceof FSDirectory) {
            String[] fileNames = reader.directory().list();
            for( int i = 0; i < fileNames.length; i++ ) {
                File file = new File( getDirectory(), fileNames[ i ] );
                
                if( !file.equals(getPropertiesFile()) && !file.delete() ) {
                    wasSuccess = false;
                }
            }
        }
        
        putIndexReader( reader );
        
        managedIndices.clear();
        
        LuceneIndexEvent event = new LuceneIndexEvent( this );
        Iterator<LuceneIndexListener> iterator = listeners.iterator();
        while( iterator.hasNext() ) {
            iterator.next().indexDeleted( event );
        }
        
        return wasSuccess;
    }
    
    
    
    
    public boolean exists () {
        return IndexReader.indexExists( getDirectory() );
    }
    
    
    
    
    
    /**
     * Retrieves the directory within which the
     * index resides.
     * 
     * @return The index's directory
     */
    
    public File getDirectory () {
        return directory;
    }
    
    
    
    public org.apache.lucene.store.Directory getLuceneDirectory () throws IOException {
        IndexReader reader = getIndexReader();
        org.apache.lucene.store.Directory dir = reader.directory();
        putIndexReader( reader );
        return dir;
    }
    
    
    
    
    
    
    /**
     * The title of the index. Reflects the "index.title"
     * property. If property is not present, defaults to
     * the index name.
     * 
     * @return the title of the index
     * @throws IOException if properties could not be loaded
     */
    
    public String getTitle () throws IOException {
        return getProperties().getProperty( "index.title", getName() );
    }
    
    
    
    
    
    
    /**
     * The name of the index. Corresponds to the name of the
     * directory in which it resides.
     * 
     * @return the name of the index
     */
    
    public String getName () {
        return directory.getName();
    }
    
    
    
    
    /**
     * Retrieves an index reader for this index. The reader
     * returned is guaranteed to reflect the most recent
     * data contained within the index on the file system.
     * 
     * @return an index reader
     * @throws IOException
     * @see    #putIndexReader(IndexReader)
     * @see    #putIndexReader(IndexReader,boolean)
     */
    
    public IndexReader getIndexReader () throws IOException {
        return IndexReader.open( getDirectory() );
    }
    
    
    
    /**
     * Puts back an <code>IndexReader</code> that was
     * presumably checked out by the {@link #getIndexReader}
     * method. Assumes no changes to the index have been made
     * by this object (for example, deletion of documents) and,
     * therefore, no commits take place.
     * 
     * @param reader an <code>IndexReader</code> object to be checked in
     * @throws       IOException
     * @see          #getIndexReader
     * @see          #putIndexReader(IndexReader,boolean)
     */
    
    public void putIndexReader (IndexReader reader) throws IOException {
        putIndexReader( reader, reader.hasDeletions() );
    }
    
    
    
    /**
     * Puts back an <code>IndexReader</code> that was
     * presumably checked out by the {@link #getIndexReader}
     * method. If the <code>modified</code> parameter
     * is true, changes are committed to the index and
     * this object's data is refreshed.
     * 
     * @param modified whether or not the <code>IndexReader</code> had modified the index in any way
     * @throws         IOException if an exception was encountered while closing reader or refreshing data
     * @see            #getIndexReader
     */
    
    public void putIndexReader (IndexReader reader, boolean modified) throws IOException {
        reader.close();
        reader = null;
        
        if (modified) {
            refresh();
        }
    }
    
    
    
    /**
     * Retrieves an {@link org.apache.lucene.index.IndexWriter} object
     * suitable for performing writes to this index. Writer is constructed
     * using the <code>Analyzer</code> returned by {@link #getAnalyzer}, the
     * class of which is governed by the "index.analyzer" property within the
     * index.properties file, located within the same directory as the index itself.
     * 
     * @return an <code>IndexWriter</code> object
     * @throws IOException if the index.properties file could not be read or the writer could not be constructed
     * @see    #getProperties
     * @see    #putIndexWriter(IndexWriter)
     * @see    #putIndexWriter(IndexWriter,boolean)
     */
    
    public IndexWriter getIndexWriter () throws IOException {
        guaranteeFreshData();
        if (writer == null) {
            writer = new IndexWriter( getDirectory(), getAnalyzer(), false );
        }
        /**
        String writeLockTimeout = getProperty("indexwriter.writelocktimeout");
        if ( writeLockTimeout != null ) {
            writer.setWriteLockTimeout( Long.valueOf(writeLockTimeout) );
        }
        writer.setWriteLockTimeout( 10000 );
        */
        return writer;
    }
    
    
    
    /**
     * Puts back an <code>IndexWriter</code> object that was
     * presumably checked out by the {@link #getIndexWriter}
     * method. Assumes changes were made to the index (for
     * example, addition of documents) and, therefore, commits
     * the changes.
     * 
     * @param writer an <code>IndexWriter</code> object to be checked in
     * @throws       IOException if the <code>IndexWriter</code> could not be closed or the data not refreshed
     * @see          #getIndexWriter
     * @see          #putIndexWriter(IndexWriter,boolean)
     */
    
    public void putIndexWriter (IndexWriter writer) throws IOException {
        putIndexWriter( writer, true );
    }
    
    
    
    /**
     * Puts back an <code>IndexWriter</code> object that was
     * presumably checked out by the {@link #getIndexWriter}
     * method. If the <code>modified</code> parameter is true,
     * changes are committed to the index and data is refreshed.
     * 
     * @param writer   an <code>IndexWriter</code> to be checked in
     * @param modified whether or not the index was modified by the provided <code>IndexWriter</code>
     * @throws         IOException if an exception was encountered while refreshing data.
     * @see            #getIndexWriter
     * @see            #putIndexWriter(IndexWriter)
     */
    
    public void putIndexWriter (IndexWriter writer, boolean modified) throws IOException {
        if (modified) {
            refresh();
        }
    }
    
    
    
    /**
     * Retrieves an {@link org.apache.lucene.search.IndexSearcher} object
     * capable of performing searches on this index. Data searched by 
     * retrieved object is guaranteed to be fresh.
     * 
     * @throws IOException if the <code>IndexSearcher</code> could not be constructed
     * @see    #putIndexSearcher(IndexSearcher)
     */
    
    public IndexSearcher getIndexSearcher () throws IOException {
        guaranteeFreshData();
        //if (searcher == null) {
            searcher = new IndexSearcher( getDirectory().getAbsolutePath() );
        //}
        return searcher;
    }
    
    
    
    /**
     * Puts back an <code>IndexSearcher</code> object that was
     * presumably checked out by the {@link #getIndexSearcher}.
     * Does not do anything, but is included for well-roundedness.
     * 
     * @param searcher the <code>IndexSearcher</code> object being checked back in
     * @throws         IOException if any sort of implementation gets added (Lucene object always through IOException, ALWAYS!)
     * @see            #getIndexSearcher
     */
    
    public void putIndexSearcher (IndexSearcher searcher) throws IOException {
        searcher = null;
    }
    
    
    
    
    /**
     * Determines whether the index possesses a document corresponding
     * to the given identifier. This is accomplished by enumerating through
     * the {@link org.apache.lucene.index.TermDocs} object returned by 
     * calling {@link org.apache.lucene.index.IndexReader#termDocs(Term)}
     * on the term [ {@link #getIdentifyingField} ] => [ <code>identifier</code> ].
     *
     * @param identifier       the identifier of the document in question
     * @param ignoreDuplicates by setting this to true, multiple document exceptions are suppressed
     * @return           true if the document was found, false otherwise
     * @throws           IOException
     * @throws           MultipleValueException if more than one document exists for the given identifier
     * @see              #getIdentifyingField
     */
    
    public boolean hasDocument (String identifier) throws IOException {
        return hasDocument( identifier, false );
    }
    
    public boolean hasDocument (String identifier, boolean ignoreDuplicates) throws IOException {
        Term term = new Term( getIdentifierFieldName(), identifier );
        
        boolean _hasDocument = false;
        
        IndexReader reader = getIndexReader();
        TermDocs documents = reader.termDocs( term );
        
        while (documents.next()) {
            LuceneDocument _document = asLuceneDocument( reader.document( documents.doc() ) );
            
            if ( isDocumentCorrectlyIdentified( _document, identifier ) ) {
                if ( _hasDocument ) {
                    if ( !ignoreDuplicates ) {
                        throw new MultipleValueException( "Multiple documents exist for identifier '" + identifier + "'" );
                    }
                }
                else {
                    _hasDocument = true;
                }
            }
        }
        
        putIndexReader( reader );
        
        return _hasDocument;
    }
    
    
    
    /**
     * Gets the document corresponding to the given identifier.
     * That is, a document containing the [ {@link #getIdentifyingField()} ]
     * => [ <code>identifier</code> ] term. If more than one such
     * document is found, a {@link net.lucenews.model.exception.MultipleValueException}
     * is thrown. Document's index is set to this index prior to
     * returning. This is accessed via the {@link net.lucenews.model.LuceneDocument#getIndex}
     * method.
     *
     * @param identifier the identifier of the desired document
     * @return           the document corresponding to the given identifier
     * @throws           DocumentNotFoundException if the document could not be found
     * @throws           IOException if an exception was encountered while reading the index
     * @throws           MultipleValueException if more than one document exists for the given identifier
     * @see              #getDocuments(String...)
     */
    
    public LuceneDocument getDocument (String identifier)
        throws DocumentNotFoundException, IOException
    {
        IndexReader reader = getIndexReader();
        
        LuceneDocument document = null;
        
        if ( !hasIdentifierFieldName() ) {
            throw new DocumentNotFoundException( identifier );
        }
        
        Term term = new Term( getIdentifierFieldName(), identifier );
        
        TermDocs documents = reader.termDocs( term );
        while ( documents.next() ) {
            LuceneDocument _document = getDocument( documents.doc(), reader );
            
            if ( isDocumentCorrectlyIdentified( _document, identifier ) ) {
                if ( document == null ) {
                    document = _document;
                }
                else {
                    throw new MultipleValueException( "Multiple documents exist for identifier '" + identifier + "'" );
                }
            }
        }
        
        if ( document == null ) {
            throw new DocumentNotFoundException( identifier );
        }
        
        putIndexReader( reader );
        
        managedDocuments.put( identifier, document );
        
        document.setIndex( this );
        
        return document;
    }
    
    public LuceneDocument getDocument (int number)
        throws DocumentNotFoundException, IOException
    {
        LuceneDocument document = null;
        
        IndexReader reader = getIndexReader();
        document = getDocument( number, reader );
        putIndexReader( reader );
        
        return document;
    }
    
    public LuceneDocument getDocument (int number, IndexReader reader)
        throws DocumentNotFoundException, IOException
    {
        if ( reader.isDeleted( number ) ) {
            throw new DocumentNotFoundException("Document number " + number + " cannot be found");
        }
        
        LuceneDocument document = asLuceneDocument( reader.document( number ) );
        
        document.setIndex( this );
        document.setNumber( number );
        
        return document;
    }
    
    
    
    
    /**
     * Gets documents corresponding to the given identifiers.
     * Iterates through the array, calling {@link #getDocument(String)}
     * on each. Throws a {@link net.luenews.model.exception.DocumentsNotFound}
     * exception if any of the documents could not be found.
     * 
     * @param identifiers the identifiers of desired documents
     * @return            an array of documents corresponding to the given identifiers
     * @throws            DocumentsNotFoundException if documents could not be found
     * @throws            IOException if an exception was encountered while reading the index
     * @see               #getDocument(String)
     */
    
    public LuceneDocument[] getDocuments (String... identifiers)
        throws DocumentsNotFoundException, IOException
    {
        LuceneDocument[] documents = new LuceneDocument[ identifiers.length ];
        
        for( int i = 0; i < documents.length; i++ ) {
            documents[ i ] = getDocument( identifiers[ i ] );
        }
        
        return documents;
    }
    
    
    
    
    
    
    /**
     * Retrieves a list of all documents within the index.
     * The list is not guaranteed to be returned in any
     * particular order, however, it is safe to presume
     * that documents have come back in the order in which
     * they appear within the index
     * 
     * @return an array of documents
     * @throws IOException if an exception was encountered while reading the index
     * @see    #getDocument(String)
     * @see    #getDocuments(String...)
     */
    
    public LuceneDocument[] getDocuments () throws IOException {
        IndexReader reader = getIndexReader();
        
        List<LuceneDocument> documents = new ArrayList<LuceneDocument>( reader.numDocs() );
        
        for (int i = 0; i < reader.maxDoc(); i++) {
            Document document = null;
            
            try {
                document = reader.document( i );
            }
            catch(IllegalArgumentException iae) {
                document = null;
            }
            
            if (document != null) {
                documents.add( new LuceneDocument( document ) );
            }
        }
        
        putIndexReader( reader );
        
        return documents.toArray( new LuceneDocument[]{} );
    }
    
    
    
    
    
    
    
    
    /**
     * Adds the given document to the index.
     *
     * @param document the document to be added
     * @throws         DocumentAlreadyExistsException if a document with the same identifier already exists in the index
     * @throws         InvalidIdentifierException if the document does not possess a valid identifier
     * @throws         InsufficientDataException if the document could not be identified
     * @throws         IOException if an exception was encountered while manipulating the index
     * @see            #addDocuments(LuceneDocument...)
     * @see            #removeDocument(String)
     * @see            #removeDocument(LuceneDocument)
     * @see            #removeDocuments(String...)
     * @see            #removeDocuments(LuceneDocument...)
     * @see            #updateDocument(LuceneDocument)
     * @see            #updateDocuments(LuceneDocument...)
     */
    
    public LuceneDocument addDocument (LuceneDocument document)
        throws
            IllegalActionException, InvalidIdentifierException,
            DocumentAlreadyExistsException, InsufficientDataException, IOException
    {
        if (isReadOnly()) {
            throw new IllegalActionException( "Index is read-only." );
        }
        
        if (hasDocument( getIdentifier( document ) )) {
            throw new DocumentAlreadyExistsException( getIdentifier( document ) );
        }
        
        if (!hasValidIdentifier( document )) {
            throw new InvalidIdentifierException( getIdentifier( document ) );
        }
        
        try {
            setLastModified( document, Calendar.getInstance(), false );
        }
        catch(DocumentNotFoundException dnfe) {
        }
        
        Document _document = asDocument( document );
        
        IndexWriter writer = getIndexWriter();
        writer.addDocument( _document );
        putIndexWriter( writer );
        
        return document;
    }
    
    
    
    
    
    
    /**
     * Adds documents to the index.
     * 
     * @param documents The documents to be added
     * @throws InvalidIdentifierException if documents possess invalid identifiers
     * @throws DocumentAlreadyExistsException if documents already exist in the index
     * @throws InsufficientDataException
     * @throws IOException
     */
    
    public LuceneDocument[] addDocuments (LuceneDocument... documents)
        throws
            IllegalActionException, InvalidIdentifierException,
            DocumentAlreadyExistsException, InsufficientDataException, IOException
    {
        LuceneDocument[] addedDocuments = new LuceneDocument[ documents.length ];
        for (int i = 0; i < documents.length; i++) {
            addedDocuments[ i ] = addDocument( documents[ i ] );
        }
        return addedDocuments;
    }
    
    
    
    
    
    /**
     * Removes the document corresponding to the given identifier from the index.
     *
     * @param identifier The identifier of the document to be removed
     * @throws DocumentNotFoundException if the document was not found
     * @throws InsufficientDataException if the document could not be identified
     * @throws IOException
     */
    
    public LuceneDocument removeDocument (String identifier)
        throws IllegalActionException, DocumentNotFoundException, IOException
    {
        if (isReadOnly()) {
            throw new IllegalActionException( "Index is read-only." );
        }
        
        if (!hasDocument( identifier )) {
            throw new DocumentNotFoundException( identifier );
        }
        
        LuceneDocument document = null;
        
        Term term = new Term( getIdentifierFieldName(), identifier );
        
        IndexReader reader = getIndexReader();
        
        TermDocs documents = reader.termDocs( term );
        while (documents.next()) {
            document = new LuceneDocument( reader.document( documents.doc() ) );
            reader.deleteDocument( documents.doc() );
        }
        documents.close();
        
        putIndexReader( reader, true );
        
        return document;
    }
    
    
    
    
    /**
     * Removes documents from the index.
     * 
     * @param identifiers The identifiers of the documents to be removed
     * @return The documents which were removed
     * @throws DocumentNotFoundException if documents could not be found
     * @throws IOException
     */
    
    public LuceneDocument[] removeDocuments (String... identifiers)
        throws IllegalActionException, DocumentNotFoundException, IOException
    {
        LuceneDocument[] documents = new LuceneDocument[ identifiers.length ];
        for (int i = 0; i < identifiers.length; i++) {
            documents[ i ] = removeDocument( identifiers[ i ] );
        }
        return documents;
    }
    
    
    
    
    
    /**
     * Removes the given document from the index. Note that it simply removes any documents
     * having the same identifier as the one passed to the method.
     *
     * @param document The document to be removed
     * @throws DocumentNotFoundException if the document was not found
     * @throws InsufficientDataException if the document could not be identified
     * @throws IOException
     */
    
    public LuceneDocument removeDocument (LuceneDocument document)
        throws
            IllegalActionException, DocumentNotFoundException,
            InsufficientDataException, IOException
    {
        return removeDocument( getIdentifier( document ) );
    }
    
    
    
    
    /**
     * Removes documents.
     */
    
    public LuceneDocument[] removeDocuments (LuceneDocument... documents)
        throws
            IllegalActionException, DocumentNotFoundException,
            InsufficientDataException, IOException
    {
        LuceneDocument[] removed = new LuceneDocument[ documents.length ];
        for (int i = 0; i < documents.length; i++) {
            removed[ i ] = removeDocument( documents[ i ] );
        }
        return removed;
    }
    
    
    
    
    
    
    /**
     * Updates the document within the index.
     *
     * @param document The document to update
     * @throws InsufficientDataException if the document could not be identified
     * @throws IOException
     */
    
    public void updateDocument (LuceneDocument document)
        throws
            IllegalActionException, InvalidIdentifierException,
            DocumentNotFoundException, InsufficientDataException, IOException
    {
        removeDocument( document );
        
        try {
            addDocument( document );
        }
        catch(DocumentAlreadyExistsException daee) {
        }
    }
    
    
    
    
    /**
     * Updates various documents.
     */
    
    public void updateDocuments (LuceneDocument... documents)
        throws
            IllegalActionException, InvalidIdentifierException,
            DocumentsNotFoundException, InsufficientDataException, IOException
    {
        for( int i = 0; i < documents.length; i++ ) {
            updateDocument( documents[ i ] );
        }
    }
    
    
    
    
    
    
    
    /**
     * Determines whether or not an index is read only. In the
     * absence of an 'index.readonly' property, the index is assumed
     * to NOT be read-only (that is, documents may be added).
     * 
     * @return true if the index is read-only, false otherwise
     * @throws      IOException
     */
    
    public boolean isReadOnly ()
        throws IOException
    {
        String _readOnly = ServletUtils.clean( getProperty( "index.readonly" ) );
        
        if (_readOnly == null) {
            return false;
        }
        
        return ServletUtils.parseBoolean( _readOnly );
    }
    
    
    
    /**
     * Retrieves the identifying field for a particular field value.
     * 
     * @return the identifying field corresponding to the value
     * @throws IOException
     */
    
    public Field getIdentifierField (String value)
        throws IOException
    {
        return new Field( getIdentifierFieldName(), value, Field.Store.YES, Field.Index.UN_TOKENIZED );
    }
    
    
    
    /**
     * Retrieves the identifying field for the index.
     * 
     * @return the identifying field for the index
     * @throws IOException
     */
    
    public String getIdentifierFieldName ()
        throws IOException
    {
        String name = null;
        if ( name == null ) { name = getProperty("document.field.<identifier>"); }
        if ( name == null ) { name = getProperty("document.field.identifier");   }
        if ( name == null ) { name = getProperty("document.identifier");         }
        if ( name == null ) { name = "id";                                       }
        return name;
    }
    
    
    
    /**
     * Determines whether or not the index has an identifying field.
     * 
     * @return true if the index has an identifying field, false otherwise
     */
    
    public boolean hasIdentifierFieldName () {
        try {
            return getIdentifierFieldName() != null;
        }
        catch(IOException ioe) {
            return false;
        }
    }
    
    
    
    
    /**
     * Manipulates the given document to make it identifiable.
     * This involves removing any fields claiming to be identifying
     * fields and replacing them with a single, identifying field.
     * 
     * @param document The document to modify
     * @param identifier The identifier to associate with the document
     * @throws IOException
     */
    
    public void identifyDocument (LuceneDocument document, String identifier)
        throws IOException
    {
        document.removeFields( getIdentifierFieldName() );
        document.add( getIdentifierField( identifier ) );
    }
    
    
    
    
    
    /**
     * Determines whether or not a document is identified.
     * 
     * @param document The document to check
     * @return True if the document is identified, false otherwise
     * @throws IOException
     */
    
    public boolean isDocumentIdentified (LuceneDocument document)
        throws IOException
    {
        String[] values = document.getValues( getIdentifierFieldName() );
        
        if (values == null) {
            return false;
        }
        
        switch (values.length) {
            case 0:
                return false;
            
            case 1:
                return true;
            
            default:
                throw new MultipleValueException( "Document has many identifying fields" );
        }
    }
    
    
    
    
    /**
     * Determines whether or not a document possesses a valid
     * identifier. The identifier is checked against the regular
     * expression contained within the "document.identifier.validator"
     * property. If such a regular expression does not exist,
     * method will return true.
     * 
     * @param document The document to check
     * @return True if the document has a valid identifier
     * @throws IOException
     * @throws InsufficientDataException
     */
    
    public boolean hasValidIdentifier (LuceneDocument document)
        throws IOException
    {
        try {
            String identifier = getIdentifier( document );
            
            String regex = getProperties().getProperty( "document.identifier.validator" );
            
            if (regex == null) {
                return true;
            }
            else {
                return identifier.matches( regex );
            }
        }
        catch(InsufficientDataException ide) {
            return false;
        }
    }
    
    
    
    
    
    
    
    /**
     * Determines whether or not a given document possesses an identifier
     * equal to the given identifier.
     * 
     * @param document The document being checked
     * @param identifier The identifier being checked for
     * @return True if the document's identifier matches the given identifier
     * @throws IOException
     */
    
    public boolean isDocumentCorrectlyIdentified (LuceneDocument document, String identifier)
        throws IOException
    {
        try {
            if (isDocumentIdentified( document )) {
                return getIdentifier( document ).equals( identifier );
            }
        }
        catch(InsufficientDataException ide) {
            return false;
        }
        
        return false;
    }
    
    
    
    
    /**
     * Retrieves the identifier from a document.
     * 
     * @param document The document to retrieve the identifier from
     * @return The identifier as specified by the document
     * @throws InsufficientDataException if no identifier was found
     * @throws IOException
     */
    
    public String getIdentifier (LuceneDocument document)
        throws InsufficientDataException, IOException
    {
        if ( isDocumentIdentified( document ) ) {
            return document.get( getIdentifierFieldName() );
        }
        
        throw new InsufficientDataException( "Document is not identified." );
    }
    
    
    
    
    /**
     * Retrieves a term reflecting a document's identifier.
     * 
     * @param document The document in question
     * @return A term reflecting a document's identifier
     * @throws InsufficientDataException
     * @throws IOException
     */
    
    public Term getIdentifierTerm (LuceneDocument document)
        throws InsufficientDataException, IOException
    {
        return new Term( getIdentifierFieldName(), getIdentifier( document ) );
    }
    
    
    
    
    
    /**
     * Retrieves an org.apache.lucene.document.Document object
     * corresponding to the given net.lucenews.model.LuceneDocument
     * object.
     * 
     * @param document A net.lucenews.model.LuceneDocument object
     * @return The corresponding org.apache.lucene.document.Document object
     */
    
    public Document asDocument (LuceneDocument document) {
        Document _document = document.getDocument();
        return _document;
    }
    
    
    
    
    /**
     * Retrieves an net.lucenews.model.Document object
     * corresponding to the given org.apache.lucene.document.Document
     * object.
     * 
     * @param document An org.apache.lucene.document.Document object
     * @return The corresponding net.lucenews.model.LuceneDocument object
     */
    
    public LuceneDocument asLuceneDocument (Document document) {
        return new LuceneDocument( document );
    }
    
    
    
    
    
    /**
     * Retrieves the analyzer for the index.
     * 
     * @return The analyzer for the index
     * @throws IOException
     */
    
    public Analyzer getAnalyzer ()
        throws IOException
    {
        Analyzer analyzer = LuceneUtils.parseAnalyzer( getProperty( "index.analyzer" ) );
        if( analyzer == null ) {
            return new StandardAnalyzer();
        }
        return analyzer;
    }
    
    
    
    
    /**
     * Invokes IndexReader.getCurrentVersion(File) as per Lucene
     * documentation:
     * 
     *  " Reads version number from segments files.
     *    The version number counts the number of 
     *    changes of the index. "
     *
     * @throws IOException if segments file cannot be read
     */
    
    public long getCurrentVersion ()
        throws IOException
    {
        return IndexReader.getCurrentVersion( getDirectory() );
    }
    
    
    
    
    
    /**
     * Retrieves the number of documents contained within the index.
     *
     * @return The number of documents within the index
     * @throws IOException
     */
    
    public int getDocumentCount ()
        throws IOException
    {
        int documentCount;
        
        IndexReader reader = getIndexReader();
        documentCount = reader.numDocs();
        putIndexReader( reader );
        
        return documentCount;
    }
    
    
    
    
    
    /**
     * Returns a list of all unique field names that exist in the index.
     * 
     * @return A <tt>Collection&lt;String&gt;</tt> of unique field names
     * @throws IOException if there is a problem with accessing the index
     */
    
    public Collection<String> getFieldNames () throws IOException {
        Collection<String> fieldNames = new TreeSet<String>();
        
        IndexReader reader = getIndexReader();
        
        Iterator<?> indexFieldNames = reader.getFieldNames( IndexReader.FieldOption.ALL ).iterator();
        while (indexFieldNames.hasNext()) {
            Object indexFieldName = indexFieldNames.next();
            if (indexFieldName instanceof String) {
                fieldNames.add( (String) indexFieldName );
            }
        }
        
        putIndexReader( reader );
        
        return fieldNames;
    }
    
    
    
    
    
    /**
     * Invokes IndexWriter.optimize() method to optimize the
     * index.
     * 
     * @throws IOException
     */
    
    public void optimize ()
        throws IOException
    {
        IndexWriter writer = getIndexWriter();
        writer.optimize();
        putIndexWriter( writer );
    }
    
    
    
    
    
    
    /**
     * Refreshes the index with current data.
     * Reader and writer are closed, priming
     * the index for the actual refreshing of
     * data to take place the next time one of
     * them is requested.
     * 
     * @throws IOException
     */
    
    public void refresh ()
        throws IOException
    {
        close();
    }
    
    
    
    /**
     * Closes all resources associated with the index.
     * 
     * @throws IOException
     */
    
    public void close ()
        throws IOException
    {
        IOException ioe = null;
        
        // Reader
        if (reader != null) {
            try {
                reader.close();
                reader = null;
            }
            catch(IOException ioeReader) {
                ioe = ioeReader;
            }
        }
        
        // Writer
        if (writer != null) {
            try {
                writer.close();
                writer = null;
            }
            catch(IOException ioeWriter) {
                ioe = ioeWriter;
            }
        }
        
        // Searcher
        if (searcher != null) {
            try {
                searcher.close();
                searcher = null;
            }
            catch(IOException ioeSearcher) {
                ioe = ioeSearcher;
            }
        }
        
        if (ioe != null) {
            throw ioe;
        }
    }
    
    
    
    /**
     * Guarantees that the data within the index remains current.
     * This will allow the index to remain synchronized even if other
     * applications are accessing the same index.
     * 
     * @throws IOException if segments file cannot be read
     */
    
    public void guaranteeFreshData () throws IOException {
        if (lastKnownVersion < getCurrentVersion()) {
            refresh();
        }
    }
    
    
    
    /**
     * Retrieves the default fields associated with the index.
     * 
     * @return The default fields associated with the index
     * @throws IOException
     */
    
    public String[] getDefaultFieldNames () throws IOException {
        String[] defaultFields = null;
        
        if (defaultFields == null) { defaultFields = ServletUtils.split( getProperty("document.fields.<default>") ); }
        if (defaultFields == null) { defaultFields = ServletUtils.split( getProperty("document.fields.default") );   }
        if (defaultFields == null) { defaultFields = ServletUtils.split( getProperty("document.defaultfields") );    }
        if (defaultFields == null) { defaultFields = ServletUtils.split( getProperty("document.defaultfield") );     }
        
        return defaultFields;
    }
    
    public QueryParser.Operator getDefaultOperator () throws IOException {
        return LuceneUtils.parseOperator( getProperty( "index.defaultoperator" ) );
    }
    
    
    
    /**
     * Retrieves a summary of the index.
     * 
     * @return A summary of the index
     * @throws IOException
     */
    
    public String getSummary () throws IOException {
        return getProperties().getProperty( "document.summary", "summary" );
    }
    
    
    
    
    
    /**
     * Retrieves a summary of the given document.
     * 
     * @param document The document to get the summary of
     * @return The summary of the given document
     * @throws IOException
     */
    
    public String getSummary (LuceneDocument document) throws IOException {
        return document.get( getSummary() );
    }
    
    
    
    
    /**
     * Retrieves the title of the given document.
     * 
     * @param document The document to get the title of
     * @return The title of the document
     * @throws IOException
     */
    
    public String getTitle (LuceneDocument document) throws IOException {
        // template
        String template = getTitleTemplate();
        if ( template != null ) {
            return ServletUtils.parse( template, asProperties( document ) );
        }
        
        // field
        String fieldName = getTitleFieldName();
        if ( fieldName != null ) {
            return document.get( fieldName );
        }
        
        return null;
    }
    
    public String getTitleFieldName () throws IOException {
        String fieldName = null;
        if ( fieldName == null ) { fieldName = getProperty("document.title.field"); }
        if ( fieldName == null ) { fieldName = getProperty("document.title");       }
        return fieldName;
    }
    
    public String getTitleTemplate () throws IOException {
        String template = null;
        if ( template == null ) { template = getProperty("document.title.template"); }
        if ( template == null ) { template = getProperty("document.title");          }
        return template;
    }
    
    
    
    /**
     * Retrieves the file in which index-related properties are stored.
     * 
     * @return The file in which index-related properties are stored.
     */
    
    public File getPropertiesFile () {
        return new File( getDirectory(), "index.properties" );
    }
    
    
    
    
    /**
     * Loads properties from properties file.
     * 
     * @throws IOException
     */
    
    public void loadProperties () throws IOException {
        properties = new Properties();
        
        File file = getPropertiesFile();
        
        try {
            java.io.InputStream in = new FileInputStream( file );
            properties.load( in );
            in.close();
        }
        catch(FileNotFoundException fnfe) {
            storeProperties();
        }
        
        propertiesLastModified = file.lastModified();
    }
    
    
    
    /**
     * Stores current properties to properties file.
     * 
     * @throws IOException
     */
    
    public void storeProperties () throws IOException {
        storeProperties( null );
    }
    
    
    
    /**
     * Stores current properties to properties file
     * with the given comment.
     * 
     * @throws IOException
     */
    
    public void storeProperties (String comment) throws IOException {
        if (properties == null) {
            return;
        }
        
        java.io.OutputStream out = new FileOutputStream( getPropertiesFile() );
        properties.store( out, comment );
        out.close();
    }
    
    
    
    
    /**
     * Gets the current properties of the index.
     * 
     * @return The current properties
     * @throws IOException
     */
    
    public Properties getProperties () throws IOException {
        guaranteeFreshProperties();
        return properties;
    }
    
    
    
    
    /**
     * Gets the property.
     * 
     * @return the property
     * @throws IOException
     */
    
    public String getProperty (String name) throws IOException {
        Properties properties = getProperties();
        if (properties == null) {
            return null;
        }
        return properties.getProperty( name );
    }
    
    
    
    /**
     * Adds the given properties to the index's current set
     * of properties.
     * 
     * @param properties the properties to add
     * @throws           IOException if an exception was encountered while retrieving or storing the properties
     */
    
    public void addProperties (Properties properties)
        throws IllegalActionException, IOException
    {
        if (isReadOnly()) {
            throw new IllegalActionException( "Index is read-only." );
        }
        
        Properties currentProperties = getProperties();
        
        Enumeration<?> names = properties.propertyNames();
        while (names.hasMoreElements()) {
            Object _name = names.nextElement();
            if (_name instanceof String) {
                String name = (String) _name;
                currentProperties.setProperty( name, properties.getProperty( name ) );
            }
        }
        
        setProperties( currentProperties );
    }
    
    
    
    /**
     * Sets the current properties of the index and stores
     * them to the properties file.
     * 
     * @param properties The new set of properties
     * @throws IOException
     */
    
    public void setProperties (Properties properties) throws IllegalActionException, IOException {
        if (isReadOnly()) {
            throw new IllegalActionException( "Index is read-only." );
        }
        
        this.properties = properties;
        storeProperties();
    }
    
    
    
    
    
    /**
     * Guarantees the the properties are fresh.
     * 
     * @throws IOException
     */
    
    public void guaranteeFreshProperties () throws IOException {
        File file = getPropertiesFile();
        if (properties == null || propertiesLastModified < file.lastModified()) {
            loadProperties();
        }
    }
    
    
    
    
    /**
     * Finalizes the object. Involves releasing all resources.
     * 
     * @throws Throwable
     */
    
    protected void finalize () throws Throwable {
        close();
    }
    
    
    
    
    /**
     * Determines whether or not the index has
     * a field reflecting the last updated time
     * of documents.
     * 
     * @return True if such a field exists, false otherwise
     */
    
    public boolean hasLastModifiedFieldName () {
        try {
            return getLastModifiedFieldName() != null;
        }
        catch (IOException ioe) {
            return false;
        }
    }
    
    
    
    
    /**
     * Gets the field responsible for updated.
     * 
     * @return The respective field
     * @throws IOException
     */
    
    public String getLastModifiedFieldName () throws IOException {
        String fieldName = null;
        if ( fieldName == null ) { fieldName = getProperty("document.field.<modified>"); }
        if ( fieldName == null ) { fieldName = getProperty("document.field.<updated>");  }
        if ( fieldName == null ) { fieldName = getProperty("document.field.updated");    }
        if ( fieldName == null ) { fieldName = getProperty("document.updated");          }
        if ( fieldName == null ) { fieldName = getProperty("updated");                   }
        return fieldName;
    }
    
    
    
    
    /**
     * Determines whether or not the given document has
     * an updated time field.
     * 
     * @return True if such a field exists, false otherwise
     * @throws IOException
     */
    
    public boolean hasLastModified (LuceneDocument document) throws IOException {
        try {
            return getLastModified( document ) != null;
        }
        catch (InsufficientDataException ide) {
            return false;
        }
        catch (java.text.ParseException pe) {
            return false;
        }
    }
    
    
    
    
    
    /**
     * Determines the precision of the time stamps in terms of 
     * milliseconds.
     * 
     * Examples:
     *   UNIT               |       PRECISION
     * ==========================================
     * millisecond (ms)     |             1
     * second (s)           |          1000
     * minute               |         60000
     * 
     * @return the precision of the time stamps
     */
    
    public Double getTimestampPrecision ()
        throws IOException
    {
        String precision = null;
        if ( precision == null ) { precision = getProperty("document.field.<updated>.precision"); }
        if ( precision == null ) { precision = getProperty("document.field.updated.precision");   }
        if ( precision == null ) { precision = getProperty("document.updated.precision");         }
        
        if ( precision == null ) {
            return null;
        }
        
        return Double.valueOf( precision );
    }
    
    public long getTimestamp (LuceneDocument document)
        throws InsufficientDataException, IOException
    {
        try {
            return net.lucenews.NumberTools.stringToLong( document.get( getLastModifiedFieldName() ) );
        }
        catch(NumberFormatException nfe) {
            throw new InsufficientDataException("Document '" + document + "' does not provide a valid updated time");
        }
        catch(NullPointerException npe) {
            throw new InsufficientDataException("Document '" + document + "' does not provide an updated time");
        }
    }
    
    
    
    /**
     * Gets the updated time of a document. This is determined based 
     * on settings in the index properties.
     * 
     * @param document The document to get the updated time of
     * @return The updated time of the document
     * @throws InsufficientDataException if the document does not specify its last updated time
     * @throws IOException
     */
    
    public Calendar getLastModified (LuceneDocument document)
        throws java.text.ParseException, InsufficientDataException, IOException
    {
        Calendar calendar = Calendar.getInstance();
        
        Logger.getLogger( this.getClass() ).debug("getLastModified");
        
        try {
            String format = getProperty("document.field.<modified>.format");
            Logger.getLogger( this.getClass() ).debug("format: "+format);
            String value  = document.get( getLastModifiedFieldName() );
            Logger.getLogger( this.getClass() ).debug("value: "+value);
            
            // do some sniffing
            try {
                Long.valueOf( value );
                format = "epoch";
            }
            catch (NumberFormatException nfe) {
            }
            
            Logger.getLogger( this.getClass() ).debug("format: "+format);
            
            if ( format != null && value != null ) {
                
                if ( format.equals("epoch") ) {
                    // determine the time stamp
                    long timestamp = getTimestamp( document );
                    
                    // determine the time stamp's precision
                    Double precision = null;
                    if ( precision == null ) { precision = getTimestampPrecision(); }
                    if ( precision == null ) { precision = 1.0;                     }
                    
                    // determine the time stamp's time zone
                    String timeZone = null;
                    if ( timeZone == null ) { timeZone = getProperty("document.field.<modified>.timezone.id"); }
                    if ( timeZone == null ) { timeZone = getProperty("document.field.<modified>.timezone");    }
                    if ( timeZone != null ) {
                        calendar.setTimeZone( TimeZone.getTimeZone( timeZone ) );
                    }
                    
                    calendar.setTime( new Date( Math.round( precision * timestamp ) ) );
                }
                else {
                    // determine whether or not we are to be lenient
                    String lenient    = null;
                    Boolean isLenient = null;
                    if ( lenient == null ) { lenient = getProperty("document.field.<modified>.lenient"); }
                    if ( lenient == null ) { lenient = getProperty("document.field.<updated>.lenient");  }
                    if ( lenient == null ) { lenient = getProperty("document.field.updated.lenient");    }
                    if ( lenient == null ) { lenient = getProperty("document.updated.lenient");          }
                    if ( lenient != null ) {
                        isLenient = ServletUtils.parseBoolean( lenient );
                    }
                    
                    // assume a format suitable for SimpleDateFormat
                    SimpleDateFormat formatter = new SimpleDateFormat( format );
                    
                    // specify whether or not we are to be lenient
                    if ( isLenient != null ) {
                        formatter.setLenient( isLenient );
                    }
                    
                    // parse the date
                    Date date = formatter.parse( value );
                    
                    // apply to the calendar
                    calendar.setTime( date );
                }
                
            }
        }
        catch (NullPointerException npe) {
            if ( isDocumentIdentified( document ) ) {
                throw new InsufficientDataException("Document '" + getIdentifier( document ) + "' does not provide a valid time stamp");
            }
            else {
                throw new InsufficientDataException("Document '" + document + "' does not provide a valid time stamp");
            }
        }
        return calendar;
    }
    
    
    
    
    /**
     * Gets the last updated time of a given document. If the document
     * does not have such a value, it is assigned the given calendar object.
     * 
     * @throws InvalidIdentifierException
     * @throws DocumentNotFoundException
     * @throws InsufficientDataException
     * @throws IOException
     */
    
    public Calendar getLastModified (LuceneDocument document, Calendar calendar)
        throws
            java.text.ParseException, InvalidIdentifierException, 
            DocumentNotFoundException, InsufficientDataException, 
            IOException
    {
        if ( !hasLastModified( document ) ) {
            try {
                setLastModified( document, calendar );
            }
            catch (IllegalActionException iae) {
            }
        }
        
        return getLastModified( document );
    }
    
    
    /**
     * Sets the updated field appropriately prior to adding document to index.
     */
    
    public void setLastModified (LuceneDocument document)
        throws
            IllegalActionException, InvalidIdentifierException,
            DocumentNotFoundException, InsufficientDataException, IOException
    {
        if ( !hasLastModifiedFieldName() ) {
            return;
        }
        
        if ( hasLastModified( document ) ) {
            setLastModified( document, getTimestamp( document ), false );
        }
        else {
            setLastModified( document, new Date(), false );
        }
    }
    
    
    
    /**
     * Sets the document's updated time to the given Calendar's time.
     * Updates the document within the index.
     * 
     * @param document the document whose updated time is to be updated
     * @param calendar the calendar representing the new value of the updated field
     */
    
    public void setLastModified (LuceneDocument document, Calendar calendar)
        throws
            IllegalActionException, InvalidIdentifierException,
            DocumentNotFoundException, InsufficientDataException, IOException
    {
        setLastModified( document, calendar.getTime().getTime() );
    }
    
    
    
    /**
     * Sets the document's updated time to the given Calendar's time.
     * Updates the document within the index if update is specified.
     * 
     * @param document the document whose updated time is to be updated
     * @param calendar the calendar representing the new value of the updated field
     * @param update   whether or not this document should be updated within the index
     */
    
    public void setLastModified (LuceneDocument document, Calendar calendar, boolean update)
        throws
            IllegalActionException, InvalidIdentifierException,
            DocumentNotFoundException, InsufficientDataException, IOException
    {
        String format = getProperty("document.field.<modified>.format");
        
        if ( format == null ) {
            setLastModified( document, calendar.getTime().getTime(), update );
            return;
        }
        
        SimpleDateFormat formatter = new SimpleDateFormat( format );
        
        if ( hasLastModifiedFieldName() ) {
            String field = getLastModifiedFieldName();
            document.removeFields( field );
            document.add( new Field( field, formatter.format( calendar.getTime() ), Field.Store.YES, Field.Index.UN_TOKENIZED ) );
            
            if (update) {
                updateDocument( document );
            }
        }
    }
    
    
    
    /**
     * Sets the document's updated time to the given date's time.
     * Updates the document within the index.
     * 
     * @param document the document whose updated time is to be updated
     * @param date     the date representing the new value of the updated field
     */
    
    public void setLastModified (LuceneDocument document, Date date)
        throws
            IllegalActionException, InvalidIdentifierException,
            DocumentNotFoundException, InsufficientDataException, IOException
    {
        setLastModified( document, date.getTime() );
    }
    
    
    
    /**
     * Sets the document's updated time to the given date's time.
     * Updates the document within the index if update is specified.
     * 
     * @param document the document whose updated time is to be updated
     * @param date     the date representing the new value of the updated field
     * @param update   whether or not this document should be updated within the index
     */
    
    public void setLastModified (LuceneDocument document, Date date, boolean update)
        throws
            IllegalActionException, InvalidIdentifierException,
            DocumentNotFoundException, InsufficientDataException, IOException
    {
        setLastModified( document, date.getTime(), update );
    }
    
    
    
    /**
     * Sets the document's updated time to the given time stamp.
     * Updates the document within the index.
     * 
     * @param document  the document whose updated time is to be updated
     * @param timestamp the time stamp representing the new value of the updated field
     */
    
    public void setLastModified (LuceneDocument document, long timestamp)
        throws
            IllegalActionException, InvalidIdentifierException,
            DocumentNotFoundException, InsufficientDataException, IOException
    {
        setLastModified( document, timestamp, true );
    }
    
    
    
    /**
     * Sets the document's updated time to the given time stamp.
     * Updates the document within the index if update is specified.
     * 
     * @param document  the document whose updated time is to be updated
     * @param timestamp the time stamp representing the new value of the updated field
     * @param update    whether or not this document should be updated within the index
     */
    
    public void setLastModified (LuceneDocument document, long timestamp, boolean update)
        throws
            IllegalActionException, InvalidIdentifierException,
            DocumentNotFoundException, InsufficientDataException, IOException
    {
        if ( hasLastModifiedFieldName() ) {
            String field = getLastModifiedFieldName();
            document.removeFields( field );
            document.add( new Field( field, net.lucenews.NumberTools.longToString( timestamp ), Field.Store.YES, Field.Index.UN_TOKENIZED ) );
            
            if (update) {
                updateDocument( document );
            }
        }
    }
    
    
    
    
    /**
     * Retrieves the last time this index was updated.
     * 
     * @return the last time this index was updated
     * @throws IOException
     */
    
    public Calendar getLastModified () throws IOException {
        File[] files = getDirectory().listFiles();
        
        long lastModified = 0;
        
        for (int i = 0; i < files.length; i++) {
            File file = files[ i ];
            
            if (lastModified < file.lastModified()) {
                lastModified = file.lastModified();
            }
        }
        
        if (lastModified == 0) {
            return null;
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date( lastModified ) );
        return calendar;
    }
    
    
    
    public OpenSearchText getRights (LuceneDocument document) throws IOException {
        String field = null;
        if ( field == null ) { field = getProperty("document.field.<rights>.field"); }
        if ( field == null ) { field = getProperty("document.field.<rights>");       }
        if ( field == null ) { field = "rights";                                     }
        return new OpenSearchText( document.get( field ) );
    }
    
    
    
    /**
     * Should really be 'hasAuthorField' to make its meaning
     * more clear. Will consider deprecation.
     * 
     */
    @Deprecated
    public boolean hasAuthor () {
        try {
            return getAuthor() != null;
        }
        catch(IOException ioe) {
            return false;
        }
    }
    
    public boolean hasAuthorField () {
        try {
            return getAuthor() != null;
        }
        catch(IOException ioe) {
            return false;
        }
    }
    
    
    
    /**
     * Should really be 'getAuthorField'
     */
    
    @Deprecated
    public String getAuthor () throws IOException {
        return getProperties().getProperty( "index.author" );
    }
    
    public String getAuthorField () throws IOException {
        String authorField = null;
        if ( authorField == null ) { authorField = getProperty("document.author.field"); }
        if ( authorField == null ) { authorField = getProperty("document.author");       }
        if ( authorField == null ) { authorField = getProperty("index.author");          }
        if ( authorField == null ) { authorField = "author";                             }
        return authorField;
    }
    
    
    
    /**
     * Retrieves the author field value from a document. The field name
     * used to obtain the value is governed by the value returned by
     * {@link #getAuthorField}.
     * 
     * @param document the document to obtain the author of
     * @return         the author of the given document as declared by its fields
     * @throws         IOException
     */
    
    public String getAuthor (LuceneDocument document) throws IOException {
        String authorTemplate = null;
        if ( authorTemplate == null ) { authorTemplate = getProperty("document.author.template"); }
        if ( authorTemplate == null ) { authorTemplate = getProperty("document.author");          }
        
        // if we have resolved the author template, parse and return it
        if ( authorTemplate != null ) {
            return ServletUtils.clean( parse( authorTemplate, document ) );
        }
        
        // if we have resolved the author field, return it
        String authorField = getAuthorField();
        if ( authorField != null ) {
            return document.get( authorField );
        }
        
        return null;
    }
    
    
    
    /**
     * Produces a set of properties reflecting the fields
     * contained within the given document.
     * 
     * @param document the document containing the fields to be translated into properties
     * @return         a <tt>Properties</tt> object containing the name/value pairs found in the document's fields
     */
    
    public static Properties asProperties (LuceneDocument document) {
        Properties properties = new Properties();
        
        Iterator<Field> fields = document.getFields().iterator();
        while ( fields.hasNext() ) {
            Field field = fields.next();
            properties.setProperty( field.name(), field.stringValue() );
        }
        
        return properties;
    }
    
    
    
    /**
     * Parses the given pattern into a new string, replacing its fields with
     * those provided by the given document. For example:
     *   Given the following document:
     *     title  => "Example title"
     *     author => "J.B. Earl"
     *   
     *   and the following pattern:
     *     "A book entitled \"[title]\" by [author]"
     *   
     *   would be parsed as:
     *     "A book entitled \"Example title\" by J.B. Earl"
     */
    
    public static String parse (String string, LuceneDocument document) {
        return ServletUtils.parse( string, asProperties( document ) );
    }
    
    
    
    /**
     * Retrieves a hash code suitable for identifying this index
     * within hash-based implementations such as {@link java.util.HashMap}.
     * The actual value is the same as that returned by calling the
     * {@link java.io.File#hashCode} method on the {@link java.io.File}
     * object containing the index data.
     * 
     * @return a hash code
     */
    
    public int hashCode () {
        return getDirectory().hashCode();
    }
    
    
    public Calendar getPropertiesLastModified () throws IOException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( new Date( getPropertiesFile().lastModified() ) );
        return calendar;
    }
    
    
    
    public OpenSearchImage getImage () throws NumberFormatException, IOException {
        OpenSearchImage image = null;
        
        String url = null;
        if ( url == null ) { url = getProperty("index.image.url"); }
        if ( url == null ) { url = getProperty("index.image");     }
        
        if (url == null || url.length() == 0) {
            return image;
        }
        
        image = new OpenSearchImage();
        
        image.setUrl( url );
        
        String height = getProperty("index.image.height");
        if ( height != null && height.trim().length() > 0 ) {
            image.setHeight( Integer.valueOf( height ) );
        }
        
        String width = getProperty("index.image.width");
        if ( width != null && width.trim().length() > 0 ) {
            image.setWidth( Integer.valueOf( width ) );
        }
        
        String type = getProperty("index.image.type");
        if ( type != null && type.trim().length() > 0 ) {
            image.setType( type );
        }
        
        return image;
    }
    
    
    /**
     * Stringifies to the index name.
     */
    
    public String toString () {
        return getName();
    }
}
