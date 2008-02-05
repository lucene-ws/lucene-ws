package net.lucenews3.lucene;

public abstract class LazyImpl<T> implements Lazy<T> {

	private boolean initialized;
	private Provider<T> provider;
	protected T target;
	
	public LazyImpl() {
		
	}
	
	public LazyImpl(Provider<T> provider) {
		this.provider = provider;
	}
	
	@Override
	public Provider<T> getProvider() {
		return provider;
	}

	@Override
	public void setProvider(Provider<T> provider) {
		this.provider = provider;
	}
	
	@Override
	public boolean initialize() {
		boolean result;
		
		if (!initialized) {
			target = provider.provide();
			initialized = true;
			result = true;
		} else {
			result = false;
		}
		
		return result;
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}
	
	protected T getTarget() {
		initialize();
		return target;
	}

}
