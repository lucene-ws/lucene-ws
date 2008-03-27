package net.lucenews3.model;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;

public class MethodInvokerImpl implements MethodInvoker {

	private MethodResolver methodResolver;
	private Caster caster;
	private ExceptionTranslator exceptionTranslator;
	
	public MethodInvokerImpl() {
		this(new AdaptiveMethodResolver());
	}
	
	public MethodInvokerImpl(MethodResolver methodResolver) {
		this.methodResolver = methodResolver;
		this.caster = new CasterImpl();
		this.exceptionTranslator = new ExceptionTranslatorImpl();
	}
	
	public MethodResolver getMethodResolver() {
		return methodResolver;
	}

	public void setMethodResolver(MethodResolver methodResolver) {
		this.methodResolver = methodResolver;
	}

	public Class<?>[] getParameterTypes(Object... arguments) {
		final Class<?>[] parameterTypes = new Class<?>[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			parameterTypes[i] = getParameterType(arguments[i]);
		}
		return parameterTypes;
	}

	/**
	 * Retrieves the type of the given parameter.
	 * 
	 * @param argument
	 * @return the result of {@link Object#getClass()} if non-null, null otherwise
	 */
	public Class<?> getParameterType(Object argument) {
		final Class<?> result;
		
		if (argument == null) {
			result = null;
		} else {
			result = argument.getClass();
		}
		
		return result;
	}

	@Override
	public Object invoke(Object object, Method method, Object... arguments) {
		final Object result;
		
		final Object[] invokationArguments = getInvokationArguments(method, arguments);
		
		try {
			result = method.invoke(object, invokationArguments);
		} catch (IllegalArgumentException e) {
			throw exceptionTranslator.translate(e);
		} catch (IllegalAccessException e) {
			throw exceptionTranslator.translate(e);
		} catch (InvocationTargetException e) {
			throw exceptionTranslator.translate(e);
		}
		
		return result;
	}
	
	@Override
	public Object invoke(Object object, String methodName, Object... arguments) {
		final Object result;
		
		final Class<?> clazz = object.getClass();
		final Class<?>[] parameterTypes = getParameterTypes(arguments);
		final Method method;
		
		try {
			method = methodResolver.resolveMethod(clazz, methodName, parameterTypes);
		} catch (SecurityException e) {
			throw exceptionTranslator.translate(e);
		} catch (NoSuchMethodException e) {
			throw exceptionTranslator.translate(e);
		}
		
		result = invoke(object, method, arguments);
		
		return result;
	}

	@Override
	public Object invoke(Class<?> clazz, String methodName, Object... arguments)
			throws NoSuchMethodRuntimeException {
		final Object result;
		
		final Class<?>[] parameterTypes = getParameterTypes(arguments);
		final Method method;
		try {
			method = methodResolver.resolveMethod(clazz, methodName, parameterTypes);
		} catch (SecurityException e) {
			throw exceptionTranslator.translate(e);
		} catch (NoSuchMethodException e) {
			throw exceptionTranslator.translate(e);
		}
		
		final Object object;
		if (Modifier.isStatic(method.getModifiers())) {
			object = null;
		} else {
			try {
				object = clazz.newInstance();
			} catch (InstantiationException e) {
				throw exceptionTranslator.translate(e);
			} catch (IllegalAccessException e) {
				throw exceptionTranslator.translate(e);
			}
		}
		
		result = invoke(object, method, arguments);
		
		return result;
	}
	
	/**
	 * Produces an array of arguments suitable to be passed to 
	 * {@link Method#invoke(Object, Object...)}. Honours vararg arguments.
	 * 
	 * @param method
	 * @param arguments
	 * @return
	 */
	public Object[] getInvokationArguments(Method method, Object... arguments) {
		final Object[] results;
		
		if (method.isVarArgs()) {
			final Class<?>[] types = method.getParameterTypes();
			final Class<?>   variableType = types[types.length - 1];
			final int standardArgumentCount = types.length - 1;
			final int variableArgumentCount = arguments.length - types.length + 1;
			final int variableArgumentOffset = standardArgumentCount;
			
			results = new Object[types.length];
			
			if (variableType.isArray()) {
				
				// Copy the standard arguments directly into the results
				System.arraycopy(arguments, 0, results, 0, standardArgumentCount);
				
				final Class<?> variableComponentType = variableType.getComponentType();
				
				if (variableArgumentCount == 1) {
					// We must tip-toe around this issue very carefully (ambiguous)
					// If it happens to be an array of the same type as the var-arg
					// array, we will simple use it. :)
					final Object variableArgument = arguments[arguments.length - 1];
					if (variableArgument == null) {
						results[results.length - 1] = null;
					} else if (variableType.isAssignableFrom(variableArgument.getClass())) {
						results[results.length - 1] = variableArgument;
					} else {
						final Object variableArgumentArray = Array.newInstance(variableComponentType, 1);
						Array.set(variableArgumentArray, 0, variableArgument);
						results[results.length - 1] = variableArgumentArray;
					}
				} else {
					final Object variableArgumentArray = Array.newInstance(variableComponentType, variableArgumentCount);
					
					if (variableArgumentArray.equals(results.getClass())) {
						System.arraycopy(arguments, variableArgumentOffset, variableArgumentArray, 0, variableArgumentCount);
					} else {
						for (int i = 0; i < variableArgumentCount; i++) {
							Array.set(variableArgumentArray, i, Array.get(arguments, variableArgumentOffset + i));
						}
					}
					
					results[results.length - 1] = variableArgumentArray;
				}
				
			} else {
				throw new RuntimeException("Method claims to be var-arg, but does not accept array as last parameter");
			}
		} else {
			results = arguments;
		}
		
		return results;
	}
	
	public Caster getCaster() {
		return caster;
	}

	public void setCaster(Caster caster) {
		this.caster = caster;
	}

	public ExceptionTranslator getExceptionTranslator() {
		return exceptionTranslator;
	}

	public void setExceptionTranslator(ExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}

}
