package net.lucenews3.lucene;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.web.servlet.view.AbstractView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlView extends AbstractView {

	private Transformer transformer;
	
	public XmlView() throws Exception {
		transformer = TransformerFactory.newInstance().newTransformer();
		this.setContentType("text/xml");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Document doc = (Document) model.get("xml");
		Element root = doc.getDocumentElement();
		Map<String, String> paths = (Map<String, String>) request.getAttribute("ParameterizedUrlHandlerMapping.path-parameters");
		Element dl = doc.createElement("dl");
		root.appendChild(dl);
		for (String key : paths.keySet()) {
			Element dt = doc.createElement("dt");
			dt.appendChild(doc.createTextNode(key));
			dl.appendChild(dt);
			Element dd = doc.createElement("dd");
			dd.appendChild(doc.createTextNode(paths.get(key)));
			dl.appendChild(dd);
		}
		transformer.transform(new DOMSource((Node) model.get("xml")), new StreamResult(response.getOutputStream()));
	}

}
