package net.lucenews.opensearch;

import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * OpenSearch Description files are used by search engines to describe 
 * themselves and how they can be queried (using OpenSearch Query 
 * Syntax). They are published as simple XML files over the web, and 
 * can be used by search clients to integrate third-party searches. 
 * Search results may be in the form of OpenSearch Response, another of 
 * the components of OpenSearch.
 *
 *  Source: http://opensearch.a9.com/spec/1.1/description/
 *
 */

public class OpenSearchDescription {
    
    private String                shortName;
    private String                description;
    private List<OpenSearchUrl>   urls;
    private String                contact;
    private String                tags;
    private String                longName;
    private List<OpenSearchImage> images;
    private List<OpenSearchQuery> queries;
    private String                developer;
    private String                attribution;
    private String                syndicationRight;
    private Boolean               adultContent;
    private List<String>          languages;
    private List<String>          inputEncodings;
    private List<String>          outputEncodings;
    
    
    
    static public final class SyndicationRight {
        static public final String OPEN    = "open";
        static public final String LIMITED = "limited";
        static public final String PRIVATE = "private";
        static public final String CLOSED  = "closed";
    }
    
    
    
    public OpenSearchDescription () {
        urls = new LinkedList<OpenSearchUrl>();
        images  = new LinkedList<OpenSearchImage>();
        queries = new LinkedList<OpenSearchQuery>();
        languages = new LinkedList<String>();
        inputEncodings  = new LinkedList<String>();
        outputEncodings = new LinkedList<String>();
    }
    
    
    
    /**
     * ShortName - A brief name that will appear in buttons, UI 
     * controls, etc., that reference this search content provider.
     * 
     * Parent: OpenSearchDescription
     * Restrictions: Must contain 16 or fewer characters (no HTML).
     * Requirements: Must appear exactly once.
     */
    
    public String getShortName () {
        return shortName;
    }
    
    public void setShortName (String shortName) {
        this.shortName = shortName;
    }
    
    
    
    /**
     * Description - A human readable text description of the search 
     * content provider.
     * 
     * Parent: OpenSearchDescription
     * Restrictions: Must contain 1024 or fewer characters of text (no 
     *               HTML).
     * Requirements: Must appear exactly once.
     */
    
    public String getDescription () {
        return description;
    }
    
    public void setDescription (String description) {
        this.description = description;
    }
    
    
    
    /**
     * Url - Please see the OpenSearch Query Syntax specification for 
     * documentation on the Url element, its attributes, and its 
     * contents.
     * 
     * Parent: OpenSearchDescription
     * Note: Updated in version 1.1.
     * Requirements: Must appear one or more times.
     */
    
    public List<OpenSearchUrl> getUrls () {
        return urls;
    }
    
    public void addUrl (OpenSearchUrl url) {
        urls.add( url );
    }
    
    public boolean removeUrl (OpenSearchUrl url) {
        return urls.remove( url );
    }
    
    
    
    /**
     * Contact - An email address at which the developer can be reached.
     * 
     * Parent: OpenSearchDescription
     * Note: Updated in version 1.1.
     * Restrictions: Must contain 64 or fewer characters of text (no 
     *               HTML).
     * Requirements: May appear zero or one time.
     */
        
    public String getContact () {
        return contact;
    }
    
    public void setContact (String contact) {
        this.contact = contact;
    }
    
    
    
    /**
     * Tags - A space-delimited set of words that are used as keywords 
     * to identify and categorize this search content.
     *
     * Parent: OpenSearchDescription
     * Note: Updated in version 1.1.
     * Restrictions: Total length of all tags, including spaces, must 
     *               contain 256 or fewer characters of text (no HTML).
     * Requirements: May appear zero or one time.
     */
    
    public String getTags () {
        return tags;
    }
    
    public void setTags (String tags) {
        this.tags = tags;
    }
    
    
    
    /**
     * LongName - The name by which this search content provider is 
     * referred to in hypertext links, etc.
     * 
     * Parent: OpenSearchDescription
     * Restrictions: Must contain 48 or fewer characters of text (no 
     *               HTML).
     * Default:      The same value as the ShortName element.
     * Requirements: May appear zero or one time.
     */
    
    public String getLongName () {
        return longName;
    }
    
    public void setLongName (String longName) {
        this.longName = longName;
    }
    
    
    
    /**
     * Image - A URL that identifies the location of an image that can 
     * be used in association with this search content.
     *
     * Parent: OpenSearchDescription
     * Attributes:
     *     o height - height, in pixels, of this image. Optional.
     *     o width - width, in pixels, of this image. Optional.
     *     o type - the MIME-type of this image. Optional.
     * Note: Valid types correspond to those that can be displayed 
     *       using the HTML <img src=""> element.
     * Note: If more than one image is available, clients will choose 
     *       the most suitable for the given application, giving 
     *       preference to those listed first.
     * Note: 64x64 JPEG/PNGs and 16x16 ICO files are recommended.
     * Note: Updated in version 1.1.
     * Requirements: May appear zero, one, or more times.
     */
    
    public List<OpenSearchImage> getImages () {
        return images;
    }
    
    public void addImage (OpenSearchImage image) {
        images.add( image );
    }
    
    public boolean removeImage (OpenSearchImage image) {
        return images.remove( image );
    }
    
    
    
    /**
     * Query - used to indicate an example search; please see the 
     * OpenSearch Query specification for more information.
     * 
     * Parent: OpenSearchDescription
     * Restrictions: The role attribute must equal example.
     * Requirements: May appear zero or more times.
     * Example: <Query role="example" searchTerms="cat" />
     */
    
    public List<OpenSearchQuery> getQueries () {
        return queries;
    }
    
    public void addQuery (OpenSearchQuery query) {
        queries.add( query );
    }
    
    public boolean removeQuery (OpenSearchQuery query) {
        return queries.remove( query );
    }
    
    
    
    /**
     * The developer or maintainer of the OpenSearch feed.
     * 
     * Parent: OpenSearchDescription
     * Restrictions: Must contain 64 or fewer characters of text 
     *               (no HTML).
     * Note: The Developer is not always the same as the owner, author, 
     *       or copyright holder of the source of the content itself. 
     *       The developer is simply the person or entity that created 
     *       the feed, though they must have permission of the 
     *       appropriate copyright holders.
     * Requirements: May appear zero or one time.
     */
    
    public String getDeveloper () {
        return developer;
    }
    
    public void setDeveloper (String developer) {
        this.developer = developer;
    }
    
    
    
    /**
     * A list of all content sources or platforms that should be credited.
     *
     * Parent: OpenSearchDescription
     * Restrictions: Must contain 256 or fewer characters of text 
     *               (no HTML).
     * Note: Please include any copyright symbols or descriptive text 
     *       as desired.
     * Requirements: May appear zero or one time.
     */
    
    public String getAttribution () {
        return attribution;
    }
    
    public void setAttribution (String attribution) {
        this.attribution = attribution;
    }
    
    
    
    /**
     * The degree to which the search results provided by this search 
     * engine can be distributed.
     *
     * Parent: OpenSearchDescription
     * Values: This element must contain one of the following values 
     *         (case insensitive):
     *     o open - search results can be published or re-published 
     *              without restriction. This is the default.
     *     o limited - search results can be published on the client 
     *                 site, but not further republished.
     *     o private - search feed may be queried, but the results may 
     *                 not be displayed at the client site.
     *     o closed - search feed should not be queried, and will 
     *                disable the column for searches.
     * Note: The SyndicationRight must be either open or limited for 
     *       the content to appear on a public search aggregation site.
     * Default: "open"
     * Requirements: May appear zero or one time.
     */
    
    public String getSyndicationRight () {
        return syndicationRight;
    }
    
    public void setSyndicationRight (String syndicationRight) {
        this.syndicationRight = syndicationRight;
    }
    
    public boolean hasValidSyndicationRight () {
        if (getSyndicationRight() == null) {
            return true;
        }
        
        String[] valid_rights = new String[] {
            SyndicationRight.OPEN,
            SyndicationRight.LIMITED,
            SyndicationRight.PRIVATE,
            SyndicationRight.CLOSED
        };
        
        for (int i = 0; i < valid_rights.length; i++) {
            if (getSyndicationRight().toLowerCase().equals(valid_rights[i])) {
                return true;
            }
        }
        
        return false;
    }
    
    
    
    /**
     * A boolean flag that must be set if the content provided is not 
     * suitable for minors
     *
     * Parent: OpenSearchDescription
     * Values: "false", "FALSE", "0", "no", and "NO" will all be 
     *         considered FALSE, all other strings will be 
     *         considered TRUE
     * Default: "FALSE"
     * Requirements: May appear zero or one time.
     */
    
    public Boolean getAdultContent () {
        return adultContent;
    }
    
    public void setAdultContent (Boolean adultContent) {
        this.adultContent = adultContent;
    }
    
    public void setAdultContent (String adultContent) {
        if ( adultContent == null ) {
            this.adultContent = null;
        }
        else {
            this.adultContent = asBoolean( adultContent );
        }
    }
    
    
    
    /**
     * Indicates that the server is capable of returning results in 
     * the specified language.
     *
     * Parent: OpenSearchDescription
     * Restrictions: Values conform those of the XML 1.0 Language 
     *               Identification, as specified by RFC 3066.
     * Note: Please refer to the language parameter in the 
     *       Query Syntax specification for more information 
     *       on how clients can request a particular language 
     *       for the search results.
     * Note: New in version 1.1.
     * Default: "*". Indicates that all languages may appear in the 
     *          results.
     * Requirements: May appear zero, one, or more times.
     */
    
    public List<String> getLanguages () {
        return languages;
    }
    
    public void addLanguage (String language) {
        languages.add( language );
    }
    
    public boolean removeLanguage (String language) {
        return languages.remove( language );
    }
    
    
    
    /**
     * Indicates that the server is capable of returning results with 
     * the specified character encoding.
     *
     * Parent: OpenSearchDescription
     * Restrictions: Values conform those of the XML 1.0 Character 
     *               Encodings, as specified by the IANA Character Set 
     *               Assignments.
     * Note: Please refer to the outputEncoding parameter in the Query 
     *       Syntax specification for more information on how clients 
     *       can request a particular character encoding for the 
     *       response.
     * Note: New in version 1.1.
     * Default: "UTF-8".
     * Requirements: May appear zero, one, or more times.
     */
    
    public List<String> getOutputEncodings () {
        return outputEncodings;
    }
    
    public void addOutputEncoding (String output_encoding) {
        outputEncodings.add( output_encoding );
    }
    
    public boolean removeOutputEncoding (String output_encoding) {
        return outputEncodings.remove( output_encoding );
    }
    
    
    
    /**
     * Indicates that the server is capable of reading queries with the 
     * specified character encoding.
     *
     * Parent: OpenSearchDescription
     * Restrictions: Values conform those of the XML 1.0 Character 
     *               Encodings, as specified by the IANA Character Set 
     *               Assignments.
     * Note: Please refer to the inputEncoding parameter in the Query 
     *       Syntax specification for more information on how clients 
     *       can indicate a particular character encoding in the 
     *       request.
     * Note: New in version 1.1.
     * Default: "UTF-8".
     * Requirements: May appear zero, one, or more times.
     */
    
    public List<String> getInputEncodings () {
        return inputEncodings;
    }
    
    public void addInputEncoding (String input_encoding) {
        inputEncodings.add( input_encoding );
    }
    
    public boolean removeInputEncoding (String input_encoding) {
        return inputEncodings.remove( input_encoding );
    }
    
    
    
    /**
     * Parses a DOM Document into an OpenSearch Description.
     */
    
    public static OpenSearchDescription asOpenSearchDescription (Document document) {
        OpenSearchDescription description = new OpenSearchDescription();
        
        // ShortName
        description.setShortName( asValue( document, "ShortName" ) );
        
        // Description
        description.setDescription( asValue( document, "Description" ) );
        
        // Url
        NodeList urls = document.getElementsByTagName("Url");
        for ( int i = 0; i < urls.getLength(); i++ ) {
            description.addUrl( OpenSearchUrl.asOpenSearchUrl( (Element) urls.item( i ) ) );
        }
        
        // Contact
        description.setContact( asValue( document, "Contact" ) );
        
        // Tags
        description.setTags( asValue( document, "Tags" ) );
        
        // LongName
        description.setLongName( asValue( document, "LongName" ) );
        
        // Image
        NodeList images = document.getElementsByTagName("Image");
        for ( int i = 0; i < images.getLength(); i++ ) {
            description.addImage( OpenSearchImage.asOpenSearchImage( (Element) images.item( i ) ) );
        }
        
        // Query
        NodeList queries = document.getElementsByTagName("Query");
        for ( int i = 0; i < queries.getLength(); i++ ) {
            description.addQuery( OpenSearchQuery.asOpenSearchQuery( (Element) queries.item( i ) ) );
        }
        
        // Developer
        description.setDeveloper( asValue( document, "Developer" ) );
        
        // Attribution
        description.setAttribution( asValue( document, "Attribution" ) );
        
        // SyndicationRight
        description.setSyndicationRight( asValue( document, "SyndicationRight" ) );
        
        // AdultContent
        description.setAdultContent( asValue( document, "AdultContent" ) );
        
        // Language
        String[] languages = asValues( document, "Language" );
        for ( int i = 0; i < languages.length; i++ ) {
            description.addLanguage( languages[ i ] );
        }
        
        // OutputEncoding
        String[] outputEncodings = asValues( document, "OutputEncoding" );
        for ( int i = 0; i < outputEncodings.length; i++ ) {
            description.addOutputEncoding( outputEncodings[ i ] );
        }
        
        // InputEncoding
        String[] inputEncodings = asValues( document, "InputEncoding" );
        for ( int i = 0; i < inputEncodings.length; i++ ) {
            description.addInputEncoding( inputEncodings[ i ] );
        }
        
        return description;
    }
    
    
    
    /**
     * Transforms the OpenSearch description into a DOM Document.
     * 
     * @throws OpenSearchException
     * @throws ParserConfigurationException
     */
    
    public Document asDocument ()
        throws OpenSearchException, ParserConfigurationException
    {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        document.appendChild( asElement( document ) );
        return document;
    }
    
    
    
    /**
     * Transforms the OpenSearch description into a DOM Document.
     * 
     * @param  mode
     * @throws OpenSearchException
     * @throws ParserConfigurationException
     */
    
    public Document asDocument (OpenSearch.Mode mode)
        throws OpenSearchException, ParserConfigurationException
    {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        document.appendChild( asElement( document, mode ) );
        return document;
    }
    
    
    
    /**
     * Transforms the OpenSearch Description into a DOM Element.
     * 
     * @param document the parent DOM Document
     * @throws OpenSearchException
     */
    
    public Element asElement (Document document) throws OpenSearchException {
        return asElement(document, OpenSearch.getDefaultMode());
    }
    
    
    
    /**
     * Transforms the OpenSearch Description into a DOM Element.
     * 
     * @param document the parent DOM Document
     * @param mode     the mode with which we are transforming
     * @throws OpenSearchException
     */
    
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
        
        
        // Tags
        if ( getTags() != null ) {
            int maximum = 256;
            if (getTags().length() > maximum) {
                if (mode == OpenSearch.STRICT) {
                    throw new OpenSearchException("Tags cannot exceed "+maximum+" characters");
                }
                if (mode == OpenSearch.ADAPTIVE) {
                    element.appendChild( asElement( document, "Tags", getTags().substring(0,maximum) ) );
                }
                else {
                    element.appendChild( asElement( document, "Tags", getTags() ) );
                }
            }
            else {
                element.appendChild( asElement( document, "Tags", getTags() ) );
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
        
        
        // Image
        if (getImages() != null) {
            Iterator<OpenSearchImage> images = getImages().iterator();
            while ( images.hasNext() ) {
                OpenSearchImage image = images.next();
                element.appendChild( image.asElement( document, mode ) );
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
    
    
    
    protected static String asValue (Element element) {
        return element.getChildNodes().item( 0 ).getNodeValue();
    }
    
    protected static String asValue (Document parent, String name) {
        return asValue( parent.getDocumentElement(), name );
    }
    
    protected static String asValue (Element parent, String name) {
        String[] values = asValues( parent, name );
        if ( values.length > 0 ) {
            return values[ 0 ];
        }
        return null;
    }
    
    protected static String[] asValues (Document parent, String name) {
        return asValues( parent.getDocumentElement(), name );
    }
    
    protected static String[] asValues (Element parent, String name) {
        List<String> values = new LinkedList<String>();
        
        NodeList nodes = parent.getElementsByTagName( name );
        for ( int i = 0; i < nodes.getLength(); i++ ) {
            Element element = (Element) nodes.item( i );
            values.add( asValue( element ) );
        }
        
        return values.toArray( new String[]{} );
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
