package net.lucenews.controller;

import java.lang.reflect.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;
import net.lucenews.atom.*;
import net.lucenews.opensearch.*;
import net.lucenews.*;
import net.lucenews.model.*;
import net.lucenews.model.exception.*;
import net.lucenews.view.*;
import org.apache.log4j.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.search.*;
import org.apache.lucene.wordnet.*;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.transform.*;



public class SearchController extends Controller {
    
    
    
    /**
     * Performs a search.
     * 
     * @param c The context
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws IndicesNotFoundException
     * @throws IOException
     * @throws InsufficientDataException
     * @throws ParseException
     */
    
    public static void doGet (LuceneContext c)
        throws
            ParserConfigurationException, TransformerException, IndicesNotFoundException,
            IOException, InsufficientDataException, org.apache.lucene.queryParser.ParseException, OpenSearchException
    {
        Logger.getLogger(SearchController.class).trace("doGet(LuceneContext)");
        
        LuceneWebService   service  = c.getService();
        LuceneIndexManager manager  = service.getIndexManager();
        LuceneRequest      request  = c.getRequest();
        LuceneIndex[]      indices  = manager.getIndices( request.getIndexNames() );
        List<String> invalidParameterNames = new LinkedList<String>();
        
        
        
        /**
         * Prepare searcher
         */
        
        	boolean sort = false;
        	IndexSearcher[] searchers = new IndexSearcher[ indices.length ];
		LuceneDocument[] docs;
        	for:outer(int i =0; i < indices.length; i++) {
			docs = indices[i].getDocuments();
	    		searchers[ i ] = indices[ i ].getIndexSearcher();
			if(docs.length > 0){
				sort=true;
				break outer;
			}
			
		}
        LuceneMultiSearcher searcher = new LuceneMultiSearcher( searchers, getSearcherIndexField() );
        c.setMultiSearcher( searcher );
        
        
        
        /**
         * Apply defaults
         */
        
        if ( c.getAnalyzer() == null )           c.setAnalyzer( new StandardAnalyzer() );
        if ( c.suggestSimilar() == null )        c.suggestSimilar( false );
        if ( c.suggestSpelling() == null )       c.suggestSpelling( false );
        if ( c.suggestSynonyms() == null )       c.suggestSynonyms( false );
        if ( c.getOpenSearchFormat() == null )   c.setOpenSearchFormat( OpenSearch.ATOM );
        if ( c.getOpenSearchResponse() == null ) c.setOpenSearchResponse( new OpenSearchResponse() );
        OpenSearchResponse response = c.getOpenSearchResponse();
        
        if ( c.getOpenSearchQuery().getCount() == null ) {
            c.getOpenSearchQuery().setCount( 10 );
        }
        
        
        
        /**
         * Prepare query
         */
        
        Query  query       = null;
        String searchTerms = c.getOpenSearchQuery().getSearchTerms();
        
        if ( query == null ) {
            if ( searchTerms == null ) {
                query = new MatchAllDocsQuery();
            }
            else {
                
                if ( c.getOpenSearchQuery() == null ) {
                    throw new InsufficientDataException("No OpenSearch Query provided");
                }
                
                if ( c.getDefaultFields() == null ) {
                    throw new InsufficientDataException("No default field(s) specified");
                }
                
                if ( c.getQueryParser() == null ) {
                    LuceneQueryParser queryParser = new LuceneQueryParser( "dd8fc45d87f91c6f9a9f43a3f355a94a", c.getAnalyzer() );
                    queryParser.setFields( c.getDefaultFields() );
                    if (c.getLocale() != null)          { queryParser.setLocale( c.getLocale() ); }
                    if (c.getDefaultOperator() != null) { queryParser.setDefaultOperator( c.getDefaultOperator() ); }
                    c.setQueryParser( queryParser );
                }
                
                c.getLogger().debug("search terms: '" + searchTerms + "'");
                query = c.getQueryParser().parse( searchTerms );
            }
        }
        
        
        Logger.getLogger(SearchController.class).info("Analyzer: " + c.getAnalyzer());
        Logger.getLogger(SearchController.class).info("Filter: " + c.getFilter());
        Logger.getLogger(SearchController.class).info("Sort: " + c.getSort());
        
        /**
         * Perform search
         */
        
        Hits hits= null;
	if(sort){
        	hits = searcher.search( query, c.getFilter(), c.getSort() );	
	   	}
	else{
		
		hits = searcher.search(query);	
	}
        
        Logger.getLogger(SearchController.class).info("Search for " + query + " returned " + hits.length() + " results");
        
        
        
        
        
        
        int maximumCorrectedTotalResults = -1;
        
        // Suggesting
        SuggestionController.doSuggest( c, query, response );
        
        
        
        
        /**
         * Adjust the totalResults, depending upon whether or not the 
         * request explicitly specified a desired totalResults values.
         */
        
        Integer totalResults = c.getOpenSearchQuery().getTotalResults();
        if ( totalResults == null || hits.length() < totalResults ) {
            c.getOpenSearchQuery().setTotalResults( hits.length() );
        }
        Logger.getLogger(SearchController.class).debug("totalResults: " + c.getOpenSearchQuery().getTotalResults());
        
        
        
        
        
        
        /**
         * ============================================
         *            OpenSearch Response
         * ============================================
         */
        
        if ( c.getTitle() == null ) {
            StringBuffer title = new StringBuffer();
            if (query instanceof MatchAllDocsQuery) {
                title.append( "All documents within " );
            }
            else {
                title.append( "Search results for '" + c.getOpenSearchQuery().getSearchTerms() + "' within " );
            }
            title.append( ServletUtils.joined( ServletUtils.objectsMapped( "getTitle", indices ) ) );
            
            response.setTitle( String.valueOf( title ) );
        }
        else {
            response.setTitle( c.getTitle() );
        }
        
        response.setId( request.getLocation() );
        response.setUpdated( Calendar.getInstance() );
        
        
        // link to OpenSearch Description
        OpenSearchLink descriptionLink = new OpenSearchLink();
        descriptionLink.setHref( service.getOpenSearchDescriptionURI( request, request.getIndexNames() ).toString() );
        descriptionLink.setRel("search");
	descriptionLink.setType("application/atom+xml");
        response.setLink( descriptionLink );
        
        
        // the original OpenSearch Query
        response.addQuery( c.getOpenSearchQuery() );
        
        
        response.setDescription( response.getTitle() );
        
        addLinks( c );
        
        
        // DOM Document to produce XML later on
        org.w3c.dom.Document domDocument = XMLController.newDocument();
        
        
        
        
        
        /**
         * ============================================
         *               ADD RESULTS
         * ============================================
         */
        
        if (c.getOpenSearchQuery().getTotalResults() > 0) {
            Integer firstIndex = c.getOpenSearchQuery().getFirstIndex();
            Integer lastIndex  = c.getOpenSearchQuery().getLastIndex();
            
            if (firstIndex != null && lastIndex != null) {
                response.setStartIndex( firstIndex );
                
                for (int number = firstIndex; number <= lastIndex; number++) {
                    LuceneDocument document = new LuceneDocument( hits.doc( number - 1 ) );
                    float          score    = hits.score( number - 1 );
                    
                    Integer searcherIndex = extractSearcherIndex( document );
                    
                    LuceneIndex index = null;
                    if (searcherIndex != null) {
                        index = indices[ searcherIndex ];
                        document.setIndex( index );
                    }
                    
                    OpenSearchResult result = new OpenSearchResult();
                    result.setTitle( document.getTitle() );
                    result.setId( service.getDocumentURI( request, index, document ).toString() );
                    //result.setRights( document.getRights() );
                    
                    result.setSummary(index.getSummary(document));
                    
                    try {
                        result.setUpdated( document.getLastModified() );
                    }
                    catch (java.text.ParseException pe) {
                    }
                    catch (InsufficientDataException ide) {
                    }
                    
                    result.setScore( score );
                    
                    
                    OpenSearchLink serviceLink = new OpenSearchLink();
                    serviceLink.setHref(service.getDocumentURI( request, index, document ).toString() );
                    serviceLink.setRel("self");
                    result.addLink( serviceLink );
                    
                    
                    String documentURI = index.getURI(document);
                    if (documentURI != null) { 
                    	OpenSearchLink alternateLink = new OpenSearchLink();
                    	alternateLink.setHref(documentURI);
                    	alternateLink.setRel("alternate");
                        result.addLink( alternateLink );
                    }
                    
                    
                    
                    if (document.getAuthor() != null) {
                        OpenSearchPerson author = new OpenSearchPerson();
                        author.setRole( "author" );
                        author.setName( document.getAuthor() );
                        result.addPerson( author );
                    }
                                       
                    
                    // content
                    Element div = domDocument.createElement("div");
                    div.setAttribute( "xmlns", "http://www.w3.org/1999/xhtml" );
                    div.appendChild( XOXOController.asElement( c, document, domDocument ) );
                    result.setContent( div );
                    
                    
                    response.addResult( result );
                }
            }
        }
        
        
        
        OpenSearchView.process( c );
    }

    
    
    
    
    /**
     * Adds paging related links to the given OpenSearch response.
     * @param c        the Lucene context
     */
    
    public static void addLinks (LuceneContext c) throws InsufficientDataException {
        LuceneRequest      request         = c.getRequest();
        OpenSearchQuery    openSearchQuery = c.getOpenSearchQuery();
        HttpURI            uri             = request.getUri();
        OpenSearch.Format  format          = c.getOpenSearchFormat();
        OpenSearchResponse response        = c.getOpenSearchResponse();
        
        
        /**
         * First
         */
        
        if ( response.getRelatedLink("first") == null && openSearchQuery.getFirstPage() != null ) {
            OpenSearchLink link = new OpenSearchLink();
            link.setHref( uri.with( c.getStartPageParameter(), openSearchQuery.getFirstPage() ).toString() );
            link.setRel( "first" );
            link.setType( OpenSearch.getContentType( format ) );
            response.addLink( link );
        }
        
        
        /**
         * Previous
         */
        
        if ( response.getRelatedLink("previous") == null && openSearchQuery.getPreviousPage() != null ) {
            OpenSearchLink link = new OpenSearchLink();
            link.setHref( uri.with( c.getStartPageParameter(), openSearchQuery.getPreviousPage() ).toString() );
            link.setRel( "previous" );
            link.setType( OpenSearch.getContentType( format ) );
            response.addLink( link );
        }
        
        
        /**
         * Self
         */
        
        if ( response.getRelatedLink("self") == null && openSearchQuery.getStartPage() != null ) {
            OpenSearchLink link = new OpenSearchLink();
            link.setHref( uri.with( c.getStartPageParameter(), openSearchQuery.getStartPage() ).toString() );
            link.setRel( "self" );
            link.setType( OpenSearch.getContentType( format ) );
            response.addLink( link );
        }
        
        
        /**
         * Next
         */
        
        if ( response.getRelatedLink("next") == null && openSearchQuery.getNextPage() != null ) {
            OpenSearchLink link = new OpenSearchLink();
            link.setHref( uri.with( c.getStartPageParameter(), openSearchQuery.getNextPage() ).toString() );
            link.setRel( "next" );
            link.setType( OpenSearch.getContentType( format ) );
            response.addLink( link );
        }
        
        
        /**
         * Last
         */
        
        if ( response.getRelatedLink("last") == null && openSearchQuery.getLastPage() != null ) {
            OpenSearchLink link = new OpenSearchLink();
            link.setHref( uri.with( c.getStartPageParameter(), openSearchQuery.getLastPage() ).toString() );
            link.setRel( "last" );
            link.setType( OpenSearch.getContentType( format ) );
            response.addLink( link );
        }
        
        
    }
    
    
    
    
    public static String getSearcherIndexField () {
        return "lucene_ws_subSearcher";
    }
    
    
    public static Integer extractSearcherIndex (LuceneDocument document) {
        Integer index = null;
        
        if (document == null)
            return index;
        
        try {
            index = Integer.valueOf( document.get( getSearcherIndexField() ) );
        }
        catch(NullPointerException npe) {
        }
        catch(NumberFormatException nfe) {
        }
        
        document.removeFields( getSearcherIndexField() );
        
        return index;
    }
    
}
