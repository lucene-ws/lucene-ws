package net.lucenews3;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class MappedViewResolver implements ViewResolver {

	private Map<String, View> viewsByName;

	public MappedViewResolver() {
		this(new HashMap<String, View>());
	}

	public MappedViewResolver(Map<String, View> viewsByName) {
		this.viewsByName = viewsByName;
	}

	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		if (viewsByName.containsKey(viewName)) {
			return viewsByName.get(viewName);
		} else {
			return null;
		}
	}

	public Map<String, View> getViewsByName() {
		return viewsByName;
	}

	public void setViewsByName(Map<String, View> viewsByName) {
		this.viewsByName = viewsByName;
	}

}
