package net.lucenews3.model;

import java.util.Iterator;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;

public class TermDocumentList extends AbstractIteratorList<Document> implements DocumentList {

	private IndexReader indexReader;
	private Term term;
	private int size;
	
	public TermDocumentList(IndexReader indexReader, Term term, int size) {
		this.indexReader = indexReader;
		this.term = term;
		this.size = size;
	}

	@Override
	public int size() {
		return size;
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

	@Override
	public Iterator<Document> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
