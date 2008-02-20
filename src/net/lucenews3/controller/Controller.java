package net.lucenews3.controller;

import org.springframework.web.servlet.ModelAndView;

public interface Controller<I, O> {

	public ModelAndView handleRequest(I input, O output) throws Exception;
	
}
