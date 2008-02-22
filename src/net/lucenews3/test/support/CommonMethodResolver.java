package net.lucenews3.test.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommonMethodResolver {

	public static void main(String... arguments) throws Exception {
		Class<?> a = HttpServletRequest.class;
		Class<?> b = HttpServletResponse.class;
		ListUtility lists = new ListUtility();
		System.out.println(lists.intersection(getMethodNames(a), getMethodNames(b)));
	}
	
	public static List<String> getMethodNames(Class<?> c) {
		final List<String> results = new ArrayList<String>();
		for (Method method : c.getMethods()) {
			results.add(method.getName());
		}
		return results;
	}
	
}
