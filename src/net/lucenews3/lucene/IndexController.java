package net.lucenews3.lucene;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class IndexController implements Controller {

	@Override
	public ModelAndView handleRequest(HttpServletRequest requesst,
			HttpServletResponse response) throws Exception {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element root = document.createElement("index");
		return new ModelAndView(new XmlView(), "xml", root);
	}

}
