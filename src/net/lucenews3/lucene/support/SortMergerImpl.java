package net.lucenews3.lucene.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

public class SortMergerImpl implements SortMerger {

	public Sort mergeSorts(Sort base, Sort delta) {
		Sort result;
		if (delta == null) {
			result = null;
		} else if (base == null) {
			result = delta;
		} else {
			// TODO: Try to straighten out those complex sort fields!
			SortField[] baseFields = base.getSort();
			List<Object> baseKeys = new ArrayList<Object>(baseFields.length);
			for (SortField baseField : baseFields) {
				Object baseKey;
				switch (baseField.getType()) {
				case SortField.SCORE:
				case SortField.DOC:
					baseKey = baseField.getType();
					break;
				default:
					baseKey = baseField.getField();
					break;
				}
				baseKeys.add(baseKey);
			}
			
			result = null;
		}
		return result;
	}

}
