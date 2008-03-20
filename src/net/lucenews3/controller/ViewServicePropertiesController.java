package net.lucenews3.controller;

import net.lucenews3.Service;

import org.springframework.web.servlet.ModelAndView;

public class ViewServicePropertiesController<I, O> implements Controller<I, O> {

	private Service service;
	
	@Override
	public ModelAndView handleRequest(I input, O output) throws Exception {
		final ModelAndView result = new ModelAndView("serviceProperties/view");
		result.addObject("properties", service.getProperties());
		return result;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

}
