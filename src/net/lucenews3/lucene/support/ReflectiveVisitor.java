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

	private Method dispatchMethod;
	private ExceptionWrapper exceptionWrapper;
	private Logger logger;

	public ReflectiveVisitor() {
		this(new DefaultExceptionWrapper());
	}

	/**
	 * Constructs a new instance using the given exception wrapper
	 * to wrap any checked exceptions encountered into runtime
	 * exceptions.
	 * @param exceptionWrapper
	 */
	public ReflectiveVisitor(ExceptionWrapper exceptionWrapper) {
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

	/**
	 * Determines which method of the visitor to invoke.
	 * 
	 * @param queryClass
	 * @return
	 */
	public Method visitationMethod(Class<? extends T> targetClass) {
		Method result = null;

		Class<?> thisClass = this.getClass();
		Class<?> currentTargetClass = targetClass;
		while (result == null && currentTargetClass != null) {
			try {
				result = thisClass.getMethod("visit", currentTargetClass);
			} catch (SecurityException e) {
				throw exceptionWrapper.wrap(e);
			} catch (NoSuchMethodException e) {
				currentTargetClass = currentTargetClass.getSuperclass();
			}
		}

		if (result == null) {
			throw exceptionWrapper.wrap(new NoSuchMethodException("Object \"" + this
							+ "\" does not implement visit(" + targetClass
							+ ")"));
		} else if (result.equals(dispatchMethod)) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Visitation method is the dispatch method. May cause infinite loop. Please ensure "
								+ thisClass.getCanonicalName()
								+ " implements the method: \"public Object visit("
								+ targetClass.getCanonicalName() + ")\"");
			}
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
