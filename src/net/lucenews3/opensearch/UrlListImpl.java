package net.lucenews3.opensearch;

import java.util.ArrayList;
import java.util.Collection;

public class UrlListImpl extends ArrayList<Url> implements UrlList {

	private static final long serialVersionUID = 9015838178926535468L;

	public UrlListImpl() {
		super();
	}

	public UrlListImpl(Collection<? extends Url> collection) {
		super(collection);
	}

	public UrlListImpl(int initialCapacity) {
		super(initialCapacity);
	}

}
