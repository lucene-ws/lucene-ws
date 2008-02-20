package net.lucenews3.controller;

import java.util.Map;

import net.lucenews3.lucene.support.Index;
import net.lucenews3.lucene.support.IndexIdentity;
import net.lucenews3.lucene.support.IndexIdentityParser;
import net.lucenews3.lucene.support.IndexImpl;
import net.lucenews3.lucene.support.IndexMetaData;

import org.springframework.web.servlet.ModelAndView;

public class CreateIndexController<I, O> implements Controller<I, O> {

	private IndexIdentityParser<I> indexIdentityParser;
	private Map<IndexIdentity, Index> indexesByIdentity;
	
	@Override
	public ModelAndView handleRequest(I input, O output) throws Exception {
		final IndexIdentity identity = null;
		
		// Retrieve meta data from request
		final IndexMetaData metaData = null;
		
		// Construct an empty index with this meta data
		final Index index = new IndexImpl(metaData);
		
		indexesByIdentity.put(identity, index);
		
		return new ModelAndView("index/create", "index", index);
	}

	public IndexIdentityParser<I> getIndexIdentityParser() {
		return indexIdentityParser;
	}

	public void setIndexIdentityParser(IndexIdentityParser<I> indexIdentityParser) {
		this.indexIdentityParser = indexIdentityParser;
	}

	public Map<IndexIdentity, Index> getIndexesByIdentity() {
		return indexesByIdentity;
	}

	public void setIndexesByIdentity(Map<IndexIdentity, Index> indexesByIdentity) {
		this.indexesByIdentity = indexesByIdentity;
	}

}
