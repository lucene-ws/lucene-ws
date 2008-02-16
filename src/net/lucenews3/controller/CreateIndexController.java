package net.lucenews3.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.lucene.support.Index;
import net.lucenews3.lucene.support.IndexImpl;
import net.lucenews3.lucene.support.IndexMetaData;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class CreateIndexController implements Controller {

	private List<Index> indexes;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// Retrieve meta data from request
		IndexMetaData metaData = null;
		
		// Construct an empty index with this meta data
		Index index = new IndexImpl(metaData);
		
		indexes.add(index);
		
		return null;
	}

}
