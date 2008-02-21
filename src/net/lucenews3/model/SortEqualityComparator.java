package net.lucenews3.model;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

public class SortEqualityComparator implements EqualityComparator<Sort> {

	private SortFieldEqualityComparator sortFieldComparator;
	
	public SortEqualityComparator() {
		this(new SortFieldEqualityComparator());
	}
	
	public SortEqualityComparator(SortFieldEqualityComparator sortFieldComparator) {
		this.sortFieldComparator = sortFieldComparator;
	}
	
	@Override
	public boolean isEqual(Sort sort1, Sort sort2) {
		boolean result;
		
		if (sort1 == null) {
			return (sort2 == null);
		}
		
		SortField[] fields1 = sort1.getSort();
		SortField[] fields2 = sort2.getSort();
		
		if (fields1.length == fields2.length) {
			int length = Math.min(fields1.length, fields2.length);
			result = true;
			for (int i = 0; i < length; i++) {
				SortField field1 = fields1[i];
				SortField field2 = fields2[i];
				if (!sortFieldComparator.isEqual(field1, field2)) {
					result = false;
					break;
				}
			}
		} else {
			result = false;
		}
		
		return result;
	}

}
