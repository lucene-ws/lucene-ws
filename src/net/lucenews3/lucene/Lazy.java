package net.lucenews3.lucene;

public interface Lazy<T> {

	public Provider<T> getProvider();
	
	public void setProvider(Provider<T> provider);
	
	/**
	 * Determines whether or not this object's value
	 * has been initialized.
	 * @return
	 */
	public boolean isInitialized();
	
	/**
	 * Initializes the object only if it has not yet
	 * been initialized before.
	 * @return
	 */
	public boolean initialize();
	
}
