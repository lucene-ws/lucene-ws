package net.lucenews3;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ExceptionTranslatorImpl implements ExceptionTranslator {

	private Map<Class<Throwable>, Class<RuntimeException>> customMappings;
	
	public ExceptionTranslatorImpl() {
		this.customMappings = new HashMap<Class<Throwable>, Class<RuntimeException>>();
	}
	
	public ExceptionTranslatorImpl(Map<Class<Throwable>, Class<RuntimeException>> customMappings) {
		this.customMappings = customMappings;
	}
	
	@Override
	public RuntimeException translate(Throwable cause) {
		RuntimeException result;
		
		Class<?> causeClass = cause.getClass();
		if (customMappings.containsKey(cause.getClass())) {
			try {
				Class<?> resultClass = customMappings.get(causeClass);
				Constructor<?> constructor;
				constructor = resultClass.getConstructor(Throwable.class);
				result = (RuntimeException) constructor.newInstance(cause);
			} catch (SecurityException e) {
				result = new RuntimeException(cause);
			} catch (NoSuchMethodException e) {
				result = new RuntimeException(cause);
			} catch (IllegalArgumentException e) {
				result = new RuntimeException(cause);
			} catch (InstantiationException e) {
				result = new RuntimeException(cause);
			} catch (IllegalAccessException e) {
				result = new RuntimeException(cause);
			} catch (InvocationTargetException e) {
				result = new RuntimeException(cause);
			} catch (NullPointerException e) {
				result = new RuntimeException(cause);
			}
		} else {
			result = new RuntimeException(cause);
		}
		
		return result;
	}
	
	@Override
	public Throwable unwrap(Throwable cause) {
		Throwable result = cause;
		while (result.getCause() != null) {
			result = result.getCause();
		}
		return result;
	}

	public Map<Class<Throwable>, Class<RuntimeException>> getCustomMappings() {
		return customMappings;
	}

	public void setCustomMappings(Map<Class<Throwable>, Class<RuntimeException>> customMappings) {
		this.customMappings = customMappings;
	}

}
