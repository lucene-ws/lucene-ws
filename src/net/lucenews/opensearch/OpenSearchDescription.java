package net.lucenews.opensearch;

import java.util.*;
import javax.xml.parsers.*;
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
    private List<String> languages;
    private List<String> input_encodings;
    private List<String> output_encodings;
    
    public OpenSearchDescription () {
        urls = new LinkedList<OpenSearchUrl>();
        languages = new LinkedList<String>();
        input_encodings  = new LinkedList<String>();
        output_encodings = new LinkedList<String>();
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
    
    public boolean hasValidSyndicationRight () {
        if (getSyndicationRight() == null) {
            return true;
        }
        
        String[] valid_rights = new String[] {
            "open",
            "limited",
            "private",
            "closed"
        };
        
        for (int i = 0; i < valid_rights.length; i++) {
            if (getSyndicationRight().toLowerCase().equals(valid_rights[i])) {
                return true;
            }
        }
        
        return false;
    }
    
    
    
    public Boolean getAdultContent () {
        return adult_content;
    }
    
    public void setAdultContent (Boolean adult_content) {
        this.adult_content = adult_content;
    }
    
    
    
    public List<String> getLanguages () {
        return languages;
    }
    
    public void addLanguage (String language) {
        languages.add( language );
    }
    
    public boolean removeLanguage (String language) {
        return languages.remove( language );
    }
    
    
    
    public List<String> getOutputEncodings () {
        return output_encodings;
    }
    
    public void addOutputEncoding (String output_encoding) {
        output_encodings.add( output_encoding );
    }
    
    public boolean removeOutputEncoding (String output_encoding) {
        return output_encodings.remove( output_encoding );
    }
    
    
    
    public List<String> getInputEncodings () {
        return input_encodings;
    }
    
    public void addInputEncoding (String input_encoding) {
        input_encodings.add( input_encoding );
    }
    
    public boolean removeInputEncoding (String input_encoding) {
        return input_encodings.remove( input_encoding );
    }
    
    
    
    public static OpenSearchDescription asOpenSearchDescription (Document document) {
        OpenSearchDescription description = new OpenSearchDescription();
        return description;
    }
    
    
    
    
    public Document asDocument ()
        throws OpenSearchException, ParserConfigurationException
    {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        document.appendChild( asElement( document ) );
        return document;
    }
    
    public Document asDocument (OpenSearch.Mode mode)
        throws OpenSearchException, ParserConfigurationException
    {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        document.appendChild( asElement( document, mode ) );
        return document;
    }
    
    
    /**
     * Transforms the OpenSearch description into a DOM Element.
     */
    
    public Element asElement (Document document) throws OpenSearchException {
        return asElement(document, OpenSearch.getDefaultMode());
    }
    
    public Element asElement (Document document, OpenSearch.Mode mode) throws OpenSearchException {
        Element element = document.createElement("OpenSearchDescription");
        element.setAttribute("xmlns","http://a9.com/-/spec/opensearch/1.1/");
        
        
        // ShortName
        if (getShortName() == null) {
            if (mode == OpenSearch.STRICT) {
                throw new OpenSearchException("No short name specified");
            }
            if (mode == OpenSearch.ADAPTIVE) {
                element.appendChild( asElement( document, "ShortName", "" ) );
            }
        }
        else if (getShortName().length() > 16) {
            if (mode == OpenSearch.STRICT) {
                throw new OpenSearchException("Short name cannot exceed 16 characters");
            }
            if (mode == OpenSearch.ADAPTIVE) {
                element.appendChild( asElement( document, "ShortName", getShortName().substring(0,16) ) );
            }
            else {
                element.appendChild( asElement( document, "ShortName", getShortName() ) );
            }
        }
        else {
            element.appendChild( asElement( document, "ShortName", getShortName() ) );
        }
        
        
        // Description
        if ((getDescription() == null || getDescription().length() > 1024) && mode == OpenSearch.STRICT) {
            throw new OpenSearchException("No description specified");
        }
        element.appendChild( asElement( document, "Description", getDescription() ) );
        
        
        // Url
        if (urls.size() == 0 && mode == OpenSearch.STRICT) {
            throw new OpenSearchException("Url must appear one or more times");
        }
        Iterator<OpenSearchUrl> urlsIterator = urls.iterator();
        while (urlsIterator.hasNext()) {
            element.appendChild( urlsIterator.next().asElement( document, OpenSearch.ATOM, mode ) );
        }
        
        
        // Contact
        if (getContact() != null) {
            int maximum = 64;
            if (getContact().length() > maximum) {
                if (mode == OpenSearch.STRICT) {
                    throw new OpenSearchException("Contact cannot exceed "+maximum+" characters");
                }
                if (mode == OpenSearch.ADAPTIVE) {
                    element.appendChild( asElement( document, "Contact", getContact().substring(0,maximum) ) );
                }
                else {
                    element.appendChild( asElement( document, "Contact", getContact() ) );
                }
            }
            else {
                element.appendChild( asElement( document, "Contact", getContact() ) );
            }
        }
        
        
        // LongName
        if (getLongName() != null) {
            int maximum = 48;
            if (getLongName().length() > maximum) {
                if (mode == OpenSearch.STRICT) {
                    throw new OpenSearchException("Long name cannot exceed "+maximum+" characters");
                }
                if (mode == OpenSearch.ADAPTIVE) {
                    element.appendChild( asElement( document, "LongName", getLongName().substring(0,maximum) ) );
                }
                else {
                    element.appendChild( asElement( document, "LongName", getLongName() ) );
                }
            }
            else {
                element.appendChild( asElement( document, "LongName", getLongName() ) );
            }
        }
        
        
        // Developer
        if (getDeveloper() != null) {
            int maximum = 64;
            if (getDeveloper().length() > maximum) {
                if (mode == OpenSearch.STRICT) {
                    throw new OpenSearchException("Developer cannot exceed "+maximum+" characters");
                }
                if (mode == OpenSearch.ADAPTIVE) {
                    element.appendChild( asElement( document, "Developer", getDeveloper().substring(0,maximum) ) );
                }
                else {
                    element.appendChild( asElement( document, "Developer", getDeveloper() ) );
                }
            }
            else {
                element.appendChild( asElement( document, "Developer", getDeveloper() ) );
            }
        }
        
        
        // Attribution
        if (getAttribution() != null) {
            int maximum = 256;
            if (getAttribution().length() > maximum) {
                if (mode == OpenSearch.STRICT) {
                    throw new OpenSearchException("Attribution cannot exceed "+maximum+" characters");
                }
                if (mode == OpenSearch.ADAPTIVE) {
                    element.appendChild( asElement( document, "Attribution", getAttribution().substring(0,maximum) ) );
                }
                else {
                    element.appendChild( asElement( document, "Attribution", getAttribution() ) );
                }
            }
            else {
                element.appendChild( asElement( document, "Attribution", getAttribution() ) );
            }
        }
        
        
        // SyndicationRight
        if (getSyndicationRight() != null) {
            if (mode == OpenSearch.STRICT && !hasValidSyndicationRight()) {
                throw new OpenSearchException("Invalid Syndication Right: " + getSyndicationRight());
            }
            element.appendChild( asElement( document, "SyndicationRight", getSyndicationRight() ) );
        }
        
        
        // AdultContent
        if (getAdultContent() != null) {
            element.appendChild( asElement( document, "AdultContent", String.valueOf( getAdultContent() ) ) );
        }
        
        
        // Language
        Iterator<String> languageIterator = getLanguages().iterator();
        while (languageIterator.hasNext()) {
            element.appendChild( asElement( document, "Language", languageIterator.next() ) );
        }
        
        
        // OutputEncoding
        Iterator<String> outputEncodingIterator = getOutputEncodings().iterator();
        while (outputEncodingIterator.hasNext()) {
            element.appendChild( asElement( document, "OutputEncoding", outputEncodingIterator.next() ) );
        }
        
        
        // InputEncoding
        Iterator<String> inputEncodingIterator = getInputEncodings().iterator();
        while (inputEncodingIterator.hasNext()) {
            element.appendChild( asElement( document, "InputEncoding", inputEncodingIterator.next() ) );
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
