package net.lucenews3.opensearch;

import java.util.ArrayList;
import java.util.Collection;

public class ImageListImpl extends ArrayList<Image> implements ImageList {

	private static final long serialVersionUID = -402587589271801004L;

	public ImageListImpl() {
		super();
	}

	public ImageListImpl(Collection<? extends Image> collection) {
		super(collection);
	}

	public ImageListImpl(int initialCapacity) {
		super(initialCapacity);
	}

}
