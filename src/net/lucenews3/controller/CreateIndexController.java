package net.lucenews3.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.lucene.support.Index;
import net.lucenews3.lucene.support.IndexIdentity;
import net.lucenews3.lucene.support.IndexImpl;
import net.lucenews3.lucene.support.IndexMetaData;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class CreateIndexController implements Controller {

	private Map<IndexIdentity, Index> indexesByIdentity;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final IndexIdentity identity = null;
		
		// Retrieve meta data from request
		final IndexMetaData metaData = null;
		
		// Construct an empty index with this meta data
		final Index index = new IndexImpl(metaData);
		
		indexesByIdentity.put(identity, index);
		
		return new ModelAndView("index/create", "index", index);
	}

}
