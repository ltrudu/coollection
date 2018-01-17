package com.wagnerandade.coollection.query.order;

import java.util.Comparator;

import com.wagnerandade.coollection.reflection.Phanton;

public class OrderComparator<T> implements Comparator<T> {

	private final String method;

	public OrderComparator(String method) {
		this.method = method;
	}

	@Override
	@SuppressWarnings("unchecked")
	public int compare(T one, T other) {
		// Pre-initialize values with parameters
		Object oneValue = one;
		Object otherValue = other;
		// If method was provided, inject parameter value
		// in the objects to be compared
		if(method.length()> 0)
		{
			oneValue = Phanton.from(one).call(method);
			otherValue = Phanton.from(other).call(method);			
		}
		if(oneValue == null || otherValue == null) {
			return 0;
		}
		if(oneValue instanceof Comparable) {
			return ((Comparable<Object>) oneValue).compareTo(otherValue);
		} else {
			return 0;
		}
	}
	
}