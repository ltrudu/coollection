package com.wagnerandade.coollection.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.wagnerandade.coollection.matcher.Matcher;
import com.wagnerandade.coollection.query.criteria.Criteria;
import com.wagnerandade.coollection.query.criteria.CriteriaList;
import com.wagnerandade.coollection.query.order.Order;
import com.wagnerandade.coollection.query.order.OrderCriteria;
import com.wagnerandade.coollection.query.specification.custom.AndSpecification;
import com.wagnerandade.coollection.query.specification.custom.OrSpecification;
import com.wagnerandade.coollection.reflection.Phanton;

public class Query<T> {

	private final Collection<T> collection;
	private CriteriaList<T> criterias;
	private OrderCriteria<T> orderCriteria;

	public Query(Collection<T> collection) {
		this.collection = collection;
		criterias = new CriteriaList<T>();
	}

	public Query<T> where(String method, Matcher matcher) {
		Criteria<T> criteria = new Criteria<T>(method, matcher);
		criterias.add(criteria);
		return this;
	}

	public Query<T> and(String method, Matcher matcher) {
		Criteria<T> criteria = new Criteria<T>(method, matcher);
		criteria.setSpecification(new AndSpecification<T>());
		criterias.add(criteria);
		return this;
	}

	public Query<T> or(String method, Matcher matcher) {
		Criteria<T> criteria = new Criteria<T>(method, matcher);
		criteria.setSpecification(new OrSpecification<T>());
		criterias.add(criteria);
		return this;
	}

	public Query<T> orderBy(String method, Order order) {
		orderCriteria = new OrderCriteria<T>(method, order);
		return this;
	}

	public Query<T> orderBy(String method) {
		return orderBy(method, Order.ASC);
	}
	
	public Query<T> order(Order orderDirection)
	{
		orderCriteria = new OrderCriteria<T>("", orderDirection);
		return this;
	}
	
	public Query<T> order()
	{
		return order(Order.ASC);
	}

	public List<T> all() {
		List<T> all = new ArrayList<T>();
		for(T item : collection) {
			if(criterias.match(item)) {
				all.add(item);
			}
		}
		if(orderCriteria != null) {
			all = orderCriteria.sort(all);
		}
		return all;
	}

	public <R> List<R> all(String method)
	{
		List<T> all = all();
		ArrayList<R> selectresult = new ArrayList<R>();
		for(T item : all)
		{
			R value = (R)Phanton.from(item).call(method);
			if(value != null)
				selectresult.add(value);
		}
		return selectresult;
	}
	
	public <R> Query<R> select(String method)
	{
		List<T> all = all();
		ArrayList<R> selectresult = new ArrayList<R>();
		for(T item : all)
		{
			R value = (R)Phanton.from(item).call(method);
			if(value != null)
				selectresult.add(value);
		}
		return new Query<R>(selectresult);
	}
	
	// Only works with
	public <R> Query<R> flatten()
	{
		ArrayList<R> flattenList = new ArrayList<R>();
		List<T> all = all();
		for(T items : all)
		{
			Collection<R> itemC = (Collection<R>) items;
			for(R item : itemC)
			{
				flattenList.add(item);
			}
		}
		return new Query<R>(flattenList);
	}
	
	// Return only unique elements
	public Query<T> unique()
	{
		ArrayList<T> uniqueList = new ArrayList<T>();
		for(T item : all())
		{
			if(uniqueList.contains(item) == false)
				uniqueList.add(item);
		}
		return new Query<T>(uniqueList);
	}

	public T first() {
		List<T> all = cloneCollection(collection);
		if(orderCriteria != null) {
			all = orderCriteria.sort(all);
		}
		for(T item : all) {
			if(criterias.match(item)) {
				return item;
			}
		}
		return null;
	}

	private List<T> cloneCollection(Collection<T> collection) {
		List<T> list = new ArrayList<T>();
		for(T item : collection) {
			list.add(item);
		}
		return list;
	}

}