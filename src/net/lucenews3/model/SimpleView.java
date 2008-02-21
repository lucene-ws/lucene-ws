package net.lucenews3.model;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class SimpleView implements View, ViewResolver {

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void render(Map model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final PrintWriter out = response.getWriter();
		for (Object key : model.keySet()) {
			Object value = model.get(key);
			if (value instanceof Collection && ((Collection) value).size() > 50) {
				out.println(key + ": " + value.getClass());
			} else {
				out.println(key + ": " + value);
			}
		}
		//response.getWriter().println(model);
	}

	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		return this;
	}

}
