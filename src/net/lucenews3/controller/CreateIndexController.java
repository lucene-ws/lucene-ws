package net.lucenews3.controller;

import java.util.Map;

import net.lucenews3.model.Index;
import net.lucenews3.model.IndexIdentity;
import net.lucenews3.model.IndexIdentityParser;
import net.lucenews3.model.IndexImpl;
import net.lucenews3.model.IndexMetaData;
import net.lucenews3.model.IndexMetaDataParser;

import org.springframework.web.servlet.ModelAndView;

public class CreateIndexController<I, O> implements Controller<I, O> {

	private IndexIdentityParser<I> indexIdentityParser;
	private IndexMetaDataParser<I> indexMetaDataParser;
	private Map<IndexIdentity, Index> indexesByIdentity;
	
	@Override
	public ModelAndView handleRequest(I input, O output) throws Exception {
		IndexIdentity indexIdentity = indexIdentityParser.parse(input);
		IndexMetaData indexMetaData = indexMetaDataParser.parse(input);
		Index index = new IndexImpl(indexMetaData);
		
		indexesByIdentity.put(indexIdentity, index);
		
		ModelAndView model = new ModelAndView("index/create");
		model.addObject("indexIdentity", indexIdentity);
		model.addObject("indexMetaData", indexMetaData);
		model.addObject("index", index);
		return model;
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
