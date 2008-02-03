package net.lucenews3.lucene.support;

import net.lucenews.http.ExceptionWrapper;

public class DefaultExceptionWrapper implements ExceptionWrapper {

	/**
	 * Returns a new instance of <code>RuntimeException</code> constructed via
	 * the {@link RuntimeException#RuntimeException(Throwable)} constructor.
	 * @param exception the exception to wrap
	 * @return a RuntimeException instance
	 */
	public RuntimeException wrap(Exception exception) {
		return new RuntimeException(exception);
	}

}
