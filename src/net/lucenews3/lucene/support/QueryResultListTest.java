package net.lucenews3.lucene.support;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.text.NumberFormat;

import net.lucenews.test.support.FileSystemUtility;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.springframework.util.StopWatch;

public class QueryResultListTest {

	public static void main(String... arguments) throws Exception {
		FileSystemUtility fileSystem = new FileSystemUtility();
		
		File temp = fileSystem.getTemporaryDirectory();
		
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriter writer = new IndexWriter(temp, analyzer, true);
		BibleIndexer indexer = new BibleIndexer();
		Reader reader = new FileReader(new File("test/data/kjv12.txt"));
		StopWatch watch = new StopWatch();
		watch.start();
		int count = indexer.index(reader, writer);
		watch.stop();
		writer.close();
		
		NumberFormat formatter = NumberFormat.getInstance();
		System.out.println("Indexed " + formatter.format(count) + " verses in " + watch.getTotalTimeSeconds() + " seconds");
		
		QueryResultList results = new QueryResultList();
		results.setQueryParser(new QueryParser("text", analyzer));
		results.setSearcher(new IndexSearcher(IndexReader.open(temp)));
		results = results.where("text:the");

		results.initialize();
		System.out.println("Query: " + results.getQuery());
		System.out.println("Sort: " + results.getSort());
		
		System.out.println(formatter.format(results.size()) + " result(s)");
		
		int index = 0;
		for (Result result : results) {
			if (index > 10) {
				break;
			}
			Document document = result.getDocument();
			String book = document.get("book");
			String chapter = document.get("chapter");
			String verse = document.get("verse");
			System.out.println();
			//System.out.println("Document ID: " + result.getDocumentId());
			index++;
		}
	}
	
}
