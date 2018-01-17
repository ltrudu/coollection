package com.wagnerandade.coollection.matcher.custom;

import java.util.Collection;
import java.util.Map;

import com.wagnerandade.coollection.matcher.Matcher;

public class EqualsIgnoreCase implements Matcher {

	private final String value;

	public EqualsIgnoreCase(String value) {
		this.value = value;
	}

	public static boolean isCollection(Object ob) {
		  return ob instanceof Collection || ob instanceof Map;
		}
	
	@Override
	public boolean match(Object anotherValue) {
		if(isCollection(anotherValue))
		{
			boolean contains = false;
			for(Object item : (Iterable<?>)anotherValue)
			{
				contains  = contains || String.valueOf(item).equalsIgnoreCase(value);
			}
			return contains;
		}
		else
			return (value).equalsIgnoreCase((String)anotherValue);
	}

}
