package net.lucenews3.http;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.Weight;

public class ReclaimableSearcher extends Searcher {

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Document doc(int arg0) throws CorruptIndexException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int docFreq(Term arg0) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Explanation explain(Weight arg0, int arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int maxDoc() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Query rewrite(Query arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void search(Weight arg0, Filter arg1, HitCollector arg2)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TopDocs search(Weight arg0, Filter arg1, int arg2)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TopFieldDocs search(Weight arg0, Filter arg1, int arg2, Sort arg3)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document doc(int arg0, FieldSelector arg1)
			throws CorruptIndexException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
