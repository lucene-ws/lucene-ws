package net.lucenews3.lucene.support;

import java.util.List;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;

public interface ResultList extends List<Result> {

	public ResultList where(Query criteria);
	
	public ResultList where(String criteria);
	
	public ResultList filteredBy(Filter filter);
	
	public ResultList sortedBy(Sort sort);
	
	@Override
	public List<Result> subList(int fromIndex, int toIndex);
	
}
