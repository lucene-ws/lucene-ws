package net.lucenews3.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

public class ViewServiceController extends AbstractController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		
		return new ModelAndView(new View(){

			public String getContentType() {
				return "text/html";
			}

			@SuppressWarnings("unchecked")
			public void render(Map arg0, HttpServletRequest arg1, HttpServletResponse arg2) throws Exception {
				arg2.getWriter().print("service view");
			}});
	}

}
