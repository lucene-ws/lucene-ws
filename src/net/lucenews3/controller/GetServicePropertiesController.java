package net.lucenews3.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lucenews3.ControllerSupport;

import org.springframework.web.servlet.ModelAndView;

public class GetServicePropertiesController extends ControllerSupport {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		ModelAndView model = new ModelAndView("serviceProperties/get");
		
		Properties properties = new Properties();
		properties.put("name", "Bill");
		model.addObject("properties", properties);
		
		return model;
	}

}
