package net.lucenews3.model;

/**
 * Responsible for casting input objects into various output types.
 *
 */
public interface Caster {

	/**
	 * Determines whether or not the given input can be casted to the given
	 * output type.
	 * 
	 * @param input
	 * @param outputType
	 * @return
	 */
	public <I, O> boolean isCastable(Class<I> input, Class<O> outputType);
	
	public <I, O> boolean isCastable(I input, Class<O> outputType);
	
	/**
	 * Casts the given input to the given output type.
	 * 
	 * @param input
	 * @param outputType
	 * @return <code>input</code> cast to the given output type
	 */
	public <I, O> O cast(I input, Class<O> outputType) throws ClassCastException;
	
}
