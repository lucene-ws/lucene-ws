package net.lucenews3.model;

/**
 * Ordinal starts at 1!
 *
 */
public class Page implements IndexRange {

	private int ordinal;
	private int size;
	
	public Page() {
		
	}
	
	public Page(int ordinal, int size) {
		this.ordinal = ordinal;
		this.size = size;
	}
	
	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int fromIndex() {
		return (ordinal - 1) * size;
	}
	
	public int toIndex() {
		return ordinal * size;
	}
	
}
