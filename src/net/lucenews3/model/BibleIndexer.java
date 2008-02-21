package net.lucenews3.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.lucenews.test.support.LuceneUtility;
import net.lucenews.test.support.MapUtility;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;

public class BibleIndexer {
	
	private LuceneUtility lucene;
	private MapUtility maps;
	private int documentCount;
	
	public BibleIndexer() {
		lucene = new LuceneUtility();
		maps = new MapUtility();
	}
	
	public int index(Reader reader, IndexWriter writer) throws IOException {
		
		BufferedReader bufferedReader = new BufferedReader(reader);
		
		Integer book = null;
		String bookName = null;
		Pattern bookPattern = Pattern.compile("Book\\s+(\\d+)\\s+(\\w+(\\s+\\w+)*)");
		
		Integer chapter = null;
		Integer verse = null;
		String text = null;
		
		Pattern referencePattern = Pattern.compile("(\\d+):(\\d+)\\s*");
		
		int count = 0;
		while (true) {
			String line = bufferedReader.readLine();
			if (line == null || count++ > 1000) {
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
					verse = Integer.valueOf(referenceMatcher.group(1));
					text = line.substring(referenceMatcher.end()).trim();
				} else {
					text += " " + line.trim();
				}
			}
		}
		
		return documentCount;
	}
	
	public Document add(IndexWriter writer, Integer book, String bookName, Integer chapter, Integer verse, String text) throws CorruptIndexException, IOException {
		Document document = lucene.buildDocument(maps.toMap("book", book, "book_title", bookName, "chapter", chapter, "verse", verse, "text", text));
		writer.addDocument(document);
		documentCount++;
		return document;
	}
	
}
