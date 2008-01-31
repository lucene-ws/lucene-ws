package net.lucenews.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.lucenews.LuceneWebService;

import org.junit.Before;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

public class ClientTest {
	
	protected File servletDirectory;
	protected DomUtility dom;
	protected DocumentBuilder documentBuilder;
	protected ServletRunner servletRunner;
	protected ServletUnitClient servletClient;
	
	public ClientTest() {
		dom = new DomUtility();
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			servletRunner = new ServletRunner(new File("conf/web.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Before
	public void setup() throws Exception {
		servletRunner.registerServlet("lucene", LuceneWebService.class.getName());
		servletClient = servletRunner.newClient();
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
	
	public byte[] buildWebXml(Map<?, ?> initialParameters) {
		// TODO
		return null;
	}
	
}
