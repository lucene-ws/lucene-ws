package net.lucenews3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Searchable;
import org.apache.lucene.search.Searcher;

public class CompositeIndex extends AbstractIndex {

	private Iterable<Index> indexes;

	public CompositeIndex(Iterable<Index> indexes) {
		this.indexes = indexes;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndexReader getReader() throws IOException {
		List<IndexReader> indexReaders = new ArrayList<IndexReader>();
		for (Index index : indexes) {
			indexReaders.add(index.getReader());
		}
		return new MultiReader(indexReaders.toArray(new IndexReader[indexReaders.size()]));
	}

	@Override
	public Searcher getSearcher() throws IOException {
		List<Searchable> searchables = new ArrayList<Searchable>();
		for (Index index : indexes) {
			searchables.add(index.getSearcher());
		}
		return new MultiSearcher(searchables.toArray(new Searchable[searchables.size()]));
	}

	@Override
	public IndexWriter getWriter() throws IOException {
		throw new UnsupportedOperationException("Cannot write to a composite index");
	}

}
