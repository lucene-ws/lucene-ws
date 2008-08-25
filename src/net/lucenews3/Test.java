package net.lucenews3;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;

public class Test {

	public static void main(String... args) throws Exception {
		IndexWriter writer = new IndexWriter("/home/adam/indexes/test", new StandardAnalyzer(), true);
		try {
			Document document = new Document();
			document.add(new Field("name", "Bob", Field.Store.YES, Field.Index.TOKENIZED));
			writer.addDocument(document);
		} finally {
			writer.close();
		}
	}

}
