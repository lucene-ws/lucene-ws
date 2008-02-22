package net.lucenews3.test.support;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListUtility {

	public <E> Set<E> union(Collection<E> a, Collection<E> b) {
		final Set<E> results = new HashSet<E>();
		
		results.addAll(a);
		results.addAll(b);
		
		return results;
	}
	
	public <E> Set<E> intersection(Collection<E> a, Collection<E> b) {
		final Set<E> results = new HashSet<E>();
		
		final Set<E> aSet = new HashSet<E>();
		aSet.addAll(a);
		
		final Set<E> bSet = new HashSet<E>();
		bSet.addAll(b);
		
		for (E element : a) {
			if (b.contains(element)) {
				results.add(element);
			}
		}
		
		return results;
	}
	
	public <E> E only(List<E> list) {
		final int size = list.size();
		switch (size) {
		case 0:
			throw new RuntimeException();
		case 1:
			return list.get(0);
		default:
			throw new RuntimeException();
		}
	}
	
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
