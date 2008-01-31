package net.lucenews.test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.LockObtainFailedException;

public class LuceneUtility {
	
	private FileSystemUtility fileSystem;
	private Analyzer defaultAnalyzer;
	
	public LuceneUtility() {
		this.fileSystem = new FileSystemUtility();
		this.defaultAnalyzer = new StandardAnalyzer();
	}
	
	public IndexWriter getTemporaryIndexWriter() throws CorruptIndexException, LockObtainFailedException, IOException {
		return getTemporaryIndexWriter(true);
	}
	
	public IndexWriter getTemporaryIndexWriter(boolean autoCreate) throws CorruptIndexException, LockObtainFailedException, IOException {
		return getTemporaryIndexWriter(defaultAnalyzer, autoCreate);
	}
	
	public IndexWriter getTemporaryIndexWriter(File directory) throws CorruptIndexException, LockObtainFailedException, IOException {
		return getTemporaryIndexWriter(directory, defaultAnalyzer);
	}
	
	public IndexWriter getTemporaryIndexWriter(File directory, Analyzer analyzer) throws CorruptIndexException, LockObtainFailedException, IOException {
		return new IndexWriter(directory, analyzer, true);
	}
	
	public IndexWriter getTemporaryIndexWriter(Analyzer analyzer, boolean autoCreate) throws CorruptIndexException, LockObtainFailedException, IOException {
		File directory = fileSystem.getTemporaryDirectory(true);
		IndexWriter writer = new IndexWriter(directory, analyzer, autoCreate);
		return writer;
	}
	
	public Document buildDocument(Map<?, ?> fields) {
		Document document = new Document();
		buildDocument(document, fields);
		return document;
	}
	
	public void buildDocument(Document document, Map<?, ?> fields) {
		for (Object key : fields.keySet()) {
			Object value = fields.get(key);
			Fieldable field = buildFieldable(key, value);
			document.add(field);
		}
	}
	
	public Fieldable buildFieldable(Object key, Object value) {
		return new Field(key.toString(), value.toString(), Field.Store.YES, Field.Index.TOKENIZED);
	}

}
