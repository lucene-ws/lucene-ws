package net.lucenews3.lucene.support;

import org.apache.lucene.search.SortField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SortFieldEqualityComparatorTest {
	
	private SortFieldEqualityComparator comparator;
	
	@Before
	public void setup() {
		comparator = new SortFieldEqualityComparator();
	}
	
	@Test
	public void testScoreFieldEquality() {
		SortField field1 = new SortField(null, SortField.SCORE);
		SortField field2 = new SortField(null, SortField.SCORE);
		Assert.assertTrue(comparator.isEqual(field1, field2));
	}
	
	@Test
	public void testScoreFieldInequality() {
		boolean reversed = true;
		SortField field1 = new SortField(null, SortField.SCORE, reversed);
		SortField field2 = new SortField(null, SortField.SCORE, !reversed);
		Assert.assertFalse(comparator.isEqual(field1, field2));
	}
	
	@Test
	public void testDocFieldEquality() {
		SortField field1 = new SortField(null, SortField.DOC);
		SortField field2 = new SortField(null, SortField.DOC);
		Assert.assertTrue(comparator.isEqual(field1, field2));
	}
	
	@Test
	public void testDocFieldInequality() {
		boolean reversed = true;
		SortField field1 = new SortField(null, SortField.DOC, reversed);
		SortField field2 = new SortField(null, SortField.DOC, !reversed);
		Assert.assertFalse(comparator.isEqual(field1, field2));
	}

}
