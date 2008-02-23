package net.lucenews3.model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class Dom4jView implements View, ViewResolver {

	@Override
	public String getContentType() {
		return "text/xml";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void render(Map model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Node> nodesByName = new HashMap<String, Node>();
		for (Object key : model.keySet()) {
			String name = key.toString();
			Object value = model.get(key);
			if (value instanceof Node) {
				nodesByName.put(name, (Node) value);
			}
		}

		if (nodesByName.isEmpty()) {
			response.getWriter().println("No nodes");
		} else {
			Node node = nodesByName.values().iterator().next();
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(response.getWriter(), format);
			writer.write(node);
			writer.close();
		}
	}

	@Override
	public View resolveViewName(String viewName, Locale locale)
			throws Exception {
		return this;
	}

}
