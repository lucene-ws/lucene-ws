package net.lucenews3.http;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestBaseUrlParser extends HttpRequestUrlParser {

	@Override
	public Url parse(HttpServletRequest request) {
		Url result = super.parse(request);
		result.getParameters().clear();
		
		// TODO Make this work in the general case
		while (result.getPath().size() > 1) {
			result.getPath().removeLast();
		}
		
		return result;
	}
	
}
