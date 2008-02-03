package net.lucenews3.lucene.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.lucenews.http.ExceptionWrapper;

public class ReflectiveVisitor<T> implements Visitor<T> {

	private Method dispatchMethod;
	private ExceptionWrapper exceptionWrapper;
	
	public ReflectiveVisitor() {
		this(new DefaultExceptionWrapper());
	}
	
	public ReflectiveVisitor(ExceptionWrapper exceptionWrapper) {
		this.exceptionWrapper = exceptionWrapper;
		try {
			this.dispatchMethod = this.getClass().getMethod("visit", Object.class);
		} catch (SecurityException e) {
			throw this.exceptionWrapper.wrap(e);
		} catch (NoSuchMethodException e) {
			throw this.exceptionWrapper.wrap(e);
		}
	}

	/**
	 * Determines which method of the visitor to invoke.
	 * @param queryClass
	 * @return
	 */
	public Method visitationMethod(Class<? extends T> targetClass) {
		Method result = null;
		
		Class<?> thisClass = this.getClass();
		Class<?> currentTargetClass = targetClass;
		while (result == null && !result.equals(dispatchMethod)) {
			try {
				result = thisClass.getMethod("visit", currentTargetClass);
			} catch (SecurityException e) {
				throw exceptionWrapper.wrap(e);
			} catch (NoSuchMethodException e) {
				currentTargetClass = currentTargetClass.getSuperclass();
			}
		}
		
		if (result == null || result.equals(dispatchMethod)) {
			throw exceptionWrapper.wrap(new NoSuchMethodException("Object \"" + this + "\" does not implement visit(" + targetClass + ")"));
		}
		
		return result;
	}
	
	/**
	 * Delegates query visitation on to the most appropriate <code>visit</code>
	 * in the current object.
	 * @param query
	 * @return the result of invoking the appropriate visit(Query) method
	 */
	@SuppressWarnings("unchecked")
	public Object visit(T target) {
		Object result;
		
		Method method = visitationMethod((Class<? extends T>) target.getClass());
		
		try {
			result = method.invoke(this, target);
		} catch (IllegalArgumentException e) {
			throw exceptionWrapper.wrap(e);
		} catch (IllegalAccessException e) {
			throw exceptionWrapper.wrap(e);
		} catch (InvocationTargetException e) {
			throw exceptionWrapper.wrap(e);
		}
		
		return result;
	}
	
}
