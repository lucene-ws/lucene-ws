package net.lucenews.opensearch;

import java.util.*;
import org.w3c.dom.*;

/**
 * OpenSearch Query provides a way of referring to a specific set of 
 * search results. It is simple, powerful and flexible, and has a wide 
 * variety of uses. In combination with an OpenSearch Description file 
 * (another of the components of OpenSearch), it can be used to 
 * generate a request for search results.
 *
 * Source: http://opensearch.a9.com/spec/1.1/query/
 */

public class OpenSearchQuery {
    
    
    static public final class Role {
        static public final String EXAMPLE    = "example";
        static public final String RELATED    = "related";
        static public final String REQUEST    = "request";
        static public final String CORRECTION = "correction";
        static public final String SUPERSET   = "superset";
        static public final String SUBSET     = "subset";
    }
    
    
    private String  role;
    private String  title;
    private String  osd;
    private Integer total_results;
    private String  search_terms;
    private Integer count;
    private Integer start_index;
    private Integer start_page;
    private String  language;
    private String  output_encoding;
    private String  input_encoding;
    private Map<String,String> namespaces;
    
    
    public OpenSearchQuery () {
        namespaces = new LinkedHashMap<String,String>();
    }
    
    
    
    public String getRole () {
        return role;
    }
    
    public void setRole (String role) {
        this.role = role;
    }
    
    public boolean hasValidRole () {
        if (getRole() == null) {
            return false;
        }
        
        String[] valid_roles = new String[] {
            Role.EXAMPLE,
            Role.RELATED,
            Role.REQUEST,
            Role.CORRECTION,
            Role.SUPERSET,
            Role.SUBSET
        };
        
        for (int i = 0; i < valid_roles.length; i++) {
            if (valid_roles[i].equals(getRole())) {
                return true;
            }
        }
        
        return false;
    }
    
    
    
    
    public String getTitle () {
        return title;
    }
    
    public void setTitle (String title) {
        this.title = title;
    }
    
    
    
    public String getOsd () {
        return osd;
    }
    
    public void setOsd (String osd) {
        this.osd = osd;
    }
    
    
    
    public Integer getTotalResults () {
        return total_results;
    }
    
    public void setTotalResults (Integer total_results) {
        this.total_results = total_results;
    }
    
    
    
    public String getSearchTerms () {
        return search_terms;
    }
    
    public void setSearchTerms (String search_terms) {
        this.search_terms = search_terms;
    }
    
    
    
    public Integer getCount () {
        return count;
    }
    
    public void setCount (Integer count) {
        this.count = count;
    }
    
    
    
    /**
     * startIndex
     *
     * Description:  The offset of the first search result, starting with one.
     * Restrictions: A positive integer.
     * Default:      "1"
     */
    
    public Integer getStartIndex () {
        return start_index;
    }
    
    public void setStartIndex (Integer start_index) {
        this.start_index = start_index;
    }
    
    
    
    /**
     * startPage
     *
     * Description:  The offset of the each group of count search results, starting with one.
     * Restrictions: A positive integer.
     * Default:      "1"
     */
    
    public Integer getStartPage () {
        return start_page;
    }
    
    public void setStartPage (Integer start_page) {
        this.start_page = start_page;
    }
    
    
    
    public String getLanguage () {
        return language;
    }
    
    public void setLanguage (String language) {
        this.language = language;
    }
    
    
    
    public String getOutputEncoding () {
        return output_encoding;
    }
    
    public void setOutputEncoding (String output_encoding) {
        this.output_encoding = output_encoding;
    }
    
    
    
    public String getInputEncoding () {
        return input_encoding;
    }
    
    public void setInputEncoding (String input_encoding) {
        this.input_encoding = input_encoding;
    }
    
    
    
    public Integer[] getBoundingIndices () {
        Integer[] bounds = new Integer[]{ null, null };
        
        Integer totalResults = getTotalResults();
        Integer count = getCount();
        
        if (totalResults == null || totalResults == 0 || count == null || count == 0) {
            return bounds;
        }
        
        Integer startIndex = getStartIndex();
        if (startIndex == null) {
            startIndex = 1; // default
        }
        
        Integer startPage = getStartPage();
        if (getStartIndex() != null || startPage == null) {
            startPage = 1; // default
        }
        
        Integer firstIndex = startIndex + (startPage - 1) * count;
        Integer  lastIndex = startIndex + (startPage * count) - 1;
        
        if (firstIndex > totalResults || lastIndex < 1) {
            return bounds;
        }
        
        bounds[0] = Math.max( firstIndex, 1 );
        bounds[1] = Math.min( lastIndex,  totalResults );
        
        return bounds;
    }
    
    
    /**
     * Returns the first index of desired results
     * in light of the total number of results
     * specified.
     * If either "count" or "totalResults" has not
     * been specified, a null value is returned.
     */
    
    public Integer getFirstIndex () {
        return getBoundingIndices()[ 0 ];
    }
    
    /**
     * Returns the last index of desired results
     * in light of the total number of results
     * specified.
     * If either "count" or "totalResults" has not
     * been specified, a null value is returned.
     */
    
    public Integer getLastIndex () {
        return getBoundingIndices()[ 1 ];
    }
    
    
    
    public Integer[] getPagingBoundaries () {
        Integer[] boundaries = new Integer[ 2 ];
        
        Integer totalResults = getTotalResults();
        Integer count        = getCount();
        
        if (totalResults == null || totalResults == 0 || count == null || count == 0) {
            return boundaries;
        }
        
        Integer startIndex = getStartIndex();
        if (startIndex == null) {
            startIndex = 1; // default
        }
        
        Integer startPage = getStartPage();
        if (startPage == null) {
            startPage = 1; // default
        }
        
        if (totalResults < startIndex) {
            return boundaries;
        }
        
        boundaries[ 0 ] = 1;
        boundaries[ 1 ] = (int) Math.ceil( ( totalResults - startIndex + 1 ) / count );
        
        return boundaries;
    }
    
    public Integer getFirstPage () {
        return getPagingBoundaries()[ 0 ];
    }
    
    public Integer getPreviousPage () {
        Integer startPage = getStartPage();
        Integer firstPage = getFirstPage();
        
        if (startPage == null) { startPage = 1; }
        
        if (firstPage == null || startPage.equals( firstPage )) {
            return null;
        }
        
        return startPage - 1;
    }
    
    public Integer getNextPage () {
        Integer startPage = getStartPage();
        Integer lastPage  = getLastPage();
        
        if (startPage == null) { startPage = 1; }
        
        if (lastPage == null || startPage.equals( lastPage )) {
            return null;
        }
        
        return startPage + 1;
    }
    
    public Integer getLastPage () {
        return getPagingBoundaries()[ 1 ];
    }
    
    
    
    public void setNamespace (String namespace, String uri) {
        namespaces.put( namespace, uri );
    }
    
    
    
    
    public static OpenSearchQuery asOpenSearchQuery (Element element) {
        OpenSearchQuery query = new OpenSearchQuery();
        
        // role
        String role = element.getAttribute("role");
        query.setRole( role );
        
        // title
        String title = element.getAttribute("title");
        query.setTitle( title );
        
        // osd
        String osd = element.getAttribute("osd");
        query.setOsd( osd );
        
        // totalResults
        String totalResults = element.getAttribute("totalResults");
        if ( totalResults != null ) {
            query.setTotalResults( Integer.valueOf( totalResults ) );
        }
        
        // searchTerms
        String searchTerms = element.getAttribute("searchTerms");
        query.setSearchTerms( searchTerms );
        
        // count
        String count = element.getAttribute("count");
        if ( count != null ) {
            query.setCount( Integer.valueOf( count ) );
        }
        
        // startIndex
        String startIndex = element.getAttribute("startIndex");
        if ( startIndex != null ) {
            query.setStartIndex( Integer.valueOf( startIndex ) );
        }
        
        // startPage
        String startPage = element.getAttribute("startPage");
        if ( startPage != null ) {
            query.setStartPage( Integer.valueOf( startPage ) );
        }
        
        // language
        String language = element.getAttribute("language");
        query.setLanguage( language );
        
        // outputEncoding
        String outputEncoding = element.getAttribute("outputEncoding");
        query.setOutputEncoding( outputEncoding );
        
        // inputEncoding
        String inputEncoding = element.getAttribute("inputEncoding");
        query.setInputEncoding( inputEncoding );
        
        return query;
    }
    
    
    
    public Element asElement (Document document, OpenSearch.Format format) throws OpenSearchException {
        return asElement(document, format, OpenSearch.STRICT);
    }
    
    public Element asElement (Document document, OpenSearch.Format format, OpenSearch.Mode mode) throws OpenSearchException {
        Element element = document.createElementNS("http://a9.com/-/spec/opensearch/1.1/","opensearch:Query");
        
        // role
        if (getRole() == null) {
            if (mode != OpenSearch.PASSIVE) {
                throw new OpenSearchException("No role specified");
            }
        }
        else {
            if (mode == OpenSearch.STRICT && !hasValidRole()) {
                throw new OpenSearchException("Invalid role: " + getRole());
            }
            if (format == OpenSearch.ATOM) {
                element.setAttribute("rel", getRole());
            }
            else {
                element.setAttribute("role", getRole());
            }
        }
        
        // title
        if (getTitle() != null) {
            element.setAttribute("title", getTitle());
        }
        
        // osd
        if (getOsd() != null) {
            element.setAttribute("osd", getOsd());
        }
        
        // totalResults
        if (getTotalResults() != null) {
            element.setAttribute("totalResults", String.valueOf(getTotalResults()));
        }
        
        // searchTerms
        if (getSearchTerms() != null) {
            element.setAttribute("searchTerms", getSearchTerms());
        }
        
        // count
        if (getCount() != null) {
            element.setAttribute("count", String.valueOf(getCount()));
        }
        
        // startIndex
        if (getStartIndex() != null) {
            element.setAttribute("startIndex", String.valueOf(getStartIndex()));
        }
        
        // startPage
        if (getStartPage() != null) {
            element.setAttribute("startPage", String.valueOf(getStartPage()));
        }
        
        // language
        if (getLanguage() != null) {
            element.setAttribute("language", getLanguage());
        }
        
        // outputEncoding
        if (getOutputEncoding() != null) {
            element.setAttribute("outputEncoding", getOutputEncoding());
        }
        
        // inputEncoding
        if (getInputEncoding() != null) {
            element.setAttribute("inputEncoding", getInputEncoding());
        }
        
        // namespaces
        Iterator<Map.Entry<String,String>> namespaceIterator = namespaces.entrySet().iterator();
        while (namespaceIterator.hasNext()) {
            Map.Entry<String,String> namespace = namespaceIterator.next();
            element.setAttribute( "xmlns:"+namespace.getKey(), namespace.getValue() );
        }
        
        return element;
    }
}
