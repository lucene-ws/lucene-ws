package net.lucenews3.model;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;
import net.lucenews3.Transformer;

import org.w3c.dom.Element;

public class SimpleElementTransformer<I> implements Transformer<I, Element> {

	private org.w3c.dom.Document document;
	private ExceptionTranslator exceptionTranslator;
	private Parser<I, String> tagNameParser;
	
	public SimpleElementTransformer() {
		this.exceptionTranslator = new ExceptionTranslatorImpl();
	}
	
	public SimpleElementTransformer(Parser<I, String> tagNameParser) {
		this();
		this.tagNameParser = tagNameParser;
	}
	
	public SimpleElementTransformer(String tagName) {
		this();
		this.tagNameParser = new ConstantTagNameParser<I>(tagName);
	}
	
	@Override
	public Element transform(I input) {
		if (document == null) {
			try {
				document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			} catch (ParserConfigurationException e) {
				throw exceptionTranslator.translate(e);
			}
		}
		
		final String tagName = tagNameParser.parse(input);
		final Element output = document.createElement(tagName);
		transform(input, output);
		return output;
	}

	@Override
	public void transform(I input, Element output) {
		if (input == null) {
			// No processing required
		} else {
			final org.w3c.dom.Document document = output.getOwnerDocument();
			final org.w3c.dom.Text text = document.createTextNode(String.valueOf(input));
			output.appendChild(text);
		}
	}

	public org.w3c.dom.Document getDocument() {
		return document;
	}

	public void setDocument(org.w3c.dom.Document document) {
		this.document = document;
	}

	public ExceptionTranslator getExceptionTranslator() {
		return exceptionTranslator;
	}

	public void setExceptionTranslator(ExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}

	public Parser<I, String> getTagNameParser() {
		return tagNameParser;
	}

	public void setTagNameParser(Parser<I, String> tagNameParser) {
		this.tagNameParser = tagNameParser;
	}

}
