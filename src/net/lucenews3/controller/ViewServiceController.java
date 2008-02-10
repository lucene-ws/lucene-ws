package net.lucenews3.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.Controller;

public class ViewServiceController implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return new ModelAndView(new View(){

			public String getContentType() {
				return "text/html";
			}

			public void render(Map arg0, HttpServletRequest arg1, HttpServletResponse arg2) throws Exception {
				arg2.getWriter().print("service view");
			}});
	}

}
