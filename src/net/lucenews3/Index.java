package net.lucenews3;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Searcher;

public interface Index {

	public String getName();

	public String getDisplayName();

	public IndexReader getReader() throws IOException;

	public IndexWriter getWriter() throws IOException;

	public Searcher getSearcher() throws IOException;

	public QueryParser getQueryParser();

	public String getIdentity(Document document) throws IOException;

	public Document getDocument(String key) throws IOException;

	public Map<String, Document> getDocuments(Iterable<String> identities) throws IOException;

	/**
	 * Attempts to get the documents identified by the given identities. Any documents found will be
	 * deposited into the given map. Therefore, it is possible that no documents of the given will be found.
	 * 
	 * @param identities
	 * @param documentsByIdentity
	 * @throws IOException
	 */
	public void getDocuments(Iterable<String> identities, Map<String, Document> documentsByIdentity) throws IOException;

	public DocumentMetaData insertDocument(Document document) throws IOException;

	public Map<Document, DocumentMetaData> insertDocuments(Iterable<Document> documents) throws IOException;

	public void insertDocuments(Iterable<Document> documents, Map<Document, DocumentMetaData> metaDataByDocument) throws IOException;

	public DocumentMetaData updateDocument(Document document) throws IOException;

	public Map<Document, DocumentMetaData> updateDocuments(Iterable<Document> documents) throws IOException;

	public void updateDocuments(Iterable<Document> documents, Map<Document, DocumentMetaData> metaDataByDocument) throws IOException;

	public Properties getProperties();

	public void setProperties(Properties properties);

}
