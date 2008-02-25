package net.lucenews3.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.lucenews3.test.support.LuceneUtility;
import net.lucenews3.test.support.MapUtility;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;

public class BibleIndexBuilder extends AbstractIndexBuilder {
	
	private File sourceFile;
	private LuceneUtility lucene;
	private MapUtility maps;
	private int documentCount;
	private int skip;
	
	public BibleIndexBuilder() {
		this.sourceFile = new File(new File(new File("test"), "data"), "kjv12.txt");
		this.lucene = new LuceneUtility();
		this.maps = new MapUtility();
	}
	
	public int buildIndex(IndexWriter writer) throws Exception {
		int result;
		Reader reader = new FileReader(sourceFile);
		result = buildIndex(reader, writer);
		reader.close();
		return result;
	}
	
	public int buildIndex(Reader reader, IndexWriter writer) throws IOException {
		try {
			IndexReader indexReader = IndexReader.open(getDirectory());
			skip = indexReader.numDocs();
			indexReader.close();
		} catch (Exception e) {
			e.printStackTrace();
			skip = 0;
		}
		
		BufferedReader bufferedReader = new BufferedReader(reader);
		
		Integer book = null;
		String bookName = null;
		Pattern bookPattern = Pattern.compile("Book\\s+(\\d+)\\s+(\\w+(\\s+\\w+)*)");
		
		Integer chapter = null;
		Integer verse = null;
		String text = null;
		
		Pattern referencePattern = Pattern.compile("(\\d+):(\\d+)\\s*");
		
		while (true) {
			String line = bufferedReader.readLine();
			if (line == null) {
				break;
			}
			Matcher bookMatcher = bookPattern.matcher(line);
			Matcher referenceMatcher = referencePattern.matcher(line);
			
			if (bookMatcher.matches()) {
				book = Integer.valueOf(bookMatcher.group(1));
				bookName = bookMatcher.group(2);
				System.out.println("Book: " + book + ", name: " + bookName);
			} else if (book != null) {
				if (chapter != null && verse != null && line.trim().length() == 0) {
					add(writer, book, bookName, chapter, verse, text);
				} else if (referenceMatcher.find()) {
					chapter = Integer.valueOf(referenceMatcher.group(1));
					verse = Integer.valueOf(referenceMatcher.group(2));
					text = line.substring(referenceMatcher.end()).trim();
				} else {
					text += " " + line.trim();
				}
			}
		}
		
		return documentCount;
	}
	
	public File getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	public Document add(IndexWriter writer, Integer book, String bookName, Integer chapter, Integer verse, String text) throws CorruptIndexException, IOException {
		Document document = lucene.buildDocument(
				maps.toMap(
						"id", bookName + book + "v" + verse,
						"book", book,
						"book_title", bookName,
						"chapter", chapter,
						"verse", verse,
						"text", text
					));
		if (skip == 0) {
			writer.addDocument(document);
			documentCount++;
		} else {
			skip--;
		}
		return document;
	}
	
}
