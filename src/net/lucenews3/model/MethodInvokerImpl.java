package net.lucenews3.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.lucenews3.ExceptionTranslator;

public class MethodInvokerImpl implements MethodInvoker {

	private MethodResolver methodResolver;
	private ExceptionTranslator exceptionTranslator;
	
	public MethodInvokerImpl() {
		this(new NativeMethodResolver());
	}
	
	public MethodInvokerImpl(MethodResolver methodResolver) {
		this.methodResolver = methodResolver;
	}
	
	public MethodResolver getMethodResolver() {
		return methodResolver;
	}

	public void setMethodResolver(MethodResolver methodResolver) {
		this.methodResolver = methodResolver;
	}

	@Override
	public Object invoke(Object object, String methodName, Object... arguments) {
		Object result;
		
		Class<?> clazz = object.getClass();
		Class<?>[] parameterTypes = new Class<?>[ arguments.length ];
		for (int i = 0; i < arguments.length; i++) {
			parameterTypes[i] = arguments[i].getClass();
		}
		Method method;
		try {
			method = methodResolver.resolveMethod(clazz, methodName, parameterTypes);
			result = method.invoke(object, arguments);
		} catch (SecurityException e) {
			throw exceptionTranslator.translate(e);
		} catch (NoSuchMethodException e) {
			throw exceptionTranslator.translate(e);
		} catch (IllegalArgumentException e) {
			throw exceptionTranslator.translate(e);
		} catch (IllegalAccessException e) {
			throw exceptionTranslator.translate(e);
		} catch (InvocationTargetException e) {
			throw exceptionTranslator.translate(e);
		}
		
		return result;
	}

}
