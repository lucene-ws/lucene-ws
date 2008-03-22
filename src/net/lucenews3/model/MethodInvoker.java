package net.lucenews3.model;

import java.lang.reflect.Method;

public interface MethodInvoker {
	
	public Object invoke(Object object, Method method, Object... arguments);

	public Object invoke(Class<?> clazz, String methodName, Object... arguments)
		throws NoSuchMethodRuntimeException;
	
	public Object invoke(Object object, String methodName, Object... arguments)
		throws NoSuchMethodRuntimeException;
	
}
