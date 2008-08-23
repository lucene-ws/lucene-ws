package net.lucenews3;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class DefaultViewResolver implements ViewResolver {

	private APPIntrospectionView introspectionView;

	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		// TODO Auto-generated method stub
		if (viewName.equals("service/view")) {
			return introspectionView;
		}
		return null;
	}

}
