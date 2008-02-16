package net.lucenews3.lucene.support;

import javax.servlet.http.HttpServletRequest;

public class HttpIndexKeyParser implements IndexKeyParser<HttpServletRequest> {

	@Override
	public Object parse(HttpServletRequest request) {
		return null;
	}

}
