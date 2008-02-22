package net.lucenews3.opensearch.transform;

import net.lucenews3.Transformer;
import net.lucenews3.atom.Entry;
import net.lucenews3.atom.EntryImpl;
import net.lucenews3.opensearch.Result;

public class AtomResultTransformer implements Transformer<Result, Entry> {

	@Override
	public Entry transform(Result result) {
		final Entry entry = new EntryImpl();
		transform(result, entry);
		return entry;
	}

	@Override
	public void transform(Result result, Entry entry) {
		
	}

}
