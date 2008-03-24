package net.lucenews3.queryParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

import net.lucenews3.Transformer;

import org.apache.lucene.queryParser.QueryParser;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class DelegatingQueryParserFactory {

	private Map<Class<QueryParser>, Transformer<QueryParser, QueryParser>> transformers;
	
	@SuppressWarnings("unchecked")
	public QueryParser buildDelegatingQueryParser(QueryParser parent) {
		final Transformer<QueryParser, QueryParser> transformer;
		
		final Class<QueryParser> parentClass = (Class<QueryParser>) parent.getClass();
		if (transformers.containsKey(parentClass)) {
			transformer = transformers.get(parentClass);
		} else {
			transformer = buildTransformer(parentClass);
			transformers.put(parentClass, transformer);
		}
		
		return transformer.transform(parent);
	}
	
	public Transformer<QueryParser, QueryParser> buildTransformer(Class<QueryParser> parentClass) {
		final Type parentType = Type.getType(parentClass);
		final Type childType = null;
		return null;
	}
	
	/**
	 * Visits a constructor in the child class which simply calls the parent class's constructor with the
	 * same arguments.
	 * 
	 * @param parentConstructor
	 * @param childClassVisitor
	 * @return
	 */
	public MethodVisitor visitDelegateConstructor(Constructor<?> parentConstructor, ClassVisitor childClassVisitor) {
		final Class<?>[] constructorParameterClasses = parentConstructor.getParameterTypes();
		final String constructorDescriptor = null;
		final Class<?> parentClass = parentConstructor.getClass();
		final Type parentType = Type.getType(parentClass);
		
		final MethodVisitor childConstructorVisitor = childClassVisitor.visitMethod(
				Opcodes.ACC_PUBLIC,
				"<init>",
				constructorDescriptor,
				null,
				null
			);
		
		childConstructorVisitor.visitCode();
		
		childConstructorVisitor.visitInsn(Opcodes.DUP);
		for (int i = 0; i < constructorParameterClasses.length; i++) {
			//final Class<?> constructorParameterClass = constructorParameterClasses[i];
			childConstructorVisitor.visitVarInsn(Opcodes.ALOAD, i + 1);
		}
		childConstructorVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, parentType.getInternalName(), "<init>", constructorDescriptor);
		
		childConstructorVisitor.visitMaxs(0, 0);
		childConstructorVisitor.visitEnd();
		
		return childConstructorVisitor;
	}
	
	/**
	 * Visits a child class method which delegates calls to its parent.
	 * 
	 * @param parentMethod
	 * @param childClassVisitor
	 * @return
	 */
	public MethodVisitor visitDelegateMethod(Method parentMethod, ClassVisitor childClassVisitor, String delegateFieldName) {
		final Class<?> parentClass = parentMethod.getClass();
		final Type parentType = Type.getType(parentClass);
		final String methodName = parentMethod.getName();
		final String methodDescriptor = Type.getMethodDescriptor(parentMethod);
		final Class<?>[] methodParameterClasses = parentMethod.getParameterTypes();
		
		final MethodVisitor childMethodVisitor = childClassVisitor.visitMethod(
				Opcodes.ACC_PUBLIC,
				methodName,
				methodDescriptor,
				null,
				null
			);
		
		childMethodVisitor.visitCode();
		
		childMethodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
		childMethodVisitor.visitFieldInsn(Opcodes.GETFIELD, null, delegateFieldName, null); // TODO
		
		for (int i = 0; i < methodParameterClasses.length; i++) {
			childMethodVisitor.visitVarInsn(Opcodes.ALOAD, i + 1);
		}
		childMethodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, parentType.getInternalName(), methodName, methodDescriptor);
		
		childMethodVisitor.visitInsn(Opcodes.ARETURN);
		childMethodVisitor.visitMaxs(0, 0);
		childMethodVisitor.visitEnd();
		
		return childMethodVisitor;
	}
	
}
