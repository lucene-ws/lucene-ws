package net.lucenews.model;

import java.io.*;
import org.apache.lucene.document.*;
import org.apache.lucene.search.*;



public class LuceneMultiSearcher extends MultiSearcher
{
	private String subSearcherField;
	
	public LuceneMultiSearcher (Searchable[] searchables, String subSearcherField)
		throws IOException
	{
		super( searchables );
		this.subSearcherField = subSearcherField;
	}
	
	public Document doc (int i)
		throws IOException
	{
		Document document = super.doc( i );
		
		// Ensure fields are not already existing
		document.removeFields( subSearcherField );
		
		// Add appropriate field
		document.add(
			new Field(
				subSearcherField,
				String.valueOf( subSearcher( i ) ),
				false,
				false,
				false,
				false
			)
		);
		
		return document;
	}
	
}
