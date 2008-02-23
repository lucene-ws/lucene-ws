package net.lucenews3.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;

public class GutenbergBookIndexer {

	private int stave;
	private int paragraph;
	private StringBuffer buffer;
	private IndexWriter writer;
	private int count;
	
	public static void main(String... arguments) throws Exception {
		GutenbergBookIndexer indexer = new GutenbergBookIndexer();
		Reader reader = new FileReader(new File("test/data/A Christmas Carol by Charles Dickens.txt"));
		File indexDirectory;
		if (arguments.length > 0) {
			indexDirectory = new File(arguments[0]);
		} else {
			indexDirectory = new File("christmascarol");
			indexDirectory.mkdirs();
		}
		IndexWriter writer = new IndexWriter(indexDirectory, new StandardAnalyzer(), true);
		indexer.index(reader, writer);
		writer.close();
		System.out.println("Indexed " + indexer.count + " documents");
	}
	
	public void index(Reader reader, IndexWriter writer) throws IOException {
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
	
}
