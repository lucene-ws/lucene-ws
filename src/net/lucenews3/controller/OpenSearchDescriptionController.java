package net.lucenews3.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import net.lucenews3.ControllerSupport;
import net.lucenews3.Index;

public class OpenSearchDescriptionController extends ControllerSupport {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("openSearch/description");
		
		Index index = service.getIndex(request);
		modelAndView.addObject("index", index);
		
		return modelAndView;
	}

}
