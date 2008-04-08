package net.lucenews.model;

/**
 * Copyright 2005 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.*;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.*;

/**
 * <p>Generates alternative queries by examining term(s) from a TermQuery
 * or a BooleanQuery with TermQuery's, and determines if a term can be 
 * replaced with a similar spelled term which can produce more hits.
 * <p>Example usage (pseudo-code):
 * <pre>
 * Query originalQuery = QueryParser.parse(queryString, &quot;contents&quot;, analyzer);
 * Hits hits = searcher.search(originalQuery);
 *
 * if (hits.length() &lt; SUGGESTION_THRESHOLD) {
 *     DidYouMeanQueryGenerator generator = new DidYouMeanQueryGenerator(originalQuery, reader);
 *     Query alternativeQuery = generator.getQuerySuggestion(true, true);
 *     if (alternativeQuery.equals(originalQuery)) {
 *         // no better alternative found...
 *     } else {
 *         // an alternate query was generated, do something with it
 *         String suggestion = alternativeQuery.toString(&quot;contents&quot;);
 *         out.write(&quot;Did you mean: &lt;a href='search?q=&quot; + suggestion + &quot;'&gt;&quot;
 *                   + suggestion + &quot;&lt;/a&gt;?&quot;);
 *     }
 * }
 *
 * // do something with hits...
 * </pre>
 *
 * @author  Ronnie Kolehmainen (ronnie.kolehmainen at ub.uu.se)
 * @version $Revision$, $Date$
 */

public class DidYouMeanQueryGenerator
{
    private Query originalQuery;
    private IndexReader reader;
    
    
    /**
     * Constructor with {@link Query} and {@link IndexReader}.
     * @param  query  the query
     * @param  reader the Indexreader
     */
    
    public DidYouMeanQueryGenerator(Query query, IndexReader reader)
    {
        this.originalQuery = query;
        this.reader = reader;
    }
    
    
    /**
     * Tries to find a query which will give more hits using the {@link FuzzyQuery}
     * algorithm (<a href="http://www.levenshtein.net/">Levenshtein distance</a>)
     * along with document frequency for a term.
     * @param  useFreqCount    Set to <tt>true</tt> to only replace terms which
     *                         have a higher document frequency count (and would
     *                         give more hits) than the replaced original term.
     * @param  useDefaultBoost Set to <tt>true</tt> to give all replaced term queries
     *                         a boost of 1.0f instead of the boost computed from
     *                         the edit distance (suitable for human-readable
     *                         query.toString(field) result).
     * @return If and only if a more suitable query can be calculated, a new {@link Query}
     *         is returned. In all other cases the original query is returned.
     */
    
    public Query getQuerySuggesstion (boolean useFreqCount, boolean useDefaultBoost)
        throws IOException
    {
        List<Query> suggestions = getQuerySuggestions( useFreqCount, useDefaultBoost );
        if( suggestions.isEmpty() )
            return null;
        return suggestions.get( 0 );
    }
    
    
    
    /**
     * Gets a list of query suggestions. Ordered by highest cumulative boost to lowest.
     * 
     * @param useFreqCount
     * @param useDefaultBoost
     * @return a list of query suggestions
     */
    
    public List<Query> getQuerySuggestions (boolean useFreqCount, boolean useDefaultBoost)
        throws IOException
    {
        List<Query> suggestions = new ArrayList<Query>();
        
        if( originalQuery instanceof TermQuery ) {
            suggestions.addAll( getBetterTermQueries( (TermQuery) originalQuery, useFreqCount, useDefaultBoost ) );
        }
        
        if( originalQuery instanceof BooleanQuery ) {
            suggestions.addAll( recursiveExtract( (BooleanQuery) originalQuery, useFreqCount, useDefaultBoost ) );
        }
        
        Float least = null;
        Iterator<Query> iterator = suggestions.iterator();
        while( iterator.hasNext() ) {
            Float localLeast = getLowestBoost( iterator.next() );
            if( least == null || ( localLeast < least ) )
                least = localLeast;
        }
        
        Float scale = least;
        if( least == null )
            scale = 1.0f;
        
        Collections.sort( suggestions, new ScaledBoostQueryComparator( scale ) );
        
        return suggestions;
    }
    
    
    /**
     * Recursive extraction of the BooleanQuery in order to find all TermQuery's.
     * Returns a list sorted from greatest boost to least
     * 
     * @param bq
     * @param useFreqCount
     * @param useDefaultBoost
     */
    
    private List<BooleanQuery> recursiveExtract (BooleanQuery bq, boolean useFreqCount, boolean useDefaultBoost)
        throws IOException
    {
        BooleanClause[] clauses = bq.getClauses();
        List<BooleanQuery> returnQueries = new ArrayList<BooleanQuery>();
        Query[][] queries = new Query[ clauses.length ][];
        
        for (int i = 0; i < clauses.length; i++) {
            BooleanClause clause = clauses[ i ];
            
            queries[ i ] = new Query[ 0 ];
            
            if( !clause.isProhibited() && clause.getQuery() instanceof BooleanQuery )
                queries[ i ] = recursiveExtract( (BooleanQuery) clause.getQuery(), useFreqCount, useDefaultBoost ).toArray( new Query[]{} );
            
            if( !clause.isProhibited() && clause.getQuery() instanceof TermQuery )
                queries[ i ] = getBetterTermQueries( (TermQuery) clause.getQuery(), useFreqCount, useDefaultBoost ).toArray( new Query[]{} );
        }
        
        
        
        int[] indices = new int[ queries.length ];
        
        while( indices[ 0 ] <= queries[ 0 ].length ) {
            
            BooleanQuery returnQuery = null;
            if ( bq instanceof ExpandedTermQuery ) {
                returnQuery = new ExpandedTermQuery();
            }
            else {
                returnQuery = new BooleanQuery();
            }
            
            boolean addThis = true;
            for( int i = 0; i < indices.length; i++ ) {
                int j = indices[ i ];
                if( queries[ i ].length > 0 && queries[ i ].length > j ) {
                    Query query = queries[ i ][ j ];
                    returnQuery.add( query, clauses[ i ].getOccur() );
                    addThis = true;
                }
                else {
                    addThis = false;
                    break;
                }
            }
            
            if( addThis )
                returnQueries.add( returnQuery );
            
            // Increment
            int i = indices.length - 1;
            indices[ i ]++;
            while( i > 0 && indices[ i ] >= queries[ i ].length ) {
                indices[ i ] = 0;
                indices[ --i ]++;
            }
        }
        
        
        return returnQueries;
    }
    
    
    
    /**
     * This is where the magic happens.
     * 
     * @param tq
     * @param useFreqCount
     * @param useDefaultBoost
     */
    
    private List<TermQuery> getBetterTermQueries (TermQuery tq, boolean useFreqCount, boolean useDefaultBoost)
        throws IOException
    {
        OrderedTermsFuzzyQuery fq = new OrderedTermsFuzzyQuery( tq );
        
        List<TermQuery> list = fq.bestOrderRewrite( reader, useFreqCount );
        
        //Iterator<TermQuery> iterator = list.iterator();
        //while( iterator.hasNext() )
        //    modifyBoost( iterator.next(), useDefaultBoost );
        
        if( list.isEmpty() )
            list.add( tq );
        
        return list;
    }
    
    
    private TermQuery modifyBoost (TermQuery tq, boolean removeComputedBoost)
    {
        if( removeComputedBoost )
            tq.setBoost( 1.0f );
        return tq;
    }
    
    
    
    
    public static Float getLowestBoost (Query query) {
        if( query instanceof BooleanQuery )
            return getLowestBoost( (BooleanQuery) query );
        return query.getBoost();
    }
    
    public static Float getLowestBoost (BooleanQuery query) {
        BooleanClause[] clauses = query.getClauses();
        
        if( clauses.length == 0 )
            return null;
        
        Float lowest = getLowestBoost( clauses[ 0 ].getQuery() );
        for( int i = 1; i < clauses.length; i++ ) {
            Float subLowest = getLowestBoost( clauses[ i ].getQuery() );
            
            if( lowest == null || ( subLowest != null && subLowest < lowest ) )
                lowest = subLowest;
        }
        
        return lowest;
    }
}


class ScaledBoostQueryComparator implements Comparator<Query> {
    
    private float scale;
    
    public ScaledBoostQueryComparator (float scale) {
        this.scale = scale;
    }
    
    public int compare (Query query1, Query query2) {
        Float boost1 = getScaledBoost( query1, scale );
        Float boost2 = getScaledBoost( query2, scale );
        return boost2.compareTo( boost1 );
    }
    
    /**
     * Returns the scaled boost of the query. Useful when sorting
     * by a de-normalized scale factor.
     */
    
    public static float getScaledBoost (Query query, float scale) {
        if( query instanceof BooleanQuery )
            return getScaledBoost( (BooleanQuery) query, scale );
        return scale * query.getBoost();
    }
    
    public static float getScaledBoost (BooleanQuery query, float scale) {
        BooleanClause[] clauses = query.getClauses();
        
        float boost = 1;
        for( int i = 0; i < clauses.length; i++ )
            if( clauses[ i ].isRequired() )
                boost *= scale * getScaledBoost( clauses[ i ].getQuery(), scale );
        
        return boost;
    }
    
}

class TermQueryBoostComparator implements Comparator<TermQuery> {
    
    public int compare (TermQuery query1, TermQuery query2) {
        Float boost1 = query1.getBoost();
        Float boost2 = query2.getBoost();
        return boost1.compareTo( boost2 );
    }
    
}
