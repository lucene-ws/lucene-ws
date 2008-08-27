package net.lucenews3.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import net.lucenews3.ControllerSupport;
import net.lucenews3.Index;

public class IndexPropertiesGetController extends ControllerSupport {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView("indexProperties/get");
		
		// TODO Auto-generated method stub
		Index index = service.getIndex(request);
		Properties properties = index.getProperties();
		model.addObject("properties", properties);
		
		return model;
	}

}
