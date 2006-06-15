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
    
    public void setShortName (String short_name) throws Exception {
        if (short_name != null && short_name.length() > 16) {
            throw new Exception("Short name cannot exceed 16 characters");
        }
        this.short_name = short_name;
    }
    
    
    
    public String getDescription () {
        return description;
    }
    
    public void setDescription (String description) throws Exception {
        if (description != null && description.length() > 1024) {
            throw new Exception("Description cannot exceed 1024 characters");
        }
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
    
    public void setContact (String contact) throws Exception {
        if (contact != null && contact.length() > 64) {
            throw new Exception("Contact cannot exceed 64 characters");
        }
        this.contact = contact;
    }
    
    
    
    public String getLongName () {
        return long_name;
    }
    
    public void setLongName (String long_name) throws Exception {
        if (long_name != null && long_name.length() > 48) {
            throw new Exception("Long name cannot exceed 48 characters");
        }
        this.long_name = long_name;
    }
    
    
    
    public String getDeveloper () {
        return developer;
    }
    
    public void setDeveloper (String developer) throws Exception {
        if (developer != null && developer.length() > 64) {
            throw new Exception("Developer cannot exceed 64 characters");
        }
        this.developer = developer;
    }
    
    
    
    public String getAttribution () {
        return attribution;
    }
    
    public void setAttribution (String attribution) throws Exception {
        if (attribution != null && attribution.length() > 256) {
            throw new Exception("Attribution cannot exceed 256 characters");
        }
        this.attribution = attribution;
    }
    
    
    
    public String getSyndicationRight () {
        return syndication_right;
    }
    
    public void setSyndicationRight (String syndication_right) throws Exception {
        if (syndication_right != null && syndication_right.length() > 256) {
            throw new Exception("Syndication Right cannot exceed 256 characters");
        }
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
    
    public Element asElement (Document document) throws Exception {
        Element element = document.createElement("OpenSearchDescription");
        
        
        // ShortName
        if (getShortName() == null) {
            throw new Exception("No short name specified");
        }
        element.appendChild( asElement( document, "ShortName", getShortName() ) );
        
        
        // Description
        if (getDescription() == null) {
            throw new Exception("No description specified");
        }
        element.appendChild( asElement( document, "Description", getDescription() ) );
        
        
        // Url
        if (urls.size() == 0) {
            throw new Exception("Url must appear one or more times");
        }
        Iterator<OpenSearchUrl> urlsIterator = urls.iterator();
        while (urlsIterator.hasNext()) {
            element.appendChild( urlsIterator.next().asElement( document ) );
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
