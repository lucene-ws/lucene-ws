package net.lucenews3;

public interface Transformer<I, O> {

	public O transform(I input);
	
	public void transform(I input, O output);
	
}
