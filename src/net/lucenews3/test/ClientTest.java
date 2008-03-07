package net.lucenews3.test;

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

import net.lucenews3.http.ByteBufferInputStream;
import net.lucenews3.http.HttpCommunication;
import net.lucenews3.test.support.ClientUtility;
import net.lucenews3.test.support.CollectionAsserter;
import net.lucenews3.test.support.DefaultHttpServletContainer;
import net.lucenews3.test.support.DomUtility;
import net.lucenews3.test.support.EntryAsserter;
import net.lucenews3.test.support.FeedAsserter;
import net.lucenews3.test.support.FileSystemUtility;
import net.lucenews3.test.support.HttpUtility;
import net.lucenews3.test.support.IntrospectionDocumentAsserter;
import net.lucenews3.test.support.ListUtility;
import net.lucenews3.test.support.LuceneUtility;
import net.lucenews3.test.support.MapUtility;
import net.lucenews3.test.support.StringUtility;
import net.lucenews3.test.support.XoxoUtility;

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

	public boolean isStrict() {
		return strict;
	}

	public void setStrict(boolean strict) {
		this.strict = strict;
	}

	public ClientUtility getClientUtility() {
		return client;
	}

	public void setClientUtility(ClientUtility client) {
		this.client = client;
	}

	public DomUtility getDomUtility() {
		return dom;
	}

	public void setDomUtility(DomUtility dom) {
		this.dom = dom;
	}

	public FileSystemUtility getFileSystemUtility() {
		return fileSystem;
	}

	public void setFileSystemUtility(FileSystemUtility fileSystem) {
		this.fileSystem = fileSystem;
	}

	public HttpUtility getHttpUtility() {
		return http;
	}

	public void setHttpUtility(HttpUtility http) {
		this.http = http;
	}

	public ListUtility getListUtility() {
		return lists;
	}

	public void setListUtility(ListUtility lists) {
		this.lists = lists;
	}

	public LuceneUtility getLuceneUtility() {
		return lucene;
	}

	public void setLuceneUtility(LuceneUtility lucene) {
		this.lucene = lucene;
	}

	public MapUtility getMapUtility() {
		return map;
	}

	public void setMapUtility(MapUtility map) {
		this.map = map;
	}

	public StringUtility getStringUtility() {
		return string;
	}

	public void setStringUtility(StringUtility string) {
		this.string = string;
	}

	public XoxoUtility getXoxoUtility() {
		return xoxo;
	}

	public void setXoxoUtility(XoxoUtility xoxo) {
		this.xoxo = xoxo;
	}

	public IntrospectionDocumentAsserter getIntrospectionDocumentAsserter() {
		return introspectionDocumentAsserter;
	}

	public void setIntrospectionDocumentAsserter(
			IntrospectionDocumentAsserter introspectionDocumentAsserter) {
		this.introspectionDocumentAsserter = introspectionDocumentAsserter;
	}

	public CollectionAsserter getCollectionAsserter() {
		return collectionAsserter;
	}

	public void setCollectionAsserter(CollectionAsserter collectionAsserter) {
		this.collectionAsserter = collectionAsserter;
	}

	public EntryAsserter getEntryAsserter() {
		return entryAsserter;
	}

	public void setEntryAsserter(EntryAsserter entryAsserter) {
		this.entryAsserter = entryAsserter;
	}

	public FeedAsserter getFeedAsserter() {
		return feedAsserter;
	}

	public void setFeedAsserter(FeedAsserter feedAsserter) {
		this.feedAsserter = feedAsserter;
	}

	public File getServletDirectory() {
		return servletDirectory;
	}

	public void setServletDirectory(File servletDirectory) {
		this.servletDirectory = servletDirectory;
	}

	public DocumentBuilder getDocumentBuilder() {
		return documentBuilder;
	}

	public void setDocumentBuilder(DocumentBuilder documentBuilder) {
		this.documentBuilder = documentBuilder;
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	public DefaultHttpServletContainer getContainer() {
		return container;
	}

	public void setContainer(DefaultHttpServletContainer container) {
		this.container = container;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
}
