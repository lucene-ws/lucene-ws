package net.lucenews3.opensearch.transform;

import java.util.Calendar;
import java.util.List;

import net.lucenews3.Transformer;
import net.lucenews3.atom.Entry;
import net.lucenews3.atom.Feed;
import net.lucenews3.atom.FeedImpl;
import net.lucenews3.opensearch.Response;
import net.lucenews3.opensearch.Result;

public class AtomResponseTransformer implements Transformer<Response, Feed> {

	private Transformer<Result, Entry> resultTransformer;
	
	@Override
	public Feed transform(Response response) {
		final Feed feed = new FeedImpl();
		transform(response, feed);
		return feed;
	}

	@Override
	public void transform(Response response, Feed feed) {
		feed.setTitle(response.getTitle());
		feed.setUpdated(Calendar.getInstance());
		
		final List<Entry> entries = feed.getEntries();
		for (Result result : response.getResults()) {
			final Entry entry = resultTransformer.transform(result);
			entries.add(entry);
		}
	}
	
}
