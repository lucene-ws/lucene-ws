package net.lucenews3.model;

import javax.servlet.http.HttpServletRequest;

public class HttpDocumentIdentityParser implements DocumentIdentityParser<HttpServletRequest> {

	@Override
	public DocumentIdentity parse(HttpServletRequest request) {
		return new DocumentIdentityImpl(request.getAttribute("documentId"));
	}

}
