package net.lucenews3.lucene.support;

import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;

public class BufferedListIteratorTest {

	@Test
	public void testSupport() {
		Iterator<Integer> i1 = toIterator();
		Assert.assertFalse(i1.hasNext());
		
		Iterator<Integer> i2 = toIterator(1);
		Assert.assertTrue(i2.hasNext());
	}
	
	@Test
	public void testEmptyHasPrevious() {
		ListIterator<Integer> i = toBufferedListIterator();
		Assert.assertFalse("does not have previous value", i.hasPrevious());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testEmptyPrevious() {
		ListIterator<Integer> i = toBufferedListIterator();
		i.previous();
	}
	
	public void testEmptyPreviousIndex() {
		ListIterator<Integer> i = toBufferedListIterator();
		Assert.assertEquals(-1, i.previousIndex());
	}

	@Test
	public void testEmptyHasNext() {
		ListIterator<Integer> i = toBufferedListIterator();
		Assert.assertFalse("does not have next value", i.hasNext());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testEmptyNext() {
		ListIterator<Integer> i = toBufferedListIterator();
		i.next();
	}
	
	@Test
	public void testEmptyNextIndex() {
		ListIterator<Integer> i = toBufferedListIterator();
		Assert.assertEquals(0, i.nextIndex());
	}

	@Test
	public void testSingleHasPrevious() {
		ListIterator<Integer> i = toBufferedListIterator(1);
		Assert.assertFalse("does not have previous value", i.hasPrevious());
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testSinglePrevious() {
		ListIterator<Integer> i = toBufferedListIterator(1);
		i.previous();
	}

	@Test
	public void testSingleHasNext() {
		ListIterator<Integer> i = toBufferedListIterator(1);
		Assert.assertTrue("does have next value", i.hasNext());
	}
	
	public void testSingleNext() {
		ListIterator<Integer> i = toBufferedListIterator(1);
		Assert.assertEquals(1, i.next());
	}
	
	@Test
	public void testTripleHasNext() {
		ListIterator<Integer> i = toBufferedListIterator(1, 2, 3);
		
		Assert.assertTrue(i.hasNext());
		Assert.assertEquals(1, i.next());
		
		Assert.assertTrue(i.hasNext());
		Assert.assertEquals(2, i.next());
		
		Assert.assertTrue(i.hasNext());
		Assert.assertEquals(3, i.next());
		
		Assert.assertFalse(i.hasNext());
	}
	
	@Test
	public void testSporaticTraversal() {
		ListIterator<Integer> i = toBufferedListIterator(1, 2, 3, 4, 5);
		
		Assert.assertTrue(i.hasNext());
		Assert.assertEquals(1, i.next());
		
		Assert.assertTrue(i.hasNext());
		Assert.assertEquals(2, i.next());
		
		Assert.assertTrue(i.hasPrevious());
		Assert.assertEquals(1, i.previous());
		
		Assert.assertTrue(i.hasNext());
		Assert.assertEquals(2, i.next());
		
		Assert.assertEquals(2, i.nextIndex());
		
		Assert.assertTrue(i.hasNext());
		Assert.assertEquals(3, i.next());
		
		Assert.assertTrue(i.hasNext());
		Assert.assertEquals(4, i.next());
		
		Assert.assertTrue(i.hasNext());
		Assert.assertEquals(5, i.next());
		
		Assert.assertFalse(i.hasNext());
		Assert.assertEquals(5, i.nextIndex());
	}
	
	public <T> Iterator<T> toIterator(T... values) {
		return Arrays.asList(values).iterator();
	}
	
	public <T> BufferedListIterator<T> toBufferedListIterator(T... values) {
		return new BufferedListIterator<T>(toIterator(values));
	}
	
}
