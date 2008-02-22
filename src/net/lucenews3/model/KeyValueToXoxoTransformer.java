package net.lucenews3.model;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;
import net.lucenews3.KeyValue;
import net.lucenews3.KeyValueList;
import net.lucenews3.Transformer;

import org.w3c.dom.Element;

/**
 * Transforms a collection of key/value pairs into an XOXO &lt;dl&gt; element.
 *
 * @param <K>
 * @param <V>
 */
public class KeyValueToXoxoTransformer<K, V> implements Transformer<KeyValueList<K, V>, Element> {

	private static final String DEFAULT_ROOT_TAG_NAME = "dl";
	private static final String DEFAULT_KEY_TAG_NAME = "dt";
	private static final String DEFAULT_VALUE_TAG_NAME = "dd";
	
	private String rootTagName;
	private Transformer<K, Element> keyTransformer;
	private String keyTagName;
	private Transformer<V, Element> valueTransformer;
	private String valueTagName;
	private org.w3c.dom.Document document;
	private ExceptionTranslator exceptionTranslator;
	
	public KeyValueToXoxoTransformer() {
		this.rootTagName = DEFAULT_ROOT_TAG_NAME;
		this.exceptionTranslator = new ExceptionTranslatorImpl();
		this.keyTagName = DEFAULT_KEY_TAG_NAME;
		this.keyTransformer = new SimpleElementTransformer<K>(keyTagName);
		this.valueTagName = DEFAULT_VALUE_TAG_NAME;
		this.valueTransformer = new SimpleElementTransformer<V>(valueTagName);
	}
	
	public KeyValueToXoxoTransformer(Transformer<K, Element> keyTransformer, Transformer<V, Element> valueTransformer) {
		this();
		this.keyTransformer = keyTransformer;
		this.valueTransformer = valueTransformer;
	}
	
	protected org.w3c.dom.Document buildDocument() {
		org.w3c.dom.Document result;
		
		try {
			result = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			throw exceptionTranslator.translate(e);
		}
		
		return result;
	}
	
	protected Element buildElement(String tagName) {
		if (document == null) {
			document = buildDocument();
		}
		
		return document.createElement(tagName);
	}
	
	@Override
	public Element transform(KeyValueList<K, V> input) {
		final Element element = buildElement(rootTagName);
		transform(input, element);
		return element;
	}

	@Override
	public void transform(KeyValueList<K, V> input, Element output) {
		for (KeyValue<K, V> keyValue : input) {
			final K key = keyValue.getKey();
			final Element keyElement = buildElement(key, keyTransformer, keyTagName);
			output.appendChild(keyElement);
			
			final V value = keyValue.getValue();
			final Element valueElement = buildElement(value, valueTransformer, valueTagName);
			output.appendChild(valueElement);
		}
	}
	
	protected <T> Element buildElement(T object, Transformer<T, Element> transformer, String tagName) {
		Element element;
		
		try {
			element = transformer.transform(object);
		} catch (UnsupportedOperationException e) {
			element = buildElement(tagName);
			transformer.transform(object, element);
		}
		
		return element;
	}

	public String getRootTagName() {
		return rootTagName;
	}

	public void setRootTagName(String rootTagName) {
		this.rootTagName = rootTagName;
	}

	public Transformer<K, Element> getKeyTransformer() {
		return keyTransformer;
	}

	public void setKeyTransformer(Transformer<K, Element> keyTransformer) {
		this.keyTransformer = keyTransformer;
	}

	public String getKeyTagName() {
		return keyTagName;
	}

	public void setKeyTagName(String keyTagName) {
		this.keyTagName = keyTagName;
	}

	public Transformer<V, Element> getValueTransformer() {
		return valueTransformer;
	}

	public void setValueTransformer(Transformer<V, Element> valueTransformer) {
		this.valueTransformer = valueTransformer;
	}

	public String getValueTagName() {
		return valueTagName;
	}

	public void setValueTagName(String valueTagName) {
		this.valueTagName = valueTagName;
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

}
