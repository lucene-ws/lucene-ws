package net.lucenews3.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;

public class GutenbergIndexBuilder extends AbstractIndexBuilder {

	private File sourceFile;
	private int stave;
	private int paragraph;
	private StringBuffer buffer;
	private IndexWriter writer;
	private int count;
	
	public static void main(String... arguments) throws Exception {
		GutenbergIndexBuilder indexBuilder = new GutenbergIndexBuilder();
		if (arguments.length > 0) {
			indexBuilder.setDirectory(FSDirectory.getDirectory(new File(arguments[0])));
		}
		int count = indexBuilder.buildIndex();
		System.out.println("Indexed " + count + " paragraphs");
	}
	
	public GutenbergIndexBuilder() throws IOException {
		this.sourceFile = new File("test/data/A Christmas Carol by Charles Dickens.txt");
		this.setDirectory(FSDirectory.getDirectory(new File("christmascarol")));
	}
	
	public int buildIndex(IndexWriter writer) throws Exception {
		int result;
		Reader reader = new FileReader(sourceFile);
		result = buildIndex(reader, writer);
		reader.close();
		return result;
	}
	
	public int buildIndex(Reader reader, IndexWriter writer) throws IOException {
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
		
		return count;
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

	public File getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}
	
}
