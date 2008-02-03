package net.lucenews3.lucene.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.lucenews.http.ExceptionWrapper;

import org.apache.lucene.search.Query;

/**
 * A query visitor which shortens the workload
 * by taking advantage of Java reflection.
 *
 */
public class ReflectiveQueryVisitor implements QueryVisitor {

	private ExceptionWrapper exceptionWrapper;
	
	public ReflectiveQueryVisitor() {
		this(new DefaultExceptionWrapper());
	}
	
	public ReflectiveQueryVisitor(ExceptionWrapper exceptionWrapper) {
		this.exceptionWrapper = exceptionWrapper;
	}
	
	/**
	 * Determines which method of the visitor to invoke.
	 * @param queryClass
	 * @return
	 */
	public Method visitationMethod(Class<? extends Query> queryClass) {
		Method result = null;
		
		Class<? extends ReflectiveQueryVisitor> thisClass = this.getClass();
		Class<?> currentQueryClass = queryClass;
		while (result == null && !currentQueryClass.equals(Query.class)) {
			try {
				result = thisClass.getMethod("visit", currentQueryClass);
			} catch (SecurityException e) {
				throw exceptionWrapper.wrap(e);
			} catch (NoSuchMethodException e) {
				currentQueryClass = currentQueryClass.getSuperclass();
			}
		}
		
		if (result == null) {
			throw exceptionWrapper.wrap(new NoSuchMethodException("Object \"" + this + "\" does not implement visit(" + queryClass + ")"));
		}
		
		return result;
	}
	
	/**
	 * Delegates query visitation on to the most appropriate <code>visit</code>
	 * in the current object.
	 * @param query
	 * @return the result of invoking the appropriate visit(Query) method
	 */
	public Object visit(Query query) {
		Object result;
		
		System.err.println("Visiting query \"" + query + "\" (" + query.getClass() + ")");
		
		Method method = visitationMethod(query.getClass());
		
		try {
			result = method.invoke(this, query);
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
