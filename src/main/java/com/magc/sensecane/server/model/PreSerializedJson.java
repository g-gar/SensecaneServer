package com.magc.sensecane.server.model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PreSerializedJson<V> implements Map<String, Object> {

	private Map<String, Object> results;
	
	public PreSerializedJson() {
		results = new HashMap<String, Object>();
	}
	
	public PreSerializedJson(V obj) {
		this(obj, new String[0]);
	}
	
	public PreSerializedJson(V obj, String...excludedFields) {
		results = new HashMap<String, Object>();
		
		List<String> excluded = Arrays.asList(excludedFields);
		boolean accesible;
		for (Field field : obj.getClass().getDeclaredFields()) {
			if (!excluded.contains(field.getName())) {
				try {
					accesible = field.isAccessible();
					field.setAccessible(true);
					results.put(field.getName(), field.get(obj));
					field.setAccessible(accesible);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public int size() {
		return results.size();
	}

	@Override
	public boolean isEmpty() {
		return results.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return results.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return results.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return results.get(key);
	}

	@Override
	public Object put(String key, Object value) {
		return results.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return results.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		results.putAll(m);
	}

	@Override
	public void clear() {
		results.clear();
	}

	@Override
	public Set<String> keySet() {
		return results.keySet();
	}

	@Override
	public Collection<Object> values() {
		return results.values();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return results.entrySet();
	}

}
