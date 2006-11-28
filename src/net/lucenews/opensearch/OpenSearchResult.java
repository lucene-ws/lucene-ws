package net.lucenews.opensearch;

import java.util.*;
import org.w3c.dom.*;

public class OpenSearchResult {
    
    
    private String   title;
    private String   id;
    private Calendar updated;
    private Float    score;
    private OpenSearchLink link;
    private OpenSearchText content;
    private List<OpenSearchPerson> people;
    private OpenSearchText rights;
    
    
    public OpenSearchResult () {
        people = new LinkedList<OpenSearchPerson>();
    }
    
    
    
    
    public String getTitle () {
        return title;
    }
    
    public void setTitle (String title) {
        this.title = title;
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
    
    
    
    public Float getScore () {
        return score;
    }
    
    public void setScore (Float score) {
        this.score = score;
    }
    
    
    @Deprecated
    public Float getRelevance () {
        return getScore();
    }
    
    @Deprecated
    public void setRelevance (Float relevance) {
        setScore( relevance );
    }
    
    
    
    public List<OpenSearchPerson> getPeople () {
        return people;
    }
    
    public void addPerson (OpenSearchPerson person) {
        people.add( person );
    }
    
    public boolean removePerson (OpenSearchPerson person) {
        return people.remove( person );
    }
    
    
    
    
    
    public OpenSearchLink getLink () {
        return link;
    }
    
    public void setLink (OpenSearchLink link) {
        this.link = link;
    }
    
    
    
    
    public OpenSearchText getContent () {
        return content;
    }
    
    public void setContent (OpenSearchText content) {
        this.content = content;
    }
    
    public void setContent (String content) {
        setContent( new OpenSearchText( content ) );
    }
    
    public void setContent (Node content) {
        setContent( new OpenSearchText( content ) );
    }
    
    public void setContent (NodeList content) {
        setContent( new OpenSearchText( content ) );
    }
    
    
    
    public OpenSearchText getRights () {
        return rights;
    }
    
    public void setRights (OpenSearchText rights) {
        this.rights = rights;
    }
    
    
    
    public Element asElement (Document document, OpenSearch.Format format) throws OpenSearchException {
        return asElement( document, format, OpenSearch.STRICT );
    }
    
    public Element asElement (Document document, OpenSearch.Format format, OpenSearch.Mode mode) throws OpenSearchException {
        
        /**
         * Atom
         */
        
        if (format == OpenSearch.ATOM) {
            Element element = document.createElement("entry");
            
            // title
            if ( getTitle() != null ) {
                element.appendChild( asElement( document, "title", getTitle() ) );
            }
            
            // updated
            if ( getUpdated() != null ) {
                element.appendChild( asElement( document, "updated", net.lucenews.atom.Entry.asString( getUpdated() ) ) );
            }
            
            // link
            if ( getLink() != null ) {
                element.appendChild( getLink().asElement( document, format, mode, true ) );
            }
            
            // id
            if ( getId() != null ) {
                element.appendChild( asElement( document, "id", getId() ) );
            }
            
            // people
            Iterator<OpenSearchPerson> peopleIterator = getPeople().iterator();
            while ( peopleIterator.hasNext() ) {
                OpenSearchPerson person = peopleIterator.next();
                element.appendChild( person.asElement( document, format, mode ) );
            }
            
            // score
            if ( getScore() != null ) {
                element.appendChild( asElementNS( document, "http://a9.com/-/opensearch/extensions/relevance/1.0/", "relevance:score", getScore() ) );
                
                // backwards compatibility
                element.appendChild( asElementNS( document, "http://a9.com/-/spec/opensearch/1.1/", "opensearch:relevance", getScore() ) );
            }
            
            // rights
            if ( getRights() != null ) {
                element.appendChild( getRights().asElement( document, format, mode ) );
            }
            
            // content
            if ( getContent() != null ) {
                element.appendChild( getContent().asElement( document, format, mode ) );
            }
            
            return element;
        }
        
        
        /**
         * RSS
         */
        
        if (format == OpenSearch.RSS) {
            Element element = document.createElement("item");
            
            // title
            if (getTitle() != null) {
                element.appendChild( asElement( document, "title", getTitle() ) );
            }
            
            // relevance
            if (getRelevance() != null) {
                element.appendChild( asElementNS( document, "http://a9.com/-/opensearch/extensions/relevance/1.0/", "relevance:score", getScore() ) );
                
                // backwards compatibility
                element.appendChild( asElementNS( document, "http://a9.com/-/spec/opensearch/1.1/", "opensearch:relevance", getScore() ) );
            }
            
            // guid
            if (getId() != null) {
                element.appendChild( asElement( document, "guid", getId() ) );
            }
            
            // updated
            if (getUpdated() != null) {
                String updated = net.lucenews.rss.Item.asString( getUpdated() );
                element.appendChild( asElement( document, "lastBuildDate", updated ) );
            }
            
            // content
            if (getContent() != null) {
                element.appendChild( getContent().asElement( document, format, mode ) );
            }
            
            return element;
        }
        
        
        
        throw new OpenSearchException("Unknown format");
    }
    
    protected Element asElement (Document document, String name, Object value) throws OpenSearchException {
        Element element = document.createElement(name);
        element.appendChild( document.createTextNode(value.toString()) );
        return element;
    }
    
    protected Element asElementNS (Document document, String namespaceURI, String qualifiedName, Object value) throws OpenSearchException {
        Element element = document.createElementNS(namespaceURI, qualifiedName);
        element.appendChild( document.createTextNode(value.toString()) );
        return element;
    }
    
}
