package net.lucenews3.lucene.support;

import javax.servlet.http.HttpServletRequest;

public class HttpIndexIdentityParser implements IndexIdentityParser<HttpServletRequest> {

	@Override
	public IndexIdentity parse(HttpServletRequest request) {
		return new IndexIdentityImpl(request.getAttribute("indexName"));
	}

}
