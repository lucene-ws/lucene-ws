package net.lucenews3.lucene.support;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
			Map<Object, SortField> baseFieldsByKey = new LinkedHashMap<Object, SortField>();
			for (SortField baseField : baseFields) {
				final Object baseFieldKey = getSortFieldKey(baseField); 
				baseKeys.add(baseFieldKey);
				baseFieldsByKey.put(baseFieldKey, baseField);
			}
			
			List<SortField> mergedFields = new ArrayList<SortField>();
			mergedFields.addAll(baseFieldsByKey.values());
			
			SortField[] deltaFields = delta.getSort();
			for (SortField deltaField : deltaFields) {
				Object deltaFieldKey = getSortFieldKey(deltaField);
				int baseFieldIndex = baseKeys.indexOf(deltaFieldKey);
			}
			
			result = null;
		}
		return result;
	}
	
	protected Object getSortFieldKey(SortField field) {
		Object result;
		switch (field.getType()) {
		case SortField.SCORE:
		case SortField.DOC:
			result = field.getType();
			break;
		default:
			result = field.getField();
			break;
		}
		return result;
	}

}
