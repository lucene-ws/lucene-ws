package net.lucenews3.model;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.text.NumberFormat;

import net.lucenews3.test.support.FileSystemUtility;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.junit.Test;
import org.springframework.util.StopWatch;

public class QueryResultListTest {

	public static void main(String... arguments) throws Exception {
		FileSystemUtility fileSystem = new FileSystemUtility();
		
		File temp = fileSystem.getTemporaryDirectory();
		
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriter writer = new IndexWriter(temp, analyzer, true);
		BibleIndexBuilder indexBuilder = new BibleIndexBuilder();
		Reader reader = new FileReader(new File("test/data/kjv12.txt"));
		StopWatch watch = new StopWatch();
		watch.start();
		int count = indexBuilder.buildIndex(reader, writer);
		watch.stop();
		writer.close();
		
		NumberFormat formatter = NumberFormat.getInstance();
		System.out.println("Indexed " + formatter.format(count) + " verses in " + watch.getTotalTimeSeconds() + " seconds");
		
		QueryResultList results = new QueryResultList();
		results.setQueryParser(new QueryParser("text", analyzer));
		results.setSearcher(new IndexSearcher(IndexReader.open(temp)));
		results = results.where("text:the");

		results.initialize();
		System.out.println("Query: " + results.getSearchRequest().getQuery());
		System.out.println("Sort: " + results.getSearchRequest().getSort());
		
		System.out.println(formatter.format(results.size()) + " result(s)");
		
		int index = 0;
		for (Result result : results) {
			if (index > 10) {
				break;
			}
			Document document = result.getDocument();
			FieldList fields = document.getFields();
			String book = fields.byName("book").first().stringValue();
			String chapter = fields.byName("chapter").first().stringValue();
			String verse = fields.byName("verse").first().stringValue();
			System.out.println();
			System.out.println("Document ID: " + result.getDocumentId() + ", book: " + book + ", chapter: " + chapter + ", verse: " + verse);
			index++;
		}
	}
	
	@Test
	public void testEventually() {
		// TODO Test QueryResultList
	}
	
}
