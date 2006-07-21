package net.lucenews.opensearch;

import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class OpenSearchResponse {
    
    private String   title;
    private String   id;
    private Calendar updated;
    private String   description;
    private Integer  total_results;
    private Integer  start_index;
    private Integer  items_per_page;
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
    
    
    
    public Integer getTotalResults () {
        return total_results;
    }
    
    public void setTotalResults (Integer total_results) {
        this.total_results = total_results;
    }
    
    
    
    public Integer getStartIndex () {
        return start_index;
    }
    
    public void setStartIndex (Integer start_index) {
        this.start_index = start_index;
    }
    
    
    
    public Integer getItemsPerPage () {
        return items_per_page;
    }
    
    public void setItemsPerPage (Integer items_per_page) {
        this.items_per_page = items_per_page;
    }
    
    
    
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
