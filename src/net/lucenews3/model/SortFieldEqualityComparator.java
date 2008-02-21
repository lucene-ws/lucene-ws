package net.lucenews3.model;

import org.apache.lucene.search.SortField;

public class SortFieldEqualityComparator implements EqualityComparator<SortField> {

	@Override
	public boolean isEqual(SortField field1, SortField field2) {
		boolean result;
		
		if (field1 == null) {
			result = (field2 == null);
		} else {
			int type1 = field1.getType();
			int type2 = field2.getType();
			
			if (type1 != type2) {
				return false;
			}
			
			int type = type1 | type2;
				
			if (type == SortField.SCORE || type == SortField.DOC) {
				// These do not depend on field name
			} else {
				String fieldName1 = field1.getField();
				String fieldName2 = field2.getField();
				if (!fieldName1.equals(fieldName2)) {
					return false;
				}
			}
			
			boolean reverse1 = field1.getReverse();
			boolean reverse2 = field2.getReverse();
			
			if (reverse1 != reverse2) {
				return false;
			}
			
			return true;
		}
		
		return result;
	}
	
}
