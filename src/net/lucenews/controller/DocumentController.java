package net.lucenews.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import net.lucenews.LuceneContext;
import net.lucenews.LuceneRequest;
import net.lucenews.LuceneResponse;
import net.lucenews.LuceneWebService;
import net.lucenews.ServletUtils;
import net.lucenews.atom.AtomParseException;
import net.lucenews.atom.Author;
import net.lucenews.atom.Content;
import net.lucenews.atom.Entry;
import net.lucenews.atom.Feed;
import net.lucenews.atom.Link;
import net.lucenews.atom.NodeContent;
import net.lucenews.atom.Text;
import net.lucenews.atom.TextContent;
import net.lucenews.model.LuceneDocument;
import net.lucenews.model.LuceneIndex;
import net.lucenews.model.LuceneIndexManager;
import net.lucenews.model.exception.DocumentNotFoundException;
import net.lucenews.model.exception.DocumentsNotFoundException;
import net.lucenews.model.exception.IllegalActionException;
import net.lucenews.model.exception.IndexNotFoundException;
import net.lucenews.model.exception.IndicesNotFoundException;
import net.lucenews.model.exception.InsufficientDataException;
import net.lucenews.model.exception.InvalidIdentifierException;
import net.lucenews.model.exception.LuceneParseException;
import net.lucenews.view.AtomView;

import org.apache.lucene.document.Field;
import org.apache.lucene.search.Hits;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DocumentController extends Controller {

	/**
	 * Deletes a document
	 * 
	 * @throws IndicesNotFoundException
	 * @throws DocumentsNotFoundException
	 * @throws IOException
	 */

	public static void doDelete(LuceneContext c) throws IllegalActionException,
			IndicesNotFoundException, DocumentsNotFoundException, IOException,
			InsufficientDataException, TransformerException,
			ParserConfigurationException {
		c.getLogger().trace("doDelete(LuceneContext)");

		LuceneWebService service = c.getService();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest request = c.getRequest();
		LuceneResponse response = c.getResponse();
		String[] indexNames = request.getIndexNames();
		LuceneIndex[] indices = manager.getIndices(indexNames);
		String[] documentIDs = request.getDocumentIDs();

		// Buffers for header location construction
		StringBuffer indexNamesBuffer = new StringBuffer();
		StringBuffer documentIDsBuffer = new StringBuffer();

		boolean deleted = false;

		// For each index...
		for (int i = 0; i < indices.length; i++) {
			LuceneIndex index = indices[i];

			if (i > 0) {
				indexNamesBuffer.append(",");
			}
			indexNamesBuffer.append(index.getName());

			// For each document...
			for (int j = 0; j < documentIDs.length; j++) {
				String documentID = documentIDs[j];

				LuceneDocument document = index.removeDocument(documentID);

				deleted = true;

				if (i == 0) {
					if (j > 0) {
						documentIDsBuffer.append(",");
					}
					documentIDsBuffer.append(index.getIdentifier(document));
				}
			}
		}

		String indexNamesString = indexNamesBuffer.toString();
		String documentIDsString = documentIDsBuffer.toString();

		if (deleted) {
			if (c.isOptimizing() == null || c.isOptimizing()) {
				IndexController.doOptimize(c);
			}
			response.addHeader("Location", service.getDocumentURI(request,
					indexNamesString, documentIDsString).toString());
		} else {
			throw new InsufficientDataException("No documents to be deleted");
		}

		XMLController.acknowledge(c);
	}

	/**
	 * Gets a document
	 * 
	 * @throws IndicesNotFoundException
	 * @throws DocumentsNotFoundException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws IOException
	 */

	public static void doGet(LuceneContext c) throws IndicesNotFoundException,
			DocumentsNotFoundException, ParserConfigurationException,
			TransformerException, IOException, InsufficientDataException {
		c.getLogger().trace("doGet(LuceneContext)");

		LuceneWebService service = c.getService();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest request = c.getRequest();
		LuceneResponse response = c.getResponse();

		Author firstAuthor = null;

		List<Entry> entries = new LinkedList<Entry>();

		LuceneIndex[] indices = manager.getIndices(request.getIndexNames());
		String[] documentIDs = request.getDocumentIDs();

		for (int i = 0; i < documentIDs.length; i++) {
			String documentID = documentIDs[i];

			for (int j = 0; j < indices.length; j++) {
				LuceneIndex index = indices[j];

				LuceneDocument document = null;

				try {
					document = index.getDocument(documentID);

					if (document.getNumber() != null) {
						// Attempt to locate similar documents
						/*
						 * IndexReader reader = index.getIndexReader();
						 * IndexSearcher searcher = index.getIndexSearcher();
						 * 
						 * MoreLikeThis moreLikeThis = new MoreLikeThis( reader );
						 * 
						 * document.setSimilarDocumentHits( searcher.search(
						 * moreLikeThis.like( document.getNumber() ),
						 * c.getFilter(), c.getSort() ) );
						 * 
						 * Logger.getLogger(DocumentController.class).debug("Set
						 * the similar documents:
						 * "+document.getSimilarDocumentHits().length());
						 * 
						 * index.putIndexReader( reader );
						 * index.putIndexSearcher( searcher );
						 */
					} else {
						c.getLogger().debug("Document number is null");
					}
				} catch (DocumentNotFoundException dnfe) {
					c.getLogger()
							.debug("Failed to set similar documents", dnfe);
				}

				if (document != null) {
					if (entries.size() == 0) {
						String name = index.getAuthor(document);
						if (name == null) {
							name = service.getTitle();
						}
						firstAuthor = new Author(name);
					}

					entries.add(asEntry(c, index, document));
				}
			}
		}

		if (entries.size() == 1) {
			entries.get(0).addAuthor(firstAuthor);
		}

		// if( documents.length == 1 )
		// AtomView.process( c, asEntry( c, documents[ 0 ] ) );
		// else
		// AtomView.process( c, asFeed( c, documents ) );

		if (entries.size() == 0) {
			throw new DocumentsNotFoundException(documentIDs);
		}
		if (entries.size() == 1) {
			AtomView.process(c, entries.get(0));
		} else {
			Feed feed = new Feed();

			feed.setTitle("Documents");
			feed.setUpdated(Calendar.getInstance());
			feed.setID(request.getLocation());
			feed.addLink(Link.Self(request.getLocation()));
			feed.addAuthor(new Author(service.getTitle()));

			Iterator<Entry> iterator = entries.iterator();
			while (iterator.hasNext()) {
				feed.addEntry(iterator.next());
			}

			AtomView.process(c, feed);
		}

	}

	/**
	 * Updates particular documents within the index
	 * 
	 * @throws InvalidIdentifierException
	 * @throws IndicesNotFoundException
	 * @throws SAXException
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws DocumentNotFoundException
	 * @throws IndexNotFoundException
	 * @throws IOException
	 * @throws AtomParseException
	 */

	public static void doPut(LuceneContext c) throws IllegalActionException,
			InvalidIdentifierException, IndicesNotFoundException, SAXException,
			TransformerException, ParserConfigurationException,
			DocumentNotFoundException, IndexNotFoundException, IOException,
			InsufficientDataException, AtomParseException, LuceneParseException {
		c.getLogger().trace("doPut(LuceneContext)");

		LuceneWebService service = c.getService();
		LuceneIndexManager manager = service.getIndexManager();
		LuceneRequest request = c.getRequest();
		LuceneResponse response = c.getResponse();

		LuceneIndex[] indices = manager.getIndices(request.getIndexNames());
		LuceneDocument[] documents = request.getLuceneDocuments();

		// Buffers for header location construction
		StringBuffer indexNamesBuffer = new StringBuffer();
		StringBuffer documentIDsBuffer = new StringBuffer();

		boolean updated = false;

		for (int i = 0; i < indices.length; i++) {
			LuceneIndex index = indices[i];

			if (i > 0) {
				indexNamesBuffer.append(",");
			}
			indexNamesBuffer.append(index.getName());

			for (int j = 0; j < documents.length; j++) {
				LuceneDocument document = documents[j];

				index.updateDocument(document);

				updated = true;

				if (i == 0) {
					if (j > 0) {
						documentIDsBuffer.append(",");
					}
					documentIDsBuffer.append(index.getIdentifier(document));
				}
			}
		}
		String indexNames = indexNamesBuffer.toString();
		String documentIDs = documentIDsBuffer.toString();

		if (updated) {
			response.addHeader("Location", service.getDocumentURI(request,
					indexNames, documentIDs).toString());

			if (c.isOptimizing() == null || c.isOptimizing()) {
				IndexController.doOptimize(c);
			}
		} else {
			throw new InsufficientDataException("No documents to be updated");
		}

		XMLController.acknowledge(c);
	}

	public static Entry asEntry(LuceneContext c, LuceneIndex index,
			LuceneDocument document) throws InsufficientDataException,
			ParserConfigurationException, IOException {
		c.getLogger()
				.trace("asEntry(LuceneContext,LuceneIndex,LuceneDocument)");

		return asEntry(c, index, document, null);
	}

	public static Entry asEntry(LuceneContext c, LuceneDocument document)
			throws InsufficientDataException, ParserConfigurationException,
			IOException {
		c.getLogger().trace("asEntry(LuceneContext,LuceneDocument)");
		return asEntry(c, document.getIndex(), document, null);
	}

	/**
	 * Returns an Atom Entry reflecting the standard format chosen for
	 * documents.
	 * 
	 * @param c
	 *            The context
	 * @param document
	 *            The document
	 * @param score
	 *            The score of the document (if it was a hit)
	 * @return An Atom Entry
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */

	public static Entry asEntry(LuceneContext c, LuceneDocument document,
			Float score) throws InsufficientDataException,
			ParserConfigurationException, IOException {
		c.getLogger().trace("asEntry(LuceneContext,LuceneDocument,Float)");
		return asEntry(c, document.getIndex(), document, score);
	}

	/**
	 * Returns an Atom Entry reflecting the standard format chosen for
	 * documents.
	 * 
	 * @param c
	 *            The context
	 * @param index
	 *            The index
	 * @param document
	 *            The document
	 * @param score
	 *            The score of the document (if it was a hit)
	 * @return An Atom Entry
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */

	public static Entry asEntry(LuceneContext c, LuceneIndex index,
			LuceneDocument document, Float score)
			throws InsufficientDataException, ParserConfigurationException,
			IOException {
		c.getLogger().trace(
				"asEntry(LuceneContext,LuceneIndex,LuceneDocument,Float)");

		LuceneWebService service = c.getService();
		LuceneRequest request = c.getRequest();

		// Entry
		Entry entry = new Entry();

		// Content
		entry.setContent(asContent(c, index, document));

		if (index == null) {
			return entry;
		}

		// ID and Link may only be added if the document is identified
		if (index.isDocumentIdentified(document)) {
			// ID
			entry.setID(service.getDocumentURI(request, index, document)
					.toString());

			// Link
			entry.addLink(Link.Alternate(service.getDocumentURI(request, index,
					document).toString()));
		}

		// links to similar documents
		if (document.getSimilarDocumentHits() != null) {
			Hits hits = document.getSimilarDocumentHits();
			// Logger.getLogger(DocumentController.class).debug("Found " +
			// hits.length() + " similar documents");
			for (int i = 0; i < hits.length(); i++) {
				try {
					LuceneDocument similarDocument = index.getDocument(hits
							.id(i));
					Link relatedLink = Link.Related(service.getDocumentURI(
							request, index.getName(),
							similarDocument.getIdentifier()).toString());
					entry.addLink(relatedLink);
					// Logger.getLogger(DocumentController.class).debug("Successfully
					// added related link");
				} catch (DocumentNotFoundException dnfe) {
					// Logger.getLogger(DocumentController.class).debug("Failed
					// to add related link");
				}
			}
		} else {
			// Logger.getLogger(DocumentController.class).debug("No similar
			// documents!");
		}

		// Title
		entry.setTitle(index.getTitle(document));

		// Updated
		try {
			entry.setUpdated(index.getLastModified(document));
		} catch (java.text.ParseException pe) {
		} catch (InsufficientDataException ide) {
		}

		// Author
		if (index.getAuthor(document) != null) {
			entry.addAuthor(new Author(index.getAuthor(document)));
		}

		// Summary
		if (index.getSummary(document) != null) {
			entry.setSummary(new Text(index.getSummary(document)));
		}

		return entry;
	}

	/**
	 * Returns a DOM Element (<div>...</div>) containing XOXO-formatted data
	 * 
	 * @param c
	 *            The context
	 * @param index
	 *            The index
	 * @param document
	 *            The document
	 * @return A DOM Element
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */

	public static Content asContent(LuceneContext c, LuceneIndex index,
			LuceneDocument document) throws ParserConfigurationException,
			IOException {
		c.getLogger().trace(
				"asContent(LuceneContext,LuceneIndex,LuceneDocument)");

		Document doc = XMLController.newDocument();

		Element div = doc.createElement("div");
		div.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");

		div.appendChild(XOXOController.asElement(c, document, doc));

		return Content.xhtml(div);
	}

	/**
	 * Retrieves the appropriate CSS class for the given field
	 */

	public static String getCSSClass(LuceneContext c, Field field) {
		c.getLogger().trace("getCSSClass(LuceneContext,Field)");

		StringBuffer _class = new StringBuffer();

		if (field.isStored()) {
			_class.append(" stored");
		}

		if (field.isIndexed()) {
			_class.append(" indexed");
		}

		if (field.isTokenized()) {
			_class.append(" tokenized");
		}

		if (field.isTermVectorStored()) {
			_class.append(" termvectorstored");
		}

		return String.valueOf(_class).trim();
	}

	/**
	 * Transforms an Atom feed into an array of Lucene documents!
	 */

	public static LuceneDocument[] asLuceneDocuments(LuceneContext c, Feed feed)
			throws ParserConfigurationException,
			TransformerConfigurationException, TransformerException,
			LuceneParseException {
		c.getLogger().trace("asLuceneDocuments(LuceneContext,Feed)");

		return asLuceneDocuments(c, feed.getEntries().toArray(new Entry[] {}));
	}

	/**
	 * Transforms Atom entries into Lucene documents
	 * 
	 * @param entries
	 *            Atom entries
	 * @return Lucene documents
	 */

	public static LuceneDocument[] asLuceneDocuments(LuceneContext c,
			Entry... entries) throws ParserConfigurationException,
			TransformerConfigurationException, TransformerException,
			LuceneParseException {
		c.getLogger().trace("asLuceneDocuments(LuceneContext,Entry...)");

		List<LuceneDocument> documents = new LinkedList<LuceneDocument>();

		for (int i = 0; i < entries.length; i++) {
			documents.addAll(Arrays.asList(asLuceneDocuments(c, entries[i])));
		}

		return documents.toArray(new LuceneDocument[] {});
	}

	/**
	 * Transforms an Atom entry into Lucene documents. Typically, this will only
	 * involve the output of one Lucene document, however, the door may be open
	 * to multiple.
	 * 
	 * @param entry
	 * @return LuceneDocument[]
	 */

	public static LuceneDocument[] asLuceneDocuments(LuceneContext c,
			Entry entry) throws ParserConfigurationException,
			TransformerConfigurationException, TransformerException,
			LuceneParseException {
		c.getLogger().trace("asLuceneDocuments(LuceneContext,Entry)");

		return asLuceneDocuments(c, entry.getContent());
	}

	/**
	 * Transforms Atom content into Lucene documents.
	 */

	public static LuceneDocument[] asLuceneDocuments(LuceneContext c,
			Content content) throws ParserConfigurationException,
			TransformerConfigurationException, TransformerException,
			LuceneParseException {
		c.getLogger().trace("asLuceneDocuments(LuceneContext,Content)");

		if (content == null) {
			throw new LuceneParseException(
					"Cannot parse Lucene document: NULL content in entry");
		}

		if (content instanceof TextContent) {
			return asLuceneDocuments(c, (TextContent) content);
		}

		if (content instanceof NodeContent) {
			return asLuceneDocuments(c, (NodeContent) content);
		}

		throw new LuceneParseException(
				"Cannot parse Lucene document: Unknown content type in entry");
	}

	/**
	 * Transforms Atom text content into Lucene documents
	 */

	public static LuceneDocument[] asLuceneDocuments(LuceneContext c,
			TextContent content) throws ParserConfigurationException,
			TransformerConfigurationException, TransformerException,
			LuceneParseException {
		c.getLogger().trace("asLuceneDocuments(LuceneContext,TextContent)");

		return asLuceneDocuments(c, content.asDocument());
	}

	/**
	 * Transforms Atom node content into Lucene documents
	 */

	public static LuceneDocument[] asLuceneDocuments(LuceneContext c,
			NodeContent content) throws LuceneParseException {
		c.getLogger().trace("asLuceneDocuments(LuceneContext,NodeContent)");

		return asLuceneDocuments(c, content.getNodes());
	}

	public static LuceneDocument[] asLuceneDocuments(LuceneContext c,
			Document document) throws LuceneParseException {
		c.getLogger().trace("asLuceneDocuments(LuceneContext,Document)");

		return asLuceneDocuments(c, document.getDocumentElement());
	}

	public static LuceneDocument[] asLuceneDocuments(LuceneContext c,
			NodeList nodeList) throws LuceneParseException {
		c.getLogger().trace("asLuceneDocuments(LuceneContext,NodeList)");

		Node[] nodes = new Node[nodeList.getLength()];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = nodeList.item(i);
		}
		return asLuceneDocuments(c, nodes);
	}

	public static LuceneDocument[] asLuceneDocuments(LuceneContext c,
			Node[] nodes) throws LuceneParseException {
		c.getLogger().trace(
				"asLuceneDocuments(LuceneContext,Node[]): " + nodes.length
						+ " nodes");
		c.getLogger().trace("Nodes: " + ServletUtils.toString(nodes));

		List<LuceneDocument> documents = new LinkedList<LuceneDocument>();
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i].getNodeType() == Node.ELEMENT_NODE) {
				documents.addAll(Arrays.asList(asLuceneDocuments(c,
						(Element) nodes[i])));
			}
		}
		return documents.toArray(new LuceneDocument[] {});
	}

	public static LuceneDocument[] asLuceneDocuments(LuceneContext c,
			Element element) throws LuceneParseException {
		c.getLogger().trace("asLuceneDocuments(LuceneContext,Element)");

		List<LuceneDocument> documents = new LinkedList<LuceneDocument>();

		if (element.getTagName().equals("div")) {
			documents.addAll(Arrays.asList(asLuceneDocuments(c,
					(NodeList) element.getChildNodes())));
		} else {
			LuceneDocument document = new LuceneDocument();
			Field[] fields = XOXOController.asFields(c, element);
			if (fields.length == 0) {
				throw new LuceneParseException(
						"No fields in specified document");
			}
			document.add(fields);
			documents.add(document);
		}

		return documents.toArray(new LuceneDocument[] {});
	}

	public static LuceneDocument asLuceneDocument(LuceneContext c,
			Element element) {
		c.getLogger().trace("asLuceneDocument(LuceneContext,Element)");

		LuceneDocument document = new LuceneDocument();

		NodeList fields = element.getElementsByTagName("field");
		for (int i = 0; i < fields.getLength(); i++) {
			document.add(asField(c, (Element) fields.item(i)));
		}

		return document;
	}

	public static Field asField(LuceneContext c, Element element) {
		c.getLogger().trace("asField(LuceneContext,Element)");

		if (!element.getTagName().equals("field")) {
			throw new RuntimeException("Must provide a <field> tag");
		}

		String name = element.getAttribute("name");
		String value = ServletUtils.toString(element.getChildNodes());

		if (element.getAttribute("type") != null) {
			String type = element.getAttribute("type").trim().toLowerCase();

			if (type.equals("keyword")) {
				return new Field(name, value, Field.Store.YES,
						Field.Index.UN_TOKENIZED);
			}

			if (type.equals("text")) {
				return new Field(name, value, Field.Store.YES,
						Field.Index.TOKENIZED);
			}

			if (type.equals("sort")) {
				return new Field(name, value, Field.Store.NO,
						Field.Index.UN_TOKENIZED);
			}

			if (type.equals("unindexed")) {
				return new Field(name, value, Field.Store.YES, Field.Index.NO);
			}

			if (type.equals("unstored")) {
				return new Field(name, value, Field.Store.NO,
						Field.Index.TOKENIZED);
			}

			return new Field(name, value, Field.Store.YES,
					Field.Index.TOKENIZED);
		} else {
			boolean index = ServletUtils.parseBoolean(element
					.getAttribute("index"));
			boolean store = ServletUtils.parseBoolean(element
					.getAttribute("store"));
			boolean token = ServletUtils.parseBoolean(element
					.getAttribute("tokenized"));

			Field.Store stored = store ? Field.Store.YES : Field.Store.NO;

			Field.Index indexed = Field.Index.NO;
			if (index) {
				if (token) {
					indexed = Field.Index.TOKENIZED;
				} else {
					indexed = Field.Index.UN_TOKENIZED;
				}
			}

			return new Field(name, value, stored, indexed);
		}
	}

}
