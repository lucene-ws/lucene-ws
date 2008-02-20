package net.lucenews3.lucene.support;

import javax.servlet.http.HttpServletRequest;

public class HttpIndexRangeParser implements IndexRangeParser<HttpServletRequest> {

	@Override
	public IndexRange parse(HttpServletRequest request) {
		IndexRange result;
		
		String pageValue = request.getParameter("page");
		
		if (pageValue == null) {
			result = new Page(1, 20);
		} else {
			result = new Page(Integer.parseInt(pageValue), 20);
		}
		
		return result;
	}

}
