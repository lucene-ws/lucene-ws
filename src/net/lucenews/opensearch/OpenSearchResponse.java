package net.lucenews.opensearch;

import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * OpenSearch Response elements are used to add search data to 
 * existing XML-based syndication formats, such as RSS 2.0 and 
 * Atom 1.0. These extensions are designed to work with OpenSearch 
 * Description files, another of the components of OpenSearch, to 
 * provide rich search syndication with a minimal amount of overhead.
 * 
 * OpenSearch Response introduces several elements that provide the 
 * necessary information for syndicating search results. Additionally, 
 * OpenSearch Response recommends clean extensibility guidelines, and 
 * suggests that clients and servers recognize certain other common 
 * extensions to enhance search results. 
 * 
 *  Source: http://opensearch.a9.com/spec/1.1/response/
 * 
 */

public class OpenSearchResponse {
    
    private String   title;
    private String   id;
    private Calendar updated;
    private String   description;
    private Integer  totalResults;
    private Integer  startIndex;
    private Integer  itemsPerPage;
    private OpenSearchLink link;
    private List<OpenSearchLink>   links;
    private List<OpenSearchQuery>  queries;
    private List<OpenSearchResult> results;
    
    
    public OpenSearchResponse () {
        links   = new LinkedList<OpenSearchLink>();
        queries = new LinkedList<OpenSearchQuery>();
        results = new LinkedList<OpenSearchResult>();
    }
    
    
    
    
    public String getTitle () {
        return title;
    }
    
    public void setTitle (String title) {
        this.title = title;
    }
    
    
    
    public String getDescription () {
        return description;
    }
    
    public void setDescription (String description) {
        this.description = description;
    }
    
    
    
    public String getId () {
        return id;
    }
    
    public void setId (String id) {
        this.id = id;
    }
    
    
    
    public Calendar getUpdated () {
        return updated;
    }
    
    public void setUpdated (Calendar updated) {
        this.updated = updated;
    }
    
    
    
    /**
     * totalResults - the maximum number of results available for these 
     * search terms
     * 
     * Restrictions: An integer greater than or equal to 0.
     * Default: The number of items that were returned in this set of 
     *          results.
     * Requirements: May appear zero or one time.
     */
    
    public Integer getTotalResults () {
        return totalResults;
    }
    
    public void setTotalResults (Integer totalResults) {
        this.totalResults = totalResults;
    }
    
    
    
    /**
     * startIndex - the index of the first item returned in the result.
     * 
     * Restrictions: An integer greater than or equal to 1.
     * Note: The first result is 1.
     * Default: 1
     * Requirements: May appear zero or one time.
     */
    
    public Integer getStartIndex () {
        return startIndex;
    }
    
    public void setStartIndex (Integer startIndex) {
        this.startIndex = startIndex;
    }
    
    
    
    /**
     * itemsPerPage - the maximum number of items that can appear in 
     * one page of results.
     * 
     * Restrictions: An integer greater than or equal to 1.
     * Default: The number of items that were returned in this set of 
     *          results.
     * Requirements: May appear zero or one time.
     */
    
    public Integer getItemsPerPage () {
        return itemsPerPage;
    }
    
    public void setItemsPerPage (Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }
    
    
    
    /**
     * link - a reference back to the OpenSearch Description file
     * 
     * Attributes: This is a clone of the link element in Atom, 
     *             including href, hreflang, rel, and type attributes.
     * Restrictions: The rel attribute must equal search.
     * Note: New in version 1.1.
     * Requirements: May appear zero or one time.
     */
    
    public OpenSearchLink getLink () {
        return link;
    }
    
    public void setLink (OpenSearchLink link) {
        this.link = link;
    }
    
    public List<OpenSearchLink> getLinks () {
        return links;
    }
    
    public void addLink (OpenSearchLink link) {
        links.add( link );
    }
    
    public boolean removeLink (OpenSearchLink link) {
        return links.remove( link );
    }
    
    public OpenSearchLink getRelatedLink (String rel) {
        Iterator<OpenSearchLink> iterator = getLinks().iterator();
        while ( iterator.hasNext() ) {
            OpenSearchLink link = iterator.next();
            if (link.getRel() != null && link.getRel().toLowerCase().trim().equals(rel.toLowerCase().trim())) {
                return link;
            }
        }
        return null;
    }
    
    
    
    /**
     * Query - in an OpenSearch Response, can be used both to echo back 
     * the original query and to suggest new searches. Please see the 
     * OpenSearch Query specification for more information.
     * 
     * Note: New in version 1.1.
     * Requirements: May appear zero or more times. Note that the "Q" 
     * is capitalized.
     */
    
    public List<OpenSearchQuery> getQueries () {
        return queries;
    }
    
    public OpenSearchQuery getRequestQuery () {
        return getRelatedQuery("request");
    }
    
    public OpenSearchQuery getRelatedQuery (String role) {
        Iterator<OpenSearchQuery> iterator = getQueries().iterator();
        while ( iterator.hasNext() ) {
            OpenSearchQuery query = iterator.next();
            if (query.getRole() != null && query.getRole().toLowerCase().trim().equals(role.toLowerCase().trim())) {
                return query;
            }
        }
        return null;
    }
    
    public void addQuery (OpenSearchQuery query) {
        queries.add( query );
    }
    
    public void addQueries (OpenSearchQuery... queries) {
        this.queries.addAll( Arrays.asList( queries ) );
    }
    
    public void addQueries (List<OpenSearchQuery> queries) {
        this.queries.addAll( queries );
    }
    
    public boolean removeQuery (OpenSearchQuery query) {
        return queries.remove( query );
    }
    
    
    
    public List<OpenSearchResult> getResults () {
        return results;
    }
    
    public void addResult (OpenSearchResult result) {
        results.add( result );
    }
    
    public boolean removeResult (OpenSearchResult result) {
        return results.remove( result );
    }
    
    
    
    
    public static OpenSearchResponse asOpenSearchResponse (Element element) {
        OpenSearchResponse response = new OpenSearchResponse();
        
        // Atom
        
        
        return response;
    }
    
    
    
    
    
    public Document asDocument (OpenSearch.Format format)
        throws OpenSearchException, ParserConfigurationException
    {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        document.appendChild(asElement( document, format ));
        return document;
    }
    
    public Document asDocument (OpenSearch.Format format, OpenSearch.Mode mode)
        throws OpenSearchException, ParserConfigurationException
    {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        document.appendChild(asElement( document, format, mode ));
        return document;
    }
    
    
    
    
    public Element asElement (Document document, OpenSearch.Format format) throws OpenSearchException {
        return asElement(document, format, OpenSearch.getDefaultMode());
    }
    
    public Element asElement (Document document, OpenSearch.Format format, OpenSearch.Mode mode) throws OpenSearchException {
        
        OpenSearchQuery requestQuery = getRequestQuery();
        
        /**
         * Atom
         */
        
        if (format == OpenSearch.ATOM) {
            Element element = document.createElement("feed");
            element.setAttribute("xmlns:opensearch","http://a9.com/-/spec/opensearch/1.1/");
            element.setAttribute("xmlns","http://www.w3.org/2005/Atom");
            
            // title
            if (getTitle() != null) {
                element.appendChild( asElement( document, "title", getTitle() ) );
            }
            
            
            // id
            if (getId() != null) {
                element.appendChild( asElement( document, "id", getId() ) );
            }
            
            
            // updated
            if (getUpdated() != null) {
                element.appendChild( asElement( document, "updated", net.lucenews.atom.Entry.asString( getUpdated() ) ) );
            }
            
            
            // Link
            if (getLink() != null) {
                if (getLink().getRel() != null) {
                    if (!getLink().getRel().equals("search")) {
                        if (mode == OpenSearch.STRICT) {
                            throw new OpenSearchException("Link relation must be 'search'");
                        }
                        else if (mode == OpenSearch.ADAPTIVE) {
                            OpenSearchLink link = getLink().clone();
                            link.setRel("search");
                            element.appendChild( link.asElement( document, format, mode ) );
                        }
                        else {
                            element.appendChild( getLink().asElement( document, format, mode ) );
                        }
                    }
                    else {
                        element.appendChild( getLink().asElement( document, format, mode ) );
                    }
                }
                else {
                    if (mode == OpenSearch.STRICT) {
                        throw new OpenSearchException("Link relation must be 'search'");
                    }
                    element.appendChild( getLink().asElement( document, format, mode ) );
                }
            }
            
            // Query
            Iterator<OpenSearchQuery> queryIterator = getQueries().iterator();
            while (queryIterator.hasNext()) {
                OpenSearchQuery query = queryIterator.next();
                element.appendChild( query.asElement( document, format, mode ) );
            }
            
            // totalResults
            Integer totalResults = getTotalResults();
            if (totalResults == null && requestQuery != null) { totalResults = requestQuery.getTotalResults(); }
            if (totalResults != null) {
                element.appendChild(asElementNS(document, "http://a9.com/-/spec/opensearch/1.1/", "opensearch:totalResults", String.valueOf(totalResults)));
            }
            
            // startIndex
            Integer startIndex = getStartIndex();
            if (startIndex == null && requestQuery != null) { startIndex = requestQuery.getStartIndex(); }
            if (startIndex != null) {
                element.appendChild(asElementNS(document, "http://a9.com/-/spec/opensearch/1.1/", "opensearch:startIndex", String.valueOf(startIndex)));
            }
            
            // itemsPerPage
            Integer itemsPerPage = getItemsPerPage();
            if (itemsPerPage == null && requestQuery != null) { itemsPerPage = requestQuery.getCount(); }
            if (itemsPerPage != null) {
                element.appendChild(asElementNS(document, "http://a9.com/-/spec/opensearch/1.1/", "opensearch:itemsPerPage", String.valueOf(itemsPerPage)));
            }
            
            // Links
            Iterator<OpenSearchLink> linksIterator = getLinks().iterator();
            while (linksIterator.hasNext()) {
                OpenSearchLink link = linksIterator.next();
                element.appendChild( link.asElement( document, format, mode ) );
            }
            
            Iterator<OpenSearchResult> resultsIterator = getResults().iterator();
            while (resultsIterator.hasNext()) {
                OpenSearchResult result = resultsIterator.next();
                element.appendChild( result.asElement( document, format, mode ) );
            }
            
            return element;
        }
        
        
        /**
         * RSS
         */
        
        if (format == OpenSearch.RSS) {
            Element element = document.createElement("rss");
            element.setAttribute("version", "2.0");
            element.setAttribute("xmlns:opensearch","http://a9.com/-/spec/opensearch/1.1/");
            
            Element channel = document.createElement("channel");
            
            // title
            if (getTitle() != null) {
                channel.appendChild( asElement( document, "title", getTitle() ) );
            }
            
            // link
            if (getLink() != null && getLink().getHref() != null) {
                channel.appendChild( asElement( document, "link", getLink().getHref() ) );
            }
            
            // description
            if (getDescription() != null) {
                channel.appendChild( asElement( document, "description", getDescription() ) );
            }
            
            // totalResults
            if (getTotalResults() != null) {
                channel.appendChild(asElementNS(document, "http://a9.com/-/spec/opensearch/1.1/", "opensearch:totalResults", String.valueOf(getTotalResults())));
            }
            
            // startIndex
            if (getStartIndex() != null) {
                channel.appendChild(asElementNS(document, "http://a9.com/-/spec/opensearch/1.1/", "opensearch:startIndex", String.valueOf(getStartIndex())));
            }
            
            // itemsPerPage
            if (getItemsPerPage() != null) {
                channel.appendChild(asElementNS(document, "http://a9.com/-/spec/opensearch/1.1/", "opensearch:itemsPerPage", String.valueOf(getItemsPerPage())));
            }
            
            // Links
            Iterator<OpenSearchLink> linksIterator = getLinks().iterator();
            while (linksIterator.hasNext()) {
                OpenSearchLink link = linksIterator.next();
                channel.appendChild( link.asElement( document, format, mode ) );
            }
            
            // items
            Iterator<OpenSearchResult> resultsIterator = getResults().iterator();
            while (resultsIterator.hasNext()) {
                OpenSearchResult result = resultsIterator.next();
                channel.appendChild( result.asElement( document, format, mode ) );
            }
            
            
            element.appendChild( channel );
            
            return element;
        }
        
        
        
        throw new OpenSearchException("Unknown format");
    }
    
    protected Element asElement (Document document, String name, String value) throws OpenSearchException {
        Element element = document.createElement(name);
        element.appendChild( document.createTextNode(value) );
        return element;
    }
    
    protected Element asElementNS (Document document, String namespaceURI, String qualifiedName, String value) throws OpenSearchException {
        Element element = document.createElementNS(namespaceURI, qualifiedName);
        element.appendChild( document.createTextNode(value) );
        return element;
    }
    
}
