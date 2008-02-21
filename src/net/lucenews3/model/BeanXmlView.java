package net.lucenews3.model;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.betwixt.io.BeanWriter;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class BeanXmlView implements View, ViewResolver {

	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		return this;
	}

	@Override
	public String getContentType() {
		return "text/xml";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void render(Map model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Object bean = model.get("bean");
		BeanWriter beanWriter = new BeanWriter(response.getWriter());
		beanWriter.write(bean);
	}

}
