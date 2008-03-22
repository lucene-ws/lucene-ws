package net.lucenews3.model;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import net.lucenews3.Side;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class Dom4jView implements View, ViewResolver {

	private Side transformLocation;
	
	public Dom4jView() {
		this.transformLocation = Side.CLIENT;
	}
	
	public Side getTransformLocation() {
		return transformLocation;
	}

	public void setTransformLocation(Side transformLocation) {
		this.transformLocation = transformLocation;
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
			
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(response.getWriter(), format);
			
			if (node instanceof Document) {
				Document document = (Document) node;
				ProcessingInstruction stylesheet = document.processingInstruction("xml-stylesheet");
				if (stylesheet == null) {
					// Nothing to see here, move along
				} else {
					if (transformLocation == Side.SERVER) {
						DocumentSource source = new DocumentSource(document);
						DocumentResult result = new DocumentResult();
						
						URL url = new URL(stylesheet.getValue("href"));
						InputStream inputStream = url.openStream();
						Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(inputStream));
						transformer.transform(source, result);
						inputStream.close();
						writer.write(result.getDocument());
						writer.close();
						return;
					}
				}
			}
			
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
