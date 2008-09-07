package net.lucenews3.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import net.lucenews3.PrettyXMLStreamWriter;
import net.lucenews3.Service;

import org.springframework.web.servlet.view.AbstractView;

/**
 * Support class for any view wishing to write XML data to the response via
 * the Java streaming XML API.
 * 
 */
public abstract class XMLStreamView extends AbstractView {

	private Service service;
	private XMLOutputFactory outputFactory;

	public XMLStreamView() {
		this(XMLOutputFactory.newInstance());
	}

	public XMLStreamView(XMLOutputFactory outputFactory) {
		this.outputFactory = outputFactory;
	}

	public XMLOutputFactory getOutputFactory() {
		return outputFactory;
	}

	public void setOutputFactory(XMLOutputFactory outputFactory) {
		this.outputFactory = outputFactory;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		XMLStreamWriter writer = new PrettyXMLStreamWriter(outputFactory.createXMLStreamWriter(response.getWriter()));
		try {
			renderMergedOutputModel(model, request, response, writer);
			writer.flush();
		} finally {
			writer.close();
		}
	}

	@SuppressWarnings("unchecked")
	protected abstract void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response, XMLStreamWriter writer) throws Exception;

}
