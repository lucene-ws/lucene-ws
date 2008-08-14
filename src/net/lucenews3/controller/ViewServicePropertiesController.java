package net.lucenews3.controller;

import net.lucenews3.Service;

import org.springframework.web.servlet.ModelAndView;

public class ViewServicePropertiesController<I, O> implements Controller<I, O> {

	private Service service;
	
	@Override
	public ModelAndView handleRequest(I input, O output) throws Exception {
		ModelAndView model = new ModelAndView("serviceProperties/view");
		model.addObject("properties", service.getProperties());
		return model;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

}
