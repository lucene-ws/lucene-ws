package net.lucenews3;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.document.Document;

public class DefaultIndexRepository implements IndexRepository {

	private Map<String, Index> indexesByName;

	@Override
	public Index getIndex(String key) throws NoSuchIndexException {
		// TODO Auto-generated method stub
		return new Index(){

			@Override
			public String addDocument(Document document) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getDisplayName() {
				// TODO Auto-generated method stub
				return "carol";
			}

			@Override
			public Document getDocument(String key) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Results search(HttpServletRequest req) {
				// TODO Auto-generated method stub
				return new DefaultResults(null);
			}

			@Override
			public String updateDocument(Document document) {
				// TODO Auto-generated method stub
				return null;
			}};
	}

	@Override
	public void putIndex(String key, Index index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<Index> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
