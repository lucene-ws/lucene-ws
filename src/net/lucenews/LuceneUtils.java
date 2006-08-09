package net.lucenews;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*; 
import net.lucenews.*;
import net.lucenews.atom.*;
import net.lucenews.model.*;
import net.lucenews.model.event.*;
import net.lucenews.model.exception.*;
import net.lucenews.opensearch.*;
import org.apache.log4j.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.document.*;
import org.apache.lucene.queryParser.*;
import org.apache.lucene.search.*;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class LuceneUtils {
    public static final String ANALYSIS_NAMESPACE = "org.apache.lucene.analysis";
    public static final String SORT_COMPARATOR_SOURCE_NAMESPACE = "org.apache.lucene.search";
    
    private static final int SORT_TYPE_DATE = 701;
    
    
    
    public static Query clone (Query original) {
        if ( original instanceof BooleanQuery ) {
            return clone( (BooleanQuery) original );
        }
        return (Query) original.clone();
    }
    
    public static BooleanQuery clone (BooleanQuery original) {
        BooleanQuery booleanQuery = (BooleanQuery) original.clone();
        
        BooleanClause[] clauses = booleanQuery.getClauses();
        for ( int i = 0; i < clauses.length; i++ ) {
            BooleanClause clause = clauses[ i ];
            clause.setQuery( clone( clause.getQuery() ) );
        }
        return booleanQuery;
    }
    
    
    
    /**
     * Parses a given string into an analyzer, if at all possible.
     * 
     * @param string The string to parse
     * @return A new instance of the class corresponding to the given string. Null if invalid
     */
    
    public static Analyzer parseAnalyzer (String string) {
        String[] possibleClassNames = new String[] {
            string,
            ANALYSIS_NAMESPACE + "." + string,
            ANALYSIS_NAMESPACE + ".standard." + string
        };
        
        for (int i = 0; i < possibleClassNames.length; i++) {
            String possibleClassName = possibleClassNames[i];
            try {
                return (Analyzer) Class.forName( possibleClassName ).newInstance();
            }
            catch (Exception e) {
            }
        }
        
        return null;
    }
    
    
    
    
    public static SortComparatorSource parseSortComparatorSource (String string) {
        String[] possibleClassNames = new String[] {
            string,
            SORT_COMPARATOR_SOURCE_NAMESPACE + "." + string
        };
        
        for (int i = 0; i < possibleClassNames.length; i++) {
            String possibleClassName = possibleClassNames[i];
            try {
                return (SortComparatorSource) Class.forName( possibleClassName ).newInstance();
            }
            catch (Exception e) {
            }
        }
        
        return null;
    }
    
    
    
    
    /**
     * Parses the operator described by the string provided.
     * This corresponds to the QueryParser.AND_OPERATOR
     * and QueryParser.OR_OPERATOR values.
     * 
     * @param string The string to parse
     * @return The QueryParser.Operator value of the operator, Null if the operator is invalid
     */
    
    public static QueryParser.Operator parseOperator (String string) {
        if (string == null) {
            return null;
        }
        
        string = string.trim().toLowerCase();
        
        if ( string.equals("and") ) {
            return QueryParser.AND_OPERATOR;
        }
        
        if ( string.equals("or") ) {
            return QueryParser.OR_OPERATOR;
        }
        
        return null;
    }
    
    
    
    public static String getOperatorName (QueryParser.Operator operator) {
        if (operator == QueryParser.AND_OPERATOR) {
            return "AND";
        }
        
        if (operator == QueryParser.OR_OPERATOR) {
            return "OR";
        }
        
        return null;
    }
    
    
    
    /**
     * Parses a filter from the given string.
     * 
     * @param string The string to parse
     * @return The corresponding filter
     */
    
    public static Filter parseFilter (String string, QueryParser parser) throws ParseException {
        if (string == null) {
            return null;
        }
        
        if (string.startsWith("QueryFilter:")) {
            return new QueryFilter( parser.parse( string.substring( "QueryFilter:".length() ) ) );
        }
        
        return null;
    }
    
    
    
    /**
     * Parses a <tt>Sort</tt> object from the given string.
     * 
     * @param string The string to parse
     * @return The corresponding Sort object
     */
    
    public static Sort parseSort (String string) {
        if (string == null) {
            return null;
        }
        
        SortField[] sortFields = parseSortFields( string );
        
        if (sortFields.length == 0) {
            return null;
        }
        
        return new Sort( sortFields );
    }
    
    
    
    public static SortField[] parseSortFields (String string) {
        if (string == null) {
            return new SortField[]{};
        }
        
        String[] strings = string.split( "," );
        
        List<SortField> sortFields = new ArrayList<SortField>( strings.length );
        
        for (int i = 0; i < strings.length; i++) {
            SortField sortField = parseSortField( strings[ i ] );
            if (sortField != null) {
                sortFields.add( sortField );
            }
        }
        
        return sortFields.toArray( new SortField[]{} );
    }
    
    /**
     * Sort field locale
     *
     * A locale, if present, resides within a set of parenthesis.
     */
    
    public static Locale parseSortLocale (String string) {
        Locale locale = null;
        
        if (string.matches( ".+\\(\\w+\\)!?" )) {
            String localeString = string.substring(
                string.indexOf( "(" ) + 1,
                string.indexOf( ")" )
            );
            
            if (localeString != null && localeString.trim().length() != 0) {
                return ServletUtils.parseLocale( localeString );
            }
        }
        
        return locale;
    }
    
    /**
     * Parses an individual sort field. Acts as a helper to
     * to the getSort() method.
     */
    
    public static SortField parseSortField (String string) {
        if (string == null || string.trim().length() == 0) {
            return null;
        }
        
        boolean reversed = parseSortReversed( string );
        Locale  locale   = parseSortLocale( string );
        int     dataType = parseSortType( string );
        
        /**
         * Field
         *
         * Fields by which results are sorted may be of the following types:
         *
         *   - Document
         *     The document sort field is indicated by the presence of '<doc>'
         *
         *   - Score
         *     The score sort field is indicated by the presence of '<score>'
         *
         *   - Custom
         *     A custom sort field is indicated by the presence of '<custom: ... >'
         *
         *   - User-defined
         *     A user defined sort field is indicated by the presence of '"$name"'
         *
         */
        
        int     fieldType = 0;
        boolean fieldTypeSpecified = false;
        String  fieldName = null;
        
        // Document/Score/Custom
        if (string.matches( "^<.+>.*" )) {
            String substring = "";
            
            if (string.indexOf( ":" ) != -1) {
                substring = string.substring(
                    string.indexOf( "<" ) + 1,
                    string.indexOf( ":" )
                ).trim();
            }
            else {
                substring = string.substring(
                    string.indexOf( "<" ) + 1,
                    string.indexOf( ">" )
                ).trim();
            }
            
            if (substring.equals( "doc" )) {
                fieldType = SortField.DOC;
                fieldTypeSpecified = true;
            }
            
            if (substring.equals( "score" )) {
                fieldType = SortField.SCORE;
                fieldTypeSpecified = true;
            }
            
            if (substring.equals( "custom" )) {
                fieldType = SortField.CUSTOM;
                fieldTypeSpecified = true;
                
                String factory   = parseCustomFieldFactory( substring );
                fieldName = parseCustomFieldName( substring );
                return new SortField( fieldName, LuceneUtils.parseSortComparatorSource( factory ) );
            }
        }
        
        // User-defined
        else if (string.matches( "^\".+\".*" )) {
            String substring = string.substring(
                string.indexOf( "\"" ) + 1,
                string.indexOf( "\"", string.indexOf( "\"" ) + 1 )
            );
            
            if (substring != null && substring.trim().length() != 0) {
                fieldName = substring.trim();
            }
            
            fieldType = parseSortType( string );
            fieldTypeSpecified = true;
            
            if (dataType == SORT_TYPE_DATE) {
                return new SortField( fieldName, new DateComparator( reversed ) );
            }
        }
        
        if (fieldTypeSpecified) {
            switch (fieldType) {
                case SortField.DOC:
                    return new SortField( null, SortField.DOC, reversed );
                    
                case SortField.SCORE:
                    return new SortField( null, SortField.SCORE, reversed );
                    
                case SortField.AUTO:
                    if (locale != null) {
                        return new SortField( fieldName, locale, reversed );
                    }
                    else {
                        return new SortField( fieldName, dataType, reversed );
                    }
                    
                default:
                    return new SortField( fieldName, dataType, reversed );
            }
        }
        
        return null;
    }
    
    
    
    public static String parseCustomFieldName (String string) {
        String[] parts = string.split( ":" );
        return parts[ 1 ].replaceAll( "\"", "" );
    }
    
    public static String parseCustomFieldFactory (String string) {
        String[] parts = string.split( ":" );
        String f = parts[2];
        
        String[] p = f.split( "@" );
        return p[0];
    }
    
    
    
    /**
     * Sort field order
     *
     * A reversed sort order is indicated by an exclamation mark
     * located at the end of the sort string. Its absense implies
     * a forward sort order (the default).
     */
    
    public static boolean parseSortReversed (String string) {
        if (string == null || string.trim().length() == 0) {
            return false;
        }
        return string.matches( ".+!$" );
    }
    
    
    
    /**
     * Sort field type
     *
     * The field being sorted on may be interpreted in light of
     * a specific data type. A type definition is indicated by the
     * presence of '[$type]' where $type may take on any of the
     * following values:
     *   - int
     *   - string
     *   - float
     *   - auto
     * 
     */
    
    public static int parseSortType (String string) {
        if (string == null || string.trim().length() == 0) {
            return SortField.AUTO;
        }
        
        if (string.matches( "^.+\\[.+\\].*$" )) {
            String substring = string.substring(
                string.indexOf( "[" ) + 1,
                string.indexOf( "]" )
            ).toLowerCase().trim();
            
            if (substring.equals( "int" )) {
                return SortField.INT;
            }
            
            if (substring.equals( "float" )) {
                return SortField.FLOAT;
            }
            
            if (substring.equals( "string" )) {
                return SortField.STRING;
            }
            
            if (substring.equals( "auto" )) {
                return SortField.AUTO;
            }
            
            if (substring.equals( "date" )) {
                return SORT_TYPE_DATE;
            }
        }
        
        return SortField.AUTO;
    }
    
}
