package net.lucenews3.controller;

import java.util.Map;

import net.lucenews3.lucene.support.Index;
import net.lucenews3.lucene.support.IndexIdentity;
import net.lucenews3.lucene.support.IndexIdentityParser;
import net.lucenews3.lucene.support.IndexImpl;
import net.lucenews3.lucene.support.IndexMetaData;
import net.lucenews3.lucene.support.IndexMetaDataParser;

import org.springframework.web.servlet.ModelAndView;

public class CreateIndexController<I, O> implements Controller<I, O> {

	private IndexIdentityParser<I> indexIdentityParser;
	private IndexMetaDataParser<I> indexMetaDataParser;
	private Map<IndexIdentity, Index> indexesByIdentity;
	
	@Override
	public ModelAndView handleRequest(I input, O output) throws Exception {
		final IndexIdentity indexIdentity = indexIdentityParser.parse(input);
		final IndexMetaData indexMetaData = indexMetaDataParser.parse(input);
		final Index index = new IndexImpl(indexMetaData);
		
		indexesByIdentity.put(indexIdentity, index);
		
		final ModelAndView result = new ModelAndView("index/create");
		result.addObject("indexIdentity", indexIdentity);
		result.addObject("indexMetaData", indexMetaData);
		result.addObject("index", index);
		return result;
	}

	public IndexIdentityParser<I> getIndexIdentityParser() {
		return indexIdentityParser;
	}

	public void setIndexIdentityParser(IndexIdentityParser<I> indexIdentityParser) {
		this.indexIdentityParser = indexIdentityParser;
	}

	public IndexMetaDataParser<I> getIndexMetaDataParser() {
		return indexMetaDataParser;
	}

	public void setIndexMetaDataParser(IndexMetaDataParser<I> indexMetaDataParser) {
		this.indexMetaDataParser = indexMetaDataParser;
	}

	public Map<IndexIdentity, Index> getIndexesByIdentity() {
		return indexesByIdentity;
	}

	public void setIndexesByIdentity(Map<IndexIdentity, Index> indexesByIdentity) {
		this.indexesByIdentity = indexesByIdentity;
	}

}
