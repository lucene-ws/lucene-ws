package net.lucenews3;

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import static net.lucenews3.XMLStreamUtility.endElement;

import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.NoSuchElementException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;

public class XMLDocumentIterator implements DocumentIterator {

	private boolean initialized;
	private boolean isCollection;
	private DateFormat dateFormat;
	private XMLStreamReader xml;
	
	private String title;
	private String id;
	private String updated;
	private String summary;

	public static void main(String[] args) throws Exception {
		DocumentIterator it = new XMLDocumentIterator(XMLInputFactory.newInstance().createXMLStreamReader(new FileReader("feed.xml")));
		
		System.out.println("Collection? " + it.isCollection());
		
		while (it.hasNext()) {
			Document document = it.next();
			System.out.println(it.getTitle());
			System.out.println(document);
		}
	}

	public XMLDocumentIterator(XMLStreamReader xml) {
		this.xml = xml;
	}

	@Override
	public boolean isCollection() {
		try {
			if (!isInitialized()) {
				initialize();
			}
			return isCollection;
		} catch (XMLStreamException e) {
			throw toRuntimeException(e);
		}
	}

	protected boolean isInitialized() {
		return initialized;
	}

	protected void initialize() throws XMLStreamException {
		if (xml.nextTag() == START_ELEMENT) {
			QName name = xml.getName();
			if (isFeed(name)) {
				if (isEntry(name)) {
					throw new XMLStreamException("Element is considered both a feed and an entry", xml.getLocation());
				} else {
					isCollection = true;
					xml.nextTag();
				}
			} else {
				if (isEntry(name)) {
					isCollection = false;
				} else {
					throw new XMLStreamException("Expected either start of feed element or start of entry element", xml.getLocation());
				}
			}
			
			initialized = true;
		} else {
			throw new XMLStreamException("Expected start of element", xml.getLocation());
		}
	}

	protected RuntimeException toRuntimeException(XMLStreamException e) {
		return new RuntimeException(e);
	}

	@Override
	public boolean hasNext() {
		try {
			return hasNextDocument();
		} catch (XMLStreamException e) {
			throw toRuntimeException(e);
		}
	}

	public boolean hasNextDocument() throws XMLStreamException {
		if (!isInitialized()) {
			initialize();
		}
		
		if (xml.getEventType() == END_DOCUMENT) {
			return false;
		} else if (xml.isStartElement() && isEntry(xml.getName())) {
			return true;
		} else {
			// Start the search for the next entry
			while (true) {
				switch (xml.next()) {
				case START_ELEMENT:
					if (isEntry(xml.getName())) {
						return true;
					} else {
						endElement(xml);
					}
					break;
				case END_ELEMENT:
					return false;
				case END_DOCUMENT:
					return false;
				}
			}
		}
	}

	@Override
	public Document next() {
		try {
			return nextDocument();
		} catch (XMLStreamException e) {
			throw toRuntimeException(e);
		}
	}

	public Document nextDocument() throws XMLStreamException {
		if (hasNext()) {
			Document document = new Document();
			getEntry(xml, document);
			return document;
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Expects the XML stream cursor to be immediately after the the start of the "entry" element.
	 * 
	 * @param xml
	 * @return
	 * @throws XMLStreamException 
	 * @throws ParseException 
	 */
	protected int getEntry(XMLStreamReader xml, Document document) throws XMLStreamException {
		int eventType;
		
		title   = null;
		id      = null;
		updated = null;
		summary = null;
		
		while ((eventType = xml.nextTag()) == START_ELEMENT) {
			QName name = xml.getName();
			if (isTitle(name)) {
				title = xml.getElementText();
			} else if (isUpdated(name)) {
				updated = xml.getElementText();
			} else if (isID(name)) {
				id = xml.getElementText();
			} else if (isSummary(name)) {
				summary = xml.getElementText();
			} else if (isContent(name)) {
				getContent(xml, document);
			} else {
				endElement(xml);
			}
		}
		
		return eventType;
	}
	
	protected boolean isFeed(QName name) {
		return "feed".equals(name.getLocalPart());
	}

	protected boolean isEntry(QName name) {
		return "entry".equals(name.getLocalPart());
	}

	protected boolean isTitle(QName name) {
		return "title".equals(name.getLocalPart());
	}

	protected boolean isUpdated(QName name) {
		return "updated".equals(name.getLocalPart());
	}

	protected boolean isID(QName name) {
		return "id".equals(name.getLocalPart());
	}

	private boolean isSummary(QName name) {
		return "summary".equals(name.getLocalPart());
	}

	private boolean isContent(QName name) {
		return "content".equals(name.getLocalPart());
	}

	/**
	 * Expects the cursor to be sitting immediately after the start of element <content...>
	 * @param xml
	 * @param document
	 * @return
	 * @throws XMLStreamException 
	 */
	protected int getContent(XMLStreamReader xml, Document document) throws XMLStreamException {
		int eventType;
		
		while ((eventType = xml.nextTag()) == START_ELEMENT) {
			QName name = xml.getName();
			if (isDiv(name)) {
				getDiv(xml, document);
			} else {
				endElement(xml);
			}
		}
		
		return eventType;
	}

	private boolean isDiv(QName name) {
		return "div".equals(name.getLocalPart());
	}

	protected int getDiv(XMLStreamReader xml, Document document) throws XMLStreamException {
		int eventType;
		
		while ((eventType = xml.nextTag()) == START_ELEMENT) {
			QName name = xml.getName();
			if (isDL(name)) {
				getDL(xml, document);
			} else {
				endElement(xml);
			}
		}
		
		return eventType;
	}

	private boolean isDL(QName tagName) {
		return "dl".equals(tagName.getLocalPart());
	}

	protected int getDL(XMLStreamReader xml, Document document) throws XMLStreamException {
		int eventType;
		
		String fieldName  = null;
		String fieldValue = null;
		String fieldClass = null;
		
		QName previousName = null;
		while ((eventType = xml.nextTag()) == START_ELEMENT) {
			QName name = xml.getName();
			if (isDT(name)) {
				if (previousName == null || isDD(previousName)) {
					fieldName  = null;
					fieldValue = null;
					fieldClass = null;
					
					int attributeCount = xml.getAttributeCount();
					for (int i = 0; i < attributeCount; i++) {
						String attributeName = xml.getAttributeLocalName(i);
						if ("class".equals(attributeName)) {
							fieldClass = xml.getAttributeValue(i);
							break;
						}
					}
					
					// This must occur AFTER acquiring the attribute(s), otherwise stream will be in incorrect state
					fieldName = xml.getElementText();
				} else {
					// ERROR
				}
				previousName = name;
			} else if (isDD(name)) {
				if (previousName == null || !isDT(previousName)) {
					// ERROR
				} else {
					fieldValue = xml.getElementText();
					Fieldable field = buildField(fieldName, fieldValue, fieldClass);
					document.add(field);
				}
				previousName = name;
			} else {
				endElement(xml);
			}
		}
		
		return eventType;
	}

	private boolean isDT(QName name) {
		return "dt".equals(name.getLocalPart());
	}

	private boolean isDD(QName name) {
		return "dd".equals(name.getLocalPart());
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public String getSummary() {
		return summary;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Date getUpdated() throws ParseException {
		if (updated == null) {
			return null;
		} else {
			return dateFormat.parse(updated);
		}
	}

	/**
	 * 
	 * @param name
	 * @param value
	 * @param className
	 * @return
	 * @see Field#Field(String, String, org.apache.lucene.document.Field.Store, org.apache.lucene.document.Field.Index)
	 */
	protected Fieldable buildField(String name, String value, String className) {
		Field.Store store;
		Field.Index index;
		
		if (className == null) {
			store = Field.Store.YES;
			index = Field.Index.TOKENIZED;
		} else {
			store = Field.Store.NO;
			index = Field.Index.NO;
			
			for (String token : className.split("\\s+")) {
				if ("stored".equals(token)) {
					store = Field.Store.YES;
				} else if ("indexed".equals(token) && index == Field.Index.NO) {
					index = Field.Index.UN_TOKENIZED;
				} else if ("tokenized".equals(token)) {
					index = Field.Index.TOKENIZED;
				}
			}
		}
		
		return new Field(name, value, store, index);
	}

}
