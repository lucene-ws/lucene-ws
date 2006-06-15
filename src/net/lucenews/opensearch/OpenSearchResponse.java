package net.lucenews.opensearch;

import java.util.*;
import org.w3c.dom.*;

public class OpenSearchResponse {
    
    private Integer total_results;
    private Integer start_index;
    private Integer items_per_page;
    private OpenSearchLink link;
    private List<OpenSearchLink> links;
    
    
    public OpenSearchResponse () {
        links = new LinkedList<OpenSearchLink>();
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
    
    
    
    
    public Element asElement (Document document, OpenSearch.Format format) throws OpenSearchException {
        return asElement(document, format, OpenSearch.STRICT);
    }
    
    public Element asElement (Document document, OpenSearch.Format format, OpenSearch.Mode mode) throws OpenSearchException {
        
        
        /**
         * Atom
         */
        
        if (format == OpenSearch.ATOM) {
            Element element = document.createElement("feed");
            
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
            
            // totalResults
            if (getTotalResults() != null) {
                element.appendChild(asElementNS(document, "http://a9.com/-/spec/opensearch/1.1/", "opensearch:totalResults", String.valueOf(getTotalResults())));
            }
            
            // startIndex
            if (getStartIndex() != null) {
                element.appendChild(asElementNS(document, "http://a9.com/-/spec/opensearch/1.1/", "opensearch:startIndex", String.valueOf(getStartIndex())));
            }
            
            // itemsPerPage
            if (getItemsPerPage() != null) {
                element.appendChild(asElementNS(document, "http://a9.com/-/spec/opensearch/1.1/", "opensearch:itemsPerPage", String.valueOf(getItemsPerPage())));
            }
            
            // Links
            Iterator<OpenSearchLink> linksIterator = getLinks().iterator();
            while (linksIterator.hasNext()) {
                OpenSearchLink link = linksIterator.next();
                element.appendChild( link.asElement( document, format, mode ) );
            }
            
            return element;
        }
        
        
        /**
         * RSS
         */
        
        if (format == OpenSearch.RSS) {
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
