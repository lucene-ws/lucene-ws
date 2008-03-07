package net.lucenews3.model;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SortFieldParserImplTest {

	private transient SortEqualityComparator sortComparator;
	private transient SortFieldEqualityComparator sortFieldComparator;
	private transient SortFieldParserImpl parser;
	
	public boolean isEqual(Sort sort1, Sort sort2) {
		return sortComparator.isEqual(sort1, sort2);
	}
	
	public boolean isEqual(SortField field1, SortField field2) {
		return sortFieldComparator.isEqual(field1, field2);
	}
	
	@Before
	public void setup() {
		sortFieldComparator = new SortFieldEqualityComparator();
		sortComparator = new SortEqualityComparator(sortFieldComparator);
		parser = new SortFieldParserImpl();
	}
	
	@Test(expected=RuntimeException.class)
	public void testNullInput() {
		parser.parse(null);
	}
	
	@Test(expected=RuntimeException.class)
	public void testEmptyInput() {
		parser.parse("");
	}
	
	@Test
	public void testField() {
		Assert.assertTrue(isEqual(new SortField("foo", SortField.STRING), parser.parse("\"foo\"")));
	}
	
	@Test
	public void testScore() {
		Assert.assertTrue(isEqual(SortField.FIELD_SCORE, parser.parse("<score>")));
	}
	
	@Test
	public void testDoc() {
		Assert.assertTrue(isEqual(SortField.FIELD_DOC, parser.parse("<doc>")));
	}
	
	@Test
	public void testCustom() {
		parser.parse("<custom:\"foo\": java.util.Locale>");
	}
	
	@Test
	public void testFieldReversed() {
		Assert.assertTrue(isEqual(new SortField("foo", SortField.STRING, true), parser.parse("\"foo\"!")));
	}
	
	@Test
	public void testScoreReversed() {
		Assert.assertTrue(isEqual(new SortField(null, SortField.SCORE, true), parser.parse("<score>!")));
	}
	
	@Test
	public void testDocReversed() {
		Assert.assertTrue(isEqual(new SortField(null, SortField.DOC, true), parser.parse("<doc>!")));
	}
	
}
