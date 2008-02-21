package net.lucenews3.model;

public interface MethodInvoker {

	public Object invoke(Object object, String methodName, Object... arguments);
	
}
