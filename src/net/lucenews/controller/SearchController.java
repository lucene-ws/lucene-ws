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
        Logger.getLogger(SearchController.class).trace("doGet(LuceneContext)");
        
        LuceneWebService   service  = c.service();
        LuceneIndexManager manager  = service.getIndexManager();
        LuceneRequest      req      = c.req();
        LuceneIndex[]      indices  = manager.getIndices( req.getIndexNames() );
        Filter             filter   = req.getFilter();
        Sort               sort     = req.getSort();
        OpenSearchResponse response = new OpenSearchResponse();
        List<String> invalidParameterNames = new LinkedList<String>();
        
        
        
        Boolean   expand  = false;
        Boolean[] expands = new Boolean[] {
            req.getBooleanParameter("expand"),
            service.getBooleanProperty("query.expand"),
            Boolean.FALSE
        };
        for (int i = 0; i < expands.length; i++) {
            if (expands[i] != null) {
                expand = expands[i];
                break;
            }
        }
        Logger.getLogger(SearchController.class).info("Expand query: " + expand);
        
        Boolean   spellcheck = false;
        Boolean[] spellchecks = new Boolean[] {
            req.getBooleanParameter("spellcheck"),
            service.getBooleanProperty("query.spellcheck"),
            Boolean.FALSE
        };
        for (int i = 0; i < spellchecks.length; i++) {
            if (spellchecks[i] != null) {
                spellcheck = spellchecks[i];
                break;
            }
        }
        Logger.getLogger(SearchController.class).info("Spell check query: " + spellcheck);
        
        Boolean   suggest  = false;
        Boolean[] suggests = new Boolean[] {
            req.getBooleanParameter("suggest"),
            service.getBooleanProperty("query.suggest"),
            Boolean.FALSE
        };
        for (int i = 0; i < suggests.length; i++) {
            if (suggests[i] != null) {
                suggest = suggests[i];
                break;
            }
        }
        Logger.getLogger(SearchController.class).info("Suggest query: " + spellcheck);
        
        
        
        
        /**
         * ============================================
         *               OpenSearch Format
         * ============================================
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
         * ============================================
         *              CREATE SEARCHER
         * ============================================
         */
        
        IndexSearcher[] searchers = new IndexSearcher[indices.length];
        for (int i = 0; i < indices.length; i++) {
            searchers[i] = indices[i].getIndexSearcher();
        }
        LuceneMultiSearcher searcher = new LuceneMultiSearcher( searchers, getSearcherIndexField() );
        
        
        
        
        
        /**
         * ============================================
         *              PREPARE THE QUERY
         * ============================================
         */
        
        Query           query = null;
        Query   supersetQuery = null;
        Query correctionQuery = null;
        
        try {
                      query = req.getQuery();
              supersetQuery = req.getQuery();
            correctionQuery = req.getQuery();
        }
        catch (InsufficientDataException ide) {
            query = null;
        }
        
        
        
        
        
        /**
         * ============================================
         *              SEARCH STRING
         * ============================================
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
        Logger.getLogger(SearchController.class).debug("Search string: " + String.valueOf( searchString ));
        
        
        
        
        
        /**
         * ============================================
         *              DEFAULT FIELD
         * ============================================
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
        
        
        
        
        
        /**
         * ============================================
         *                  ANALYZER
         * ============================================
         */
        
        Analyzer analyzer = req.getAnalyzer();
        if (analyzer == null) {
            invalidParameterNames.add( req.getParameterName( LuceneKeys.ANALYZER ) );
        }
        
        
        
        
        
        /**
         * ============================================
         *      INFORM USER OF INVALID PARAMETERS
         * ============================================
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
        
        
        
        
        
        /**
         * ============================================
         *              PARSE THE QUERY
         * ============================================
         */
        
        QueryParser.Operator defaultOperator = null;
        
        if (query == null) {
            
            /**
             * Build the query parser
             */
            
            LuceneQueryParser parser = new LuceneQueryParser( defaultField, analyzer );
            
            
            Locale locale = req.getLocale();
            if (locale != null) {
                parser.setLocale( locale );
            }
            
            
            /**
             * Resolve the default operator
             */
            
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
            
            
            
            query = parser.parse( searchString );
            if (expand) {
                parser.setSynonymSearcher( manager.getIndex(service.getProperty("query.expand.index", "wordnet")).getIndexSearcher() );
                supersetQuery = parser.parse( searchString );
                parser.setSynonymSearcher( null );
            }
            if (spellcheck) {
                correctionQuery = parser.parse( searchString );
            }
        }
        
        
        
        
        
        
        
        /**
         * ============================================
         *              PERFORM SEARCH
         * ============================================
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
        
        
        
        
        
        
        int maximumCorrectedTotalResults = -1;
        
        /**
         * ============================================
         *               EXPANDED QUERY
         * ============================================
         */
        
        if (expand) {
            //LuceneQueryExpander expander = new LuceneQueryExpander();
            //expander.setSearcher( manager.getIndex(service.getProperty("query.expand.index", "wordnet")).getIndexSearcher() );
            //expander.setAnalyzer( analyzer );
            //supersetQuery = expander.expand( supersetQuery );
            
            OpenSearchQuery superset = new OpenSearchQuery();
            superset.setRole( "superset" );
            superset.setSearchTerms( rewriteExpandedQuery( searchString, supersetQuery ) );
            superset.setTotalResults( searcher.search( supersetQuery, filter ).length() );
            if (!supersetQuery.equals(query)) {
                if (superset.getTotalResults() > maximumCorrectedTotalResults) {
                    maximumCorrectedTotalResults = superset.getTotalResults();
                }
                response.addQuery( superset );
            }
        }
        
        
        
        
        
        /**
         * ============================================
         *            SPELL CHECKED QUERIES
         * ============================================
         */
        
        if (spellcheck) {
            LuceneSpellChecker spellChecker = new LuceneSpellChecker( manager.getIndex(service.getProperty("query.spellcheck.index","spelling")).getLuceneDirectory() );
            spellChecker.setMaximumSuggestions(5);
            Query[] correctionQueries = spellChecker.suggestSimilar( correctionQuery );
            
            for (int i = 0; i < correctionQueries.length; i++) {
                OpenSearchQuery correction = new OpenSearchQuery();
                correction.setRole( "correction" );
                correction.setSearchTerms( correctionQueries[ i ].toString() );
                correction.setTotalResults( searcher.search( correctionQueries[ i ], filter ).length() );
                
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
        
        if (suggest) {
            Query[] suggestedQueries = getSuggestedQueries( query, indices );
            for (int i = 0; i < suggestedQueries.length; i++) {
                Query suggestedQuery = suggestedQueries[ i ];
                
                if (suggestedQuery.equals(query)) {
                    continue;
                }
                
                OpenSearchQuery correction = new OpenSearchQuery();
                correction.setRole( "correction" );
                correction.setSearchTerms( rewriteQuery( searchString, suggestedQuery ) );
                correction.setTotalResults( searcher.search( suggestedQueries[ i ], filter ).length() );
                
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
        
        
        
        
        
        
        
        /**
         * ============================================
         *              LIMITER / ITERATOR
         * ============================================
         */
        
        Limiter limiter = req.getLimiter();
        limiter.setTotalEntries( null );
        Integer maximum = null;
        
        try {
            String maximumString = req.getCleanParameter("maximum");
            if (maximumString != null) {
                maximum = Integer.valueOf( maximumString );
                limiter.setTotalEntries( maximum );
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
        
        
        
        
        
        /**
         * ============================================
         *            OpenSearch Response
         * ============================================
         */
        
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
        response.setTotalResults( limiter.getTotalEntries() );
        
        
        OpenSearchLink descriptionLink = new OpenSearchLink();
        descriptionLink.setHref( service.getOpenSearchDescriptionURL( req, req.getIndexNames() ) );
        descriptionLink.setRel("search");
        descriptionLink.setType("application/opensearchdescription+xml");
        response.setLink( descriptionLink );
        
        
        OpenSearchQuery requestQuery = new OpenSearchQuery();
        requestQuery.setRole("request");
        requestQuery.setTotalResults( limiter.getTotalEntries() );
        requestQuery.setSearchTerms( searchString );
        if (maximum != null) {
            requestQuery.setCount( maximum );
        }
        response.addQuery( requestQuery );
        
        response.setDescription( title.toString() );
        addPagingInformation( c, response, limiter, format );
        
        
        // DOM Document to produce XML later on
        org.w3c.dom.Document domDocument = XMLController.newDocument();
        
        
        
        
        
        /**
         * ============================================
         *               ADD RESULTS
         * ============================================
         */
        
        iterator.reset();
        while( iterator.hasNext() ) {
            LuceneDocument document = iterator.next();
            Integer searcherIndex = extractSearcherIndex( document );
            
            LuceneIndex index = null;
            if (searcherIndex != null) {
                index = indices[ searcherIndex ];
                document.setIndex( index );
            }
            
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
    
    
    
    
    /**
     * Adds paging information to the given OpenSearch response.
     * @param c        the Lucene context
     * @param response the OpenSearch response
     * @param limiter  the limiter
     */
    
    public static void addPagingInformation (LuceneContext c, OpenSearchResponse response, Limiter limiter, OpenSearch.Format format)
        throws InsufficientDataException
    {
        LuceneRequest req = c.req();
        
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
