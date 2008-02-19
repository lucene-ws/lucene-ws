package net.lucenews3.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.lucene.support.Index;
import net.lucenews3.lucene.support.IndexIdentity;
import net.lucenews3.lucene.support.IndexIdentityParser;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class RemoveIndexController implements Controller {

	private IndexIdentityParser<HttpServletRequest> indexIdentityParser;
	private Map<IndexIdentity, Index> indexesByIdentity;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final IndexIdentity identity = indexIdentityParser.parse(request);
		final Index index = indexesByIdentity.remove(identity);
		return new ModelAndView("index/removed", "index", index);
	}

	public IndexIdentityParser<HttpServletRequest> getIndexIdentityParser() {
		return indexIdentityParser;
	}

	public void setIndexIdentityParser(
			IndexIdentityParser<HttpServletRequest> indexIdentityParser) {
		this.indexIdentityParser = indexIdentityParser;
	}

	public Map<IndexIdentity, Index> getIndexesByIdentity() {
		return indexesByIdentity;
	}

	public void setIndexesByIdentity(Map<IndexIdentity, Index> indexesByIdentity) {
		this.indexesByIdentity = indexesByIdentity;
	}

}
