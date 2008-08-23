package net.lucenews3;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.springframework.web.servlet.view.AbstractView;

public abstract class XMLStreamView extends AbstractView {

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
