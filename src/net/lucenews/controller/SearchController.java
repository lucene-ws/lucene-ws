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
        Sort               sort     = request.getSort();
        List<String> invalidParameterNames = new LinkedList<String>();
        
        
        
        /**
         * Prepare searcher
         */
        
        IndexSearcher[] searchers = new IndexSearcher[ indices.length ];
        for (int i = 0; i < indices.length; i++) {
            searchers[ i ] = indices[ i ].getIndexSearcher();
        }
        LuceneMultiSearcher searcher = new LuceneMultiSearcher( searchers, getSearcherIndexField() );
        c.setMultiSearcher( searcher );
        
        
        
        /**
         * Apply defaults
         */
        
        if (c.getAnalyzer() == null)           { c.setAnalyzer( new StandardAnalyzer() ); }
        if (c.isExpanding() == null)           { c.isExpanding( false ); }
        if (c.isSpellChecking() == null)       { c.isSpellChecking( false ); }
        if (c.isSuggesting() == null)          { c.isSuggesting( false ); }
        if (c.getOpenSearchFormat() == null)   { c.setOpenSearchFormat( OpenSearch.ATOM ); }
        if (c.getOpenSearchResponse() == null) { c.setOpenSearchResponse( new OpenSearchResponse() ); }
        OpenSearchResponse response = c.getOpenSearchResponse();
        
        if (c.getOpenSearchQuery() == null) {
            throw new InsufficientDataException("No OpenSearch Query provided");
        }
        
        if (c.getOpenSearchQuery().getCount() == null) {
            c.getOpenSearchQuery().setCount( 10 );
        }
        
        if (c.getDefaultField() == null) {
            throw new InsufficientDataException("No default field specified");
        }
        
        if (c.getQueryParser() == null) {
            QueryParser queryParser = new LuceneQueryParser( c.getDefaultField(), c.getAnalyzer() );
            if (c.getLocale() != null)          { queryParser.setLocale( c.getLocale() ); }
            if (c.getDefaultOperator() != null) { queryParser.setDefaultOperator( c.getDefaultOperator() ); }
            c.setQueryParser( queryParser );
        }
        
        
        /**
         * Prepare query
         */
        
        Query  query       = null;
        String searchTerms = c.getOpenSearchQuery().getSearchTerms();
        
        if (query == null) {
            if (searchTerms == null) {
                query = new MatchAllDocsQuery();
            }
            else {
                query = c.getQueryParser().parse( searchTerms );
            }
        }
        
        
        Logger.getLogger(SearchController.class).info("Analyzer: " + c.getAnalyzer());
        Logger.getLogger(SearchController.class).info("Filter: " + c.getFilter());
        Logger.getLogger(SearchController.class).info("Sort: " + c.getSort());
        
        
        /**
         * Perform search
         */
        
        Hits hits = null;
        
        if (c.getFilter() != null && c.getSort() != null) {
            hits = searcher.search( query, c.getFilter(), c.getSort() );
        }
        else if (c.getFilter() != null) {
            hits = searcher.search( query, c.getFilter() );
        }
        else if (c.getSort() != null) {
            hits = searcher.search( query, c.getSort() );
        }
        else {
            hits = searcher.search( query );
        }
        
        Logger.getLogger(SearchController.class).info("Search for " + query + " returned " + hits.length() + " results");
        
        
        
        
        
        
        int maximumCorrectedTotalResults = -1;
        
        /**
         * ============================================
         *               EXPANDED QUERY
         * ============================================
         */
        
        if (c.isExpanding()) {
            //LuceneQueryExpander expander = new LuceneQueryExpander();
            //expander.setSearcher( manager.getIndex(service.getProperty("query.expand.index", "wordnet")).getIndexSearcher() );
            //expander.setAnalyzer( analyzer );
            //supersetQuery = expander.expand( supersetQuery );
            
            OpenSearchQuery superset = new OpenSearchQuery();
            superset.setRole( "superset" );
            //superset.setSearchTerms( rewriteExpandedQuery( searchString, supersetQuery ) );
            //superset.setTotalResults( searcher.search( supersetQuery, c.getFilter() ).length() );
            /**
            if (!supersetQuery.equals(query)) {
                if (superset.getTotalResults() > maximumCorrectedTotalResults) {
                    maximumCorrectedTotalResults = superset.getTotalResults();
                }
                response.addQuery( superset );
            }
            */
        }
        
        
        
        
        
        /**
         * ============================================
         *            SPELL CHECKED QUERIES
         * ============================================
         */
        
        if (c.isSpellChecking()) {
            LuceneSpellChecker spellChecker = new LuceneSpellChecker( manager.getIndex(service.getProperty("query.spellcheck.index","spelling")).getLuceneDirectory() );
            spellChecker.setMaximumSuggestions(5);
            List<Query> correctionQueries = spellChecker.suggestSimilar( query );
            
            Iterator<Query> iterator = correctionQueries.iterator();
            while (iterator.hasNext()) {
                Query correctionQuery = iterator.next();
                OpenSearchQuery correction = new OpenSearchQuery();
                correction.setRole( "correction" );
                correction.setSearchTerms( correctionQuery.toString() );
                correction.setTotalResults( searcher.search( correctionQuery, c.getFilter() ).length() );
                
                if (correction.getTotalResults() > 0) {
                    if (correction.getTotalResults() > maximumCorrectedTotalResults) {
                        maximumCorrectedTotalResults = correction.getTotalResults();
                    }
                    response.addQuery( correction );
                }
            }
        }
        
        
        
        
        
        /**
         * ============================================
         *              SUGGESTED QUERY
         * ============================================
         */
        
        if (c.isSuggesting()) {
            List<Query> suggestedQueries = getSuggestedQueries( c, query );
            Iterator<Query> iterator = suggestedQueries.iterator();
            while (iterator.hasNext()) {
                Query suggestedQuery = iterator.next();
                
                if (suggestedQuery.equals(query)) {
                    continue;
                }
                
                OpenSearchQuery correction = new OpenSearchQuery();
                correction.setRole( "correction" );
                correction.setSearchTerms( rewriteQuery( c.getOpenSearchQuery().getSearchTerms(), suggestedQuery ) );
                correction.setTotalResults( searcher.search( suggestedQuery, c.getFilter() ).length() );
                
                if (correction.getTotalResults() > 0) {
                    if (correction.getTotalResults() > maximumCorrectedTotalResults) {
                        maximumCorrectedTotalResults = correction.getTotalResults();
                    }
                    response.addQuery( correction );
                }
            }
        }
        
        
        
        
        Iterator<OpenSearchQuery> queryIterator = response.getQueries().iterator();
        float threshold = 0.5f;
        List<OpenSearchQuery> revoked = new LinkedList<OpenSearchQuery>();
        while (queryIterator.hasNext()) {
            OpenSearchQuery q = queryIterator.next();
            float ratio = (float) q.getTotalResults() / maximumCorrectedTotalResults;
            if (q.getRole().equals("correction") && ratio < threshold) {
                revoked.add( q );
            }
        }
        Iterator<OpenSearchQuery> r = revoked.iterator();
        while (r.hasNext()) {
            response.removeQuery( r.next() );
        }
        Collections.sort( response.getQueries(), new OpenSearchQueryComparator() );
        
        
        
        
        
        Integer totalResults = c.getOpenSearchQuery().getTotalResults();
        if (totalResults == null || hits.length() < totalResults) {
            totalResults = hits.length();
            c.getOpenSearchQuery().setTotalResults( totalResults );
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
        //response.setTotalResults( limiter.getTotalEntries() );
        
        
        OpenSearchLink descriptionLink = new OpenSearchLink();
        descriptionLink.setHref( service.getOpenSearchDescriptionURL( request, request.getIndexNames() ) );
        descriptionLink.setRel("search");
        descriptionLink.setType("application/opensearchdescription+xml");
        response.setLink( descriptionLink );
        
        
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
                    result.setId( service.getDocumentURL( request, index, document ) );
                    
                    try {
                        result.setUpdated( document.getUpdated() );
                    }
                    catch (InsufficientDataException ide) {
                    }
                    
                    result.setRelevance( score );
                    
                    OpenSearchLink resultLink = new OpenSearchLink();
                    resultLink.setHref( service.getDocumentURL( request, index, document ) );
                    result.setLink( resultLink );
                    
                    
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

    
    
    public static List<Query> getExpandedQueries (LuceneContext c) {
        return new LinkedList<Query>();
    }
    
    public static List<Query> getSpellCheckedQueries (LuceneContext c) {
        return new LinkedList<Query>();
    }
    
    public static List<Query> getSuggestedQueries (LuceneContext c) {
        return new LinkedList<Query>();
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
            link.setHref( uri.with( "page", openSearchQuery.getFirstPage() ).toString() );
            link.setRel( "first" );
            link.setType( OpenSearch.getContentType( format ) );
            response.addLink( link );
        }
        
        
        /**
         * Previous
         */
        
        if ( response.getRelatedLink("previous") == null && openSearchQuery.getPreviousPage() != null ) {
            OpenSearchLink link = new OpenSearchLink();
            link.setHref( uri.with( "page", openSearchQuery.getPreviousPage() ).toString() );
            link.setRel( "previous" );
            link.setType( OpenSearch.getContentType( format ) );
            response.addLink( link );
        }
        
        
        /**
         * Self
         */
        
        if ( response.getRelatedLink("self") == null && openSearchQuery.getStartPage() != null ) {
            OpenSearchLink link = new OpenSearchLink();
            link.setHref( uri.with( "page", openSearchQuery.getStartPage() ).toString() );
            link.setRel( "self" );
            link.setType( OpenSearch.getContentType( format ) );
            response.addLink( link );
        }
        
        
        /**
         * Next
         */
        
        if ( response.getRelatedLink("next") == null && openSearchQuery.getNextPage() != null ) {
            OpenSearchLink link = new OpenSearchLink();
            link.setHref( uri.with( "page", openSearchQuery.getNextPage() ).toString() );
            link.setRel( "next" );
            link.setType( OpenSearch.getContentType( format ) );
            response.addLink( link );
        }
        
        
        /**
         * Last
         */
        
        if ( response.getRelatedLink("last") == null && openSearchQuery.getLastPage() != null ) {
            OpenSearchLink link = new OpenSearchLink();
            link.setHref( uri.with( "page", openSearchQuery.getLastPage() ).toString() );
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
    
    
    
    public static List<Query> getExpandedQueries (LuceneContext c, Query original) throws Exception {
        List<Query> expanded = new LinkedList<Query>();
        return expanded;
    }
    
    
    
    /**
     * Retrieves a list of queries which reflect the current query as 
     * examined by the spell checking system.
     */
    
    public static List<Query> getSpellCheckedQueries (LuceneContext c, Query original) throws Exception {
        LuceneWebService   service = c.getService();
        LuceneIndexManager manager = service.getIndexManager();
        
        OpenSearchResponse response = c.getOpenSearchResponse();
        
        List<Query> spellChecked = new LinkedList<Query>();
        
        LuceneSpellChecker spellChecker = new LuceneSpellChecker( manager.getIndex(service.getProperty("query.spellcheck.index","spelling")).getLuceneDirectory() );
        spellChecker.setMaximumSuggestions( 5 );
        spellChecked.addAll( spellChecker.suggestSimilar( original ) );
        
        Iterator<Query> iterator = spellChecked.iterator();
        while (iterator.hasNext()) {
            Query suggestedQuery = iterator.next();
            
            OpenSearchQuery correction = new OpenSearchQuery();
            correction.setRole( "correction" );
            correction.setSearchTerms( suggestedQuery.toString() );
            correction.setTotalResults( c.getIndexSearcher().search( suggestedQuery, c.getFilter() ).length() );
            
            if (correction.getTotalResults() > 0) {
                //if (correction.getTotalResults() > maximumCorrectedTotalResults) {
                //    maximumCorrectedTotalResults = correction.getTotalResults();
                //}
                response.addQuery( correction );
            }
        }
        
        return spellChecked;
    }
    
    
    
    public static List<Query> getSuggestedQueries (LuceneContext c, Query original)
        throws IndicesNotFoundException, IOException
    {
        List<Query> suggested = new LinkedList<Query>();
        
        LuceneIndex[] indices = c.getService().getIndexManager().getIndices( c.getRequest().getIndexNames() );
        
        IndexReader[] readers = new IndexReader[ indices.length ];
        
        for (int i = 0; i < indices.length; i++) {
            readers[ i ] = indices[ i ].getIndexReader();
        }
        
        MultiReader reader = new MultiReader( readers );
        
        suggested.addAll( getSuggestedQueries( original, reader ) );
        
        for (int i = 0; i < indices.length; i++) {
            indices[ i ].putIndexReader( readers[ i ] );
        }
        
        return suggested;
    }
    
    
    public static List<Query> getSuggestedQueries (Query original, IndexReader reader)
        throws IOException
    {
        List<Query> suggestions = new LinkedList<Query>();
        
        try {
            DidYouMeanQueryGenerator generator = new DidYouMeanQueryGenerator( original, reader );
            suggestions.addAll( generator.getQuerySuggestions( true, true ) );
            
            Iterator<Query> iterator = suggestions.iterator();
            while (iterator.hasNext()) {
                setDefaultBoost( iterator.next() );
            }
            
            return suggestions;
        }
        catch (Error err) {
        }
        catch (Exception e) {
        }
        
        return suggestions;
    }
    
    
    public static void setDefaultBoost (Query query) {
        if (query instanceof BooleanQuery) {
            setDefaultBoost( (BooleanQuery) query );
        }
        else {
            query.setBoost( 1.0f );
        }
    }
    
    public static void setDefaultBoost (BooleanQuery query) {
        BooleanClause[] clauses = query.getClauses();
        for (int i = 0; i < clauses.length; i++) {
            setDefaultBoost( clauses[ i ].getQuery() );
        }
        query.setBoost( 1.0f );
    }
    
    
    
    public static String rewriteQuery (String original, Query alternate) {
        StringBuffer rewritten = new StringBuffer();
        
        List<TokenTermQuery> termQueries = findTokenTermQueries( alternate );
        Iterator<TokenTermQuery> iterator = termQueries.iterator();
        
        int cursor = 0;
        while (iterator.hasNext()) {
            TokenTermQuery query = iterator.next();
            rewritten.append( original.substring( cursor, query.getToken().beginColumn ) );
            rewritten.append( query.getTerm().text() );
            cursor = query.getToken().endColumn;
        }
        
        rewritten.append( original.substring( cursor ) );
        
        return rewritten.toString();
    }
    
    
    /**
     * For boolean queries
     */
    
    public static String rewriteExpandedQuery (String original, Query alternate) {
        StringBuffer rewritten = new StringBuffer();
        
        List<TokenBooleanQuery> booleanQueries = findTokenBooleanQueries( alternate );
        Iterator<TokenBooleanQuery> iterator = booleanQueries.iterator();
        
        int cursor = 0;
        while (iterator.hasNext()) {
            TokenBooleanQuery query = iterator.next();
            int beginColumn = query.getToken().beginColumn;
            int endColumn   = query.getToken().endColumn;
            
            List<TokenTermQuery> termQueries = findTokenTermQueries( query );
            
            rewritten.append( original.substring( cursor, beginColumn ) );
            
            if (termQueries.size() == 1) {
                rewritten.append( termQueries.get(0).getTerm().text() );
            }
            else {
                rewritten.append("(");
                Iterator<TokenTermQuery> it = termQueries.iterator();
                boolean first = true;
                while (it.hasNext()) {
                    TermQuery tq = it.next();
                    if (first) {
                        first = false;
                    }
                    else {
                        rewritten.append(" ");
                    }
                    rewritten.append( tq.getTerm().text() );
                }
                rewritten.append(")");
            }
            
            //rewritten.append( original.substring( cursor, beginColumn ) );
            //rewritten.append( query.getTerm().text() );
            cursor = endColumn;
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
    
    public static List<TokenBooleanQuery> findTokenBooleanQueries (Query query) {
        List<TokenBooleanQuery> queries = new ArrayList<TokenBooleanQuery>();
        
        if (query instanceof BooleanQuery) {
            queries.addAll( findTokenBooleanQueries( (BooleanQuery) query ) );
        }
        
        Collections.sort( queries, new TokenBooleanQueryComparator() );
        return queries;
    }
    
    public static List<TokenBooleanQuery> findTokenBooleanQueries (BooleanQuery query) {
        BooleanClause[] clauses = query.getClauses();
        List<TokenBooleanQuery> queries = new ArrayList<TokenBooleanQuery>(clauses.length);
        
        if (query instanceof TokenBooleanQuery) {
            queries.add( (TokenBooleanQuery) query );
        }
        
        for (int i = 0; i < clauses.length; i++) {
            queries.addAll( findTokenBooleanQueries( clauses[i].getQuery() ) );
        }
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

class TokenBooleanQueryComparator implements Comparator<TokenBooleanQuery> {
    
    public int compare (TokenBooleanQuery query1, TokenBooleanQuery query2) {
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

class OpenSearchQueryComparator implements Comparator<OpenSearchQuery> {
    
    public int compare (OpenSearchQuery q1, OpenSearchQuery q2) {
        Integer count1 = q1.getTotalResults();
        Integer count2 = q2.getTotalResults();
        
        if (count1 == null && count2 == null) {
            return 0;
        }
        
        if (count1 == null) {
            return -1;
        }
        
        if (count2 == null) {
            return 1;
        }
        
        return count2.compareTo( count1 );
    }
    
}

class SearchedQueryComparator implements Comparator<SearchedQuery> {
    
    public int compare (SearchedQuery q1, SearchedQuery q2) {
        Integer hits1 = q1.getHits().length();
        Integer hits2 = q2.getHits().length();
        return hits2.compareTo( hits1 );
    }
    
}
