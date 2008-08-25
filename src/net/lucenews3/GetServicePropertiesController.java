package net.lucenews3;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
