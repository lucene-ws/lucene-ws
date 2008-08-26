package net.lucenews3.controller;

import static javax.xml.stream.XMLStreamReader.*;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import net.lucenews3.ControllerSupport;
import net.lucenews3.XMLStreamUtility;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

public class ServicePostController extends ControllerSupport {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Creating an index");
		
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLStreamReader xml = xmlInputFactory.createXMLStreamReader(request.getReader());

		String indexName = null;
		if (xml.nextTag() == START_ELEMENT) {
			QName name = xml.getName();
			if ("entry".equals(name.getLocalPart())) {
				while (xml.nextTag() == START_ELEMENT) {
					QName tagName = xml.getName();
					if ("title".equals(tagName.getLocalPart())) {
						indexName = xml.getElementText();
						break;
					} else {
						XMLStreamUtility.endElement(xml);
					}
				}
			}
		}
		
		final String nname = indexName;
		
		System.out.println("Creating index \"" + indexName + "\"");
		return new ModelAndView(new View() {

			@Override
			public String getContentType() {
				// TODO Auto-generated method stub
				return "text/plain";
			}

			@Override
			public void render(Map arg0, HttpServletRequest arg1,
					HttpServletResponse arg2) throws Exception {
				arg2.getWriter().write("created " + nname + "!");
			}});
	}

}
