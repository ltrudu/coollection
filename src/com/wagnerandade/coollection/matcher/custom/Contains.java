package com.wagnerandade.coollection.matcher.custom;

import java.util.Collection;
import java.util.Map;

import com.wagnerandade.coollection.matcher.Matcher;

public class Contains implements Matcher {

	private final String matcherValue;

	public Contains(String matcherValue) {
		this.matcherValue = matcherValue;
	}

	public static boolean isCollection(Object ob) {
		  return ob instanceof Collection || ob instanceof Map;
		}

	@Override
	public boolean match(Object value) {
		if(isCollection(value))
		{
			boolean contains = false;
			for(Object item : (Iterable<?>)value)
			{
				contains  = contains || String.valueOf(item).contains(matcherValue);
			}
			return contains;
		}
		else
			return String.valueOf(value).contains(matcherValue);
	}
}
