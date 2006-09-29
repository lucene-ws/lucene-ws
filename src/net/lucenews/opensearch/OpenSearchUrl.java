package net.lucenews.opensearch;

import java.util.*;
import org.w3c.dom.*;

/**
 * OpenSearch Query Syntax is a simple way of specifying HTTP queries 
 * for the purpose of requesting search results. Search engines can 
 * publish a URL in Query Syntax, which can then be used by a search 
 * client to make requests. This is usually done in an OpenSearch 
 * Description file, another of the components of OpenSearch.
 *
 * Query Syntax essentially consists of a template, which contains one 
 * or more search parameters. With a simple substitution grammar, the 
 * parameters are replaced with actual values to form a request. 
 * Several search parameters are defined here, others may be used in a 
 * fashion similar to XML namespaces.
 * 
 * When found in an OpenSearch Description, the Url element may appear 
 * more than once, listed in order of priority according to the search 
 * provider. Clients will also take into account the response format 
 * when selecting which to use. 
 *
 * Source: http://opensearch.a9.com/spec/1.1/querysyntax/
 */

public class OpenSearchUrl {
    
    private String template;
    private String type;
    private String method;
    private String encodingType;
    private Integer indexOffset;
    private Integer pageOffset;
    private List<OpenSearchParameter> parameters;
    private Map<String,String> namespaces;
    
    
    public OpenSearchUrl () {
        parameters = new LinkedList<OpenSearchParameter>();
        namespaces = new LinkedHashMap<String,String>();
    }
    
    
    
    /**
     * template - a value containing the URL that will undergo 
     * parameter substitution.
     * 
     * Note: New in version 1.1.
     * Requirements: Must appear one time.
     */
    
    public String getTemplate () {
        return template;
    }
    
    public void setTemplate (String template) {
        this.template = template;
    }
    
    
    
    /**
     * type - the MIME type of the search results.
     *
     * Note: New in version 1.1.
     * Restrictions: MIME types must conform to the values defined in 
     *               the IANA MIME Media Type Registry.
     * Requirements: Must appear one time.
     */
    
    public String getType () {
        return type;
    }
    
    public void setType (String type) {
        this.type = type;
    }
    
    
    
    /**
     * indexOffset - Contains the index number of the first search 
     *               result.
     * 
     * Restrictions: The value must be an integer. 
     * Default: "1" 
     * Requirements: This attribute is optional. 
     */
    
    public Integer getIndexOffset () {
        return indexOffset;
    }
    
    public void setIndexOffset (Integer indexOffset) {
        this.indexOffset = indexOffset;
    }
    
    
    
    /**
     * pageOffset - Contains the page number of the first set of 
     *              search results.
     * 
     * Restrictions: The value must be an integer. 
     * Default: "1". 
     * Requirements: This attribute is optional. 
     */
    
    public Integer getPageOffset () {
        return pageOffset;
    }
    
    public void setPageOffset (Integer pageOffset) {
        this.pageOffset = pageOffset;
    }
    
    
    
    /**
     * method - a value indicating the HTTP request method.
     * 
     * Note: New in version 1.1.
     * Restrictions: A case insensitive value of either "get" or "post".
     * Default: "get"
     * Requirements: May appear one time.
     */
    
    public String getMethod () {
        return method;
    }
    
    public void setMethod (String method) {
        this.method = method;
    }
    
    
    
    public String getEncodingType () {
        return encodingType;
    }
    
    public void setEncodingType (String encodingType) {
        this.encodingType = encodingType;
    }
    
    
    
    /**
     * Parameter - An empty node that is used to describe HTTP POST 
     * parameters to be passed along with a query of method="post".
     * 
     * Parent: Url
     * Attributes:
     *     o name - the name of the HTTP POST parameter.
     *           + Requirements: Must appear one time.
     *     o value - the value of the HTTP POST parameter. Will 
     *               undergo parameter substitution before being sent.
     *           + Requirements: Must appear one time.
     * Note: New in version 1.1.
     * Note: The Param element is ignored if the method of the parent 
     *       <Url> is anything other than "post".
     * Note: If the parameter substitution results in an empty string, 
     *       the parameter may still be sent with the query.
     * Requirements: May appear zero, one, or more times.
     */
    
    public void addParameter (String name, String value) {
        addParameter( new OpenSearchParameter( name, value ) );
    }
    
    public void addParameter (OpenSearchParameter parameter) {
        parameters.add( parameter );
    }
    
    public boolean removeParameter (String name) {
        return removeParameter( new OpenSearchParameter( name, null ) );
    }
    
    public boolean removeParameter (OpenSearchParameter parameter) {
        return parameters.remove( parameter );
    }
    
    public List<OpenSearchParameter> getParameters () {
        return parameters;
    }
    
    
    
    public void setNamespace (String namespace, String uri) {
        namespaces.put( namespace, uri );
    }
    
    
    
    public static OpenSearchUrl asOpenSearchUrl (Element element) {
        OpenSearchUrl url = new OpenSearchUrl();
        
        // template
        String template = element.getAttribute("template");
        url.setTemplate( template );
        
        // type
        String type = element.getAttribute("type");
        url.setType( type );
        
        // method
        String method = element.getAttribute("method");
        url.setMethod( method );
        
        // enctype
        String enctype = element.getAttribute("enctype");
        url.setEncodingType( enctype );
        
        // Parameter
        NodeList parameters = element.getElementsByTagName("Parameter");
        for ( int i = 0; i < parameters.getLength(); i++ ) {
            Element parameter = (Element) parameters.item( i );
            url.addParameter( OpenSearchParameter.asOpenSearchParameter( parameter ) );
        }
        
        return url;
    }
    
    
    public Element asElement (Document document, OpenSearch.Format format) throws OpenSearchException {
        return asElement(document, format, OpenSearch.STRICT);
    }
    
    public Element asElement (Document document, OpenSearch.Format format, OpenSearch.Mode mode) throws OpenSearchException {
        Element element = document.createElement("Url");
        
        // type
        if ( getType() != null ) {
            element.setAttribute( "type", getType() );
        }
        
        // method
        if ( getMethod() != null ) {
            element.setAttributeNS( "http://a9.com/-/spec/opensearch/extensions/parameters/1.0/", "parameters:method", getMethod() );
        }
        
        // enctype
        if ( getEncodingType() != null ) {
            element.setAttributeNS( "http://a9.com/-/spec/opensearch/extensions/parameters/1.0/", "parameters:enctype", getEncodingType() );
        }
        
        // indexOffset
        if ( getIndexOffset() != null ) {
            element.setAttribute( "indexOffset", getIndexOffset().toString() );
        }
        
        // pageOffset
        if ( getPageOffset() != null ) {
            element.setAttribute( "pageOffset", getPageOffset().toString() );
        }
        
        // template
        if ( getTemplate() != null ) {
            element.setAttribute( "template", getTemplate() );
        }
        
        // namespaces
        Iterator<Map.Entry<String,String>> namespaceIterator = namespaces.entrySet().iterator();
        while (namespaceIterator.hasNext()) {
            Map.Entry<String,String> namespace = namespaceIterator.next();
            element.setAttribute( "xmlns:"+namespace.getKey(), namespace.getValue() );
        }
        
        // Parameter
        Iterator<OpenSearchParameter> parameters = getParameters().iterator();
        while ( parameters.hasNext() ) {
            OpenSearchParameter parameter = parameters.next();
            element.appendChild( parameter.asElement( document, format, mode ) );
        }
        
        return element;
    }
    
}
