package net.lucenews3.model;

import net.lucenews3.opensearch.Response;

public interface OpenSearchResponseParser<I> extends Parser<I, Response> {

	@Override
	public Response parse(I input);
	
}
