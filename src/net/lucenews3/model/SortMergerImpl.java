package net.lucenews3.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

public class SortMergerImpl implements SortMerger {

	@Override
	public Sort merge(Sort base, Sort delta) {
		final Sort result;
		
		if (delta == null) {
			result = null;
		} else if (base == null) {
			result = delta;
		} else {
			List<SortField> mergedSortFields = new ArrayList<SortField>();
			
			// Establish which sort fields are mentioned in the delta.
			Set<Object> deltaSortFieldKeys = new HashSet<Object>();
			for (SortField deltaSortField : delta.getSort()) {
				deltaSortFieldKeys.add(getKey(deltaSortField));
			}
			
			// Add the base fields which are not mentioned in the delta
			for (SortField baseSortField : base.getSort()) {
				Object baseSortFieldKey = getKey(baseSortField);
				if (!deltaSortFieldKeys.contains(baseSortFieldKey)) {
					mergedSortFields.add(baseSortField);
				}
			}
			
			// Add all the fields mentioned in the delta
			mergedSortFields.addAll(Arrays.asList(delta.getSort()));
			
			result = new Sort(mergedSortFields.toArray(new SortField[]{}));
		}
		
		return result;
	}
	
	/**
	 * Retrieves an object which identifies what the given
	 * sort field actually sorts. If the result is an integer,
	 * the sort field is sorting either by score or by document
	 * id. If the result is a string, the sort field is sorting
	 * by a regular document field of that name.
	 * @param field
	 * @return
	 */
	protected Object getKey(SortField field) {
		Object key;
		
		switch (field.getType()) {
		case SortField.SCORE:
		case SortField.DOC:
			key = field.getType();
			break;
		default:
			key = field.getField();
			break;
		}
		
		return key;
	}

}
