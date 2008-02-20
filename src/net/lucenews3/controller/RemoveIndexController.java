package net.lucenews3.controller;

import java.util.Map;

import net.lucenews3.lucene.support.Index;
import net.lucenews3.lucene.support.IndexIdentity;
import net.lucenews3.lucene.support.IndexIdentityParser;

import org.springframework.web.servlet.ModelAndView;

public class RemoveIndexController<I, O> implements Controller<I, O> {

	private IndexIdentityParser<I> indexIdentityParser;
	private Map<IndexIdentity, Index> indexesByIdentity;
	
	public ModelAndView handleRequest(I input, O output) throws Exception {
		final IndexIdentity identity = indexIdentityParser.parse(input);
		final Index index = indexesByIdentity.remove(identity);
		return new ModelAndView("index/removed", "index", index);
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
