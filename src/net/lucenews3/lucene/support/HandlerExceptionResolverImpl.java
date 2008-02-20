package net.lucenews3.lucene.support;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

public class HandlerExceptionResolverImpl implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, final Exception exception) {
		// TODO Auto-generated method stub
		return new ModelAndView(new View(){

			@Override
			public String getContentType() {
				// TODO Auto-generated method stub
				return "text/plain";
			}

			@SuppressWarnings("unchecked")
			@Override
			public void render(Map arg0, HttpServletRequest arg1,
					HttpServletResponse response) throws Exception {
				PrintWriter out = response.getWriter();
				out.println("What did you do? Here's what:");
				exception.printStackTrace(out);
				out.flush();
			}});
	}

}
