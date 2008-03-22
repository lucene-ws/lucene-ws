package net.lucenews3.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides sensible method resolution, much like the Java compiler's
 * algorithm.
 *
 */
public class AdaptiveMethodResolver implements MethodResolver {

	private Caster caster;
	
	public static void main(String... arguments) throws Exception {
		if (arguments.length > 0) {
			return;
		}
		
		for (Method method : AdaptiveMethodResolver.class.getDeclaredMethods()) {
			Class<?>[] types = method.getParameterTypes();
			System.out.println(method);
			for (int i = 0; i < types.length; i++) {
				System.out.println(" " + i + ": " + types[i]);
			}
			System.out.println();
		}
		
		AdaptiveMethodResolver r = new AdaptiveMethodResolver();
		//Method method = r.resolveMethod(AdaptiveMethodResolver.class, "foo", int.class, String.class, char.class, char.class);
		MethodInvokerImpl i = new MethodInvokerImpl();
		//i.setMethodResolver(new AdaptiveMethodResolver());
		i.invoke(AdaptiveMethodResolver.class, "foo", 56, "disgusting", 'A', 'R');
	}
	
	public static void foo(int count, String smell, char... chs) {
		System.out.println("count: " + count);
		System.out.println("small: " + smell);
		System.out.println("chs:");
		for (int i = 0; i < chs.length; i++) {
			System.out.println(" " + i + ": " + chs[i]);
		}
	}
	
	public AdaptiveMethodResolver() {
		this.caster = new CasterImpl();
	}
	
	@Override
	public Method resolveMethod(Class<?> clazz, String methodName,
			Class<?>... parameterTypes) throws SecurityException,
			NoSuchMethodException {
		List<Method> candidates = new ArrayList<Method>();
		
		final Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (isCompatible(method, methodName, parameterTypes)) {
				candidates.add(method);
			}
		}
		
		if (candidates.isEmpty()) {
			final StringBuffer message = new StringBuffer();
			message.append(clazz.getCanonicalName());
			message.append(".");
			message.append(methodName);
			message.append(" (");
			for (int i = 0; i < parameterTypes.length; i++) {
				Class<?> parameterType = parameterTypes[i];
				if (i > 0) {
					message.append(", ");
				}
				
				if (parameterType == null) {
					message.append("<nulltype>");
				} else {
					message.append(parameterType.getCanonicalName());
				}
			}
			message.append(")");
			throw new NoSuchMethodException(message.toString());
		} else if (candidates.size() == 1) {
			Method method = candidates.iterator().next();
			return method;
		} else {
			throw new RuntimeException("Ambiguous method definitions");
		}
	}
	
	public boolean isCompatible(Method target, String sourceName, Class<?>... sourceParameterTypes) {
		if (!sourceName.equals(target.getName())) {
			return false;
		}
		
		Class<?>[] targetParameterTypes = target.getParameterTypes();
		
		return isCompatible(targetParameterTypes, sourceParameterTypes, target.isVarArgs());
	}
	
	public boolean isCompatible(Class<?>[] targetTypes, Class<?>[] sourceTypes) {
		return isCompatible(targetTypes, sourceTypes, false);
	}
	
	public boolean isCompatible(Class<?>[] targetTypes, Class<?>[] sourceTypes, boolean isTargetVarArgs) {
		boolean result;
		
		if (isTargetVarArgs) {
			Class<?>[] nonVarArgTargetTypes = new Class<?>[targetTypes.length - 1];
			System.arraycopy(targetTypes, 0, nonVarArgTargetTypes, 0, nonVarArgTargetTypes.length);
			Class<?>[] nonVarArgSourceTypes = new Class<?>[nonVarArgTargetTypes.length];
			System.arraycopy(sourceTypes, 0, nonVarArgSourceTypes, 0, nonVarArgSourceTypes.length);
			Class<?> varArgTargetType = targetTypes[targetTypes.length - 1];
			Class<?>[] varArgSourceTypes = new Class<?>[sourceTypes.length - nonVarArgSourceTypes.length];
			System.arraycopy(sourceTypes, nonVarArgSourceTypes.length, varArgSourceTypes, 0, varArgSourceTypes.length);
			
			if (isCompatible(nonVarArgTargetTypes, nonVarArgSourceTypes, false)) {
				if (varArgTargetType.isArray()) {
					final Class<?> varArgTargetComponentType = varArgTargetType.getComponentType();
					
					result = true;
					for (Class<?> varArgSourceType : varArgSourceTypes) {
						if (isCompatible(varArgTargetComponentType, varArgSourceType)) {
							// Fine
						} else {
							result = false;
							break;
						}
					}
				} else {
					throw new RuntimeException("Method claims to have variable arguments, but is not an array type");
				}
			} else {
				result = false;
			}
		} else {
			if (targetTypes.length == sourceTypes.length) {
				result = true;
				
				final int length = Math.min(targetTypes.length, sourceTypes.length);
				for (int i = 0; i < length; i++) {
					final Class<?> targetType = targetTypes[i];
					final Class<?> sourceType = sourceTypes[i];
					if (isCompatible(targetType, sourceType)) {
						// Fine
					} else {
						result = false;
						break;
					}
				}
			} else {
				result = false;
			}
		}
		
		return result;
	}
	
	/**
	 * Determines whether objects of the source type can be assigned to
	 * variables declared as the target type.
	 * 
	 * @param targetType
	 * @param sourceType
	 * @return
	 */
	public boolean isCompatible(Class<?> targetType, Class<?> sourceType) {
		boolean result;
		
		if (sourceType == null) {
			// Source is any null-able reference
			result = !targetType.isPrimitive();
		} else {
			result = caster.isCastable(sourceType, targetType);
		}
		
		return result;
	}

	public Caster getCaster() {
		return caster;
	}

	public void setCaster(Caster caster) {
		this.caster = caster;
	}

}
