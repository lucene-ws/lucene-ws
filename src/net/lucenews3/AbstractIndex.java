package net.lucenews3;

import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Searcher;

public abstract class AbstractIndex implements Index {

	public static final String DEFAULT_PRIMARY_FIELD_NAME = "id";
	public static final String DEFAULT_VERSION_FIELD_NAME = "version";
	
	private Logger logger;
	private String name;
	private String defaultFieldName;
	private String identityField;
	private String versionFieldName;
	private Analyzer analyzer;

	public AbstractIndex() {
		this.logger = Logger.getLogger(getClass());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public abstract String getDisplayName();

	@Override
	public abstract IndexReader getReader() throws IOException;

	@Override
	public abstract Searcher getSearcher() throws IOException;

	@Override
	public abstract IndexWriter getWriter() throws IOException;

	public Analyzer getAnalyzer() throws IOException {
		return analyzer;
	}

	@Override
	public Document getDocument(String identity) throws IOException {
		IndexReader reader = getReader();
		try {
			return getDocument(reader, identity);
		} finally {
			reader.close();
		}
	}

	public Document getDocument(IndexReader reader, String identity) throws IOException {
		TermDocs termDocs = reader.termDocs(new Term(identityField, identity));
		try {
			if (termDocs.next()) {
				return reader.document(termDocs.doc());
			} else {
				throw new NoSuchDocumentException("Document \"" + identity + "\" does not exist");
			}
		} finally {
			termDocs.close();
		}
	}

	@Override
	public Map<String, Document> getDocuments(Iterable<String> identities) throws IOException {
		Map<String, Document> documentsByIdentity = new LinkedHashMap<String, Document>();
		getDocuments(identities, documentsByIdentity);
		return documentsByIdentity;
	}

	@Override
	public void getDocuments(Iterable<String> identities, Map<String, Document> documentsByIdentity) throws IOException {
		IndexReader reader = getReader();
		try {
			for (String identity : identities) {
				Document document = getDocument(reader, identity);
				documentsByIdentity.put(identity, document);
			}
		} finally {
			reader.close();
		}
	}

	@Override
	public QueryParser getQueryParser() {
		return new QueryParser(defaultFieldName, analyzer);
	}

	/**
	 * Either inserts or updates a document within the index.
	 * 
	 * @param document
	 * @return
	 * @throws IOException
	 */
	public String putDocument(Document document) throws IOException {
		IndexReader reader = getReader();
		try {
			try {
				return updateDocument(reader, document);
			} catch (NoSuchDocumentException e) {
				return insertDocument(reader, document);
			}
		} finally {
			reader.close();
		}
	}

	public boolean containsDocument(String identity) throws IOException {
		IndexReader reader = getReader();
		try {
			return containsDocument(reader, identity);
		} finally {
			reader.close();
		}
	}

	protected boolean containsDocument(IndexReader reader, String identity) throws IOException {
		TermDocs termDocs = reader.termDocs(new Term(identityField, identity));
		try {
			return termDocs.next();
		} finally {
			termDocs.close();
		}
	}

	/**
	 * Determines the identity of the given document within the context of this particular
	 * index.
	 * 
	 * @param document
	 * @return
	 * @throws IllegalArgumentException
	 */
	@Override
	public String getIdentity(Document document) throws IllegalArgumentException, IOException {
		String[] identities = document.getValues(identityField);
		if (identities.length == 1) {
			return identities[0];
		} else if (identities.length == 0) {
			throw new IllegalArgumentException("Document not identified. Please ensure field with name \"" + identityField + "\" exists");
		} else if (identities.length > 0) {
			throw new IllegalArgumentException("Document ambiguously identified. Please ensure only field with name \"" + identityField + "\" exists");
		} else {
			throw new RuntimeException("Something has gone seriously wrong. Arrays lengths should not be negative!");
		}
	}

	@Override
	public String insertDocument(Document document) throws IOException {
		String identity = getIdentity(document);
		
		if (containsDocument(identity)) {
			throw new RuntimeException("Cannot insert document \"" + identity + "\", it already exists"); // TODO Choose a more appropriate exception class
		} else {
			IndexWriter writer = getWriter();
			try {
				writer.addDocument(document);
			} finally {
				writer.close();
			}
		}
		
		return identity;
	}

	protected String insertDocument(IndexReader reader, Document document) throws IOException {
		String identity = getIdentity(document);
		
		if (containsDocument(reader, identity)) {
			throw new RuntimeException("Cannot insert document \"" + identity + "\", it already exists"); // TODO Choose a more appropriate exception class
		} else {
			IndexWriter writer = getWriter();
			try {
				writer.addDocument(document);
			} finally {
				writer.close();
			}
		}
		
		return identity;
	}

	protected String insertDocument(IndexReader reader, IndexWriter writer, Document document) throws IOException {
		String identity = getIdentity(document);
		
		if (containsDocument(reader, identity)) {
			throw new RuntimeException("Cannot insert document \"" + identity + "\", it already exists"); // TODO Choose a more appropriate exception class
		} else {
			writer.addDocument(document);
		}
		
		return identity;
	}

	public Map<Document, String> insertDocuments(Iterable<Document> documents) throws IOException {
		Map<Document, String> identitiesByDocument = new IdentityHashMap<Document, String>();
		insertDocuments(documents, identitiesByDocument);
		return identitiesByDocument;
	}

	public void insertDocuments(Iterable<Document> documents, Map<Document, String> identitiesByDocument) throws IOException {
		IndexReader reader = getReader();
		try {
			IndexWriter writer = getWriter();
			try {
				for (Document document : documents) {
					String identity = insertDocument(reader, writer, document);
					identitiesByDocument.put(document, identity);
				}
			} finally {
				writer.close();
			}
		} finally {
			reader.close();
		}
	}

	@Override
	public String updateDocument(Document document) throws IOException {
		String identity = getIdentity(document);
		
		if (removeDocument(identity) == 0) {
			throw new RuntimeException("Cannot update document \"" + identity + "\", it doesn't already exist"); // TODO Choose a more appropriate exception class
		}
		
		IndexWriter writer = getWriter();
		try {
			writer.addDocument(document);
		} finally {
			writer.close();
		}
		
		return identity;
	}

	protected String updateDocument(IndexReader reader, Document document) throws IOException {
		String identity = getIdentity(document);
		
		if (removeDocument(reader, identity) == 0) {
			throw new RuntimeException("Cannot update document \"" + identity + "\", it doesn't already exist"); // TODO Choose a more appropriate exception class
		}
		
		IndexWriter writer = getWriter();
		try {
			writer.addDocument(document);
		} finally {
			writer.close();
		}
		
		return identity;
	}

	protected String updateDocument(IndexReader reader, IndexWriter writer, Document document) throws IOException {
		String identity = getIdentity(document);
		
		if (removeDocument(reader, identity) == 0) {
			throw new RuntimeException("Cannot update document \"" + identity + "\", it doesn't already exist"); // TODO Choose a more appropriate exception class
		}
		
		writer.addDocument(document);
		
		return identity;
	}

	/**
	 * Updates a series of documents, returning the associated identities via a Map object.
	 * 
	 * @param documents
	 * @return
	 * @throws IOException
	 */
	public Map<Document, String> updateDocuments(Iterable<Document> documents) throws IOException {
		Map<Document, String> identitiesByDocument = new IdentityHashMap<Document, String>();
		updateDocuments(documents, identitiesByDocument);
		return identitiesByDocument;
	}

	public void updateDocuments(Iterable<Document> documents, Map<Document, String> identitiesByDocument) throws IOException {
		IndexReader reader = getReader();
		try {
			IndexWriter writer = getWriter();
			try {
				for (Document document : documents) {
					String identity = updateDocument(reader, writer, document);
					identitiesByDocument.put(document, identity);
				}
			} finally {
				writer.close();
			}
		} finally {
			reader.close();
		}
	}

	/**
	 * 
	 * @param identity
	 * @return the number of actual documents removed corresponding to the given identity
	 * @throws IOException
	 */
	public int removeDocument(String identity) throws IOException {
		IndexReader reader = getReader();
		try {
			return removeDocument(reader, identity);
		} finally {
			reader.close();
		}
	}

	/**
	 * 
	 * @param reader
	 * @param identity
	 * @return the number of actual documents removed corresponding to the given identity
	 * @throws IOException
	 */
	protected int removeDocument(IndexReader reader, String identity) throws IOException {
		TermDocs termDocs = reader.termDocs(new Term(identityField, identity));
		try {
			int count = 0;
			while (termDocs.next()) {
				reader.deleteDocument(termDocs.doc());
				count++;
			}
			return count;
		} finally {
			termDocs.close();
		}
	}

}
