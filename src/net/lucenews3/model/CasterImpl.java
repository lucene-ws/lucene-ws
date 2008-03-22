package net.lucenews3.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;

public class CasterImpl implements Caster {

	private static Map<Class<?>, Class<?>> wrapperClasses = new HashMap<Class<?>, Class<?>>();
	private static Map<Class<?>, Class<?>> primitiveClasses = new HashMap<Class<?>, Class<?>>();
	private static Map<Entry<Class<?>, Class<?>>, Method> castMethods = new HashMap<Map.Entry<Class<?>, Class<?>>, Method>();
	
	private ExceptionTranslator exceptionTranslator;
	private Logger logger;
	
	public CasterImpl() {
		this.logger = Logger.getLogger(getClass());
		this.exceptionTranslator = new ExceptionTranslatorImpl();
		
		synchronized (wrapperClasses) {
			if (wrapperClasses.isEmpty()) {
				wrapperClasses.put(boolean.class, Boolean.class);
				wrapperClasses.put(char.class,    Character.class);
				wrapperClasses.put(byte.class,    Byte.class);
				wrapperClasses.put(short.class,   Short.class);
				wrapperClasses.put(int.class,     Integer.class);
				wrapperClasses.put(long.class,    Long.class);
				wrapperClasses.put(float.class,   Float.class);
				wrapperClasses.put(double.class,  Double.class);
				wrapperClasses.put(void.class,    Void.class);
			}
		}
		
		synchronized (primitiveClasses) {
			if (primitiveClasses.isEmpty()) {
				primitiveClasses = new HashMap<Class<?>, Class<?>>();
				primitiveClasses.put(Boolean.class,   boolean.class);
				primitiveClasses.put(Character.class, char.class);
				primitiveClasses.put(Byte.class,      byte.class);
				primitiveClasses.put(Short.class,     short.class);
				primitiveClasses.put(Integer.class,   int.class);
				primitiveClasses.put(Long.class,      long.class);
				primitiveClasses.put(Float.class,     float.class);
				primitiveClasses.put(Double.class,    double.class);
				primitiveClasses.put(Void.class,      void.class);
			}
		}
		
		synchronized (castMethods) {
			for (Method method : getClass().getMethods()) {
				final int modifiers = method.getModifiers();
				final Class<?>[] parameterTypes = method.getParameterTypes();
				final Class<?> returnType = method.getReturnType();
				
				if ((method.getAnnotation(CastMethod.class) != null) && !Modifier.isStatic(modifiers) && parameterTypes.length == 1 && !returnType.equals(void.class)) {
					Class<?> inputType = parameterTypes[0];
					Class<?> outputType = returnType;
					
					// Basic mapping
					castMethods.put(toClassEntry(inputType, outputType), method);
					
					if (wrapperClasses.containsKey(inputType)) {
						castMethods.put(toClassEntry(wrapperClasses.get(inputType), outputType), method);
						
						if (wrapperClasses.containsKey(outputType)) {
							castMethods.put(toClassEntry(wrapperClasses.get(inputType), wrapperClasses.get(outputType)), method);
						}
						
						if (primitiveClasses.containsKey(outputType)) {
							castMethods.put(toClassEntry(wrapperClasses.get(inputType), primitiveClasses.get(outputType)), method);
						}
					} else {
						if (wrapperClasses.containsKey(outputType)) {
							castMethods.put(toClassEntry(inputType, wrapperClasses.get(outputType)), method);
						}
						
						if (primitiveClasses.containsKey(outputType)) {
							castMethods.put(toClassEntry(inputType, primitiveClasses.get(outputType)), method);
						}
					}
					
					if (primitiveClasses.containsKey(inputType)) {
						castMethods.put(toClassEntry(primitiveClasses.get(inputType), outputType), method);
						
						if (wrapperClasses.containsKey(outputType)) {
							castMethods.put(toClassEntry(primitiveClasses.get(inputType), wrapperClasses.get(outputType)), method);
						}
						
						if (primitiveClasses.containsKey(outputType)) {
							castMethods.put(toClassEntry(primitiveClasses.get(inputType), primitiveClasses.get(outputType)), method);
						}
					} else {
						if (wrapperClasses.containsKey(outputType)) {
							castMethods.put(toClassEntry(inputType, wrapperClasses.get(outputType)), method);
						}
						
						if (primitiveClasses.containsKey(outputType)) {
							castMethods.put(toClassEntry(inputType, primitiveClasses.get(outputType)), method);
						}
					}
				}
			}
		}
	}
	
	public static Map.Entry<Class<?>, Class<?>> toClassEntry(Class<?> inputType, Class<?> outputType) {
		return new MapEntryImpl<Class<?>, Class<?>>(inputType, outputType);
	}
	
	public static <K, V> Map.Entry<K, V> toEntry(K key, V value) {
		return new MapEntryImpl<K, V>(key, value);
	}
	
	public <I, O> boolean hasCastMethod(Class<I> inputType, Class<O> outputType) {
		return getCastMethod(inputType, outputType) != null;
	}
	
	public <I, O> Method getCastMethod(Class<I> inputType, Class<O> outputType) {
		Method result;
		
		final Map.Entry<Class<?>, Class<?>> entry = toClassEntry(inputType, outputType);
		if (castMethods.containsKey(entry)) {
			result = castMethods.get(entry);
		} else {
			result = null;
		}
		
		return result;
	}
	
	@Override
	public <I, O> boolean isCastable(Class<I> inputType, Class<O> outputType) {
		return outputType.isAssignableFrom(inputType) || hasCastMethod(inputType, outputType);
	}
	
	@Override
	public <I, O> boolean isCastable(I input, Class<O> outputType) {
		boolean result;
		
		try {
			cast(input, outputType);
			result = true;
		} catch (ClassCastException e) {
			result = false;
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <I, O> O cast(I input, Class<O> outputType) throws ClassCastException {
		O result;
		
		if (input == null) {
			if (outputType.isPrimitive()) {
				throw buildClassCastException(input, outputType);
			} else {
				result = (O) input;
			}
		} else {
			final Class<?> inputType = input.getClass();
			
			Method castMethod = castMethods.get(toClassEntry(inputType, outputType));
			
			if (castMethod == null) {
				throw buildClassCastException(input, outputType);
			} else {
				try {
					if (logger.isTraceEnabled()) {
						logger.trace("Casting input \"" + input + "\" of type " + inputType.getCanonicalName() + " to type " + outputType.getCanonicalName() + " via " + castMethod.getName());
					}
					result = (O) castMethod.invoke(this, input);
				} catch (IllegalArgumentException e) {
					throw exceptionTranslator.translate(e);
				} catch (IllegalAccessException e) {
					throw exceptionTranslator.translate(e);
				} catch (InvocationTargetException e) {
					throw exceptionTranslator.translate(e);
				}
			}
		}
		
		return result;
	}
	
	public <I, O> ClassCastException buildClassCastException(I input, Class<O> outputType) {
		StringBuffer message = new StringBuffer();
		message.append("Cannot cast input \"");
		message.append(input);
		message.append("\"");
		
		if (input == null) {
			// Can't really mention much
		} else {
			message.append(" of type " + input.getClass().getCanonicalName());
		}
		
		message.append(" to type " + outputType.getCanonicalName());
		
		return new ClassCastException(message.toString());
	}
	
	public Class<?> getWrappingClass(Class<?> primitiveClass) {
		return null;
	}
	
	@CastMethod
	public Boolean castBoolean(boolean b) {
		return Boolean.valueOf(b);
	}

	@CastMethod
	public Character castCharacter(char ch) {
		return Character.valueOf(ch);
	}

	@CastMethod
	public Byte castByte(byte b) {
		return Byte.valueOf(b);
	}

	@CastMethod
	public Short castShort(short s) {
		return Short.valueOf(s);
	}

	@CastMethod
	public Short castShort(Byte b) {
		return b.shortValue();
	}

	@CastMethod
	public Integer castInteger(int i) {
		return Integer.valueOf(i);
	}

	@CastMethod
	public Integer castInteger(Byte b) {
		return b.intValue();
	}

	@CastMethod
	public Integer castInteger(Short s) {
		return s.intValue();
	}

	@CastMethod
	public Integer castInteger(char ch) {
		return Character.getNumericValue(ch);
	}

	@CastMethod
	public Long castLong(long l) {
		return Long.valueOf(l);
	}

	@CastMethod
	public Long castLong(Byte b) {
		return b.longValue();
	}

	@CastMethod
	public Long castLong(Short s) {
		return s.longValue();
	}

	@CastMethod
	public Long castLong(Integer i) {
		return i.longValue();
	}

	@CastMethod
	public Float castFloat(float f) {
		return Float.valueOf(f);
	}

	@CastMethod
	public Double castDouble(double d) {
		return Double.valueOf(d);
	}

	@CastMethod
	public Double castDouble(Float f) {
		return f.doubleValue();
	}

}
