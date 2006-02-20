package net.lucenews.model;

import java.io.*;
import java.util.*;
import net.lucenews.*;
import net.lucenews.model.event.*;
import net.lucenews.model.exception.*;
import org.apache.lucene.index.*;


public class LuceneIndexManager implements LuceneIndexListener
{
	
	private Map<String,File> directories;
	private LuceneWebService service;
	
	
	public LuceneIndexManager (LuceneWebService service)
	{
		this.service = service;
		directories = new HashMap<String,File>();
		LuceneIndex.addIndexListener( this );
	}
	
	
	
	public LuceneIndex getIndex (String name)
		throws IndexNotFoundException, IOException
	{
		if( !directories.containsKey( name ) )
		{
			// Perhaps we're a little old...
			refresh();
		}
		
		File directory = directories.get( name );
		
		if( directory == null )
			throw new IndexNotFoundException( name );
		
		try
		{
			return LuceneIndex.retrieve( directory );
		}
		catch(IndexNotFoundException infe)
		{
			throw new IndexNotFoundException( name );
		}
	}
	
	public LuceneIndex[] getIndices ()
		throws IndicesNotFoundException, IOException
	{
		guaranteeFreshDirectories();
		
		
		
		// Get and sort directory names
		List<String> namesList = new ArrayList<String>( directories.keySet() );
		Collections.sort( namesList );
		String[] names = namesList.toArray( new String[]{} );
		
		
		
		LuceneIndex[] indices = new LuceneIndex[ names.length ];
		for( int i = 0; i < names.length; i++ )
		{
			try
			{
				indices[ i ] = LuceneIndex.retrieve( directories.get( names[ i ] ) );
			}
			catch(IndexNotFoundException infe)
			{
				refresh();
				throw infe;
			}
		}
		
		return indices;
	}
	
	public LuceneIndex[] getIndices (String... names)
		throws IndicesNotFoundException, IOException
	{
		LuceneIndex[] indices = new LuceneIndex[ names.length ];
		
		for( int i = 0; i < names.length; i++ )
			indices[ i ] = getIndex( names[ i ] );
		
		return indices;
	}
	
	
	
	
	public File[] getIndicesDirectories ()
		throws IOException
	{
		String pathsString = service.getProperty( "indices.directories", service.getProperty( "indexDirectory", "c:/indices" ) );
		String[] paths = pathsString.split( ";" );
		
		List<File> directories = new LinkedList<File>();
		for( int i = 0; i < paths.length; i++ )
		{
			String path = ServletUtils.clean( paths[ i ] );
			if( path != null )
			{
				directories.add( new File( path ) );
			}
		}
		
		return directories.toArray( new File[]{} );
	}
	
	public File getCreatedIndicesDirectory ()
		throws IOException
	{
		return getIndicesDirectories()[ 0 ];
	}
	
	
	public void guaranteeFreshDirectories ()
		throws IOException
	{
		loadDirectories();
	}
	
	public void loadDirectories ()
		throws IOException
	{
		directories.clear();
		
		File[] indicesDirectories = getIndicesDirectories();
		for( int i = 0; i < indicesDirectories.length; i++ )
		{
			if( !indicesDirectories[ i ].isDirectory() )
				continue;
			
			File indicesDirectory = indicesDirectories[ i ];
			
			File[] files = indicesDirectory.listFiles();
			for( int j = 0; j < files.length; j++ )
			{
				if( !files[ j ].isDirectory() )
					continue;
				
				File directory = files[ j ];
				
				if( IndexReader.indexExists( directory ) )
				{
					String name = directory.getName();
					if( !directories.containsKey( name ) )
						directories.put( name, directory );
				}
			}
		}
	}
	
	
	public void indexCreated (LuceneIndexEvent e)
	{
		try
		{
			refresh();
		}
		catch(IOException ioe)
		{
		}
	}
	
	public void indexDeleted (LuceneIndexEvent e)
	{
		try
		{
			refresh();
		}
		catch(IOException ioe)
		{
		}
	}
	
	public void indexModified (LuceneIndexEvent e)
	{
	}
	
		
	public void refresh ()
		throws IOException
	{
		loadDirectories();
	}
	
	
}
