package net.lucenews3.model;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;

public interface FilterTermQuery extends FilterQuery {

	public Term getTerm();
	
	@Override
	public TermQuery getTarget();
	
}
