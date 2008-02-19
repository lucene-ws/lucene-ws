package net.lucenews3.lucene.support;

import java.util.HashMap;
import java.util.Map;

public class IndexRepositoryImpl extends HashMap<IndexIdentity, Index> implements IndexRepository {

	private static final long serialVersionUID = -2217943915091467426L;

	public IndexRepositoryImpl() {
		super();
	}

	public IndexRepositoryImpl(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public IndexRepositoryImpl(int initialCapacity) {
		super(initialCapacity);
	}

	public IndexRepositoryImpl(Map<? extends IndexIdentity, ? extends Index> m) {
		super(m);
	}

}
