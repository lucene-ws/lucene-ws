package net.lucenews3.model;

import java.util.Iterator;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;

public class TermDocumentListImpl extends AbstractIteratorList<Document> implements DocumentList {

	private IndexReader indexReader;
	
	public TermDocumentListImpl(IndexReader indexReader) {
		this.indexReader = indexReader;
	}
	
	@Override
	public Iterator<Document> iterator() {
		//return new TermDocumentIterator(indexReader);
		return null;
	}

	@Override
	public Map<Term, DocumentList> byTerm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocumentList byTerm(Term term) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocumentList filteredBy(Filter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultList searchBy(Query query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultList searchBy(SearchRequest searchRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
