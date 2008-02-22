package net.lucenews3.http.test;

import java.util.Iterator;
import java.util.List;

import net.lucenews3.http.Header;
import net.lucenews3.http.HeaderImpl;
import net.lucenews3.http.HeaderListImpl;
import net.lucenews3.http.KeyValue;
import net.lucenews3.http.KeyValueMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HeaderListImplTest {

	private HeaderListImpl headers;
	
	@Before
	public void setup() {
		headers = new HeaderListImpl();
	}
	
	@Test
	public void testSingleAdd() {
		Header header = new HeaderImpl("foo", "bar");
		Assert.assertEquals("empty headers before add", 0, headers.size());
		headers.add(header);
		Assert.assertEquals("single header after add", 1, headers.size());
		Assert.assertEquals("same header", header, headers.iterator().next());
		KeyValueMap<String, String> headersByName = headers.byKey();
		Assert.assertNotNull("headers by name is not null", headersByName);
		Assert.assertEquals("single key in by-key mapping", 1, headersByName.size());
		List<String> values = headersByName.get(header.getKey());
		Assert.assertNotNull(values);
		Assert.assertEquals("single value in by-key mapping", 1, values.size());
	}
	
	@Test
	public void testMultipleValueAdd() {
		Header header1 = new HeaderImpl("foo", "bar");
		Header header2 = new HeaderImpl("foo", "baz");
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
		List<String> values = headersByName.get(header1.getKey());
		Assert.assertNotNull(values);
		Assert.assertEquals("two values in by-key mapping", 2, values.size());
	}
	
}
