package net.lucenews3.lucene.support;

import java.lang.reflect.Method;

/**
 * Resolves a method on a class at run time.
 *
 */
public interface MethodResolver {

	public Method resolveMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws SecurityException, NoSuchMethodException;
	
}
