package net.lucenews3.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.InitializingBean;

public class GutenbergBookIndexer implements InitializingBean {

	private Logger logger;
	private File bookFile;
	private Directory indexDirectory;
	private int stave;
	private int paragraph;
	private StringBuffer buffer;
	private IndexWriter writer;
	private int count;
	
	public static void main(String... arguments) throws Exception {
		GutenbergBookIndexer indexer = new GutenbergBookIndexer();
		if (arguments.length > 0) {
			indexer.indexDirectory = FSDirectory.getDirectory(new File(arguments[0]));
		}
		int count = indexer.buildIndex();
		System.out.println("Indexed " + count + " paragraphs");
	}
	
	public GutenbergBookIndexer() throws IOException {
		this.logger = Logger.getLogger(getClass());
		this.bookFile = new File("test/data/A Christmas Carol by Charles Dickens.txt");
		this.indexDirectory = FSDirectory.getDirectory(new File("christmascarol"));
	}
	
	public int buildIndex() throws IOException {
		Reader reader = new FileReader(bookFile);
		IndexWriter writer = new IndexWriter(indexDirectory, new StandardAnalyzer(), true);
		buildIndex(reader, writer);
		writer.close();
		return count;
	}
	
	public void afterPropertiesSet() throws Exception {
		if (IndexReader.indexExists(indexDirectory)) {
			if (logger.isInfoEnabled()) {
				logger.info("No index created at " + indexDirectory);
			}
		} else {
			int count = buildIndex();
			if (logger.isInfoEnabled()) {
				logger.info("Indexed " + count + " paragraphs from " + bookFile + " at " + indexDirectory);
			}
		}
	}
	
	public void buildIndex(Reader reader, IndexWriter writer) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(reader);
		
		boolean active = false;
		
		this.buffer = null;
		this.writer = writer;
		
		while (true) {
			String line = bufferedReader.readLine();
			
			if (line == null) {
				break;
			}
			
			if (active) {
				if (line.startsWith("*** END OF THIS PROJECT GUTENBERG EBOOK")) {
					flush();
					active = false;
				} else if (line.startsWith("STAVE ")) {
					Matcher matcher = Pattern.compile("STAVE (\\w+):").matcher(line);
					matcher.find();
					flush();
					stave = romanToInt(matcher.group(1));
					paragraph = 1;
				} else if (line.trim().equals("")) {
					flush();
					paragraph++;
				} else {
					if (buffer == null) {
						buffer = new StringBuffer();
					}
					buffer.append(line + " ");
				}
			} else if (line.startsWith("*** START OF THIS PROJECT GUTENBERG EBOOK")) {
				active = true;
			}
		}
	}
	
	public void flush() throws CorruptIndexException, IOException {
		if (buffer != null && stave > 0) {
			String text = buffer.toString();
			org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
			document.add(new Field("id", String.valueOf(stave) + "_" + String.valueOf(paragraph), Field.Store.YES, Field.Index.UN_TOKENIZED));
			document.add(new Field("stave", String.valueOf(stave), Field.Store.YES, Field.Index.UN_TOKENIZED));
			document.add(new Field("paragraph", String.valueOf(paragraph), Field.Store.YES, Field.Index.UN_TOKENIZED));
			document.add(new Field("text", text, Field.Store.YES, Field.Index.TOKENIZED));
			writer.addDocument(document);
			count++;
			buffer = null;
		}
	}
	
	public int romanToInt(String roman) {
		if (roman.equals("I")) {
			return 1;
		} else if (roman.equals("II")) {
			return 2;
		} else if (roman.equals("III")) {
			return 3;
		} else if (roman.equals("IV")) {
			return 4;
		} else if (roman.equals("V")) {
			return 5;
		}
		throw new RuntimeException();
	}

	public File getBookFile() {
		return bookFile;
	}

	public void setBookFile(File bookFile) {
		this.bookFile = bookFile;
	}

	public Directory getIndexDirectory() {
		return indexDirectory;
	}

	public void setIndexDirectory(Directory indexDirectory) {
		this.indexDirectory = indexDirectory;
	}
	
}
