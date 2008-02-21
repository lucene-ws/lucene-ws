package net.lucenews3.model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.w3c.dom.Node;

public class DomView implements View, ViewResolver {

	private Transformer transformer;
	private ExceptionTranslator exceptionTranslator;
	
	public DomView() {
		this.exceptionTranslator = new ExceptionTranslatorImpl();
		try {
			this.transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException e) {
			throw exceptionTranslator.translate(e);
		} catch (TransformerFactoryConfigurationError e) {
			throw exceptionTranslator.translate(e);
		}
	}
	
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
			transformer.transform(new DOMSource(node), new StreamResult(response.getWriter()));
		}
	}

	public Transformer getTransformer() {
		return transformer;
	}

	public void setTransformer(Transformer transformer) {
		this.transformer = transformer;
	}

	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		return this;
	}

}
