package net.lucenews.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.lucenews.LuceneWebService;
import net.lucenews.http.DefaultHttpRequest;
import net.lucenews.http.HttpRequest;
import net.lucenews.http.HttpResponse;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.meterware.httpunit.WebResponse;
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
	}
	
	public void setup() throws Exception {
		runner.registerServlet("lucene", LuceneWebService.class.getName());
		client = runner.newClient();
	}
	
	public Document toDocument(WebResponse response) throws SAXException, IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		InputStream stream = response.getInputStream();
		while (true) {
			int b = stream.read();
			if (b < 0) {
				break;
			}
			buffer.write(b);
			System.out.write(b);
		}
		return documentBuilder.parse(new ByteArrayInputStream(buffer.toByteArray()));
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
	public HttpRequest getRequest() {
		return new DefaultHttpRequest();
	}
	
	public HttpResponse getResponse(HttpRequest request) {
		HttpResponse result;
		// TODO
		result = null;
		return result;
	}
	
}
