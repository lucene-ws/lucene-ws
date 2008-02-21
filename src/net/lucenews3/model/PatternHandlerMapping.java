package net.lucenews3.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

public class PatternHandlerMapping implements HandlerMapping {

	private Map<Pattern, Object> mappings;
	
	public PatternHandlerMapping() {
		System.out.println("constructed");
	}
	
	public void setMappings(Properties mappings) {
		this.mappings = new LinkedHashMap<Pattern, Object>();
		for (Entry<Object, Object> mapping : mappings.entrySet()) {
			final Pattern pattern = Pattern.compile(mapping.getKey().toString());
			final Object handler = mapping.getValue();
			this.mappings.put(pattern, handler);
		}
	}

	public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
		HandlerExecutionChain result = null;
		String contextPath = request.getContextPath();
		if (contextPath.equals("")) {
			contextPath = "/";
		}
		
		for (Entry<Pattern, Object> mapping : mappings.entrySet()) {
			final Pattern pattern = mapping.getKey();
			final Object handler = mapping.getValue();
			
			String text = request.getMethod() + " " + contextPath;
			
			final Matcher matcher = pattern.matcher(text);
			if (matcher.matches()) {
				int groupCount = matcher.groupCount();
				for (int i = 1; i < groupCount; i++) {
				}
				result = new HandlerExecutionChain(handler);
			}
		}
		return result;
	}
	
}
