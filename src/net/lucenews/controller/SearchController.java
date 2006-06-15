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
import org.apache.lucene.analysis.*;
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
            IOException, InsufficientDataException, ParseException, OpenSearchException
    {
        c.log().debug("SearchController.doGet(LuceneContext)");
        
        LuceneWebService   service  = c.service();
        LuceneIndexManager manager  = service.getIndexManager();
        LuceneRequest      req      = c.req();
        LuceneIndex[]      indices  = manager.getIndices( req.getIndexNames() );
        OpenSearchResponse response = new OpenSearchResponse();
        
        
        /**
         * Format
         */
        
        OpenSearch.Format format = OpenSearch.ATOM;
        try {
            format = OpenSearch.getFormat( req.getParameter("format") );
        }
        catch (OpenSearchException ose) {
            format = OpenSearch.ATOM;
        }
        catch (NullPointerException npe) {
            format = OpenSearch.ATOM;
        }
        
        
        
        /**
         * Searcher
         */
        
        IndexSearcher[] searchers = new IndexSearcher[indices.length];
        for (int i = 0; i < indices.length; i++) {
            searchers[i] = indices[i].getIndexSearcher();
        }
        LuceneMultiSearcher searcher = new LuceneMultiSearcher( searchers, getSearcherIndexField() );
        
        
        
        List<String> invalidParameterNames = new LinkedList<String>();
        
        
        /**
         * Query
         */
        
        Query query = null;
        try {
            query = req.getQuery();
        }
        catch (InsufficientDataException ide) {
            query = null;
        }
        
        
        
        Filter filter = req.getFilter();
        Sort   sort   = req.getSort();
        
        
        /**
         * Search string
         */
        
        String searchString = req.getSearchString();
        if (searchString == null) {
            if (sort == null) {
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
        for (int i = 0; i < indices.length && defaultField == null; i++) {
            defaultField = indices[ i ].getDefaultField();
        }
        
        // Service-specified default field
        if (defaultField == null) {
            defaultField = service.getDefaultField();
        }
        
        if (defaultField == null) {
            invalidParameterNames.add( req.getParameterName( LuceneKeys.DEFAULT_FIELD ) );
        }
        
        
        
        
        Analyzer analyzer = req.getAnalyzer();
        if( analyzer == null )
            invalidParameterNames.add( req.getParameterName( LuceneKeys.ANALYZER ) );
        
        if (ServletUtils.parseBoolean(service.getProperty("query.expand","false"))) {
            analyzer = new SynonymAnalyzer(
                analyzer,
                new SynExpand(),
                manager.getIndex(service.getProperty("synonym.index","wordnet")).getIndexSearcher()
            );
        }
        
        
        
        /**
         * Invalid parameter names
         */
        
        if (!invalidParameterNames.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            
            if (invalidParameterNames.size() == 1) {
                buffer.append( "Valid '" + invalidParameterNames.get( 0 ) + "' parameter required." );
            }
            else {
                buffer.append( "Valid" + ServletUtils.joined(invalidParameterNames.toArray(new String[]{})) + " parameters required." );
            }
            
            throw new InsufficientDataException( String.valueOf( buffer ) );
        }
        
        
        QueryParser.Operator defaultOperator = null;
        
        if (query == null) {
            
            /**
             * Build the query parser
             */
            
            QueryParser parser = new CustomQueryParser( defaultField, analyzer );
            
            
            
            Locale locale = req.getLocale();
            if (locale != null) {
                parser.setLocale( locale );
            }
            
            defaultOperator = req.getDefaultOperator();
            
            for (int i = 0; i < indices.length; i++) {
                if (defaultOperator != null) {
                    break;
                }
                defaultOperator = indices[ i ].getDefaultOperator();
            }
            
            if (defaultOperator == null) {
                defaultOperator = service.getDefaultOperator();
            }
            
            if (defaultOperator == null) {
                defaultOperator = QueryParser.AND_OPERATOR;
            }
            
            if (defaultOperator != null) {
                parser.setDefaultOperator( defaultOperator );
            }
            
            
            
            /**
             * Build the query
             */
            
            query = parser.parse( searchString );
        }
        
        
        
        
        /**
         * Perform the search
         */
        
        Hits hits = null;
        
        if (filter != null && sort != null) {
            hits = searcher.search( query, filter, sort );
        }
        else if (filter != null) {
            hits = searcher.search( query, filter );
        }
        else if (sort != null) {
            hits = searcher.search( query, sort );
        }
        else {
            hits = searcher.search( query );
        }
        
        
        
        
        
        Limiter limiter = req.getLimiter();
        limiter.setTotalEntries( null );
        Integer max = null;
        
        try {
            String maximum = req.getCleanParameter("maximum");
            if (maximum != null && maximum.trim().length() > 0) {
                max = Integer.valueOf( maximum );
                limiter.setTotalEntries( max );
            }
        }
        catch (NumberFormatException nfe) {
        }
        
        HitsIterator iterator = null;
        try {
            iterator = new HitsIterator( hits, limiter, c.service().getIndexManager().getIndex( req.getIndexName() ) );
        }
        catch (MultipleValueException mve) {
            iterator = new HitsIterator( hits, limiter );
        }
        
        response.setTotalResults( limiter.getTotalEntries() );
        
        StringBuffer title = new StringBuffer();
        if (query instanceof MatchAllDocsQuery) {
            title.append( "All documents in " );
        }
        else {
            title.append( "Search results for query '" + req.getSearchString() + "' on " );
        }
        title.append( ( indices.length == 1 ? "index" : "indices" ) + " " );
        title.append( ServletUtils.joined( ServletUtils.mapped( "'[content]'", ServletUtils.objectsMapped( "getTitle", indices ) ) ) );
        
        response.setTitle( String.valueOf( title ) );
        response.setId( req.getLocation() );
        response.setUpdated( Calendar.getInstance() );
        
        /**
         * Add OpenSearch Description Document information
         */
        
        OpenSearchLink descriptionLink = new OpenSearchLink();
        descriptionLink.setHref( service.getOpenSearchDescriptionURL( req, req.getIndexNames() ) );
        descriptionLink.setRel("search");
        descriptionLink.setType("application/opensearchdescription+xml");
        response.setLink( descriptionLink );
        
        
        OpenSearchQuery requestQuery = new OpenSearchQuery();
        requestQuery.setRole("request");
        requestQuery.setTitle( title.toString() );
        requestQuery.setOsd( service.getOpenSearchDescriptionURL( req, req.getIndexNames() ) );
        requestQuery.setTotalResults( limiter.getTotalEntries() );
        requestQuery.setSearchTerms( searchString );
        if (max != null) {
            requestQuery.setCount( max );
        }
        response.addQuery( requestQuery );
        
        response.setDescription( title.toString() );
        
        
        /**
         * Apply paging information to OpenSearch response
         */
        
        if (limiter != null && limiter instanceof Pager) {
            Pager pager = (Pager) limiter;
            
            response.setTotalResults( pager.getTotalEntries() );
            response.setStartIndex( pager.getFirst() );
            response.setItemsPerPage( pager.getEntriesPerPage() );
            
            if (pager.getCurrentPage() != null) {
                OpenSearchLink link = new OpenSearchLink();
                link.setHref( req.getUrlWith( "page", pager.getCurrentPage() ) );
                link.setRel("self");
                link.setType(OpenSearch.getContentType(format));
                response.addLink(link);
            }
            
            if (pager.getFirstPage() != null) {
                OpenSearchLink link = new OpenSearchLink();
                link.setHref( req.getUrlWith( "page", pager.getFirstPage() ) );
                link.setRel("first");
                link.setType(OpenSearch.getContentType(format));
                response.addLink(link);
            }
            
            if (pager.getPreviousPage() != null) {
                OpenSearchLink link = new OpenSearchLink();
                link.setHref( req.getUrlWith( "page", pager.getPreviousPage() ) );
                link.setRel("previous");
                link.setType(OpenSearch.getContentType(format));
                response.addLink(link);
            }
            
            if (pager.getNextPage() != null) {
                OpenSearchLink link = new OpenSearchLink();
                link.setHref( req.getUrlWith( "page", pager.getNextPage() ) );
                link.setRel("next");
                link.setType(OpenSearch.getContentType(format));
                response.addLink(link);
            }
            
            if (pager.getLastPage() != null) {
                OpenSearchLink link = new OpenSearchLink();
                link.setHref( req.getUrlWith( "page", pager.getLastPage() ) );
                link.setRel("last");
                link.setType(OpenSearch.getContentType(format));
                response.addLink(link);
            }
        }
        
        
        // DOM Document to produce XML later on
        org.w3c.dom.Document domDocument = XMLController.newDocument();
        
        
        iterator.reset();
        while( iterator.hasNext() ) {
            LuceneDocument document = iterator.next();
            Integer searcherIndex = extractSearcherIndex( document );
            
            LuceneIndex index = null;
            if (searcherIndex != null) {
                index = indices[ searcherIndex ];
                document.setIndex( index );
            }
            
            //response.addEntry( DocumentController.asEntry( c, index, document, iterator.score() ) );
            
            OpenSearchResult result = new OpenSearchResult();
            result.setTitle( document.getTitle() );
            result.setId( service.getDocumentURL( req, index, document ) );
            result.setUpdated( document.getUpdated() );
            result.setRelevance( iterator.score() );
            
            OpenSearchLink resultLink = new OpenSearchLink();
            resultLink.setHref( service.getDocumentURL( req, index, document ) );
            result.setLink( resultLink );
            
            String name = document.getAuthor();
            if (name != null) {
                OpenSearchPerson author = new OpenSearchPerson();
                author.setRole("author");
                author.setName( document.getAuthor() );
                result.addPerson( author );
            }
            
            
            // content
            Element div = domDocument.createElement("div");
            div.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
            div.appendChild( XOXOController.asElement( c, document, domDocument ) );
            result.setContent(div);
            
            
            response.addResult( result );
        }
        
        OpenSearchView.process( c, response, format );
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
            return new Query[]{};
        }
        catch(Exception e) {
            e.printStackTrace();
            return new Query[]{};
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
    
    
    
    public static String rewriteQuery (String original, Query alternate) {
        StringBuffer rewritten = new StringBuffer();
        
        List<TokenTermQuery> termQueries = findTokenTermQueries( alternate );
        Iterator<TokenTermQuery> iterator = termQueries.iterator();
        
        int cursor = 0;
        while( iterator.hasNext() ) {
            TokenTermQuery query = iterator.next();
            rewritten.append( original.substring( cursor, query.getToken().beginColumn ) );
            rewritten.append( query.getTerm().text() );
            cursor = query.getToken().endColumn;
        }
        
        rewritten.append( original.substring( cursor ) );
        
        return rewritten.toString();
    }
    
    public static List<TokenTermQuery> findTokenTermQueries (Query query) {
        List<TokenTermQuery> queries = new ArrayList<TokenTermQuery>();
        if( query instanceof BooleanQuery )
            queries.addAll( findTokenTermQueries( (BooleanQuery) query ) );
        if( query instanceof TermQuery )
            queries.addAll( findTokenTermQueries( (TermQuery) query ) );
        
        Collections.sort( queries, new TokenTermQueryComparator() );
        return queries;
    }
    
    public static List<TokenTermQuery> findTokenTermQueries (BooleanQuery query) {
        BooleanClause[] clauses = query.getClauses();
        List<TokenTermQuery> queries = new ArrayList<TokenTermQuery>(clauses.length);
        for( int i = 0; i < clauses.length; i++ ) {
            queries.addAll( findTokenTermQueries( clauses[i].getQuery() ) );
        }
        return queries;
    }
    
    public static List<TokenTermQuery> findTokenTermQueries (TermQuery query) {
        List<TokenTermQuery> queries = new ArrayList<TokenTermQuery>(1);
        if( query instanceof TokenTermQuery )
            queries.add( (TokenTermQuery) query );
        return queries;
    }
    
}

class TokenTermQueryComparator implements Comparator<TokenTermQuery> {
    
    public int compare (TokenTermQuery query1, TokenTermQuery query2) {
        Integer column1 = query1.getToken().beginColumn;
        Integer column2 = query2.getToken().beginColumn;
        return column1.compareTo(column2);
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
            return new TokenTermQuery( termQuery.getTerm(), getToken( 0 ) );
        }
        return query;
    }
    
    protected Query getRangeQuery (String field, String part1, String part2, boolean inclusive)
    throws ParseException
    {
        return new ConstantScoreRangeQuery( field, part1, part2, inclusive, inclusive );
    }
    
}
