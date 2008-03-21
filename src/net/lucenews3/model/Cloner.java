package net.lucenews3.model;

public interface Cloner {

	public <T> T clone(T source);
	
}
