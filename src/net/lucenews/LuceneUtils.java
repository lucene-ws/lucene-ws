package net.lucenews;

import java.io.*;
import net.lucenews.atom.*;

import net.lucenews.*;
import net.lucenews.model.*;
import net.lucenews.model.event.*;
import net.lucenews.model.exception.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.FactoryConfigurationError;  
import javax.xml.parsers.ParserConfigurationException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortComparatorSource;
import org.apache.lucene.search.SortField;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class LuceneUtils
{
	public static final String ANALYSIS_NAMESPACE = "org.apache.lucene.analysis";
	public static final String SORT_COMPARATOR_SOURCE_NAMESPACE = "org.apache.lucene.search";
	
	
	
	/**
	 * Parses a given string into an analyzer, if at all possible.
	 * 
	 * @param string The string to parse
	 * @return A new instance of the class corresponding to the given string. Null if invalid
	 */
	public static Analyzer parseAnalyzer (String string)
	{
		String[] possibleClassNames = new String[]
		{
			string,
			ANALYSIS_NAMESPACE + "." + string,
			ANALYSIS_NAMESPACE + ".standard." + string
		};
		
		for( int i = 0; i < possibleClassNames.length; i++ )
		{
			String possibleClassName = possibleClassNames[i];
			try
			{
				return (Analyzer) Class.forName( possibleClassName ).newInstance();
			}
			catch(Exception e)
			{
			}
		}
		
		return null;
	}
	
	
	
	
	public static SortComparatorSource parseSortComparatorSource (String string)
	{
		String[] possibleClassNames = new String[]
		{
			string,
			SORT_COMPARATOR_SOURCE_NAMESPACE + "." + string
		};
		
		for( int i = 0; i < possibleClassNames.length; i++ )
		{
			String possibleClassName = possibleClassNames[i];
			try
			{
				return (SortComparatorSource) Class.forName( possibleClassName ).newInstance();
			}
			catch(Exception e)
			{
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
	public static QueryParser.Operator parseOperator (String string)
	{
		if( string == null )
			return null;
		
		string = string.trim().toLowerCase();
		
		if( string.equals( "and" ) )
			return QueryParser.AND_OPERATOR;
		
		if( string.equals( "or" ) )
			return QueryParser.OR_OPERATOR;
		
		return null;
	}
	
	
	
	public static String getOperatorName (QueryParser.Operator operator)
	{
        if( operator == QueryParser.AND_OPERATOR )
            return "AND";
        
        if( operator == QueryParser.OR_OPERATOR )
            return "OR";
        
		return null;
	}
	
	
	
	/**
	 * Parses a filter from the given string.
	 * 
	 * @param string The string to parse
	 * @return The corresponding filter
	 */
	public static Filter parseFilter (String string)
	{
		return null;
	}
	
	
	
	/**
	 * Parses a <tt>Sort</tt> object from the given string.
	 * 
	 * @param string The string to parse
	 * @return The corresponding Sort object
	 */
	public static Sort parseSort (String string)
	{
		if( string == null )
			return null;
		
		SortField[] sortFields = parseSortFields( string );
		
		if( sortFields.length == 0 )
			return null;
		
		return new Sort( sortFields );
	}
	
	
	
	public static SortField[] parseSortFields (String string)
	{
		if( string == null )
			return new SortField[]{};
		
		String[] strings = string.split( "," );
		
		List<SortField> sortFields = new ArrayList<SortField>( strings.length );
		
		for( int i = 0; i < strings.length; i++ )
		{
			SortField sortField = parseSortField( strings[ i ] );
			if( sortField != null )
				sortFields.add( sortField );
		}
		
		return sortFields.toArray( new SortField[]{} );
	}
	
	/**
	 * Sort field locale
	 *
	 * A locale, if present, resides within a set of parenthesis.
	 */
	public static Locale parseSortLocale (String string)
	{
		Locale locale = null;

		if( string.matches( ".+\\(\\w+\\)!?" ) )
		{
			String localeString = string.substring(
				string.indexOf( "(" ) + 1,
				string.indexOf( ")" )
			);
			
			if( localeString != null && localeString.trim().length() != 0 )
				return ServletUtils.parseLocale( localeString );
		}
		
		return locale;
	}
	
	/**
	 * Parses an individual sort field. Acts as a helper to
	 * to the getSort() method.
	 */
	public static SortField parseSortField (String string)
	{
		if( string == null || string.trim().length() == 0 )
			return null;
		
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
		if( string.matches( "^<.+>.*" ) )
		{
			String substring = "";
			
			if( string.indexOf( ":" ) != -1 )
				substring = string.substring(
					string.indexOf( "<" ) + 1,
					string.indexOf( ":" )
				).trim();
			else
				substring = string.substring(
					string.indexOf( "<" ) + 1,
					string.indexOf( ">" )
				).trim();
			
			if( substring.equals( "doc" ) )
			{
				fieldType = SortField.DOC;
				fieldTypeSpecified = true;
			}
			
			if( substring.equals( "score" ) )
			{
				fieldType = SortField.SCORE;
				fieldTypeSpecified = true;
			}
			
			if( substring.equals( "custom" ) )
			{
				fieldType = SortField.CUSTOM;
				fieldTypeSpecified = true;
				
				String factory   = parseCustomFieldFactory( substring );
				fieldName = parseCustomFieldName( substring );
				return new SortField( fieldName, LuceneUtils.parseSortComparatorSource( factory ) );
			}
		}
		
		// User-defined
		else if( string.matches( "^\".+\".*" ) )
		{
			String substring = string.substring(
				string.indexOf( "\"" ) + 1,
				string.indexOf( "\"", string.indexOf( "\"" ) + 1 )
			);
			
			if( substring != null && substring.trim().length() != 0 )
				fieldName = substring.trim();
			
			fieldType = parseSortType( string );
			fieldTypeSpecified = true;
			
			if( dataType == LuceneKeys.DATE )
				return new SortField( fieldName, new DateComparator( reversed ) );
		}
		
		if( fieldTypeSpecified )
		{
			switch( fieldType )
			{
				case SortField.DOC:
					return new SortField( null, SortField.DOC, reversed );
				
				case SortField.SCORE:
					return new SortField( null, SortField.SCORE, reversed );
				
				case SortField.AUTO:
					if( locale != null )
						return new SortField( fieldName, locale, reversed );
					else
						return new SortField( fieldName, dataType, reversed );
				
				default:
					return new SortField( fieldName, dataType, reversed );
			}
		}
		
		return null;
	}
	
	
	
	public static String parseCustomFieldName (String string)
	{
		String[] parts = string.split( ":" );
		return parts[ 1 ].replaceAll( "\"", "" );
	}
	
	public static String parseCustomFieldFactory (String string)
	{
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
	public static boolean parseSortReversed (String string)
	{
		if( string == null || string.trim().length() == 0 )
			return false;
		
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
	public static int parseSortType (String string)
	{
		if( string == null || string.trim().length() == 0 )
			return SortField.AUTO;
		
		if( string.matches( "^.+\\[.+\\].*$" ) )
		{
			String substring = string.substring(
				string.indexOf( "[" ) + 1,
				string.indexOf( "]" )
			).toLowerCase().trim();
			
			if( substring.equals( "int" ) )
				return SortField.INT;
			
			if( substring.equals( "float" ) )
				return SortField.FLOAT;
			
			if( substring.equals( "string" ) )
				return SortField.STRING;
			
			if( substring.equals( "auto" ) )
				return SortField.AUTO;
			
			if( substring.equals( "date" ) )
				return LuceneKeys.DATE;
		}
		
		return SortField.AUTO;
	}

	/**
	 * Builds a <code>Query</code> object based on the provided
	 * <code>LuceneSearchRequest</code> object
	 */
	
	/**public static Query buildQuery (LuceneSearchRequest req) throws ParseException, InsufficientDataException
	{
		Query query = null;
		
		if( req.getDefaultField() == null )
			throw new InsufficientDataException( "Must specify a default field" );
		
		if( req.getAnalyzer() == null )
			throw new InsufficientDataException( "Must specify an analyzer" );
		
		QueryParser parser = new QueryParser( req.getDefaultField(), req.getAnalyzer() );
		parser.setLocale( req.getLocale() );
		if( req.getDefaultOperatorCode() != null )
			parser.setOperator( req.getDefaultOperatorCode().intValue() );
		
		query = parser.parse( req.getSearchString() );
		
		return query;
	}*/
	
	
	
	/**
	 * Parses a list of <code>Document</code>s from a provided <code>InputStream</code>
	 */
	/**public static List<LuceneDocument> parseDocuments (InputStream inputStream) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		org.w3c.dom.Document document = builder.parse( inputStream );
		return parseDocuments( document );
	}	
	
	
	@Deprecated
	public static List<LuceneDocument> parseDocuments (org.w3c.dom.Document doc)
	{
		List<LuceneDocument> documents = new LinkedList<LuceneDocument>();
		
		Element root = doc.getDocumentElement();
		
		if( root.getTagName().equals( "document" ) )
			documents.add( parseDocument( root, null ) );
		
		else
		{
			/**
			 * Default analyzer
			Analyzer defaultAnalyzer = null;
			NodeList analyzers = root.getElementsByTagName( "analyzer" );
			for( int i = 0; i < analyzers.getLength(); i++ )
				if( analyzers.item( i ).getParentNode() == root )
				{
					Element element = (Element) analyzers.item( i );
					Analyzer candidateAnalyzer = parseAnalyzer( ServletUtils.toString( element.getChildNodes() ) );
					if( candidateAnalyzer != null )
						defaultAnalyzer = candidateAnalyzer;
				}
			
			NodeList elements = root.getElementsByTagName( "document" );
			for( int i = 0; i < elements.getLength(); i++ )
			{
				Element element = (Element) elements.item( i );
				documents.add( parseDocument( element, defaultAnalyzer ) );
			}
		}
		
		return documents;
	}
	
	
	/**
	public static List<Field> parseFields (NodeList nodes)
	{
		List<Field> fields = new LinkedList<Field>();
		
		for( int i = 0; i < nodes.getLength(); i++ )
		{
			if( nodes.item( i ).getNodeType() != Node.ELEMENT_NODE )
				continue;
			
			Element element = (Element) nodes.item( i );
			
			if( element.getTagName().equals( "field" ) )
				fields.add( parseField( element ) );
			
			if( element.getTagName().equals( "fields" ) )
				fields.addAll( parseFields( element.getChildNodes() ) );
				
		}
		
		return fields;
	}
	
	/**
	@Deprecated
	public static Field parseField (Element element)
	{
		if( !element.getTagName().equals( "field" ) )
			throw new RuntimeException( "Must provide a <field> tag" );
		
		String name  = element.getAttribute( "name" );
		String value = ServletUtils.toString( element.getChildNodes() );
		
		if( element.getAttribute( "type" ) != null )
		{
			String type = element.getAttribute( "type" ).trim().toLowerCase();
			
			if( type.equals( "keyword" ) )
				return Field.Keyword( name, value );
			
			if( type.equals( "text" ) )
				return Field.Text( name, value );
			
			if( type.equals( "sort" ) )
				return new Field( name, value, true, true, false );
			
			if( type.equals( "unindexed" ) )
				return Field.UnIndexed( name, value );
			
			if( type.equals( "unstored" ) )
				return Field.UnStored( name, value );
			
			return Field.Text( name, value );
		}
		else
		{
			boolean index = ServletUtils.parseBoolean( element.getAttribute( "index" ) );
			boolean store = ServletUtils.parseBoolean( element.getAttribute( "store" ) );
			boolean token = ServletUtils.parseBoolean( element.getAttribute( "tokenized" ) );
			
			return new Field( name, value, index, store, token );
		}
	}
	
	
	
	/**
	 * A helper method to parse a Lucene document from a DOM Element.

	@Deprecated
	public static LuceneDocument parseDocument (Element element, Analyzer defaultAnalyzer)
	{
		if( !element.getTagName().equals( "document" ) )
			throw new RuntimeException( "Must provide a <document> tag" );
		
		org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
		
		NodeList fields = element.getElementsByTagName( "field" );
		for( int i = 0; i < fields.getLength(); i++ )
			document.add( parseField( (Element) fields.item( i ) ) );
		
		
		/**
		 * Analyzer for the document

		Analyzer analyzer = defaultAnalyzer;
		
		NodeList analyzers = element.getElementsByTagName( "analyzer" );
		for( int i = 0; i < analyzers.getLength(); i++ )
		{
			Analyzer candidateAnalyzer = parseAnalyzer( ServletUtils.toString( analyzers.item( i ).getChildNodes() ) );
			if( candidateAnalyzer != null )
				analyzer = candidateAnalyzer;
		}
		
		return new LuceneDocument( document, analyzer );
	}
	
	
	public static String parseTypeString (Field field)
	{
		// Text: true, true, true
		if( field.isStored() && field.isIndexed() && field.isTokenized() )
			return "Text";
		
		// Keyword: true, true, false
		if( field.isStored() && field.isIndexed() && !field.isTokenized() )
			return "Keyword";
		
		// UnIndexed: true, false, false
		if( field.isStored() && !field.isIndexed() && !field.isTokenized() )
			return "UnIndexed";
		
		// UnStored: false, true, true
		if( !field.isStored() && field.isIndexed() && field.isTokenized() )
			return "UnStored";
		
		// Sort: false, true, false
		if( !field.isStored() && field.isIndexed() && !field.isTokenized() )
			return "Sort";
		
		return null;
	}
	
	
	
	@Deprecated
	public static List<LuceneDocument> parseLuceneDocuments (Feed feed)
	{
		return parseLuceneDocuments( feed.getEntries() );
	}
	
	@Deprecated
	public static List<LuceneDocument> parseLuceneDocuments (List<Entry> entries)
	{
		List<LuceneDocument> documents = new LinkedList<LuceneDocument>();
		
		Iterator<Entry> iterator = entries.iterator();
		while( iterator.hasNext() )
			documents.addAll( parseLuceneDocuments( iterator.next() ) );
		
		return documents;
	}
	
	@Deprecated
	public static List<LuceneDocument> parseLuceneDocuments (Entry entry)
	{
		List<LuceneDocument> documents = new LinkedList<LuceneDocument>();
		
		if( entry.getContent() != null )
		{
			if( entry.getContent().getData() != null )
			{
				Object content = entry.getContent().getData();
				
				if( content instanceof Document )
				{
					documents.addAll( parseLuceneDocuments( (Document) content ) );
				}
				
				else if( content instanceof NodeList )
				{
					documents.addAll( parseLuceneDocuments( (NodeList) content ) );
				}
				
				else if( content instanceof Element )
				{
					documents.addAll( parseLuceneDocuments( (Element) content ) );
				}
			}
		}
		
		return documents;
	}
	
	@Deprecated
	public static List<LuceneDocument> parseLuceneDocuments (Document document)
	{
		return parseLuceneDocuments( document.getDocumentElement() );
	}
	
	@Deprecated
	public static List<LuceneDocument> parseLuceneDocuments (NodeList nodes)
	{
		List<LuceneDocument> documents = new LinkedList<LuceneDocument>();
		for( int i = 0; i < nodes.getLength(); i++ )
		{
			if( nodes.item( i ).getNodeType() == Node.ELEMENT_NODE )
				documents.addAll( parseLuceneDocuments( (Element) nodes.item( i ) ) );
		}
		return documents;
	}
	
	@Deprecated
	public static List<LuceneDocument> parseLuceneDocuments (Element element)
	{
		List<LuceneDocument> documents = new LinkedList<LuceneDocument>();
		
		if( element.getTagName().equals( "documents" ) )
		{
			documents.addAll( parseLuceneDocuments( element.getChildNodes() ) );
		}
		else if( element.getTagName().equals( "document" ) )
		{
			documents.add( parseLuceneDocument( element ) );
		}
		
		return documents;
	}
	
	@Deprecated
	public static LuceneDocument parseLuceneDocument (Element element)
	{
		LuceneDocument document = new LuceneDocument();
		
		/**if( element.getAttribute( "id" ) != null )
			try
			{
				//document.setDocumentID( element.getAttribute( "id" ) );
			}
			catch(IOException ioe)
			{
			}
		
		NodeList fields = element.getElementsByTagName( "field" );
		for( int i = 0; i < fields.getLength(); i++ )
			document.add( parseField( (Element) fields.item( i ) ) );
		
		return document;
	}
	
	
	@Deprecated
	public static Element asElement (Enumeration<Field> fields, Document document)
	{
		if( true )
		{
			return asXOXOElement( fields, document );
		}
		
		Element element = document.createElement( "fields" );
		
		while( fields.hasMoreElements() )
			element.appendChild( asElement( fields.nextElement(), document ) );
		
		return element;
	}
	@Deprecated
	public static Element asXOXOElement (Enumeration<Field> fields, Document document)
	{
		Element dl = document.createElement( "dl" );
		dl.setAttribute( "class", "xoxo" );
		
		while( fields.hasMoreElements() )
			asXOXOElement( fields.nextElement(), document, dl );
		
		return dl;
	}
	@Deprecated
	public static void asXOXOElement (Field field, Document document, Element dl)
	{
		//Element li = document.createElement( "li" );
		
		//li.appendChild( document.createTextNode( field.stringValue() ) );
		
		//Element dl = document.createElement( "dl" );
		//
		
		
		StringBuffer _class = new StringBuffer();
		
		if( field.isStored() )
			_class.append( " stored" );
		
		if( field.isIndexed() )
			_class.append( " indexed" );
		
		if( field.isTokenized() )
			_class.append( " tokenized" );
		
		if( field.isTermVectorStored() )
			_class.append( " termvectorstored" );
		
		String className = String.valueOf( _class ).trim();
		
		Element dt = document.createElement( "dt" );
		dt.setAttribute( "class", className );
		dt.appendChild( document.createTextNode( String.valueOf( field.name() ) ) );
		dl.appendChild( dt );
		
		Element dd = document.createElement( "dd" );
		dd.appendChild( document.createTextNode( String.valueOf( field.stringValue() ) ) );
		dl.appendChild( dd );
		
		
		//li.appendChild( dl );
		
		//return li;
	}
	@Deprecated
	public static Element asElement (Field field, Document document)
	{
		Element element = document.createElement( "field" );
		
		element.setAttribute( "name", field.name() );
		
		String type = parseTypeString( field );
		
		if( type != null )
		{
			element.setAttribute( "type", type.toLowerCase() );
		}
		else
		{
			element.setAttribute( "indexed",   String.valueOf( field.isIndexed() ) );
			element.setAttribute( "stored",    String.valueOf( field.isStored() ) );
			element.setAttribute( "tokenized", String.valueOf( field.isTokenized() ) );
		}
		
		element.appendChild( document.createTextNode( String.valueOf( field.stringValue() ) ) );
		
		return element;
	}
	*/
}
