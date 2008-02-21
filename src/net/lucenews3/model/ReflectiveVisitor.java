package net.lucenews3.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;

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
	private ExceptionTranslator exceptionTranslator;
	private Logger logger;

	public ReflectiveVisitor() {
		this(new ExceptionTranslatorImpl());
	}

	public ReflectiveVisitor(MethodResolver methodResolver) {
		this(methodResolver, new ExceptionTranslatorImpl());
	}
	
	public ReflectiveVisitor(ExceptionTranslator exceptionTranslator) {
		this(new NativeMethodResolver(), exceptionTranslator);
	}
	
	/**
	 * Constructs a new instance using the given exception wrapper
	 * to wrap any checked exceptions encountered into runtime
	 * exceptions.
	 * @param exceptionWrapper
	 */
	public ReflectiveVisitor(MethodResolver methodResolver, ExceptionTranslator exceptionTranslator) {
		this.methodResolver = methodResolver;
		this.exceptionTranslator = exceptionTranslator;
		this.logger = Logger.getLogger("net.lucenews3.lucene.support");
		try {
			this.dispatchMethod = this.getClass().getMethod("visit", Object.class);
		} catch (SecurityException e) {
			throw this.exceptionTranslator.translate(e);
		} catch (NoSuchMethodException e) {
			throw this.exceptionTranslator.translate(e);
		}
	}

	public ExceptionTranslator getExceptionTranslator() {
		return exceptionTranslator;
	}

	public void setExceptionTranslator(ExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}

	public MethodResolver getMethodResolver() {
		return methodResolver;
	}

	public void setMethodResolver(MethodResolver methodResolver) {
		this.methodResolver = methodResolver;
	}

	public Method visitationMethod(Class<? extends T> targetClass) {
		return visitationMethod("visit", targetClass);
	}
	
	/**
	 * Determines which method of the visitor to invoke.
	 * 
	 * @param methodName the name of the visitation method
	 * @param queryClass
	 * @return
	 */
	public Method visitationMethod(String methodName, Class<? extends T> targetClass) {
		Method result;

		final Class<?> thisClass = this.getClass();
		try {
			result = methodResolver.resolveMethod(thisClass, methodName, targetClass);
		} catch (SecurityException e) {
			throw exceptionTranslator.translate(e);
		} catch (NoSuchMethodException e) {
			throw exceptionTranslator.translate(e);
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
			throw exceptionTranslator.translate(e);
		} catch (IllegalAccessException e) {
			throw exceptionTranslator.translate(e);
		} catch (InvocationTargetException e) {
			throw exceptionTranslator.translate(e);
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
