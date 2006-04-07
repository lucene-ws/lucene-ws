package net.lucenews.controller;

import java.lang.reflect.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;
import net.lucenews.atom.*;
import net.lucenews.*;
import net.lucenews.model.*;
import net.lucenews.model.exception.*;
import net.lucenews.view.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.queryParser.*;
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
        throws ParserConfigurationException, TransformerException, IndicesNotFoundException, IOException, InsufficientDataException, ParseException
    {
        LuceneWebService   service = c.service();
        LuceneIndexManager manager = service.getIndexManager();
        LuceneRequest      req     = c.req();
        LuceneIndex[]      indices = manager.getIndices( req.getIndexNames() );
        
        LuceneMultiSearcher   searcher  = null;
        IndexSearcher[] searchers = new IndexSearcher[ indices.length ];
        
        for( int i = 0; i < indices.length; i++ )
            searchers[ i ] = indices[ i ].getIndexSearcher();
        
        searcher = new LuceneMultiSearcher( searchers, getSearcherIndexField() );
        
        
        
        List<String> invalidParameterNames = new LinkedList<String>();
        
        
        /**
         * Query
         */
        
        Query query = null;
        try {
            query = req.getQuery();
        }
        catch(InsufficientDataException ide) {
            query = null;
        }
        
        
        
        Filter filter = req.getFilter();
        Sort   sort   = req.getSort();
        
        
        /**
         * Search string
         */
        
        String searchString = req.getSearchString();
        if( searchString == null ) {
            if( sort == null ) {
                invalidParameterNames.add( req.getParameterName( LuceneKeys.SEARCH_STRING ) );
            }
            else {
                query = new MatchAllDocsQuery();
            }
        }
        
        
        
        /**
         * Default field
         */
        
        // User-specified default field
        String defaultField = req.getDefaultField();
        
        // Index-specified default field
        for( int i = 0; i < indices.length && defaultField == null; i++ )
            defaultField = indices[ i ].getDefaultField();
        
        // Service-specified default field
        if( defaultField == null )
            defaultField = service.getDefaultField();
        
        if( defaultField == null )
            invalidParameterNames.add( req.getParameterName( LuceneKeys.DEFAULT_FIELD ) );
        
        
        
        
        Analyzer analyzer = req.getAnalyzer();
        if( analyzer == null )
            invalidParameterNames.add( req.getParameterName( LuceneKeys.ANALYZER ) );
        
        
        
        
        
        if( !invalidParameterNames.isEmpty() ) {
            StringBuffer buffer = new StringBuffer();
            
            if( invalidParameterNames.size() == 1 ) {
                buffer.append( "Valid '" + invalidParameterNames.get( 0 ) + "' parameter required." );
            }
            else {
                buffer.append( "Valid" );
                for( int i = 0; i < invalidParameterNames.size(); i++ ) {
                    if( i > 0 )
                        if( i == ( invalidParameterNames.size() - 1 ) )
                            buffer.append( " and" );
                        else
                            buffer.append( "," );
                    
                    buffer.append( " '" + invalidParameterNames.get( i ) + "'" );
                }
                
                buffer.append( " parameters required." );
            }
            
            
            throw new InsufficientDataException( String.valueOf( buffer ) );
        }
        
        
        QueryParser.Operator defaultOperator = null;
        
        if( query == null ) {
            
            /**
             * Build the query parser
             */
            
            QueryParser parser = new CustomQueryParser( defaultField, analyzer );
            
            
            
            Locale locale = req.getLocale();
            if( locale != null )
            parser.setLocale( locale );
            
            defaultOperator = req.getDefaultOperator();
            
            for( int i = 0; i < indices.length; i++ )
            {
            if( defaultOperator != null )
            break;
            defaultOperator = indices[ i ].getDefaultOperator();
            }
            
            if( defaultOperator == null )
            defaultOperator = service.getDefaultOperator();
            
            if( defaultOperator == null )
            defaultOperator = QueryParser.AND_OPERATOR;
            
            if( defaultOperator != null )
            parser.setDefaultOperator( defaultOperator );
            
            
            
            /**
             * Build the query
             */
            
            query = parser.parse( searchString );
        }
        
        
        
        
        /**
         * Perform the search
         */
        
        Hits hits = null;
        
        if( filter != null && sort != null )
            hits = searcher.search( query, filter, sort );
        
        else if( filter != null )
            hits = searcher.search( query, filter );
        
        else if( sort != null )
            hits = searcher.search( query, sort );
        
        else
            hits = searcher.search( query );
        
        
        
        
        
        /**
         * Alternate query
         */
        
        int maxSuggestions = 5;
        int maxResults = 1;
        PriorityQueue<SearchedQuery> queue = new PriorityQueue<SearchedQuery>( maxSuggestions, new SearchedQueryComparator() );
        Query[]  suggestedQueries      = getSuggestedQueries( query, indices );
        String[] suggestedQueryStrings = new String[ Math.min( maxResults, suggestedQueries.length ) ];
        int[]    suggestedQueryCounts  = new int[ Math.min( maxResults, suggestedQueries.length ) ];
        
        for( int i = 0; i < suggestedQueries.length && i < maxSuggestions; i++ ) {
            Query suggestedQuery = suggestedQueries[ i ];
            Hits suggestedHits = searcher.search( suggestedQuery );
            if( suggestedHits.length() >= hits.length() )
                queue.add( new SearchedQuery( suggestedQuery, suggestedHits ) );
        }
        
        int i = 0;
        while( !queue.isEmpty() && i < maxResults ) {
            SearchedQuery q = queue.poll();
            suggestedQueryStrings[ i ] = q.getQuery().toString( defaultField );
            suggestedQueryCounts[ i ] = q.getHits().length();
            i++;
        }
        
        Limiter limiter = req.getLimiter();
        
        HitsIterator iterator = null;
        try {
            iterator = new HitsIterator( hits, limiter, c.service().getIndexManager().getIndex( req.getIndexName() ) );
        }
        catch(MultipleValueException mve) {
            iterator = new HitsIterator( hits, limiter );
        }
        
        OpenSearchResponse response = asOpenSearchResponse( c, iterator, suggestedQueryStrings, suggestedQueryCounts );
        
        StringBuffer title = new StringBuffer();
        if( query instanceof MatchAllDocsQuery ) {
            title.append( "All documents in " );
        }
        else {
            title.append( "Search results for query '" + req.getSearchString() + "' on " );
        }
        title.append( ( indices.length == 1 ? "index" : "indices" ) + " " );
        title.append( ServletUtils.joined( ServletUtils.mapped( "'[content]'", ServletUtils.objectsMapped( "getTitle", indices ) ) ) );
        
        response.setTitle( String.valueOf( title ) );
        
        AtomView.process( c, response );
    }
    
    
    
    /**
     * Transforms the given hits iterator into an OpenSearch
     * response.
     * 
     * @param c The context
     * @param iterator The hits iterator
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws IndicesNotFoundException
     */
    
    public static OpenSearchResponse asOpenSearchResponse (LuceneContext c, HitsIterator iterator, String[] suggestions, int[] suggestionCounts)
    throws ParserConfigurationException, IOException, IndicesNotFoundException, InsufficientDataException
    {
        LuceneWebService   service = c.service();
        LuceneIndexManager manager = service.getIndexManager();
        LuceneRequest      req     = c.req();
        LuceneResponse     res     = c.res();
        LuceneIndex[]      indices = manager.getIndices( req.getIndexNames() );
        
        
        
        
        OpenSearchResponse response = new OpenSearchResponse();
        
        
        
        response.setLinkHREF( service.getOpenSearchDescriptionURL( req, req.getIndexNames() ) );
        response.setSearchTerms( req.getSearchString() );
        response.setUpdated( Calendar.getInstance() );
        response.addAuthor( new Author( "Lucene Web Service, version " + service.getVersion() ) );
        response.setID( req.getRequestURL() + ( ( req.getQueryString() != null ) ? "?" + req.getQueryString()  : "" ) );
        
        for( int i = 0; i < suggestions.length; i++ )
            response.addQuerySuggestion( suggestions[ i ], suggestionCounts[ i ] );
        
        StringBuffer buffer = new StringBuffer();
        
        buffer.append( req.getRequestURL() );
        
        Enumeration names = req.getParameterNames();
        boolean first = true;
        while( names.hasMoreElements() ) {
            String name = (String) names.nextElement();
            
            if( name.equals( "page" ) )
            continue;
            
            String[] values = req.getParameterValues( name );
            for( int i = 0; i < values.length; i++ ) {
                if( first ) {
                    buffer.append( "?" );
                    first = false;
                }
                else {
                    buffer.append( "&" );
                }
                buffer.append( name + "=" + values[i] );
            }
            
        }
        
        buffer.append( first ? "?" : "&" );
        buffer.append( "page=" );
        
        String baseURL = String.valueOf( buffer );
        
        
        
        
        Limiter limiter = iterator.getLimiter();
        
        if( limiter != null && limiter instanceof Pager ) {
            Pager pager = (Pager) limiter;
            response.setTotalResults( pager.getTotalEntries() );
            response.setStartIndex( pager.getFirst() );
            response.setItemsPerPage( pager.getEntriesPerPage() );
            
            if( pager.getCurrentPage() != null )
                response.addLink( new Link( baseURL + pager.getCurrentPage(), "self", "application/atom+xml" ) );
            
            if( pager.getFirstPage() != null )
                response.addLink( new Link( baseURL + pager.getFirstPage(), "first", "application/atom+xml" ) );
            
            if( pager.getPreviousPage() != null )
                response.addLink( new Link( baseURL + pager.getPreviousPage(), "previous", "application/atom+xml" ) );
            
            if( pager.getNextPage() != null )
                response.addLink( new Link( baseURL + pager.getNextPage(), "next", "application/atom+xml" ) );
            
            if( pager.getLastPage() != null )
                response.addLink( new Link( baseURL + pager.getLastPage(), "last", "application/atom+xml" ) );
        }
        
        iterator.reset();
        while( iterator.hasNext() ) {
            LuceneDocument document = iterator.next();
            Integer searcherIndex = extractSearcherIndex( document );
            
            LuceneIndex index = null;
            if( searcherIndex != null )
                index = indices[ searcherIndex ];
            
            response.addEntry( DocumentController.asEntry( c, index, document, iterator.score() ) );
        }
        
        return response;
    }
    
    
    
    public static String getSearcherIndexField () {
        return "lucene_ws_subSearcher";
    }
    
    
    public static Integer extractSearcherIndex (LuceneDocument document) {
        Integer index = null;
        
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
    
    
    public static Query[] getSuggestedQueries (Query original, LuceneIndex[] indices)
        throws IOException
    {
        IndexReader[] readers = new IndexReader[ indices.length ];
        for( int i = 0; i < indices.length; i++ )
            readers[ i ] = indices[ i ].getIndexReader();
        
        MultiReader reader = new MultiReader( readers );
        Query[] suggested = getSuggestedQueries( original, reader );
        
        for( int i = 0; i < indices.length; i++ )
            indices[ i ].putIndexReader( readers[ i ] );
        
        return suggested;
    }
    
    
    public static Query[] getSuggestedQueries (Query original, IndexReader reader)
        throws IOException
    {
        try {
            DidYouMeanQueryGenerator generator = new DidYouMeanQueryGenerator( original, reader );
            List<Query> suggestions = generator.getQuerySuggestions( true, true );
            Iterator<Query> iterator = suggestions.iterator();
            while( iterator.hasNext() )
                setDefaultBoost( iterator.next() );
            return suggestions.toArray( new Query[]{} );
        }
        catch(Error err) {
            return null;
        }
        catch(Exception e) {
            return null;
        }
    }
    
    
    public static void setDefaultBoost (Query query) {
        if( query instanceof BooleanQuery ) {
            setDefaultBoost( (BooleanQuery) query );
        }
        else {
            query.setBoost( 1.0f );
        }
    }
    
    public static void setDefaultBoost (BooleanQuery query) {
        BooleanClause[] clauses = query.getClauses();
        for( int i = 0; i < clauses.length; i++ ) {
            setDefaultBoost( clauses[ i ].getQuery() );
        }
        query.setBoost( 1.0f );
    }
    
}

class SearchedQuery {
    
    public Query query;
    public Hits  hits;
    
    public SearchedQuery (Query query, Hits hits) {
        this.query = query;
        this.hits  = hits;
    }
    
    public Query getQuery () {
        return query;
    }
    
    public Hits getHits () {
        return hits;
    }
    
}

class SearchedQueryComparator implements Comparator<SearchedQuery> {
    
    public int compare (SearchedQuery q1, SearchedQuery q2) {
        Integer hits1 = q1.getHits().length();
        Integer hits2 = q2.getHits().length();
        return hits2.compareTo( hits1 );
    }
    
}

class CustomTermQuery extends TermQuery {
    
    private org.apache.lucene.queryParser.Token token;
    
    public CustomTermQuery (Term term) {
        super( term );
    }
    
    public CustomTermQuery (Term term, org.apache.lucene.queryParser.Token token) {
        super( term );
        this.token = token;
    }
    
    public org.apache.lucene.queryParser.Token getToken () {
        return token;
    }
    
}

class CustomQueryParser extends QueryParser {
    
    public CustomQueryParser (String f, Analyzer a) {
        super( f, a );
    }
    
    public CustomQueryParser (CharStream stream) {
        super( stream );
    }
    
    public CustomQueryParser (QueryParserTokenManager tm) {
        super( tm );
    }
    
    protected Query getFieldQuery (String field, String queryText ) throws ParseException {
        Query query = super.getFieldQuery( field, queryText );
        if( query instanceof TermQuery ) {
            TermQuery termQuery = (TermQuery) query;
            return new CustomTermQuery( termQuery.getTerm(), getToken( 0 ) );
        }
        return query;
    }
    
    protected Query getRangeQuery (String field, String part1, String part2, boolean inclusive)
    throws ParseException
    {
        return new ConstantScoreRangeQuery( field, part1, part2, inclusive, inclusive );
    }
    
}
