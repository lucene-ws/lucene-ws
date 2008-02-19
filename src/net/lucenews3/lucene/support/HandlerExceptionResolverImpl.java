package net.lucenews3.lucene.support;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

public class HandlerExceptionResolverImpl implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exception) {
		// TODO Auto-generated method stub
		return new ModelAndView(new View(){

			@Override
			public String getContentType() {
				// TODO Auto-generated method stub
				return "text/plain";
			}

			@Override
			public void render(Map arg0, HttpServletRequest arg1,
					HttpServletResponse response) throws Exception {
				response.getWriter().println("What did you do?");
			}});
	}

}
