package net.lucenews.opensearch;

import java.util.*;
import org.w3c.dom.*;

public class OpenSearchDescription {
    
    private String short_name;
    private String long_name;
    private String description;
    private String developer;
    private String attribution;
    private String syndication_right;
    private Boolean adult_content;
    private String contact;
    private List<OpenSearchUrl> urls;
    
    public OpenSearchDescription () {
        urls = new LinkedList<OpenSearchUrl>();
    }
    
    
    
    public String getShortName () {
        return short_name;
    }
    
    public void setShortName (String short_name) {
        this.short_name = short_name;
    }
    
    
    
    public String getDescription () {
        return description;
    }
    
    public void setDescription (String description) {
        this.description = description;
    }
    
    
    
    public List<OpenSearchUrl> getUrls () {
        return urls;
    }
    
    public void addUrl (OpenSearchUrl url) {
        urls.add( url );
    }
    
    
    
    public String getContact () {
        return contact;
    }
    
    public void setContact (String contact) {
        this.contact = contact;
    }
    
    
    
    public String getLongName () {
        return long_name;
    }
    
    public void setLongName (String long_name) {
        this.long_name = long_name;
    }
    
    
    
    public String getDeveloper () {
        return developer;
    }
    
    public void setDeveloper (String developer) {
        this.developer = developer;
    }
    
    
    
    public String getAttribution () {
        return attribution;
    }
    
    public void setAttribution (String attribution) {
        this.attribution = attribution;
    }
    
    
    
    public String getSyndicationRight () {
        return syndication_right;
    }
    
    public void setSyndicationRight (String syndication_right) {
        this.syndication_right = syndication_right;
    }
    
    
    
    public Boolean getAdultContent () {
        return adult_content;
    }
    
    public void setAdultContent (Boolean adult_content) {
        this.adult_content = adult_content;
    }
    
    
    
    public static OpenSearchDescription asOpenSearchDescription (Document document) {
        OpenSearchDescription description = new OpenSearchDescription();
        return description;
    }
    
    
    
    /**
     * Transforms the OpenSearch description into a DOM Element.
     */
    
    public Element asElement (Document document, OpenSearch.Format format) throws OpenSearchException {
        return asElement(document, format, OpenSearch.STRICT);
    }
    
    public Element asElement (Document document, OpenSearch.Format format, OpenSearch.Mode mode) throws OpenSearchException {
        Element element = document.createElement("OpenSearchDescription");
        
        
        // ShortName
        if (getShortName() == null && mode == OpenSearch.STRICT) {
            throw new OpenSearchException("No short name specified");
        }
        element.appendChild( asElement( document, "ShortName", getShortName() ) );
        
        
        // Description
        if (getDescription() == null && mode == OpenSearch.STRICT) {
            throw new OpenSearchException("No description specified");
        }
        element.appendChild( asElement( document, "Description", getDescription() ) );
        
        
        // Url
        if (urls.size() == 0 && mode == OpenSearch.STRICT) {
            throw new OpenSearchException("Url must appear one or more times");
        }
        Iterator<OpenSearchUrl> urlsIterator = urls.iterator();
        while (urlsIterator.hasNext()) {
            element.appendChild( urlsIterator.next().asElement( document, format, mode ) );
        }
        
        
        // Contact
        if (getContact() != null) {
            element.appendChild( asElement( document, "Contact", getContact() ) );
        }
        
        
        // LongName
        if (getLongName() != null) {
            element.appendChild( asElement( document, "LongName", getLongName() ) );
        }
        
        
        // Developer
        if (getDeveloper() != null) {
            element.appendChild( asElement( document, "Developer", getDeveloper() ) );
        }
        
        
        // Attribution
        if (getAttribution() != null) {
            element.appendChild( asElement( document, "Attribution", getAttribution() ) );
        }
        
        
        // SyndicationRight
        if (getSyndicationRight() != null) {
            element.appendChild( asElement( document, "SyndicationRight", getSyndicationRight() ) );
        }
        
        
        // AdultContent
        if (getAdultContent() != null) {
            element.appendChild( asElement( document, "AdultContent", String.valueOf( getAdultContent() ) ) );
        }
        
        
        
        return element;
    }
    
    
    
    protected Element asElement (Document document, String name, String value) {
        if (value == null) {
            return null;
        }
        
        Element element = document.createElement(name);
        element.appendChild(document.createTextNode(value));
        return element;
    }
    
    
    
    /**
     * Parses boolean values as per A9 OpenSearch specification:
     * 
     *     "false", "FALSE", "0", "no", and "NO" will all be considered FALSE,
     *     all other strings will be considered TRUE
     */
    
    public static boolean asBoolean (String value) {
        String[] negatives = new String[]{ "false", "FALSE", "0", "no", "NO" };
        for (int i = 0; i < negatives.length; i++) {
            if (value.equals(negatives[i])) {
                return false;
            }
        }
        return true;
    }
    
}
