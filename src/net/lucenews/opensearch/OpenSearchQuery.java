package net.lucenews.opensearch;

import org.w3c.dom.*;

public class OpenSearchQuery {
    
    private String role;
    private String title;
    private String osd;
    private Integer total_results;
    private String search_terms;
    private Integer count;
    private Integer start_index;
    private Integer start_page;
    private String language;
    private String output_encoding;
    private String input_encoding;
    
    
    public OpenSearchQuery () {
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
            "example",
            "related",
            "request",
            "correction",
            "superset",
            "subset"
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
    
    
    
    public Integer getStartIndex () {
        return start_index;
    }
    
    public void setStartIndex (Integer start_index) {
        this.start_index = start_index;
    }
    
    
    
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
        
        return element;
    }
}