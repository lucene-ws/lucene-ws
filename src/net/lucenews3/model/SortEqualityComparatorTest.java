package net.lucenews3.model;

import org.apache.lucene.search.Sort;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SortEqualityComparatorTest {
	
	private SortEqualityComparator comparator;
	
	@Before
	public void setup() {
		comparator = new SortEqualityComparator();
	}
	
	@Test
	public void testEmptySortEquality() {
		Sort sort1 = new Sort();
		Sort sort2 = new Sort();
		Assert.assertTrue(comparator.isEqual(sort1, sort2));
	}

}
