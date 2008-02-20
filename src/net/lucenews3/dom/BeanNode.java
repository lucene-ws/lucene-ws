package net.lucenews3.dom;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class BeanNode extends NodeImpl {
	
	private Object bean;
	
	public BeanNode(Object bean) {
		BeanWrapper wrapper = new BeanWrapperImpl(bean);
		for (PropertyDescriptor descriptor : wrapper.getPropertyDescriptors()) {
		}
	}
	
}
