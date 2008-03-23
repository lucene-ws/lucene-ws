package net.lucenews3.model;

import java.lang.reflect.Method;
import java.util.Comparator;

public class MethodDistanceComparator implements Comparator<Method> {

	private Caster caster;
	
	public MethodDistanceComparator() {
		this.caster = new CasterImpl();
	}
	
	@Override
	public int compare(Method m1, Method m2) {
		if (isMoreSpecific(m1, m2)) {
			if (isMoreSpecific(m2, m1)) {
				return 0;
			} else {
				return 1;
			}
		} else {
			if (isMoreSpecific(m2, m1)) {
				return -1;
			} else {
				return 0;
			}
		}
	}
	
	public boolean isMoreSpecific(Method m1, Method m2) {
		boolean result;
		
		final Class<?> c1 = m1.getClass();
		final Class<?> c2 = m2.getClass();
		
		if (caster.isCastable(c1, c2)) {
			// Fine
			result = true;
			final Class<?>[] parameterTypes1 = m1.getParameterTypes();
			final Class<?>[] parameterTypes2 = m2.getParameterTypes();
			final int length = Math.min(parameterTypes1.length, parameterTypes2.length);
			
			for (int i = 0; i < length; i++) {
				if (caster.isCastable(parameterTypes1[i], parameterTypes2[i])) {
					// Fine
				} else {
					result = false;
					break;
				}
			}
		} else {
			result = false;
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
