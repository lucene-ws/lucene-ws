package net.lucenews3.model;

import net.lucenews3.Transformer;
import net.lucenews3.atom.Entry;
import net.lucenews3.atom.EntryImpl;
import net.lucenews3.opensearch.Result;

public class OpenSearchResultToEntryTransformer implements Transformer<Result, Entry> {

	private FieldListToXoxoTransformer xoxoTransformer;
	
	public OpenSearchResultToEntryTransformer() {
		this.xoxoTransformer = new FieldListToXoxoTransformer();
	}
	
	@Override
	public Entry transform(Result result) {
		final Entry entry = new EntryImpl();
		transform(result, entry);
		return entry;
	}

	@Override
	public void transform(Result result, Entry entry) {
		entry.setId(result.getId());
		entry.setTitle(result.getTitle());
		entry.setContent(result.getContent());
		entry.setLinks(result.getLinks());
	}

}
