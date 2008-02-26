package net.lucenews3.http;

import net.lucenews3.KeyValue;
import net.lucenews3.KeyValueList;

import org.junit.Assert;
import org.junit.Test;

public class UrlImplTest {

	@Test
	public void testSingleParameter() throws Exception {
		Url url = new UrlImpl("http://localhost/foo?name=adam");
		
		KeyValueList<String, String> parameters = url.getParameters();
		Assert.assertEquals(1, parameters.size());
		
		KeyValue<String, String> parameter = parameters.get(0);
		Assert.assertEquals("name", parameter.getKey());
		Assert.assertEquals("adam", parameter.getValue());
	}
	
	@Test
	public void testSomething() throws Exception {
		Url url = new UrlImpl("http://localhost/foo?name=adam&page=3");
		
		KeyValueList<String, String> parameters = url.getParameters();
		Assert.assertEquals(2, parameters.size());
		
		String originalValue = parameters.byKey().get("page").get(0);
		Assert.assertEquals("3", originalValue);
		
		parameters.byKey().put("page", "4");
		
		Assert.assertEquals(1, parameters.byKey().get("page").size());
		
		String modifiedValue = parameters.byKey().get("page").get(0);
		Assert.assertEquals("4", modifiedValue);
	}
	
}
