package net.lucenews3;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class GetServiceController extends ControllerSupport {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView("service/get");
		model.addObject("indexes", service.getIndexes());
		return model;
	}

}
