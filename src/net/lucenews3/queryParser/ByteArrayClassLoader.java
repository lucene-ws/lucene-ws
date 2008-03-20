package net.lucenews3.queryParser;

public class ByteArrayClassLoader extends ClassLoader {
	
	public ByteArrayClassLoader() {
		super();
	}

	public ByteArrayClassLoader(ClassLoader parent) {
		super(parent);
	}

	public Class<?> defineClass(String name, byte[] bytes) {
		return defineClass(name, bytes, 0, bytes.length);
	}

}
