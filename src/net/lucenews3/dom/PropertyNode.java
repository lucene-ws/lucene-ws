package net.lucenews3.dom;

import java.beans.PropertyDescriptor;

public class PropertyNode extends NodeImpl {

	private PropertyDescriptor descriptor;
	private Object bean;
	
	public PropertyNode(Object bean, PropertyDescriptor descriptor) {
		this.bean = bean;
		this.descriptor = descriptor;
	}
	
}
