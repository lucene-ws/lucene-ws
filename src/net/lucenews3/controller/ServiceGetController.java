package net.lucenews3.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.ControllerSupport;

import org.springframework.web.servlet.ModelAndView;

public class ServiceGetController extends ControllerSupport {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView("service/get");
		model.addObject("indexes", service.getIndexes());
		return model;
	}

}
