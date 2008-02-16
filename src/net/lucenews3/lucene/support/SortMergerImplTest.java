package net.lucenews3.lucene.support;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SortMergerImplTest {

	private SortEqualityComparator sortComparator;
	private SortFieldEqualityComparator sortFieldComparator;
	private SortMergerImpl merger;
	
	@Before
	public void setup() {
		merger = new SortMergerImpl();
		sortFieldComparator = new SortFieldEqualityComparator();
		sortComparator = new SortEqualityComparator(sortFieldComparator);
	}
	
	public boolean isEqual(Sort sort1, Sort sort2) {
		return sortComparator.isEqual(sort1, sort2);
	}
	
	public boolean isEqual(SortField field1, SortField field2) {
		return sortFieldComparator.isEqual(field1, field2);
	}
	
	@Test
	public void testStaticScoreKey() {
		SortField field = SortField.FIELD_SCORE;
		Assert.assertEquals(SortField.SCORE, merger.getKey(field));
	}
	
	@Test
	public void testConstructedScoreKey() {
		SortField field = new SortField(null, SortField.SCORE);
		Assert.assertEquals(SortField.SCORE, merger.getKey(field));
	}
	
	@Test
	public void testStaticDocKey() {
		SortField field = SortField.FIELD_DOC;
		Assert.assertEquals(SortField.DOC, merger.getKey(field));
	}
	
	@Test
	public void testConstructedDocKey() {
		SortField field = new SortField(null, SortField.DOC);
		Assert.assertEquals(SortField.DOC, merger.getKey(field));
	}
	
	@Test
	public void testNullBaseMerge() {
		Sort base = null;
		Sort delta = new Sort(new SortField("foo"));
		Sort result = merger.merge(base, delta);
		Assert.assertTrue(isEqual(delta, result));
	}
	
	@Test
	public void testNullDeltaMerge() {
		Sort base = new Sort(new SortField("foo"));
		Sort delta = null;
		Sort result = merger.merge(base, delta);
		Assert.assertTrue(isEqual(null, result));
	}
	
	@Test
	public void testEmptyBaseMerge() {
		Sort base = new Sort();
		Sort delta = new Sort(new SortField("foo"));
		Sort result = merger.merge(base, delta);
		Assert.assertTrue(isEqual(new Sort(new SortField[]{ SortField.FIELD_SCORE, SortField.FIELD_DOC, new SortField("foo") }), result));
	}
	
}
