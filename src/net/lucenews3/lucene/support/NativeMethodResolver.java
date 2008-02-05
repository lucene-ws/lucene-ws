package net.lucenews3.lucene.support;

import java.lang.reflect.Method;

public class NativeMethodResolver implements MethodResolver {

	public Method resolveMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws SecurityException, NoSuchMethodException {
		return clazz.getMethod(methodName, parameterTypes);
	}

}
