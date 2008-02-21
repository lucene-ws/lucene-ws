package net.lucenews3.model;

import javax.servlet.http.HttpServletRequest;

public class HttpIndexIdentityParser implements IndexIdentityParser<HttpServletRequest> {

	@Override
	public IndexIdentity parse(HttpServletRequest request) {
		return new IndexIdentityImpl(request.getAttribute("indexName"));
	}

}
