package net.lucenews3.model;

import java.lang.reflect.Method;

/**
 * Provides sensible method resolution, much like the Java compiler's
 * algorithm.
 *
 */
public class AdaptiveMethodResolver implements MethodResolver {

	@Override
	public Method resolveMethod(Class<?> clazz, String methodName,
			Class<?>... parameterTypes) throws SecurityException,
			NoSuchMethodException {
		final Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			
		}
		return null;
	}

}
