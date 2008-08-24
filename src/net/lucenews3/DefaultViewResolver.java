package net.lucenews3;

import java.util.Locale;
import java.util.Map;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class DefaultViewResolver implements ViewResolver {

	private Map<String, View> viewsByName;

	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		if (viewsByName.containsKey(viewName)) {
			return viewsByName.get(viewName);
		} else {
			return null;
		}
	}

	public synchronized Map<String, View> getViewsByName() {
		return viewsByName;
	}

	public synchronized void setViewsByName(Map<String, View> viewsByName) {
		this.viewsByName = viewsByName;
	}

}
