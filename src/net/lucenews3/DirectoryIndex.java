package net.lucenews3;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;

public class DirectoryIndex extends AbstractIndex {

	private Directory directory;

	public DirectoryIndex(Directory directory) {
		this.directory = directory;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndexReader getReader() throws IOException {
		return IndexReader.open(directory);
	}

	@Override
	public Searcher getSearcher() throws IOException {
		return new IndexSearcher(directory);
	}

	@Override
	public IndexWriter getWriter() throws IOException {
		return new IndexWriter(directory, getAnalyzer());
	}

}
