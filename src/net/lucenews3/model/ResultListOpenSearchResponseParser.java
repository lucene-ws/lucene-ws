package net.lucenews3.model;

import net.lucenews.atom.Link;
import net.lucenews3.opensearch.Query;
import net.lucenews3.opensearch.QueryImpl;
import net.lucenews3.opensearch.Response;
import net.lucenews3.opensearch.ResponseImpl;

public class ResultListOpenSearchResponseParser implements OpenSearchResponseParser<ResultList> {

	@Override
	public Response parse(ResultList results) {
		final Response response = new ResponseImpl();
		
		Query query = new QueryImpl();
		response.getQueries().add(query);

		// link to OpenSearch Description
		Link descriptionLink = new Link();
		descriptionLink.setHref("http://description");
		descriptionLink.setRel("search");
		descriptionLink.setType("application/opensearchdescription+xml");
		response.setLink(descriptionLink);
		
		for (net.lucenews3.model.Result r : results) {
			response.getResults().add(parse(r));
		}
		
		return response;
	}
	
	public net.lucenews3.opensearch.Result parse(net.lucenews3.model.Result r) {
		return null;
	}

}
