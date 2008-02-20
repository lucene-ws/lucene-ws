package net.lucenews3.controller;

import org.springframework.web.servlet.ModelAndView;

public class ViewServiceController<I, O> implements Controller<I, O> {

	@Override
	public ModelAndView handleRequest(I input, O output) throws Exception {
		return new ModelAndView("service/view");
	}

}
