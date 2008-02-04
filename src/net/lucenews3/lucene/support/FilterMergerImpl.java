package net.lucenews3.lucene.support;

import org.apache.lucene.misc.ChainedFilter;
import org.apache.lucene.search.Filter;

public class FilterMergerImpl implements FilterMerger {

	public Filter mergeFilters(Filter base, Filter delta) {
		Filter result;
		if (delta == null) {
			result = null;
		} else if (base == null) {
			result = delta;
		} else {
			result = new ChainedFilter(new Filter[]{ base, delta }, ChainedFilter.AND);
		}
		return result;
	}

}
