package net.lucenews;

import java.io.*;
import java.lang.reflect.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.*; 
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import net.lucenews.atom.*;
import net.lucenews.*;
import net.lucenews.model.*;
import net.lucenews.model.event.*;
import net.lucenews.model.exception.*;
import net.lucenews.opensearch.*;
import org.apache.log4j.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.search.*;
import org.w3c.dom.*;

public class ServletUtils {
    
    public static void prepareContext (LuceneContext c)
        throws
            IndicesNotFoundException,
            org.apache.lucene.queryParser.ParseException, IOException
    {
        LuceneWebService   service  = c.getService();
        LuceneIndexManager manager  = service.getIndexManager();
        LuceneRequest      request  = c.getRequest();
        LuceneIndex[]      indices  = manager.getIndices( request.getIndexNames() );
        
        
        // OpenSearch query
        if ( c.getOpenSearchQuery() == null ) {
            OpenSearchQuery query = new OpenSearchQuery();
            
            
            /**
             * searchTerms
             */
            
            String searchTerms = null;
            if (searchTerms == null) { searchTerms = request.getCleanParameter("searchTerms"); }
            if (searchTerms == null) { searchTerms = request.getCleanParameter("query");       }
            if (searchTerms == null) { searchTerms = request.getCleanParameter("q");           }
            query.setSearchTerms( searchTerms );
            
            
            /**
             * startIndex
             */
            
            Integer startIndex = null;
            if (startIndex == null) { startIndex = request.getIntegerParameter("startIndex");  }
            if (startIndex == null) { startIndex = request.getIntegerParameter("start_index"); }
            if (startIndex == null) { startIndex = request.getIntegerParameter("start");       }
            if (startIndex == null) { startIndex = request.getIntegerParameter("offset");      }
            query.setStartIndex( startIndex );
            
            
            /**
             * startPage
             */
            
            Integer startPage = null;
            if (startPage == null) {
                startPage = request.getIntegerParameter("startPage");
                c.setStartPageParameter("startPage");
            }
            if (startPage == null) {
                startPage = request.getIntegerParameter("start_page");
                c.setStartPageParameter("start_page");
            }
            if (startPage == null) {
                startPage = request.getIntegerParameter("page");
                c.setStartPageParameter("page");
            }
            
            if (startPage == null) c.setStartPageParameter( null );
            
            query.setStartPage( startPage );
            
            
            /**
             * count
             */
            
            Integer count = null;
            if (count == null) { count = request.getIntegerParameter("count");            }
            if (count == null) { count = request.getIntegerParameter("limit");            }
            if (count == null) { count = request.getIntegerParameter("itemsPerPage");     }
            if (count == null) { count = request.getIntegerParameter("items_per_page");   }
            if (count == null) { count = request.getIntegerParameter("entriesPerPage");   }
            if (count == null) { count = request.getIntegerParameter("entries_per_page"); }
            query.setCount( count );
            
            
            /**
             * totalResults
             */
            
            Integer totalResults = null;
            if (totalResults == null) { totalResults = request.getIntegerParameter("totalResults");  }
            if (totalResults == null) { totalResults = request.getIntegerParameter("total_results"); }
            if (totalResults == null) { totalResults = request.getIntegerParameter("total");         }
            query.setTotalResults( totalResults );
            
            
            /**
             * language
             */
            
            String language = null;
            if (language == null) { language = request.getCleanParameter("language"); }
            if (language == null) { language = request.getCleanParameter("lang");     }
            if (language == null) { language = request.getCleanParameter("locale");   } // This needs fixing
            query.setLanguage( language );
            
            
            /**
             * inputEncoding
             */
            
            String inputEncoding = null;
            if (inputEncoding == null) { inputEncoding = request.getCleanParameter("inputEncoding");  }
            if (inputEncoding == null) { inputEncoding = request.getCleanParameter("input_encoding"); }
            if (inputEncoding == null) { inputEncoding = request.getCleanParameter("input");          }
            query.setInputEncoding( inputEncoding );
            
            
            /**
             * outputEncoding
             */
            
            String outputEncoding = null;
            if (outputEncoding == null) { outputEncoding = request.getCleanParameter("outputEncoding");  }
            if (outputEncoding == null) { outputEncoding = request.getCleanParameter("output_encoding"); }
            if (outputEncoding == null) { outputEncoding = request.getCleanParameter("output");          }
            query.setOutputEncoding( outputEncoding );
            
            
            /**
             * role
             */
            
            query.setRole( OpenSearchQuery.Role.REQUEST );
            
            
            c.setOpenSearchQuery( query );
        }
        
        
        // analyzer
        if (c.getAnalyzer() == null) {
            Analyzer analyzer = null;
            
            if (analyzer == null) { analyzer = LuceneUtils.parseAnalyzer( request.getCleanParameter("analyzer") ); }
            if (analyzer == null) { analyzer = new StandardAnalyzer(); }
            
            c.setAnalyzer( analyzer );
        }
        
        
        // filter analyzer
        if (c.getFilterAnalyzer() == null) {
            Analyzer analyzer = null;
            
            if (analyzer == null) { analyzer = LuceneUtils.parseAnalyzer( request.getCleanParameter("filterAnalyzer") ); }
            
            c.setFilterAnalyzer( analyzer );
        }
        
        
        // default operator
        if (c.getDefaultOperator() == null) {
            QueryParser.Operator defaultOperator = null;
            
            // defaultOperator
            if (defaultOperator == null) {
                defaultOperator = LuceneUtils.parseOperator(request.getCleanParameter("defaultOperator"));
            }
            
            // default_operator
            if (defaultOperator == null) {
                defaultOperator = LuceneUtils.parseOperator(request.getCleanParameter("default_operator"));
            }
            
            // operator
            if (defaultOperator == null) {
                defaultOperator = LuceneUtils.parseOperator(request.getCleanParameter("operator"));
            }
            
            // indices' default operators
            for (int i = 0; i < indices.length; i++) {
                if (defaultOperator != null) {
                    break;
                }
                defaultOperator = indices[ i ].getDefaultOperator();
            }
            
            // service's default operator
            if (defaultOperator == null) {
                defaultOperator = service.getDefaultOperator();
            }
            
            c.setDefaultOperator( defaultOperator );
        }
        
        
        // default field
        if (c.getDefaultFields() == null) {
            String[] defaultFields = null;
            
            if (defaultFields == null) { defaultFields = ServletUtils.split( request.getCleanParameterValues("defaultFields") );  }
            if (defaultFields == null) { defaultFields = ServletUtils.split( request.getCleanParameterValues("defaultField") );   }
            if (defaultFields == null) { defaultFields = ServletUtils.split( request.getCleanParameterValues("default_fields") ); }
            if (defaultFields == null) { defaultFields = ServletUtils.split( request.getCleanParameterValues("default_field") );  }
            if (defaultFields == null) { defaultFields = ServletUtils.split( request.getCleanParameterValues("defaults") );       }
            if (defaultFields == null) { defaultFields = ServletUtils.split( request.getCleanParameterValues("default") );        }
            
            // index-specified default field
            for (int i = 0; i < indices.length && defaultFields == null; i++) {
                defaultFields = indices[ i ].getDefaultFieldNames();
            }
            
            // service-specified default field
            if (defaultFields == null) { defaultFields = service.getDefaultFields(); }
            
            c.setDefaultFields( defaultFields );
        }
        
        
        // OpenSearch format
        if (c.getOpenSearchFormat() == null) {
            OpenSearch.Format format = null;
            
            // "format" request parameter
            if (format == null) {
                try {
                    format = OpenSearch.getFormat( request.getParameter("format") );
                }
                catch (Exception e) {
                }
            }
            
            // "atom" request parameter
            if (format == null) {
                try {
                    if ( ServletUtils.parseBoolean( request.getParameter("atom") ) ) {
                        format = OpenSearch.Format.ATOM;
                    }
                }
                catch (Exception e) {
                }
            }
            
            // "rss" request parameter
            if (format == null) {
                try {
                    if ( ServletUtils.parseBoolean( request.getParameter("rss") ) ) {
                        format = OpenSearch.Format.RSS;
                    }
                }
                catch (Exception e) {
                }
            }
            
            c.setOpenSearchFormat( format );
        }
        
        
        // locale
        if (c.getLocale() == null) {
            c.setLocale( ServletUtils.parseLocale( request.getCleanParameter("locale") ) );
        }
        
        
        // suggest synonyms
        if (c.suggestSynonyms() == null) {
            Boolean suggestSynonyms = null;
            if (suggestSynonyms == null) { suggestSynonyms = request.getBooleanParameter("synonyms");        }
            if (suggestSynonyms == null) { suggestSynonyms = service.getBooleanProperty("suggest.synonyms"); }
            c.suggestSynonyms( suggestSynonyms );
            
            // Synonym expander
            if ( suggestSynonyms != null && suggestSynonyms && c.getSynonymExpander() == null ) {
                String synonymsIndex = null;
                
                if ( synonymsIndex == null ) { synonymsIndex = service.getCleanProperty("suggest.synonyms.index"); }
                
                if ( synonymsIndex != null ) {
                    LuceneSynonymExpander synonymExpander = new LuceneSynonymExpander();
                    synonymExpander.setAnalyzer( c.getAnalyzer() );
                    synonymExpander.setSearcher( manager.getIndex( synonymsIndex ).getIndexSearcher() );
                    c.setSynonymExpander( synonymExpander );
                }
            }
        }
        
        
        // is optimizing
        if (c.isOptimizing() == null) {
            String optimize = request.getCleanParameter("optimize");
            
            if (optimize == null) {
                c.isOptimizing( true );
            }
            else {
                c.isOptimizing( ServletUtils.parseBoolean( optimize ) );
            }
        }
        
        
        // suggest spelling
        if (c.suggestSpelling() == null) {
            Boolean suggestSpelling = null;
            if (suggestSpelling == null) { suggestSpelling = request.getBooleanParameter("spelling");        }
            if (suggestSpelling == null) { suggestSpelling = service.getBooleanProperty("suggest.spelling"); }
            c.suggestSpelling( suggestSpelling );
            
            if ( suggestSpelling != null && suggestSpelling ) {
                String spellCheckIndex = null;
                
                if ( spellCheckIndex == null ) { spellCheckIndex = service.getProperty("suggest.spelling.index"); }
                if ( spellCheckIndex == null ) { spellCheckIndex = "spelling";                                    }
                
                if ( spellCheckIndex != null ) {
                    LuceneSpellChecker spellChecker = new LuceneSpellChecker( manager.getIndex( spellCheckIndex ).getLuceneDirectory() );
                    spellChecker.setMaximumSuggestions( 5 );
                    c.setSpellChecker( spellChecker );
                }
            }
        }
        
        
        // suggest similar
        if (c.suggestSimilar() == null) {
            Boolean suggestSimilar = null;
            if (suggestSimilar == null) { suggestSimilar = request.getBooleanParameter("similar");        }
            if (suggestSimilar == null) { suggestSimilar = service.getBooleanProperty("suggest.similar"); }
            c.suggestSimilar( suggestSimilar );
        }
        
        
        // QueryParser
        if (c.getQueryParser() == null) {
            if (c.getDefaultFields() != null && c.getAnalyzer() != null) {
                String fakeDefaultField = "dd8fc45d87f91c6f9a9f43a3f355a94a"; // MD5 hash of "boat"
                LuceneQueryParser queryParser = new LuceneQueryParser( fakeDefaultField, c.getAnalyzer() );
                //LuceneQueryParser queryParser = new LuceneQueryParser( c.getDefaultFields()[ 0 ], c.getAnalyzer() );
                queryParser.setFields( c.getDefaultFields() );
                queryParser.setDefaultOperator( c.getDefaultOperator() );
                c.setQueryParser( queryParser );
            }
        }
        
        
        // filter
        if (c.getFilter() == null && c.getQueryParser() != null) {
            String filterString = null;
            
            if (filterString == null) { filterString = request.getCleanParameter("searchFilter");  };
            if (filterString == null) { filterString = request.getCleanParameter("search_filter"); };
            if (filterString == null) { filterString = request.getCleanParameter("filter");        };
            
            QueryParser filterParser = null;
            if ( c.getFilterAnalyzer() == null ) {
                filterParser = c.getQueryParser();
            }
            else {
                filterParser = new QueryParser( c.getQueryParser().getField(), c.getFilterAnalyzer() );
            }
            
            c.getLogger().debug("Filter query parser: " + filterParser);
            
            if (filterString != null) {
                c.getLogger().debug("Generic filter string: " + filterString);
                c.setFilter( LuceneUtils.parseFilter( filterString, filterParser ) );
            }
            else {
                
                String queryFilterString = null;
                if ( queryFilterString == null ) { queryFilterString = request.getCleanParameter("searchTermsFilter"); }
                if ( queryFilterString == null ) { queryFilterString = request.getCleanParameter("QueryFilter");       }
                if ( queryFilterString == null ) { queryFilterString = request.getCleanParameter("queryFilter");       }
                
                if ( queryFilterString != null ) {
                    c.getLogger().debug("Query filter string: " + queryFilterString);
                    c.setFilter( new QueryFilter( filterParser.parse( queryFilterString ) ) );
                }
                
            }
        }
        
        
        // sort
        if (c.getSort() == null) {
            String sortString = null;
            
            if (sortString == null) { sortString = request.getCleanParameter("sort"); }
            
            if (sortString != null) {
                c.setSort( LuceneUtils.parseSort( sortString ) );
            }
        }
        
        
        c.setQueryReconstructor( new QueryReconstructor() );
    }
    
    
    
    public static String[] split (String source) {
        return split( source, "," );
    }
    
    public static String[] split (String source, String delimiter) {
        if ( source == null ) {
            return null;
        }
        
        List<String> tokenList = new ArrayList<String>();
        
        String[] tokens = source.split( delimiter );
        for ( int i = 0; i < tokens.length; i++ ) {
            String token = clean( tokens[ i ] );
            if ( token != null ) {
                tokenList.add( token );
            }
        }
        
        if ( tokenList.size() == 0 ) {
            return null;
        }
        else {
            return tokenList.toArray( new String[]{} );
        }
    }
    
    public static String[] split (String[] sources) {
        return split( sources, "," );
    }
    
    public static String[] split (String[] sources, String delimiter) {
        if ( sources == null || sources.length == 0 ) {
            return null;
        }
        
        List<String> stringList = new ArrayList<String>();
        
        for ( int i = 0; i < sources.length; i++ ) {
            String[] source = split( sources[ i ], delimiter );
            if ( source != null ) {
                stringList.addAll( Arrays.asList( source ) );
            }
        }
        
        if ( stringList.size() == 0 ) {
            return null;
        }
        else {
            return stringList.toArray( new String[]{} );
        }
    }
    
    
    
    public static Integer parseDateFormatStyle (String string) {
        if (clean( string ) == null) {
            return null;
        }
        
        string = clean( string ).toLowerCase().trim();
        
        if (string.equals( "default" )) {
            return DateFormat.DEFAULT;
        }
        
        if (string.equals( "full" )) {
            return DateFormat.FULL;
        }
        
        if (string.equals( "long" )) {
            return DateFormat.LONG;
        }
        
        if (string.equals( "medium" )) {
            return DateFormat.MEDIUM;
        }
        
        if (string.equals( "short" )) {
            return DateFormat.SHORT;
        }
        
        return null;
    }
    
    
    
    /**
     * Escapes the characters within a given string
     *
     * @param string A <code>String</code> to escape
     * @return The escaped version of the provided string
     */
    
    public static String escape (String string) {
        if (string == null) {
            return null;
        }
        return string.replaceAll( "<", "&lt;" ).replaceAll( ">", "&gt;" );
    }
    
    
    
    /**
     * Escapes the characters within a given object's toString()
     * output.
     *
     * @param object An object to escape
     * @return The escaped version of the provided object's toString() method
     */
    
    public static String escape (Object object) {
        if (object == null) {
            return null;
        }
        return escape( object.toString() );
    }
    
    
    
    /**
     * Cleans a given string. This involves turning empty
     * strings into nulls.
     *
     * @param string A <code>String</code> to clean
     * @return The cleaned version of the provided string
     */
    
    public static String clean (String string) {
        if (string == null || string.trim().length() == 0) {
            return null;
        }
        return string;
    }
    
    public static String[] clean (String[] strings) {
        if ( strings == null || strings.length == 0 ) {
            return null;
        }
        
        List<String> stringList = new ArrayList<String>();
        
        for ( int i = 0; i < strings.length; i++ ) {
            String string = clean( strings[ i ] );
            if ( string != null ) {
                stringList.add( string );
            }
        }
        
        if ( stringList.size() == 0 ) {
            return null;
        }
        else {
            return stringList.toArray( new String[]{} );
        }
    }
    
    
    
    /**
     * Cleans a given object's toString output. This involves turning empty
     * strings into nulls.
     *
     * @param object An object to clean
     * @return The cleaned version of the provided object's toString() output
     */
    
    public static String clean (Object object) {
        return clean( object.toString() );
    }
    
    
    
    /**
     * Parses a <code>Locale</code> from the provided string.
     * Essentially reverse engineers the string based on 
     * <code>Locale</code>'s toString format.
     *
     * @param string String to parse <code>Locale</code> from
     * @return A <code>Locale</code>
     */
    
    public static Locale parseLocale (String string) {
        if (string == null || string.trim().length() == 0) {
            return null;
        }
        
        String[] localeParts = string.split( "_" );
        
        switch (localeParts.length) {
            case (3):
                return new Locale( localeParts[ 0 ], localeParts[ 1 ], localeParts[ 2 ] );
            case (2):
                return new Locale( localeParts[ 0 ], localeParts[ 1 ] );
            case (1):
                return new Locale( localeParts[ 0 ] );
        }
        
        return null;
    }
    
    public static String[] getPathComponents (HttpServletRequest req) {
        String[] tokens = req.getRequestURI().split( "/" );
        int difference = 2;
        
        String[] components = new String[ tokens.length - difference ];
        for (int i = 0; i < components.length; i++) {
            components[ i ] = tokens[ i + difference ];
        }
        
        return components;
    }
    
    public static File parseDirectory (String path) {
        if (clean( path ) == null) {
            return null;
        }
        File directory = new File( path );
        return directory;
    }
    
    
    /**
     * Extracts a list of directories from a given string.
     *
     * @param string A String from which to extract directories
     * @return A <code>List&lt;File&gt;</code> object
     */
    
    public static List<File> parseDirectories (String string) {
        return parseDirectories( string, ";" );
    }
    
    public static List<File> parseDirectories (String string, String separator) {
        List<File> directories = new ArrayList<File>();
        
        if (clean( string ) == null) {
            return directories;
        }
        
        String[] paths = string.split( separator );
        for (int i = 0; i < paths.length; i++) {
            String path = paths[ i ];
            
            if (clean( path ) == null) {
                continue;
            }
            
            directories.add( new File( path ) );
        }
        
        return directories;
    }
    
    
    /**
     * Converts a string into a Boolean value.
     */
    
    public static Boolean parseBoolean (String string) {
        if (string == null) {
            return null;
        }
        
        string = string.trim().toLowerCase();
        
        if ( string.equals("1") ) {
            return true;
        }
        
        if ( string.equals("yes") ) {
            return true;
        }
        
        if ( string.equals("true") ) {
            return true;
        }
        
        return false;
    }
    
    public static Integer parseInteger (String string) {
        Integer integer = null;
        
        try {
            integer = Integer.valueOf( string );
        }
        catch (NumberFormatException e) {
            integer = null;
        }
        
        return integer;
    }
    
    
    
    public static String toString (NodeList nodes) {
        StringBuffer buffer = new StringBuffer();
        
        for (int i = 0; i < nodes.getLength(); i++) {
            buffer.append( toString( nodes.item( i ) ) );
        }
        
        return buffer.toString();
    }
    
    public static String toString (Node[] nodes) {
        StringBuffer buffer = new StringBuffer();
        
        for (int i = 0; i < nodes.length; i++) {
            buffer.append( toString( nodes[ i ] ) );
        }
        
        return buffer.toString();
    }
    
    public static String toString (Node node) {
        switch (node.getNodeType()) {
            case Node.ATTRIBUTE_NODE:
                Attr attribute = (Attr) node;
                return attribute.getName() + "=\"" + attribute.getValue() + "\"";
                
            case Node.ELEMENT_NODE:
                Element element = (Element) node;
                StringBuffer buffer = new StringBuffer();
                
                buffer.append( "<" );
                buffer.append( element.getTagName() );
                
                NamedNodeMap map = element.getAttributes();
                for (int i = 0; i < map.getLength(); i++) {
                    buffer.append( " " + toString( map.item( i ) ) );
                }
                
                NodeList children = element.getChildNodes();
                
                if (children.getLength() == 0) {
                    buffer.append( " />" );
                }
                else {
                    buffer.append( toString( children ) );
                    buffer.append( "</" );
                    buffer.append( element.getTagName() );
                    buffer.append( ">" );
                }
                
                return buffer.toString();
                
            case Node.TEXT_NODE:
                return node.getNodeValue();
                
            default:
                return "";
        }
    }
    
    public static StackTraceElement[] getStackTrace () {
        try {
            throw new Throwable( "" );
        }
        catch (Throwable t) {
            StackTraceElement[] stack = t.getStackTrace();
            StackTraceElement[] cleanStack = new StackTraceElement[ stack.length - 1 ];
            
            for (int i = 0; i < cleanStack.length; i++) {
                cleanStack[i] = stack[i+1];
            }
            
            return cleanStack;
        }
    }
    
    public static void indentDocument (Document document) {
        if (document == null) {
            return;
        }
        indentNodes( document.getDocumentElement().getChildNodes(), document, 0 );
    }
    
    public static void indentNode (Node node, Document document, int level) {
        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
                Node parent  = node.getParentNode();
                Node sibling = node.getNextSibling();
                
                String indent = "";
                for (int i = 0; i < level; i++) {
                    indent += "\t";
                }
                
                break;
        }
        
        indentNodes( node.getChildNodes(), document, level + 1 );
    }
    
    public static void indentNodes (NodeList nodes, Document document, int level) {
        for (int i = 0; i < nodes.getLength(); i++) {
            indentNode( nodes.item( i ), document, level );
        }
    }
    
    public static void indentElement (int level, Element element, Document document) {
        NodeList nodes = element.getChildNodes();
        
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item( i );
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String buffer = "";
                for (int j = 0; j < level; j++) {
                    buffer += "\t";
                }
                buffer += "\n";
                element.insertBefore( document.createTextNode( buffer ), element );
            }
            
            indentElement( level + 1, (Element) node, document );
        }
    }
    
    public static void printDocument (Document document, PrintWriter out) throws Exception {
        printDocument( document, new StreamResult( out ) );
    }
    
    public static void printDocument (Document document, Result result) throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        
        DOMSource source = new DOMSource( document );
        
        transformer.transform( source, result );
    }
    
    public static Document newDocument () {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.newDocument();
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static Document buildExceptionDocument (Exception e) {
        return buildExceptionDocument( e, null );
    }
    
    public static Document buildExceptionDocument (Exception e, String suggestion) {
        Document doc = newDocument();
        
        Element errorElement = doc.createElement( "error" );
        
        Element typeElement = doc.createElement( "type" );
        typeElement.appendChild( doc.createTextNode( e.getClass().getSimpleName() ) );
        errorElement.appendChild( typeElement );
        
        /**
        Element classElement = doc.createElement( "class" );
        classElement.appendChild( doc.createTextNode( e.getClass().getCanonicalName() ) );
        errorElement.appendChild( classElement );
        */
        
        Element messageElement = doc.createElement( "message" );
        messageElement.appendChild( doc.createTextNode( e.getMessage() ) );
        errorElement.appendChild( messageElement );
        
        if (suggestion != null) {
            Element suggestionElement = doc.createElement( "suggestion" );
            suggestionElement.appendChild( doc.createTextNode( suggestion ) );
            errorElement.appendChild( suggestionElement );
        }
        
        errorElement.appendChild( buildStackTraceElement( e.getStackTrace(), doc ) );
        
        doc.appendChild( errorElement);
        
        return doc;
    }
    
    public static Element buildStackTraceElement (StackTraceElement[] stack, Document xmlDocument) {
        Element traceElement = xmlDocument.createElement( "trace" );
        
        for (int i = 0; i < stack.length; i++) {
            Element traceElementElement = xmlDocument.createElement( "element" );
            traceElementElement.appendChild( xmlDocument.createTextNode( String.valueOf( stack[ i ] ) ) );
            traceElement.appendChild( traceElementElement );
        }
        
        return traceElement;
    }
    
    public static void removeNulls (Document document) {
        if (document == null) {
            return;
        }
        removeNulls( document.getDocumentElement() );
    }
    
    public static void removeNulls (Node node) {
        if (node == null) {
            Node parent = node.getParentNode();
            if (parent != null) {
                parent.removeChild( node );
            }
        }
        
        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node childNode = nodes.item( i );
            
            if (childNode == null || ( childNode.getNodeType() == Node.TEXT_NODE && childNode.getNodeValue() == null )) {
                node.removeChild( childNode );
            }
            else {
                removeNulls( childNode );
            }
        }
    }
    
    
    public static String join (Collection<String> list) {
        return join( list, ", " );
    }
    
    public static String join (Collection<String> list, String delimiter) {
        Iterator<String> iterator = list.iterator();
        StringBuffer buffer = new StringBuffer();
        int iteration = 0;
        while (iterator.hasNext()) {
            iteration++;
            if (iteration > 1) {
                buffer.append( delimiter );
            }
            buffer.append( String.valueOf( iterator.next() ) );
        }
        return String.valueOf( buffer );
    }
    
    
    
    
    public static String toCamelCase (String... parts) {
        if (parts.length == 0) {
            return null;
        }
                
        StringBuffer buffer = new StringBuffer();
        
        buffer.append( parts[ 0 ].toLowerCase() );
        for (int i = 1; i < parts.length; i++) {
            buffer.append( parts[i].substring(0,1).toUpperCase() + parts[i].substring(1) );
        }
        
        return String.valueOf( buffer );
    }
    
    
    
    /**
     * Converts an XOXO list to a properties list
     */
    
    public static Properties asProperties (Element xoxo) {
        char state = 't'; // collecting <dt> initially, will switch between 't' and 'd' (<dd>)
        
        
        Properties properties = new Properties();
        
        
        String name  = null;
        String value = null;
        
        
        NodeList nodes = xoxo.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item( i );
            
            switch (node.getNodeType()) {
                case Node.ELEMENT_NODE:
                    Element element = (Element) node;
                    
                    if (element.getTagName().equals( "dt" )) {
                        if (state == 't') {
                            name = element.getFirstChild().getNodeValue();
                            state = 'd';
                        }
                        else {
                            properties.setProperty( name, value );
                            state = 't';
                        }
                    }
                    
                    if (element.getTagName().equals( "dd" )) {
                        if (state == 'd') {
                            value = element.getFirstChild().getNodeValue();
                            if (name != null && value != null) {
                                properties.setProperty( name, value );
                            }
                            value = null;
                            state = 't';
                        }
                        else {
                        }
                    }
                    
                    break;
            }
        }
        
        return properties;
    }
    
    
    
    
    public static Element asXOXOElement (Document document, Properties properties) {
        Element dl = document.createElement( "dl" );
        
        Enumeration names = properties.propertyNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = properties.getProperty( name );
            
            Element dt = document.createElement( "dt" );
            dt.appendChild( document.createTextNode( String.valueOf( name ) ) );
            dl.appendChild( dt );
            
            Element dd = document.createElement( "dd" );
            dd.appendChild( document.createTextNode( String.valueOf( value ) ) );
            dl.appendChild( dd );
        }
        
        return dl;
    }
    
    
    
    
    
    public static String[] objectsMapped (String method, Object[] objects) {
        String[] strings = new String[ objects.length ];
        
        for (int i = 0; i < objects.length; i++) {
            try {
                strings[ i ] = (String) objects[ i ].getClass().getMethod( method ).invoke( objects[ i ] );
            }
            catch (NoSuchMethodException nsme) {
            }
            catch (IllegalAccessException iae) {
            }
            catch (InvocationTargetException ite) {
            }
            catch (ClassCastException cce) {
            }
        }
        
        return strings;
    }
    
    public static String join (String delimiter, Object... objects) {
        StringBuffer buffer = new StringBuffer();
        
        for (int i = 0; i < objects.length; i++) {
            if (i > 0) {
                buffer.append( delimiter );
            }
            buffer.append( objects[ i ].toString() );
        }
        
        return buffer.toString();
    }
    
    public static String joined (Object... objects) {
        String[] strings = new String[ objects.length ];
        
        for (int i = 0; i < objects.length; i++) {
            strings[ i ] = objects[ i ].toString();
        }
        
        return joined( strings );
    }
    
    public static String joined (String... strings) {
        StringBuffer buffer = new StringBuffer();
        
        for (int i = 0; i < strings.length; i++) {
            if (i > 0) {
                if (i == ( strings.length - 1 )) {
                    buffer.append( " and" );
                }
                else {
                    buffer.append( "," );
                }
            }
            buffer.append( " " + strings[ i ] );
        }
        
        return buffer.toString().trim();
    }
    
    
    public static String[] filtered (String regex, String... strings) {
        List<String> filtered = new ArrayList<String>( strings.length );
        
        for (int i = 0; i < strings.length; i++) {
            if (strings[ i ].matches( regex )) {
                filtered.add( strings[ i ] );
            }
        }
        
        return filtered.toArray( new String[]{} );
    }
    
    
    
    public static String[] mapped (String pattern, String... strings) {
        String[] mapped = new String[ strings.length ];
        
        Properties properties = new Properties();
        
        for (int i = 0; i < strings.length; i++) {
            properties.setProperty( "content", strings[ i ] );
            mapped[ i ] = parse( pattern, properties );
        }
        
        return mapped;
    }
    
    
    
    public static String parse (String string, Properties properties) {
        char state = 'd';
        
        StringBuffer buffer = new StringBuffer();
        StringBuffer parsed = new StringBuffer();
        
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt( i );
            
            switch (state) {
                
                // Data collecting state
                case 'd':
                    if (c == '[') {
                        parsed.append( buffer );
                        buffer = new StringBuffer();
                        state = 'f';
                    }
                    else if (c == '\\') {
                        state = 'e';
                    }
                    else {
                        buffer.append( c );
                    }
                    break;
                    
                    
                    
                // Field state
                case 'f':
                    if (c == ']') {
                        String value = properties.getProperty( buffer.toString() );
                        if (value != null) {
                            parsed.append( value );
                        }
                        
                        buffer = new StringBuffer();
                        state = 'd';
                    }
                    else {
                        buffer.append( c );
                    }
                    break;
                    
                    
                    
                // Escaping state
                case 'e':
                    buffer.append( c );
                    state = 'd';
                    break;
            }
        }
        
        if (buffer.length() > 0) {
            switch (state) {
                
                case 'd':
                    parsed.append( buffer );
                    break;
                    
                case 'f':
                    String value = properties.getProperty( buffer.toString() );
                    if (value != null) {
                        parsed.append( value );
                    }
                    
                    break;
                    
            }
        }
        
        return parsed.toString();
    }
    
    
    
    
    /**
     * Retrieves a {@link java.text.SimpleDateFormat} compatible String pattern.
     * Complies with HTTP/1.1 Protocol Parameters full date.
     * 
     * @return pattern for SimpleDateFormat object
     * @see    <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.3.1">HTTP/1.1: Protocol Parameters</a>
     */
    
    public static String getHTTPDatePattern () {
        // Format:
        //      Sun, 06 Nov 1994 14:49:37 GMT
        return "EEE, dd MMM yyyy kk:mm:ss zzz";
    }
    
    
    
    public static String asHTTPDate (Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat( getHTTPDatePattern() );
        
        StringBuffer buffer = new StringBuffer();
        
        format.format( calendar.getTime(), buffer, new FieldPosition( DateFormat.TIMEZONE_FIELD ) );
        
        return buffer.toString();
    }
    
    
    
    public static Calendar asCalendarFromHTTPDate (String httpDate) {
        if (httpDate == null) {
            return null;
        }
        
        try {
            SimpleDateFormat format = new SimpleDateFormat( getHTTPDatePattern() );
            
            Date date = format.parse( httpDate, new ParsePosition( 0 ) );
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( date );
            
            return calendar;
        }
        catch (NullPointerException npe) {
            return null;
        }
    }
    
    
}
