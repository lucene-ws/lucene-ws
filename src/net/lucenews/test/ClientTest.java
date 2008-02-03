package net.lucenews.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Hashtable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.lucenews.LuceneWebService;
import net.lucenews.http.ByteBufferInputStream;
import net.lucenews.http.HttpCommunication;
import net.lucenews.test.support.ClientUtility;
import net.lucenews.test.support.CollectionAsserter;
import net.lucenews.test.support.DefaultHttpServletContainer;
import net.lucenews.test.support.DomUtility;
import net.lucenews.test.support.EntryAsserter;
import net.lucenews.test.support.FeedAsserter;
import net.lucenews.test.support.FileSystemUtility;
import net.lucenews.test.support.HttpUtility;
import net.lucenews.test.support.IntrospectionDocumentAsserter;
import net.lucenews.test.support.ListUtility;
import net.lucenews.test.support.LuceneUtility;
import net.lucenews.test.support.MapUtility;
import net.lucenews.test.support.StringUtility;
import net.lucenews.test.support.XoxoUtility;
import net.lucenews3.WebService;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ClientTest {
	
	protected boolean strict;
	
	protected ClientUtility client;
	protected DomUtility dom;
	protected FileSystemUtility fileSystem;
	protected HttpUtility http;
	protected ListUtility lists;
	protected LuceneUtility lucene;
	protected MapUtility map;
	protected StringUtility string;
	protected XoxoUtility xoxo;
	
	protected IntrospectionDocumentAsserter introspectionDocumentAsserter;
	protected CollectionAsserter collectionAsserter;
	protected EntryAsserter entryAsserter;
	protected FeedAsserter feedAsserter;
	
	protected File servletDirectory;
	protected DocumentBuilder documentBuilder;
	protected Random random;
	protected DefaultHttpServletContainer container;
	protected Logger logger;
	
	public ClientTest() {
		//this(LuceneWebService.class);
		this(WebService.class);
	}
	
	public ClientTest(Class<? extends HttpServlet> servletClass) {
		this.introspectionDocumentAsserter = new IntrospectionDocumentAsserter();
		dom = new DomUtility();
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.random = new Random();
		this.fileSystem = new FileSystemUtility();
		this.lists = new ListUtility();
		this.lucene = new LuceneUtility();
		this.xoxo = new XoxoUtility();
		this.string = new StringUtility();
		this.entryAsserter = new EntryAsserter();
		this.feedAsserter = new FeedAsserter();
		this.map = new MapUtility();
		this.container = new DefaultHttpServletContainer();
		this.container.setServletClass(servletClass);
		this.http = new HttpUtility(this.container);
		this.client = new ClientUtility(this.container, this.http);
		this.logger = Logger.getLogger(this.getClass().getName());
		this.logger.setLevel(Level.INFO);
	}
	
	public Document toDocument(HttpCommunication communication) throws SAXException, IOException {
		final ByteBuffer body = communication.getBody();
		final InputStream inputStream = new ByteBufferInputStream(body);
		return documentBuilder.parse(inputStream);
	}
	
	public Hashtable<?, ?> toMap(final Object... objects) {
		return map.toHashtable(objects);
	}
	
}
