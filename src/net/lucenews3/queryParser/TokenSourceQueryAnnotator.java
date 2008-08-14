package net.lucenews3.queryParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.lucenews3.ExceptionTranslator;
import net.lucenews3.ExceptionTranslatorImpl;
import net.lucenews3.model.TokenSource;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.Token;
import org.apache.lucene.search.Query;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.springframework.util.StopWatch;

public class TokenSourceQueryAnnotator {

	private Map<Class<?>, Method> instantiatorsByClass;
	private ExceptionTranslator exceptionTranslator;
	private ByteArrayClassLoader classLoader;
	
	public static void main(String... arguments) throws Exception {
		final TokenSourceQueryAnnotator wrapper = new TokenSourceQueryAnnotator();
		
		final QueryParser queryParser = new QueryParser("default", new StandardAnalyzer());
		
		Query beforeQuery;
		
		boolean term = true;
		if (term) {
			beforeQuery = queryParser.parse("sound:woof");
		} else {
			beforeQuery = queryParser.parse("sound:woof AND foo:baz");
		}
		
		final Token beforeToken = new Token();
		beforeToken.image = "I CAN HAS CHEESEBURGER";
		
		StopWatch watch = new StopWatch();
		watch.start();
		final Query afterQuery = wrapper.annotate(beforeQuery, beforeToken);
		watch.stop();
		System.out.println("Annotated in " + watch.getTotalTimeMillis() + " ms");
		System.out.println(afterQuery.getClass());
		
		System.out.println("Is instance of TokenSource? " + (afterQuery instanceof TokenSource));
		
		final TokenSource tokenSource = (TokenSource) afterQuery;
		final Token afterToken = tokenSource.getToken();
		System.out.println(afterToken);
		System.out.println(beforeToken == afterToken);
		
		watch.start();
		wrapper.annotate(afterQuery, beforeToken);
		watch.stop();
		System.out.println("Annotated #2 in " + watch.getTotalTimeMillis() + " ms");
	}
	
	public TokenSourceQueryAnnotator() {
		this.exceptionTranslator = new ExceptionTranslatorImpl();
		this.instantiatorsByClass = new HashMap<Class<?>, Method>();
		this.classLoader = new ByteArrayClassLoader(Thread.currentThread().getContextClassLoader());
	}
	
	/**
	 * Annotates the given query with the given token.
	 * @param query
	 * @param token
	 * @return
	 */
	public Query annotate(final Query query, final Token token) {
		Query result;
		
		if (query instanceof TokenSource) {
			TokenSource tokenSource = (TokenSource) query;
			tokenSource.setToken(token);
			result = query;
		} else {
			Class<? extends Query> queryClass = query.getClass();
			Class<? extends Token> tokenClass = token.getClass();
			
			Method instantiator;
			if (instantiatorsByClass.containsKey(queryClass)) {
				instantiator = instantiatorsByClass.get(queryClass);
			} else {
				Class<? extends Query> wrapperClass = buildWrapperClass(queryClass, tokenClass);
				
				try {
					instantiator = wrapperClass.getMethod("getInstance", queryClass, tokenClass);
				} catch (SecurityException e) {
					throw exceptionTranslator.translate(e);
				} catch (NoSuchMethodException e) {
					throw exceptionTranslator.translate(e);
				}
				
				instantiatorsByClass.put(queryClass, instantiator);
			}
			
			// Get the instance using the query and token as arguments
			try {
				result = (Query) instantiator.invoke(null, query, token);
			} catch (IllegalArgumentException e) {
				throw exceptionTranslator.translate(e);
			} catch (IllegalAccessException e) {
				throw exceptionTranslator.translate(e);
			} catch (InvocationTargetException e) {
				throw exceptionTranslator.translate(e);
			}
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends Query> buildWrapperClass(Class<? extends Query> queryClass, Class<? extends Token> tokenClass) {
		String className = buildWrapperClassName(queryClass, tokenClass);
		byte[] classBytes = buildWrapperClassBytes(queryClass, tokenClass);
		return (Class<? extends Query>) classLoader.defineClass(className, classBytes);
	}
	
	public String buildWrapperClassName(Class<? extends Query> queryClass, Class<? extends Token> tokenClass) {
		return "net.lucenews3.queryParser." + queryClass.getSimpleName() + "TokenSource";
	}
	
	/**
	 * Builds a class capable of wrapping the given target class.
	 * @param targetClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public byte[] buildWrapperClassBytes(Class<? extends Query> queryClass, Class<? extends Token> tokenClass) {
		Type queryType = Type.getType(queryClass);
		Type tokenType = Type.getType(tokenClass);
		
		ClassWriter cw = new ClassWriter(0);
		
		String subclassName = buildWrapperClassName(queryClass, tokenClass);
		String subclassInternalName = subclassName.replaceAll("\\.", "/");
		Type subclassType = Type.getObjectType(subclassInternalName);
		
		// Create class
		cw.visit(
				Opcodes.V1_5,
				Opcodes.ACC_PUBLIC,
				subclassInternalName,
				null,
				Type.getInternalName(queryClass),
				new String[] { Type.getInternalName(TokenSource.class) } // Interfaces
			);
		
		// Create field to store instance of prototype
		cw.visitField(Opcodes.ACC_PUBLIC, "target", Type.getDescriptor(queryClass), null, null);
		
		try {
			// Attempt to locate the default constructor, if any
			queryClass.getConstructor();
			
			// Declare constructor: public <init>(Query target, Token token)
			MethodVisitor constructor = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, new Type[] { queryType, tokenType }), null, null);
			
			constructor.visitCode();
			constructor.visitVarInsn(Opcodes.ALOAD, 0);
			
			// Super class constructor
			constructor.visitInsn(Opcodes.DUP);
			constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, queryType.getInternalName(), "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, new Type[] { }));
			
			// Store target
			constructor.visitInsn(Opcodes.DUP);
			constructor.visitVarInsn(Opcodes.ALOAD, 1);
			constructor.visitFieldInsn(Opcodes.PUTFIELD, subclassType.getInternalName(), "target", queryType.getDescriptor());
			
			// Store query
			constructor.visitVarInsn(Opcodes.ALOAD, 2);
			constructor.visitFieldInsn(Opcodes.PUTFIELD, subclassType.getInternalName(), "token", tokenType.getDescriptor());
			
			constructor.visitInsn(Opcodes.RETURN);
			constructor.visitMaxs(3, 3);
			constructor.visitEnd();
			
		} catch (SecurityException e) {
			throw exceptionTranslator.translate(e);
		} catch (NoSuchMethodException e) {
			// Oh no, we must adapt. Perhaps we can use default values! :)
			
			Constructor constructor = queryClass.getConstructors()[0];
			Class<?>[] parameterClasses = constructor.getParameterTypes();
			Type[] parameterTypes = new Type[parameterClasses.length];
			for (int i = 0; i < parameterClasses.length; i++) {
				parameterTypes[i] = Type.getType(parameterClasses[i]);
			}
			
			MethodVisitor cons = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, new Type[]{ queryType, tokenType }), null, null);
			cons.visitCode();
			
			// Okay, let's load the default values!
			cons.visitVarInsn(Opcodes.ALOAD, 0);
			cons.visitInsn(Opcodes.DUP);
			for (int i = 0; i < parameterTypes.length; i++) {
				visitLoadDefaultInsn(cons, parameterTypes[i]);
			}
			cons.visitMethodInsn(Opcodes.INVOKESPECIAL, queryType.getInternalName(), "<init>", Type.getConstructorDescriptor(constructor));
			
			// Store target
			cons.visitInsn(Opcodes.DUP);
			cons.visitVarInsn(Opcodes.ALOAD, 1);
			cons.visitFieldInsn(Opcodes.PUTFIELD, subclassType.getInternalName(), "target", queryType.getDescriptor());
			
			// Store query
			cons.visitVarInsn(Opcodes.ALOAD, 2);
			cons.visitFieldInsn(Opcodes.PUTFIELD, subclassType.getInternalName(), "token", tokenType.getDescriptor());
			
			cons.visitInsn(Opcodes.RETURN);
			cons.visitMaxs(parameterTypes.length + 3, 3);
			cons.visitEnd();
			
			
			//throw exceptionTranslator.translate(e);
		}
		
		// Make the static method
		MethodVisitor ins = cw.visitMethod(
				Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
				"getInstance",
				Type.getMethodDescriptor(subclassType, new Type[] { Type.getType(queryClass), Type.getType(tokenClass) }),
				null,
				null
			);
		ins.visitCode();
		ins.visitTypeInsn(Opcodes.NEW, subclassType.getInternalName());
		ins.visitInsn(Opcodes.DUP);
		ins.visitVarInsn(Opcodes.ALOAD, 0); // query
		ins.visitVarInsn(Opcodes.ALOAD, 1); // token
		ins.visitMethodInsn(Opcodes.INVOKESPECIAL, subclassType.getInternalName(), "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, new Type[] { queryType, tokenType }));
		ins.visitInsn(Opcodes.ARETURN);
		ins.visitMaxs(5, 3);
		ins.visitEnd();
		
		// Generate getter/setter methods for target
		visitFieldAccessor(cw, subclassInternalName, "target", Type.getDescriptor(queryClass));
		visitFieldMutator(cw, subclassInternalName, "target", Type.getDescriptor(queryClass));
		
		// Create field to store instance of token
		cw.visitField(Opcodes.ACC_PUBLIC, "token", Type.getDescriptor(tokenClass), null, null);
		
		// Generate getter/setter methods for token
		visitFieldAccessor(cw, subclassInternalName, "token", Type.getDescriptor(tokenClass));
		visitFieldMutator(cw, subclassInternalName, "token", Type.getDescriptor(tokenClass));
		
		for (Method method : queryClass.getMethods()) {
			visitForwardedMethod(cw, method, subclassInternalName, queryClass);
		}
		
		cw.visitEnd();
		
		return cw.toByteArray();
	}
	
	public String getAccessorMethodName(String fieldName) {
		return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}
	
	public MethodVisitor visitFieldAccessor(ClassVisitor cv, String owner, String name, String desc) {
		MethodVisitor mv = cv.visitMethod(
				Opcodes.ACC_PUBLIC,
				getAccessorMethodName(name),
				Type.getMethodDescriptor(Type.getType(desc), new Type[0]),
				null,
				null
			);
		visitFieldAccessor(mv, owner, name, desc);
		return mv;
	}
	
	public void visitFieldAccessor(MethodVisitor mv, String owner, String name, String desc) {
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, owner, name, desc);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}
	
	public String getMutatorMethodName(String fieldName) {
		return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}
	
	public MethodVisitor visitFieldMutator(ClassVisitor cv, String owner, String name, String desc) {
		MethodVisitor mv = cv.visitMethod(
				Opcodes.ACC_PUBLIC,
				getMutatorMethodName(name),
				Type.getMethodDescriptor(Type.VOID_TYPE, new Type[] { Type.getType(desc) }),
				null,
				null
			);
		visitFieldMutator(mv, owner, name, desc);
		return mv;
	}
	
	public void visitFieldMutator(MethodVisitor mv, String owner, String name, String desc) {
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, owner, name, desc);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(2, 2);
		mv.visitEnd();
	}
	
	public void visitForwardedMethod(ClassVisitor cv, Method method, String targetOwner, Class<?> targetType) {
		boolean isPublic = Modifier.isPublic(method.getModifiers());
		boolean isStatic = Modifier.isStatic(method.getModifiers());
		boolean isFinal = Modifier.isFinal(method.getModifiers());
		
		if (isPublic && !isStatic && !isFinal) {
			MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
			mv.visitCode();
			
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitFieldInsn(Opcodes.GETFIELD, targetOwner, "target", Type.getDescriptor(targetType));
			
			// Target is now on stack. Must copy arguments
			Class<?>[] parameterTypes = method.getParameterTypes();
			for (int i = 0; i < parameterTypes.length; i++) {
				Class<?> parameterType = parameterTypes[i];
				visitLoadInsn(mv, Type.getType(parameterType), i + 1);
			}
			
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getInternalName(method.getDeclaringClass()), method.getName(), Type.getMethodDescriptor(method));
			
			Class<?> returnType = method.getReturnType();
			if (returnType.equals(void.class)) {
				mv.visitInsn(Opcodes.RETURN);
			} else if (returnType.equals(int.class)) {
				mv.visitInsn(Opcodes.IRETURN);
			} else if (returnType.equals(long.class)) {
				mv.visitInsn(Opcodes.LRETURN);
			} else if (returnType.equals(boolean.class)) {
				mv.visitInsn(Opcodes.IRETURN);
			} else if (returnType.equals(float.class)) {
				mv.visitInsn(Opcodes.FRETURN);
			} else {
				mv.visitInsn(Opcodes.ARETURN);
			}
			
			mv.visitMaxs(3, 1 + parameterTypes.length);
			mv.visitEnd();
		}
	}
	
	public void visitLoadDefaultInsn(MethodVisitor mv, Type type) {
		if (type.equals(Type.INT_TYPE)) {
			mv.visitInsn(Opcodes.ICONST_0);
		} else if (type.getSort() == Type.OBJECT) {
			mv.visitInsn(Opcodes.ACONST_NULL);
		} else {
			throw new RuntimeException("Unknown default value: " + type);
		}
	}
	
	public void visitLoadInsn(MethodVisitor mv, Type type, int var) {
		if (type.equals(Type.INT_TYPE)) {
			mv.visitVarInsn(Opcodes.ILOAD, var);
		} else if (type.equals(Type.LONG_TYPE)) {
			mv.visitVarInsn(Opcodes.LLOAD, var);
		} else if (type.equals(Type.FLOAT_TYPE)) {
			mv.visitVarInsn(Opcodes.FLOAD, var);
		} else {
			mv.visitVarInsn(Opcodes.ALOAD, var);
		}
	}
	
	public void visitReturnInsn(MethodVisitor mv, Type returnType) {
		if (returnType.equals(Type.VOID_TYPE)) {
			mv.visitInsn(Opcodes.RETURN);
		} else if (returnType.equals(Type.INT_TYPE)) {
			mv.visitInsn(Opcodes.IRETURN);
		} else if (returnType.equals(Type.LONG_TYPE)) {
			mv.visitInsn(Opcodes.LRETURN);
		} else if (returnType.equals(Type.BOOLEAN_TYPE)) {
			mv.visitInsn(Opcodes.IRETURN);
		} else if (returnType.equals(Type.FLOAT_TYPE)) {
			mv.visitInsn(Opcodes.FRETURN);
		} else {
			mv.visitInsn(Opcodes.ARETURN);
		}
	}
	
}
