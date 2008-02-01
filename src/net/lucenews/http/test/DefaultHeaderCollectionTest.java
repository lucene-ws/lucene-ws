package net.lucenews.http.test;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.lucenews.http.DefaultHeader;
import net.lucenews.http.DefaultHeaderCollection;
import net.lucenews.http.Header;
import net.lucenews.http.KeyValue;
import net.lucenews.http.KeyValueMap;
import net.lucenews.http.ValueCollection;

public class DefaultHeaderCollectionTest {

	private DefaultHeaderCollection headers;
	
	@Before
	public void setup() {
		headers = new DefaultHeaderCollection();
	}
	
	@Test
	public void testSingleAdd() {
		Header header = new DefaultHeader("foo", "bar");
		Assert.assertEquals("empty headers before add", 0, headers.size());
		headers.add(header);
		Assert.assertEquals("single header after add", 1, headers.size());
		Assert.assertEquals("same header", header, headers.iterator().next());
		KeyValueMap<String, String> headersByName = headers.byKey();
		Assert.assertNotNull("headers by name is not null", headersByName);
		Assert.assertEquals("single key in by-key mapping", 1, headersByName.size());
		ValueCollection<String> values = headersByName.get(header.getKey());
		Assert.assertNotNull(values);
		Assert.assertEquals("single value in by-key mapping", 1, values.size());
	}
	
	@Test
	public void testMultipleValueAdd() {
		Header header1 = new DefaultHeader("foo", "bar");
		Header header2 = new DefaultHeader("foo", "baz");
		Assert.assertEquals("empty headers before add", 0, headers.size());
		headers.add(header1);
		headers.add(header2);
		Assert.assertEquals("single header after add", 2, headers.size());
		Iterator<KeyValue<String, String>> i = headers.iterator();
		Assert.assertEquals("same header1", header1, i.next());
		Assert.assertEquals("same header2", header2, i.next());
		KeyValueMap<String, String> headersByName = headers.byKey();
		Assert.assertNotNull("headers by name is not null", headersByName);
		Assert.assertEquals("single key in by-key mapping", 1, headersByName.size());
		ValueCollection<String> values = headersByName.get(header1.getKey());
		Assert.assertNotNull(values);
		Assert.assertEquals("two values in by-key mapping", 2, values.size());
	}
	
}
