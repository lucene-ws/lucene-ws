package net.lucenews3.lucene.support;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

public class RestHandlerMapping implements HandlerMapping {

	public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		return null;
	}
	
}
