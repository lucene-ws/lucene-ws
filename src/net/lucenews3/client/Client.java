package net.lucenews3.client;

import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import net.lucenews3.AtomPropertiesParser;
import net.lucenews3.XMLStreamUtility;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;

public class Client {

	private XMLInputFactory xmlInputFactory;
	private XMLOutputFactory xmlOutputFactory;
	private HttpClient httpClient;

	public static void main(String... args) throws Exception {
		Client client = new Client();
		//client.createIndex("donkey");
		
		System.out.println(client.getIndexes(new URI("http://localhost:8080/lucene/")));
		
		//Document document = new Document();
		//document.add(new Field("id", "1", Field.Store.YES, Field.Index.UN_TOKENIZED));
		//document.add(new Field("name", "John", Field.Store.YES, Field.Index.TOKENIZED));
		//client.createDocument("donkey", document);
		//Document document = new Document();
		//document.add(new Field("name", "stan", Field.Store.YES, Field.Index.TOKENIZED));
		//client.insertDocument("test", document);
	}

	public Client() {
		this.xmlInputFactory = XMLInputFactory.newInstance();
		this.xmlOutputFactory = XMLOutputFactory.newInstance();
		this.httpClient = new HttpClient();
	}

	public URI getServiceURI() {
		return null;
	}

	public URI getIndexURI(String index) throws URISyntaxException {
		return new URI("http://localhost:8080/lucene/" + index);
	}

	private URI getDocumentURI(String index, Document document) {
		// TODO Auto-generated method stub
		return null;
	}

	protected URI getServicePropertiesURI() throws URISyntaxException {
		return new URI("http://localhost:8080/lucene/service.properties");
	}

	public void createIndex(String name) throws XMLStreamException, HttpException, IOException {
		createIndex(name, (Properties) null);
	}

	public URI createIndex(String indexName, Properties indexProperties) throws XMLStreamException, HttpException, IOException {
		return createIndex(getServiceURI(), indexName, indexProperties);
	}

	public URI createIndex(URI serviceURI, String indexName, Properties indexProperties) throws XMLStreamException, HttpException, IOException {
		PostMethod postMethod = new PostMethod("http://localhost:8080/lucene/");
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		XMLStreamWriter xml = XMLOutputFactory.newInstance().createXMLStreamWriter(buffer);
		xml.writeStartDocument();
		xml.writeStartElement("entry");
		xml.writeDefaultNamespace("http://www.w3.org/2005/Atom");
		
		xml.writeStartElement("title");
		xml.writeCharacters(indexName);
		xml.writeEndElement();
		
		xml.writeStartElement("content");
		xml.writeAttribute("type", "xhtml");
		xml.writeStartElement("div");
		xml.writeDefaultNamespace("http://www.w3.org/1999/xhtml");
		if (indexProperties == null) {
			xml.writeEmptyElement("dl");
		} else {
			xml.writeStartElement("dl");
			for (Entry<Object, Object> entry : indexProperties.entrySet()) {
				Object key = entry.getKey();
				if (key == null) {
					xml.writeEmptyElement("dt");
				} else {
					xml.writeStartElement("dt");
					xml.writeCharacters(String.valueOf(key));
					xml.writeEndElement();
				}
				
				Object value = entry.getValue();
				if (value == null) {
					xml.writeEmptyElement("dd");
				} else {
					xml.writeStartElement("dd");
					xml.writeCharacters(String.valueOf(value));
					xml.writeEndElement();
				}
			}
			xml.writeEndElement(); // dl
		}
		
		xml.writeEndElement(); // div
		xml.writeEndElement(); // content
		xml.writeEndElement(); // entry
		xml.writeEndDocument();
		
		byte[] content = buffer.toByteArray();
		postMethod.setRequestEntity(new ByteArrayRequestEntity(content));
		httpClient.executeMethod(postMethod);
		
		System.out.println(postMethod.getStatusLine());
		System.out.println(postMethod.getResponseBodyAsString());
		//XMLStreamReader xmlr = null;
		
		return null;
	}

	public Properties getServiceProperties() throws XMLStreamException, HttpException, IOException, URISyntaxException {
		GetMethod method = new GetMethod(getServicePropertiesURI().toString());
		httpClient.executeMethod(method);
		
		XMLStreamReader xml = xmlInputFactory.createXMLStreamReader(method.getResponseBodyAsStream());
		
		AtomPropertiesParser parser = new AtomPropertiesParser(xml);
		parser.parse();
		return parser.getProperties();
	}

	public void createIndexProperties(String index, Properties properties) throws XMLStreamException, HttpException, IOException, URISyntaxException {
		PostMethod method = new PostMethod(getServicePropertiesURI().toString());
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		XMLStreamWriter xml = xmlOutputFactory.createXMLStreamWriter(buffer);
		
		byte[] content = buffer.toByteArray();
		method.setRequestEntity(new ByteArrayRequestEntity(content));
		
		httpClient.executeMethod(method);
	}

	public List<URI> getIndexes(URI serviceURI) throws XMLStreamException, URISyntaxException, HttpException, IOException {
		GetMethod method = new GetMethod(serviceURI.toString());
		
		httpClient.executeMethod(method);
		
		List<URI> indexes = new ArrayList<URI>();
		
		XMLStreamReader xml = xmlInputFactory.createXMLStreamReader(method.getResponseBodyAsStream());
		if (xml.nextTag() == START_ELEMENT) {
			String serviceLocalName = xml.getLocalName();
			if ("service".equals(serviceLocalName)) {
				while (xml.nextTag() == START_ELEMENT) {
					String workspaceLocalName = xml.getLocalName();
					if ("workspace".equals(workspaceLocalName)) {
						while (xml.nextTag() == START_ELEMENT) {
							String collectionLocalName = xml.getLocalName();
							if ("collection".equals(collectionLocalName)) {
								String href = null;
								String title = null;
								
								int attributeCount = xml.getAttributeCount();
								for (int i = 0; i < attributeCount; i++) {
									if ("href".equals(xml.getAttributeLocalName(i))) {
										href = xml.getAttributeValue(i);
									} else if ("title".equals(xml.getAttributeLocalName(i))) {
										title = xml.getAttributeValue(i);
									}
								}
								
								indexes.add(new URI(href));
							} else {
								XMLStreamUtility.endElement(xml);
							}
						}
					} else {
						XMLStreamUtility.endElement(xml);
					}
				}
			} else {
				XMLStreamUtility.endElement(xml);
			}
		}
		
		return indexes;
	}

	public void updateIndexProperties(String index, Properties properties) {
		// TODO
	}

	protected void writeXOXOList(Iterable<Fieldable> fields, XMLStreamWriter xml) throws XMLStreamException {
		if (fields == null) {
			xml.writeEmptyElement("dl");
			xml.writeAttribute("class", "xoxo");
		} else {
			xml.writeStartElement("dl");
			xml.writeAttribute("class", "xoxo");
			
			for (Fieldable field : fields) {
				String name = field.name();
				String value = field.stringValue();
				
				String className;
				if (field.isStored()) {
					if (field.isTokenized()) {
						className = "stored indexed tokenized";
					} else if (field.isIndexed()) {
						className = "stored indexed";
					} else {
						className = "stored";
					}
				} else {
					if (field.isTokenized()) {
						className = "indexed tokenized";
					} else if (field.isIndexed()) {
						className = "indexed";
					} else {
						className = "";
					}
				}
				
				xml.writeStartElement("dt");
				xml.writeAttribute("class", className);
				xml.writeCharacters(name);
				xml.writeEndElement();
				
				xml.writeStartElement("dd");
				xml.writeCharacters(value);
				xml.writeEndElement();
			}
			
			xml.writeEndElement(); // dl
		}
	}

	@SuppressWarnings("unchecked")
	public URI createDocument(String index, Document document) throws XMLStreamException, HttpException, IOException, URISyntaxException {
		return createDocument(getIndexURI(index), document);
	}

	public URI createDocument(URI indexURI, Document document) throws XMLStreamException, HttpException, IOException {
		PostMethod postMethod = new PostMethod(indexURI.toString());
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		XMLStreamWriter xml = xmlOutputFactory.createXMLStreamWriter(buffer);
		
		xml.writeStartDocument();
		xml.writeStartElement("entry");
		xml.writeDefaultNamespace("http://www.w3.org/2005/Atom");
		
		xml.writeStartElement("content");
		xml.writeAttribute("type", "xhtml");
		xml.writeStartElement("div");
		xml.writeDefaultNamespace("http://www.w3.org/1999/xhtml");
		
		writeXOXOList(document.getFields(), xml);
		
		xml.writeEndElement(); // div
		xml.writeEndElement(); // content
		xml.writeEndElement(); // entry
		xml.writeEndDocument();
		
		byte[] content = buffer.toByteArray();
		postMethod.setRequestEntity(new ByteArrayRequestEntity(content));
		
		httpClient.executeMethod(postMethod);
		
		return null; // TODO
	}

	@SuppressWarnings("unchecked")
	public URI updateDocument(String index, Document document) throws XMLStreamException, HttpException, IOException {
		return updateDocument(getDocumentURI(index, document), document);
	}

	public URI updateDocument(URI documentURI, Document document) throws XMLStreamException, HttpException, IOException {
		PutMethod postMethod = new PutMethod(documentURI.toString());
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		XMLStreamWriter xml = xmlOutputFactory.createXMLStreamWriter(buffer);
		
		xml.writeStartDocument();
		xml.writeStartElement("entry");
		xml.writeDefaultNamespace("http://www.w3.org/2005/Atom");
		
		xml.writeStartElement("content");
		xml.writeAttribute("type", "xhtml");
		xml.writeStartElement("div");
		xml.writeDefaultNamespace("http://www.w3.org/1999/xhtml");
		
		writeXOXOList(document.getFields(), xml);
		
		xml.writeEndElement(); // div
		xml.writeEndElement(); // content
		xml.writeEndElement(); // entry
		xml.writeEndDocument();
		
		byte[] content = buffer.toByteArray();
		postMethod.setRequestEntity(new ByteArrayRequestEntity(content));
		
		httpClient.executeMethod(postMethod);
		
		return null; // TODO
	}

	public void deleteDocument(URI documentURI) throws HttpException, IOException {
		DeleteMethod method = new DeleteMethod(documentURI.toString());
		httpClient.executeMethod(method);
		
		int statusCode = method.getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {
			// Yippee!
		} else {
			// Uh-oh!
		}
	}

}
