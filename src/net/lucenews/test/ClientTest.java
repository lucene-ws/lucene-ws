package net.lucenews.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.lucenews.LuceneWebService;
import net.lucenews.http.ByteBufferInputStream;
import net.lucenews.http.DefaultHttpRequest;
import net.lucenews.http.DefaultHttpResponse;
import net.lucenews.http.HttpCommunication;
import net.lucenews.http.HttpRequest;
import net.lucenews.http.HttpResponse;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

public class ClientTest {
	
	protected FileSystemUtility fileSystem;
	protected LuceneUtility lucene;
	protected DomUtility dom;
	
	protected IntrospectionDocumentAsserter introspectionDocumentAsserter;
	protected CollectionAsserter collectionAsserter;
	protected EntryAsserter entryAsserter;
	
	protected File servletDirectory;
	protected DocumentBuilder documentBuilder;
	protected ServletRunner runner;
	protected ServletUnitClient client;
	protected Random random;
	protected DefaultHttpServletContainer servletContainer;
	
	public ClientTest() {
		this.introspectionDocumentAsserter = new IntrospectionDocumentAsserter();
		dom = new DomUtility();
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.random = new Random();
		runner = new ServletRunner();
		this.fileSystem = new FileSystemUtility();
		this.lucene = new LuceneUtility();
		this.entryAsserter = new EntryAsserter();
		this.servletContainer = new DefaultHttpServletContainer();
		this.servletContainer.setServletClass(LuceneWebService.class);
	}
	
	public void setup() throws Exception {
		runner.registerServlet("lucene", LuceneWebService.class.getName());
		client = runner.newClient();
	}
	
	public Document toDocument(HttpCommunication communication) throws SAXException, IOException {
		final ByteBuffer body = communication.getBody();
		final InputStream inputStream = new ByteBufferInputStream(body);
		
		body.rewind();
		while (true) {
			int b = inputStream.read();
			if (b < 0) {
				break;
			}
			System.out.write(b);
		}
		
		body.rewind();
		return documentBuilder.parse(inputStream);
	}
	
	public InputStream toWebConfigStream(Map<?, ?> initialParameters) {
		return new ByteArrayInputStream(toWebConfig(initialParameters).getBytes());
	}
	
	public String toWebConfig(Map<?, ?> initialParameters) {
		return toWebConfig("The Lucene Web Service", "LuceneWebService", LuceneWebService.class, initialParameters);
	}
	
	public String toWebConfig(String displayName, String servletName, Class<?> servletClass, Map<?, ?> initialParameters) {
		StringBuffer buffer = new StringBuffer();
		try {
			appendWebConfig(buffer, displayName, servletName, servletClass, initialParameters);
		} catch (IOException e) {
			// Shouldn't be thrown from a StringBuffer object, but hey...
			e.printStackTrace();
		}
		return buffer.toString();
	}
	
	/**
	 * Builds a web.xml file, appending the content to the given appendable object.
	 * @param appendable
	 * @param displayName
	 * @param servletName
	 * @param servletClass
	 * @param initialParameters
	 * @throws IOException
	 */
	public void appendWebConfig(Appendable appendable, String displayName, String servletName, Class<?> servletClass, Map<?, ?> initialParameters) throws IOException {
		appendable.append("<?xml version=\"1.0\"?>");
		appendLine(appendable);
		
		appendable.append("<web-app>");
		appendLine(appendable);
		
		appendable.append("\t<display-name>" + displayName + "</display-name>");
		appendLine(appendable);
		
		appendable.append("\t<servlet>");
		appendLine(appendable);
		
		appendable.append("\t\t<servlet-name>" + servletName + "</servlet-name>");
		appendLine(appendable);
		
		appendable.append("\t\t<servlet-class>" + servletClass.getCanonicalName() + "</servlet-class>");
		appendLine(appendable);
		
		for (Object initParamKey : initialParameters.keySet()) {
			appendable.append("\t\t<init-param>");
			appendLine(appendable);
			appendable.append("\t\t\t<param-name>" + initParamKey + "</param-name>");
			appendLine(appendable);
			appendable.append("\t\t\t<param-value>" + initialParameters.get(initParamKey) + "</param-value>");
			appendLine(appendable);
			appendable.append("\t\t</init-param>");
			appendLine(appendable);
		}
		
		appendable.append("\t</servlet>");
		appendLine(appendable);
		
		appendable.append("</web-app>");
		appendLine(appendable);
	}
	
	public void appendLine(Appendable appendable) throws IOException {
		appendable.append("\n");
	}
	
	public <K, V> Map<K, V> toTypedMap(Class<K> keyType, Class<V> valueType, Object... objects) {
		return null;
	}
	
	public Hashtable<?, ?> toMap(Object... objects) {
		Hashtable<Object, Object> result = new Hashtable<Object, Object>();
		for (int i = 0; i < objects.length; i += 2) {
			Object key = objects[i];
			Object value;
			try {
				value = objects[i + 1];
			} catch (ArrayIndexOutOfBoundsException badIndex) {
				value = null;
			}
			result.put(key, value);
		}
		return result;
	}
	
	/**
	 * Constructs a new HTTP request.
	 * @return
	 */
	public HttpRequest newRequest() {
		return new DefaultHttpRequest();
	}
	
	public HttpRequest newRequest(String method) {
		HttpRequest request = newRequest();
		request.setMethod(method);
		return request;
	}
	
	public HttpRequest newRequest(String method, String resource) {
		HttpRequest request = newRequest(method);
		// TODO: request.setResource(resource);
		return request;
	}
	
	public HttpRequest getRequest(String resource) {
		return newRequest("GET", resource);
	}
	
	public HttpRequest postRequest(String resource) {
		return newRequest("POST", resource);
	}
	
	public HttpRequest putRequest(String resource) {
		return newRequest("PUT", resource);
	}
	
	public HttpRequest deleteRequest(String resource) {
		return newRequest("DELETE", resource);
	}
	
	public HttpResponse getResponse() {
		return new DefaultHttpResponse();
	}
	
	public HttpResponse getResponse(HttpRequest request) throws Exception {
		return getResponse(servletContainer, request);
	}
	
	public HttpResponse getResponse(HttpServletContainer servletContainer, HttpRequest request) throws Exception {
		HttpResponse response = getResponse();
		servletContainer.service(request, response);
		return response;
	}
	
}