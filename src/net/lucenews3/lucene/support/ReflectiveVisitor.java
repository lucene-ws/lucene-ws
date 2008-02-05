package net.lucenews3.lucene.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.lucenews.http.ExceptionWrapper;

/**
 * Implements the visitor pattern using Java's reflection facilities. Subclasses
 * overloads the {@link #visit(Object)} method with different parameter types.
 * When {@link #visit(Object)} is invoked, the class automatically determines
 * which of the <code>visit</code> methods is most appropriate for the
 * argument handed it and invokes that one.
 * 
 * @param <T>
 */
public abstract class ReflectiveVisitor<T> implements Visitor<T> {

	private MethodResolver methodResolver;
	private Method dispatchMethod;
	private ExceptionWrapper exceptionWrapper;
	private Logger logger;

	public ReflectiveVisitor() {
		this(new DefaultExceptionWrapper());
	}

	public ReflectiveVisitor(MethodResolver methodResolver) {
		this(methodResolver, new DefaultExceptionWrapper());
	}
	
	public ReflectiveVisitor(ExceptionWrapper exceptionWrapper) {
		this(new NativeMethodResolver(), exceptionWrapper);
	}
	
	/**
	 * Constructs a new instance using the given exception wrapper
	 * to wrap any checked exceptions encountered into runtime
	 * exceptions.
	 * @param exceptionWrapper
	 */
	public ReflectiveVisitor(MethodResolver methodResolver, ExceptionWrapper exceptionWrapper) {
		this.methodResolver = methodResolver;
		this.exceptionWrapper = exceptionWrapper;
		this.logger = Logger.getLogger("net.lucenews3.lucene.support");
		try {
			this.dispatchMethod = this.getClass().getMethod("visit", Object.class);
		} catch (SecurityException e) {
			throw this.exceptionWrapper.wrap(e);
		} catch (NoSuchMethodException e) {
			throw this.exceptionWrapper.wrap(e);
		}
	}

	public ExceptionWrapper getExceptionWrapper() {
		return exceptionWrapper;
	}

	public void setExceptionWrapper(ExceptionWrapper exceptionWrapper) {
		this.exceptionWrapper = exceptionWrapper;
	}

	public MethodResolver getMethodResolver() {
		return methodResolver;
	}

	public void setMethodResolver(MethodResolver methodResolver) {
		this.methodResolver = methodResolver;
	}

	/**
	 * Determines which method of the visitor to invoke.
	 * 
	 * @param queryClass
	 * @return
	 */
	public Method visitationMethod(Class<? extends T> targetClass) {
		Method result;

		final Class<?> thisClass = this.getClass();
		final String methodName = "visit";
		try {
			result = methodResolver.resolveMethod(thisClass, methodName, targetClass);
		} catch (SecurityException e) {
			throw exceptionWrapper.wrap(e);
		} catch (NoSuchMethodException e) {
			throw exceptionWrapper.wrap(e);
		}
		
		if (result.equals(dispatchMethod) && logger.isLoggable(Level.WARNING)) {
			logger.warning("Visitation method is the dispatch method. May cause infinite loop. Please ensure "
							+ thisClass.getCanonicalName()
							+ " implements the method: \"public Object visit("
							+ targetClass.getCanonicalName() + ")\"");
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public Object dispatchVisit(T target) {
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
	
	/**
	 * Invokes {@link #dispatchVisit(Object)}
	 */
	public Object visit(T target) {
		return dispatchVisit(target);
	}

}
