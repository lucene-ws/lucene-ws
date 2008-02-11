package net.lucenews3.lucene.support;

public interface MethodInvoker {

	public Object invoke(Object object, String methodName, Object... arguments);
	
}
