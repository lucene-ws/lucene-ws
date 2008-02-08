package net.lucenews3.lucene.support;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

public class RestHandlerMapping implements HandlerMapping {

	public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		final Deque<String> paths = getPaths(request);
		
		// TODO Auto-generated method stub
		return null;
	}
	
	public Deque<String> getPaths(HttpServletRequest request) throws Exception {
		final Deque<String> result = new ArrayDeque<String>();
		final String contextPath = request.getContextPath();
		if (!contextPath.equals("")) {
			result.addAll(Arrays.asList(contextPath.split("/")));
		}
		return result;
	}
	
}
