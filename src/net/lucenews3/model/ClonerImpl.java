package net.lucenews3.model;

import org.springframework.beans.BeanUtils;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;

public class ClonerImpl implements Cloner {

	private MethodInvoker methodInvoker;
	private ExceptionTranslator exceptionTranslator;
	
	public ClonerImpl() {
		this.methodInvoker = new MethodInvokerImpl();
		this.exceptionTranslator = new ExceptionTranslatorImpl();
	}
	
	public ClonerImpl(MethodInvoker methodInvoker) {
		this.methodInvoker = methodInvoker;
		this.exceptionTranslator = new ExceptionTranslatorImpl();
	}
	
	public MethodInvoker getMethodInvoker() {
		return methodInvoker;
	}

	public void setMethodInvoker(MethodInvoker methodInvoker) {
		this.methodInvoker = methodInvoker;
	}

	public ExceptionTranslator getExceptionTranslator() {
		return exceptionTranslator;
	}

	public void setExceptionTranslator(ExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T clone(T source) {
		T result;
		
		try {
			result = (T) methodInvoker.invoke(source, "clone");
		} catch (NoSuchMethodRuntimeException e) {
			result = reconstruct(source);
		} catch (SecurityException e) {
			result = reconstruct(source);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T reconstruct(T source) {
		T result;
		
		try {
			Class<T> sourceClass = (Class<T>) source.getClass();
			result = sourceClass.newInstance();
			BeanUtils.copyProperties(source, result);
		} catch (InstantiationException e) {
			throw exceptionTranslator.translate(e);
		} catch (IllegalAccessException e) {
			throw exceptionTranslator.translate(e);
		}
		
		return result;
	}

}
