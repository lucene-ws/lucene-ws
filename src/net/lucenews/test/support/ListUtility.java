package net.lucenews.test.support;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

public class ListUtility {

	public <E> List<E> filter(List<E> list, int mask) {
		BitSet bits = new BitSet();
		final int size = list.size();
		for (int i = 0; i < size; i++) {
			if ((mask & (1 << i)) != 0) {
				bits.set(i, true);
			}
		}
		return filter(list, bits);
	}
	
	public <E> List<E> filter(List<E> list, BitSet mask) {
		List<E> result = new ArrayList<E>();
		final int size = list.size();
		for (int i = 0; i < size; i++) {
			if (mask.get(i)) {
				result.add(list.get(i));
			}
		}
		return result;
	}
	
	public <E> Collection<List<E>> buildSublists(List<E> list) {
		Collection<List<E>> results = new ArrayList<List<E>>();
		
		final int size = list.size();
		final int maximumMask = 1 << size;
		for (int mask = 0; mask < maximumMask; mask++) {
			List<E> sublist = new ArrayList<E>();
			for (int i = 0; i < size; i++) {
				if ((mask & (1 << i)) != 0) {
					sublist.add(list.get(i));
				}
			}
			results.add(sublist);
		}
		
		return results;
	}
	
}
